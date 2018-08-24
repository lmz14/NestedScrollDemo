package com.lmz.viewdemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lmz.viewdemo.R;

import java.util.NoSuchElementException;
/**
 * @author lmz14
 * @date 2018.8.20
 */
public class StickyLayout extends LinearLayout {
    private static final String TAG = "StickyLayout";
    private static final boolean DEBUG = true;

    /**
     * 用来判断listview是否滑动到顶端
     */
    public interface OnGiveUpTouchEventListener {
        boolean giveUpTouchEvent(MotionEvent event);
    }

    /**
     * 上半部分布局
     */
    private View mHeader;

    /**
     * 下半部分布局
     */
    private View mContent;
    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

    /**
     * header的高度  单位：px
     */
    private int mOriginalHeaderHeight;
    private int minHeadTopHeight;
    private int maxHeadTopHeight;
    private int mHeaderHeight;
    /**
     * 滑动方向
     */
    private int mStatus = STATUS_EXPANDED;
    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;
    /**
     * 滚动的像素值
     */
    private int mTouchSlop;

    /**
     * 分别记录上次滑动的坐标
     */
    private int mLastX = 0;
    private int mLastY = 0;

    /**
     * 分别记录上次滑动的坐标(onInterceptTouchEvent)
     */
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    /**
     * 用来控制滑动角度，仅当角度a满足如下条件才进行滑动：tan a = deltaX / deltaY > 2
     */
    private static final int TAN = 2;

    private boolean mIsSticky = true;
    private boolean mInitDataSucceed = false;
    private boolean mDisallowInterceptTouchEventOnHeader = true;

    private int searchBarHeight;
    private int searchBarScale;
    private View rlSearchBar;
    private int textSize;
    private int textScale;

    public StickyLayout(Context context) {
        super(context);
    }

    public StickyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StickyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && (mHeader == null || mContent == null)) {
            initData();
        }
    }

    private void initData() {
        int headerId= getResources().getIdentifier("sticky_header", "id", getContext().getPackageName());
        int contentId = getResources().getIdentifier("sticky_content", "id", getContext().getPackageName());
        if (headerId != 0 && contentId != 0) {
            mHeader = findViewById(headerId);
            mContent = findViewById(contentId);
            rlSearchBar = mHeader.findViewById(R.id.rlSearchBar);
            mOriginalHeaderHeight = mHeader.getMeasuredHeight();
            mHeaderHeight = mOriginalHeaderHeight;
            maxHeadTopHeight = (int) getContext().getResources().getDimension(R.dimen.maxHeadTopHeight);
            minHeadTopHeight = (int) getContext().getResources().getDimension(R.dimen.minHeadTopHeight);
            searchBarHeight = (int) getContext().getResources().getDimension(R.dimen.searchBarHiight);
            searchBarScale = (int) getContext().getResources().getDimension(R.dimen.searchBarScale);
            maxOffset = maxHeadTopHeight - minHeadTopHeight;
            textSize = (int) getContext().getResources().getDimension(R.dimen.textSize);
            textScale = (int) getContext().getResources().getDimension(R.dimen.textScale);
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            if (mHeaderHeight > 0) {
                mInitDataSucceed = true;
            }
            if (DEBUG) {
                Log.d(TAG, "mTouchSlop = " + mTouchSlop + "mHeaderHeight = " + mHeaderHeight);
            }
        } else {
            throw new NoSuchElementException("Did your view with id \"sticky_header\" or \"sticky_content\" exists?");
        }
    }

    /**
     * 传递跟布局
     * @param l this
     */
    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastX = x;
                mLastY = y;
                intercepted = 0;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                Log.d(TAG,"onInterceptTouchEvent y:"+y+",mHeight:"+getHeaderHeight()+",mstatus:"+mStatus+",mTouchSlop:"+mTouchSlop+",deltaY:"+deltaY);
                if (mDisallowInterceptTouchEventOnHeader && y <= getHeaderHeight()) {
                    //竖直滑动距离小于header高度，不拦截
                    intercepted = 0;
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    //竖直距离差小于等于水平距离不拦截
                    intercepted = 0;
                } else if ((mStatus == STATUS_EXPANDED) && deltaY <= -mTouchSlop) {
                    //header展开状态，向上滑动，拦截
                    intercepted = 1;
                } else if (mGiveUpTouchEventListener != null) {
                    //布局不为空
                    if (mGiveUpTouchEventListener.giveUpTouchEvent(event) && deltaY >= mTouchSlop) {
                        //listview滑动到顶部并向下滑动，拦截
                        intercepted = 1;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = 0;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            }
            default:
                break;
        }

        if (DEBUG) {
            Log.d(TAG, "intercepted=" + intercepted);
        }
        return intercepted != 0 && mIsSticky;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (DEBUG) {
                    Log.d(TAG, "mHeaderHeight=" + mHeaderHeight + "  deltaY=" + deltaY + "  mlastY=" + mLastY);
                }
                mHeaderHeight += deltaY;
                setHeaderHeight(mHeaderHeight);
                break;
            }
            case MotionEvent.ACTION_UP: {
                // 这里做了下判断，当松开手的时候，会自动向两边滑动，具体向哪边滑，要看当前所处的位置
                int destHeight;

                if (mHeaderHeight <= minHeadTopHeight) {
                    destHeight = 0;
                    mStatus = STATUS_COLLAPSED;
                } else {
                    int deltaY = y - mLastY;
                    mHeaderHeight += deltaY;
                    destHeight = mHeaderHeight;
                }
                // 慢慢滑向终点
                this.smoothSetHeaderHeight(mHeaderHeight, destHeight, 500);
                break;
            }
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public void smoothSetHeaderHeight(final int from, final int to, long duration) {
        smoothSetHeaderHeight(from, to, duration, false);
    }

    /**
     * 线程设置高度变化
     * @param from head初始高度
     * @param to  移动到的高度
     * @param duration  时间
     * @param modifyOriginalHeaderHeight
     */
    public void smoothSetHeaderHeight(final int from, final int to, long duration, final boolean modifyOriginalHeaderHeight) {
        final int frameCount = (int) (duration / 1000f * 30) + 1;
        //速度公式
        final float partation = (to - from) / (float) frameCount;
        new Thread("Thread#smoothSetHeaderHeight") {

            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    //动态变化
                    final int height;
                    if (i == frameCount - 1) {
                        height = to;
                    } else {
                        height = (int) (from + partation * i);
                    }
                    post(new Runnable() {
                        public void run() {
                            setHeaderHeight(height);
                        }
                    });
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (modifyOriginalHeaderHeight) {
                    setOriginalHeaderHeight(to);
                }
            };

        }.start();
    }

    public void setOriginalHeaderHeight(int originalHeaderHeight) {
        mOriginalHeaderHeight = originalHeaderHeight;
    }

    public void setHeaderHeight(int height, boolean modifyOriginalHeaderHeight) {
        if (modifyOriginalHeaderHeight) {
            setOriginalHeaderHeight(height);
        }
        setHeaderHeight(height);
    }

    /**
     * 赋值 并设置高度到Header中
     * @param height
     */
    public void setHeaderHeight(int height) {
        if (!mInitDataSucceed) {
            initData();
        }

        if (DEBUG) {
            Log.e(TAG, "setHeaderHeight height=" + height);
        }
        int temHeight = height;
        if (height <= minHeadTopHeight) {
            height = minHeadTopHeight;
        } else if (height > mOriginalHeaderHeight) {
            //这行注释掉可实现下拉到任意位置后弹回
            height = mOriginalHeaderHeight;
        }

        if (height == minHeadTopHeight) {
            mStatus = STATUS_COLLAPSED;
        }else{
            mStatus = STATUS_EXPANDED;
        }

        if (mHeader != null && mHeader.getLayoutParams() != null) {
            mHeader.getLayoutParams().height = height;
            mHeader.requestLayout();
            mHeaderHeight = height;
            setSearchBarCover(temHeight,height);
        } else {
            if (DEBUG) {
                Log.e(TAG, "null LayoutParams when setHeaderHeight");
            }
        }
    }

    private int maxOffset;
    private void setSearchBarCover(int height,int realHeight){
        if(rlSearchBarCover!=null){
            boolean isShowConverView = height<= maxHeadTopHeight ?true:false;
            if(isShowConverView){
                rlSearchBarCover.setVisibility(View.VISIBLE);
                rlSearchBar.setVisibility(View.INVISIBLE);
                if(realHeight<=maxHeadTopHeight && realHeight>=minHeadTopHeight){

                    int offset = maxHeadTopHeight - realHeight;
                    float rate = (1.0f * offset)/maxOffset;
                   int h = (int) (searchBarHeight - searchBarScale * rate);
                   rlSearchBarCover.getLayoutParams().height = h;
                   int mTextSize = (int) (textSize - textScale * rate);
                   tvSearchContentCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                   tvSearchBtnCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                }
            }else{
                rlSearchBar.setVisibility(View.VISIBLE);
                rlSearchBarCover.setVisibility(View.GONE);
            }
        }
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setSticky(boolean isSticky) {
        mIsSticky = isSticky;
    }

    public void requestDisallowInterceptTouchEventOnHeader(boolean disallowIntercept) {
        mDisallowInterceptTouchEventOnHeader = disallowIntercept;
    }

    private View rlSearchBarCover;
    private TextView tvSearchBtnCover;
    private TextView tvSearchContentCover;
    public void setRlSearchBarCover(View rlSearchBarCover){
        this.rlSearchBarCover = rlSearchBarCover;
        tvSearchBtnCover = rlSearchBarCover.findViewById(R.id.tvSearchBtnCover);
        tvSearchContentCover = rlSearchBarCover.findViewById(R.id.tvSearchContentCover);
    }

    public int getStatus(){
        return mStatus;
    }

    /**
     * 是否允许下拉，与PTR结合使用
     * @return
     */
    public boolean isAllExpanded(){
        return mHeaderHeight == mOriginalHeaderHeight;
    }

}

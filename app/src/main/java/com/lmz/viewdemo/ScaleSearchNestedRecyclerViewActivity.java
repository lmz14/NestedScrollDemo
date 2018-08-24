package com.lmz.viewdemo;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.lmz.viewdemo.adapter.ListAdapter;
import com.lmz.viewdemo.view.XRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class ScaleSearchNestedRecyclerViewActivity extends AppCompatActivity {

    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.rlSearchBarCover)
    RelativeLayout rlSearchBarCover;
    @BindView(R.id.rlSearchBar)
    RelativeLayout rlSearchBar;
    @BindView(R.id.tvSearchContentCover)
    TextView tvSearchContentCover;
    @BindView(R.id.tvSearchBtnCover)
    TextView tvSearchBtnCover;
    @BindView(R.id.rlSearchBarCoverLayout)
    RelativeLayout rlSearchBarCoverLayout;
    @BindView(R.id.refreshview)
    XRefreshView refreshview;
    @BindView(R.id.ivHeadCoverBg)
    ImageView ivSearchBarCoverBg;

    private ListAdapter adapter;
    private XRefreshLayout xRefreshLayout;
    private int headHeight;
    private int maxHeadTopHeight,minHeadTopHeight,maxLimit,minLimit,maxMarginBottom,marginLeft;
    private int searchBarHeight,textSize;
    private int searchBarScale,textScale,marginBottomScale;
    private int maxOffset;
    /**
     * 默认展开状态
     */
    private int mStatus = STATUS_EXPANDED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nested_recyclerview);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        xRefreshLayout = new XRefreshLayout(this);
        refreshview.setCustomHeaderView(xRefreshLayout);
        refreshview.setPinnedContent(true);
        refreshview.setIsFixHeaderView(true, (int) this.getResources().getDimension(R.dimen.search_home_pull_refresh_largest_height));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new ListAdapter(this);
        recyclerview.setAdapter(adapter);

        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    private void initData(){
        headHeight = (int) this.getResources().getDimension(R.dimen.headHeight);
        minHeadTopHeight = (int) this.getResources().getDimension(R.dimen.minHeadTopHeight);
        maxHeadTopHeight = (int) this.getResources().getDimension(R.dimen.maxHeadTopHeight);
        maxOffset = maxHeadTopHeight - minHeadTopHeight;
        searchBarHeight = (int) this.getResources().getDimension(R.dimen.searchBarHiight);
        searchBarScale = (int) this.getResources().getDimension(R.dimen.searchBarScale);
        textSize = (int) this.getResources().getDimension(R.dimen.textSize);
        textScale = (int) this.getResources().getDimension(R.dimen.textScale);
        maxMarginBottom = (int) this.getResources().getDimension(R.dimen.maxMarginBottom);
        marginBottomScale = (int) this.getResources().getDimension(R.dimen.marginBottomScale);
        marginLeft = (int) this.getResources().getDimension(R.dimen.marginLeft);
        maxLimit = (int) this.getResources().getDimension(R.dimen.maxLimit);
        minLimit = (int) this.getResources().getDimension(R.dimen.minLimit);
    }

    private void initListener(){
        refreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
                refreshview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshview.stopRefresh();
                    }
                },1000);
            }

            @Override
            public void onHeaderMove(double offset, int offsetY) {
                super.onHeaderMove(offset, offsetY);
                //offset:移动距离和headerview高度的比例。范围是0~1，0：headerview全然没显示 1：headerview全然显示
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
            }
        });

        appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset 向上滑动得到的值是负的，初始值为0 就是展开状态
                //剩下未滑出屏幕的高度
                int h = headHeight + verticalOffset ;
                if(verticalOffset == 0){
                    //展开状态
                    mStatus = STATUS_EXPANDED;
                }else if(h == minHeadTopHeight){
                    mStatus = STATUS_COLLAPSED;
                }else{
                    mStatus = 0;
                }

                if(h<=maxHeadTopHeight){
                    rlSearchBarCoverLayout.setVisibility(View.VISIBLE);
                    rlSearchBar.setVisibility(View.GONE);
                    //当前滑出屏幕的距离
                    int offset = maxHeadTopHeight - h;
                    //maxOffset需要滑出屏幕的最大距离
                    float rate = (1.0f * offset)/maxOffset;
                    //背景图片透明度
                    ivSearchBarCoverBg.setAlpha(1 * rate);
                    //搜索布局容器高度缩放
                    rlSearchBarCoverLayout.getLayoutParams().height = h;
                    //搜索框内容部分缩放
                    if(h<=maxLimit){
                        int mMaxOffSet = maxLimit - minLimit;
                        int mOffset = maxLimit - h;
                        float mRate = (1.0f * mOffset)/mMaxOffSet;
                        //搜索框高度缩放
                        int rlSearchBoxCoverH = (int) (searchBarHeight - searchBarScale * mRate);
                        rlSearchBarCover.getLayoutParams().height = rlSearchBoxCoverH;
                        //字体缩放
                        int mTextSize = (int) (textSize - textScale * mRate);
                        tvSearchContentCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                        tvSearchBtnCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
                        //底部marginBottom距离缩放
                        mMaxOffSet = maxLimit - minHeadTopHeight;
                        mRate = (1.0f * mOffset)/mMaxOffSet;
                        int marginBottom = (int) (maxMarginBottom - marginBottomScale * mRate);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearchBarCover.getLayoutParams();
                        params.setMargins(marginLeft,0,marginLeft,marginBottom);
                    }else{
                        //下滑时，由于比例不会等于0，进行复位
                        rlSearchBarCover.getLayoutParams().height = searchBarHeight;
                        tvSearchContentCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                        tvSearchBtnCover.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlSearchBarCover.getLayoutParams();
                        params.setMargins(marginLeft,0,marginLeft,maxMarginBottom);
                    }
                    //重新布局
                    rlSearchBarCoverLayout.requestLayout();
                }else{
                    rlSearchBar.setVisibility(View.VISIBLE);
                    rlSearchBarCoverLayout.setVisibility(View.GONE);
                    //下滑时，由于比例不会等于0，所以隐藏搜索框时，进行复位
                    rlSearchBarCoverLayout.getLayoutParams().height = maxHeadTopHeight;
                    ivSearchBarCoverBg.setAlpha(1f);
                    rlSearchBarCoverLayout.requestLayout();
                }
            }
        });
    }



    private boolean isDragDown;
    private int mLastX = 0;
    private int mLastY = 0;
    private int mTouchSlop;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isDragDown = false;
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if(Math.abs(deltaY) >= Math.abs(deltaX) && deltaY >=mTouchSlop){
                    isDragDown = true;
                }else{
                    isDragDown = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                mLastX = 0;
                mLastY = 0;
                isDragDown = false;
                break;
            default:
                break;
        }
        canPtr(isDragDown);

        return super.dispatchTouchEvent(event);
    }

    private void canPtr(boolean isDragDown){
        //展开 && recyclerView列表数据置顶 && 向下滑动时，可下来刷新
        //若正在刷新时，上滑，下拉刷新控件需要消耗事件，不往下传递事件
        boolean isCanPtr = isDragDown && mStatus==STATUS_EXPANDED && isRecyclerTop();
        refreshview.disallowInterceptTouchEvent(!isCanPtr);
    }

    private boolean isRecyclerTop(){
        if(recyclerview!=null){
            RecyclerView.LayoutManager layoutManager = recyclerview.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerview.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition <= 1 && childAt.getTop() == 0)) {
                    return true;
                }
            }
        }
        return false;
    }
}

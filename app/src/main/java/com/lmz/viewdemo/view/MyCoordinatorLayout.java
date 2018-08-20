package com.lmz.viewdemo.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.lmz.viewdemo.Utils.Util;
import com.lmz.viewdemo.iface.AppBarLayoutObserved;
import com.lmz.viewdemo.iface.ScrollableContainer;

public class MyCoordinatorLayout extends CoordinatorLayout{
    private final String TAG = "MyCoordinatorLayout";
    private AppBarLayoutObserved observed;
    private XRefreshView xRefreshView;
    private NestedScrollView nestedScrollView;
    private ScrollableContainer currentScrollableContainer;
    private boolean isTouchPointInBannerView;

    public MyCoordinatorLayout(Context context) {
        super(context);
        init();
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    public void setAppBarLayoutObserved(AppBarLayoutObserved observed){
        this.observed = observed;
    }

    public void setCurrentScrollableContainer(ScrollableContainer scrollableContainer){
        this.currentScrollableContainer = scrollableContainer;
    }

    public void setxRefreshView(XRefreshView xRefreshView){
        this.xRefreshView = xRefreshView;
    }

    public void setNestedScrollView(NestedScrollView nestedScrollView){
        this.nestedScrollView = nestedScrollView;
    }

    private int mLastX,mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //内部拦截法
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(xRefreshView!=null){
                    xRefreshView.disallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if(Math.abs(deltaY)>Math.abs(deltaX)){
//                    if(observed!=null && observed.getAppBarLayoutStatus() == 1 && deltaY>0 && xRefreshView!=null && isTop()){
//                        //垂直向下滑动，appBarLayout展开状态，列表第一个item可见，将事件交还给父容器XRefreshView
//                        xRefreshView.disallowInterceptTouchEvent(false);
//                    }

                    // 注意：若此时item位置不是第一个可见时，不能下拉刷新，若不需要item位置第一个可见时就可以下拉刷新，可以把isTop判断去掉，若下所示：
                    if(observed!=null && observed.getAppBarLayoutStatus() == 1 && deltaY>0 && xRefreshView!=null){
                        //垂直向下滑动，appBarLayout展开状态，将事件交还给父容器XRefreshView
                        xRefreshView.disallowInterceptTouchEvent(false);
                    }else{
                        //判断触摸点是否落在banner上
                        bannerView = getBannerView();
                        if(bannerView!=null){
                            isTouchPointInBannerView = Util.calcViewScreenLocation(bannerView).contains(ev.getRawX(),ev.getRawY());
                        }else{
                            isTouchPointInBannerView = false;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouchPointInBannerView = false;
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        Log.e(TAG,"target:"+target);
//        Log.e(TAG,"super before dy:"+dy);
        super.onNestedPreScroll(target, dx, dy, consumed, type);//必须放在前面调用，后面对父容器消耗的dy进行处理，来解决与子元素的滑动冲突
//        Log.e(TAG,"super after dy:"+dy+",dx:"+dx+",consumed[1]:"+consumed[1]);
        if(consumed[1] == 0 && !isTouchPointInBannerView){
            //AppbarLayout折叠或下滑时，consumed[1]=0,并且触摸点不在Banner上
            nsvMaxOffsetY = getNestedScrollViewMaxOffset();
            if(nsvMaxOffsetY>0 && nestedScrollView !=null){
                //MyNestedScrollView存在最大滑出屏幕的偏移量时，需要对dy消耗进行处理
                if(dy>0){
                    //上滑
                    if(nsvMaxOffsetY == nestedScrollView.getScrollY()){
                        //banner隐藏时，不消耗dy，交给列表，列表滑动dy
                        consumed[1] = 0;
                    }else{
                        //banner可见
                        //触摸点在RecyclerView上时，设置父容器消耗dy,不让列表滑动，同时滚动NestedScrollView
                        consumed[1] = dy;
                        nestedScrollViewScrollBy(0,dy);
                    }

                }else{
                    //下滑
                    if(nestedScrollView.getScrollY() == nsvMaxOffsetY){
                        //banner隐藏
                        if(isTop()){
                            //列表第一个item可见，设置父容器消耗dy,不让列表滑动，同时滚动NestedScrollView
                            consumed[1] = dy;
                            nestedScrollViewScrollBy(0,dy);
                        }else{
                            //列表第一个item不可见，父容器不消耗dy，交给RecyclerView消耗dy
                            consumed[1] = 0;
                        }
                    }else if(nestedScrollView.getScrollY()>0){
                        //banner可见未完全展开
                        //触摸点在RecyclerView上时，设置父容器消耗dy,不让列表滑动，同时滚动NestedScrollView
                        consumed[1] = dy;
                        nestedScrollViewScrollBy(0,dy);
                    }
                }
            }
        }
    }

    private void nestedScrollViewScrollBy(int x, int y){
        if(nestedScrollView!=null){
            int mScrollY = nestedScrollView.getScrollY();
            int desY = y + mScrollY;
            desY = desY>=nsvMaxOffsetY?nsvMaxOffsetY:(desY<=0?0:desY);
            nestedScrollView.scrollTo(x,desY);
        }
    }

    private int nsvMaxOffsetY;//允许NestedScrollView划出屏幕的最大距离
    private int getNestedScrollViewMaxOffset(){
        if(nestedScrollView !=null && nestedScrollView.getChildCount()>0 && nsvMaxOffsetY == 0){
            View child = nestedScrollView.getChildAt(0);
            if(child!=null && child instanceof VerticalLinearLayout){
                nsvMaxOffsetY = ((VerticalLinearLayout) child).getMaxOffsetY();
            }
        }
        return nsvMaxOffsetY;
    }

    public boolean isTop(){
        View scrollableView = getScrollableView();
        if(scrollableView == null){
            return true;
        }
        if(scrollableView instanceof RecyclerView){
            return isRecyclerViewTop((RecyclerView) scrollableView);
        }

        return true;
    }

    private View getScrollableView(){
        if(currentScrollableContainer == null){
            return null;
        }
        return currentScrollableContainer.getScrollableView();
    }

    private boolean isRecyclerViewTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition <= 1 && childAt.getTop() == 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private LinearLayout bannerView;
    private LinearLayout getBannerView(){
        if(bannerView == null && nestedScrollView!=null && nestedScrollView.getChildCount()>0){
            View child = nestedScrollView.getChildAt(0);
            if(child!=null && child instanceof VerticalLinearLayout){
                VerticalLinearLayout verticalLinearLayout = (VerticalLinearLayout) child;
                if(verticalLinearLayout.getChildCount()>0 && verticalLinearLayout.getChildAt(0) instanceof LinearLayout){
                    bannerView =  (LinearLayout) verticalLinearLayout.getChildAt(0);
                }
            }
        }
        return bannerView;
    }

}

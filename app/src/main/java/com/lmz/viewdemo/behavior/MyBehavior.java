package com.lmz.viewdemo.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyBehavior extends AppBarLayout.ScrollingViewBehavior {

    public MyBehavior(){
        super();
    }

    public MyBehavior(Context context, AttributeSet attrs){
        super(context,attrs);
    }

//    private int mLastInterceptX,mLastInterceptY;
//
//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
//        Log.e("test99","mybehavior child top:"+child.getTop()+",canScrollVertically:"+child.canScrollVertically(-1)+",scrollY:"+child.getScrollY());
//
//        boolean intercepted = false;
//        int x = (int) ev.getX();
//        int y = (int) ev.getY();
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                intercepted = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int deltaX = x - mLastInterceptX;
//                int deltaY = y - mLastInterceptY;
//                if (Math.abs(deltaY) > Math.abs(deltaX)) {
//                    //垂直方向滑动
//                    if(child.canScrollVertically(-1) || child.getScrollY()>0){
//                        intercepted = false;
//                    }else{
//                        intercepted = true;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                intercepted = false;
//                break;
//            default:
//                break;
//        }
//
//        mLastInterceptX = x;
//        mLastInterceptY = y;
//        return intercepted;
////        return super.onInterceptTouchEvent(parent, child, ev);
//    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.e("test99","MyBehavior onNestedPreScroll dy:"+ dy);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}

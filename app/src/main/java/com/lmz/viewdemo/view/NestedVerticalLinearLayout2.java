package com.lmz.viewdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class NestedVerticalLinearLayout2 extends LinearLayout implements NestedScrollingChild,NestedScrollingParent {

    private NestedScrollingChildHelper mChildHelper;
    private NestedScrollingParentHelper mParentHelper;
    private int maxOffsetY;

    public NestedVerticalLinearLayout2(Context context) {
        super(context);
        init();
    }

    public NestedVerticalLinearLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedVerticalLinearLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mChildHelper = new NestedScrollingChildHelper(this);
        mParentHelper = new NestedScrollingParentHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        View child;
        int childCount = getChildCount();
        int offset = 0;
        for(int i=0;i<childCount;i++){
            child = getChildAt(i);
            if(child!=null && child.getVisibility()!=View.GONE
                    && !(child instanceof ViewPager)  && !(child instanceof SmartTabLayout)){
                measureChildWithMargins(child,widthMeasureSpec,0,MeasureSpec.UNSPECIFIED,0);
                offset = offset + child.getMeasuredHeight();
            }
        }
        this.maxOffsetY = offset;//可滑出屏幕的最大距离
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(specSize + offset,MeasureSpec.EXACTLY));
    }

    public int getMaxOffsetY(){
        return maxOffsetY;
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        //滑动开始的调用
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        //表示本次处理结束
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        //Child 先询问 Parent 是否需要滑动
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }


    //NestedScrollingParent
//
//    // 当开启、停止嵌套滚动时被调用
//    @Override
//    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        Log.e("test99","-----onStartNestedScroll()----");
////        Log.e("test99","child:"+child);
////        Log.e("test99","target:"+target);
////        Log.e("test99","nestedScrollAxes:"+nestedScrollAxes);
//
//        if(nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && target instanceof RecyclerView){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//
//    @Override
//    public void onNestedScrollAccepted(View child, View target, int axes) {
//        Log.e("test99","-----onNestedScrollAccepted()----");
//        mParentHelper.onNestedScrollAccepted(child,target,axes);
//    }
//
//    @Override
//    public void onStopNestedScroll(View target) {
//        Log.e("test99","-----onStopNestedScroll()----");
////        Log.e("test99","child:"+target);
//        mParentHelper.onStopNestedScroll(target);
//    }
//
//    /**
//     * 在滑动事件产生但是子 view 还没处理前可以调用 dispatchNestedPreScroll(0,dy,consumed,offsetInWindow)
//     * 这个方法把事件传给父 view 这样父 view 就能在onNestedPreScroll 方法里面收到子 view 的滑动信息，
//     * 然后做出相应的处理把处理完后的结果通过 consumed 传给子 view
//     * @param target
//     * @param dx
//     * @param dy
//     * @param consumed
//     */
//    @Override
//    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        Log.e("test99","-----onNestedPreScroll()----");
////        Log.e("test99","child:"+target);
//        Log.e("test99","dx:"+dx+",dy:"+dy);
//        dispatchNestedPreScroll(dx, dy, consumed, null);
//    }
//
//    @Override
//    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        //Child 滑动以后，会调用
//        Log.e("test99","-----onNestedScroll()----");
////        final int oldScrollY = getScrollY();
////        scrollBy(0, dyUnconsumed);
////        final int myConsumed = getScrollY() - oldScrollY;
////        final int myUnconsumed = dyUnconsumed - myConsumed;
////        dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
//        dispatchNestedScroll(0,dyConsumed,0,dyUnconsumed,null);
//    }

}

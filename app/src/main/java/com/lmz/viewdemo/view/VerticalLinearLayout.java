package com.lmz.viewdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class VerticalLinearLayout extends LinearLayout{

    private int maxOffsetY;

    public VerticalLinearLayout(Context context) {
        super(context);
        init();
    }

    public VerticalLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

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

}

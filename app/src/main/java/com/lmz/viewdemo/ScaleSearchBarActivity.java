package com.lmz.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import com.andview.refreshview.XRefreshView;
import com.lmz.viewdemo.adapter.ListAdapter;
import com.lmz.viewdemo.view.StickyLayout;
import com.lmz.viewdemo.view.XRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScaleSearchBarActivity extends AppCompatActivity {

    @BindView(R.id.rlSearchBar)
    RelativeLayout rlSearchBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.stickyLayout)
    StickyLayout stickyLayout;
    @BindView(R.id.rlSearchBarCover)
    RelativeLayout rlSearchBar_cover;
    @BindView(R.id.refreshLayout)
    XRefreshView refreshLayout;

    private XRefreshLayout xRefreshLayout;
    private ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_search_bar);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        stickyLayout.setRlSearchBarCover(rlSearchBar_cover);
        xRefreshLayout = new XRefreshLayout(this);
        refreshLayout.setCustomHeaderView(xRefreshLayout);
        refreshLayout.setPinnedContent(true);
    }

    private void initData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new ListAdapter(this);
        recyclerview.setAdapter(adapter);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }


    private void initListener() {
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
//                if(recyclerview!=null){
//                    RecyclerView.LayoutManager layoutManager = recyclerview.getLayoutManager();
//                    if (layoutManager instanceof LinearLayoutManager) {
//                        int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//                        if(firstVisibleItemPosition == 0){
//                            View child = recyclerview.getChildAt(0);
//                            if(child!=null && child.getTop()>=0){
//                                return true;
//                            }
//                        }
//                    }
//                }
                return isRecyclerTop();
            }
        });
        stickyLayout.setXRefreshView(refreshLayout);
        refreshLayout.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.stopRefresh();
                    }
                },1000);
            }
        });
    }

    private boolean isRecyclerTop(){
        if(recyclerview!=null){
            RecyclerView.LayoutManager layoutManager = recyclerview.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if(firstVisibleItemPosition == 0){
                    View child = recyclerview.getChildAt(0);
                    if(child!=null && child.getTop()>=0){
                        return true;
                    }
                }
            }
        }
        return false;
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
        boolean isCanPtr = xRefreshLayout.isRefreshing() || (isDragDown && stickyLayout.isAllExpanded() && isRecyclerTop());
        refreshLayout.disallowInterceptTouchEvent(!isCanPtr);
    }
}

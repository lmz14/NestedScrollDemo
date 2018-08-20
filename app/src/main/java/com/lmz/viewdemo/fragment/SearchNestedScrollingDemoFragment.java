package com.lmz.viewdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmz.viewdemo.R;
import com.lmz.viewdemo.ScaleSearchNestedViewPagerActivity;
import com.lmz.viewdemo.iface.ScrollableContainer;
import com.lmz.viewdemo.adapter.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchNestedScrollingDemoFragment extends Fragment implements ScrollableContainer {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private ScaleSearchNestedViewPagerActivity mActivity;
    private ListAdapter adapter;
    private int index;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            this.mActivity = (ScaleSearchNestedViewPagerActivity) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            this.mActivity = (ScaleSearchNestedViewPagerActivity) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nested_scroll_demo,null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {

    }

    private GridLayoutManager gridLayoutManager;
    private void initView() {
        gridLayoutManager = new GridLayoutManager(mActivity,1);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new ListAdapter(mActivity,mActivity.getListData((index+1)*10));
        recyclerview.setAdapter(adapter);
    }

    public void setIndex(int index){
        this.index = index;
    }

    @Override
    public View getScrollableView() {
        return recyclerview;
    }

    public boolean isTop(){
        if (gridLayoutManager!=null && gridLayoutManager instanceof LinearLayoutManager) {
            int firstVisibleItemPosition = ((LinearLayoutManager) gridLayoutManager).findFirstVisibleItemPosition();
            View childAt = recyclerview.getChildAt(0);
            if (childAt == null || (firstVisibleItemPosition <= 1 && childAt.getTop() == 0)) {
                return true;
            }
        }
        return false;
    }
}

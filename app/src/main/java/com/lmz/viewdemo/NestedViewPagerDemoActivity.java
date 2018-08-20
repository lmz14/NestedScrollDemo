package com.lmz.viewdemo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lmz.viewdemo.adapter.NestedScrollingFragmentAdapter;
import com.lmz.viewdemo.fragment.NestedScrollingDemoFragment;
import com.lmz.viewdemo.view.MyNestedScrollView2;
import com.lmz.viewdemo.view.NestedVerticalLinearLayout2;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NestedViewPagerDemoActivity extends AppCompatActivity{

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.NestedScrollingView)
    NestedVerticalLinearLayout2 NestedScrollingView;
    @BindView(R.id.nestedScrollView)
    MyNestedScrollView2 nestedScrollView;
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;

    private List<NestedScrollingDemoFragment> fragments;
    private NestedScrollingFragmentAdapter fragmentAdapter;
    private List<String> tabs;
    private int tabCount = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scrolling_demo);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        tabs = new ArrayList<>();
        tabs.add("精选");
        tabs.add("女装");
        tabs.add("男装");
        tabs.add("鞋包");
        fragments = new ArrayList<>();
        for(int i=0;i<tabCount;i++){
            NestedScrollingDemoFragment fragment = new NestedScrollingDemoFragment();
            fragment.setIndex(i);
            fragments.add(fragment);
        }

    }

    private void initView() {
        fragmentAdapter = new NestedScrollingFragmentAdapter(getSupportFragmentManager(),fragments);
        viewpager.setAdapter(fragmentAdapter);
        nestedScrollView.setCurrentScrollableContainer(fragments.get(0));
        smartTabLayout.setCustomTabView(new MyTabProvider());
        smartTabLayout.setViewPager(viewpager);
    }

    private void initListener(){
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                nestedScrollView.setCurrentScrollableContainer(fragments.get(position));
            }
        });

    }

    public List<String> getListData(int count){
        List<String> listData = new ArrayList<>();
        for(int i=0;i<count;i++){
            listData.add("Item-"+i);
        }
        return listData;
    }

    private class MyTabProvider implements SmartTabLayout.TabProvider{

        private LayoutInflater inflater;

        public MyTabProvider(){
            inflater = LayoutInflater.from(NestedViewPagerDemoActivity.this);
        }

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            View view = inflater.inflate(R.layout.category_tab,container,false);
            TextView textView = view.findViewById(R.id.tvTabText);
            textView.setText(tabs.get(position));
            return view;
        }
    }

}

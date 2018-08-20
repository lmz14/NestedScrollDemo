package com.lmz.viewdemo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lmz.viewdemo.fragment.NestedScrollingDemoFragment;
import com.lmz.viewdemo.fragment.SearchNestedScrollingDemoFragment;

import java.util.List;

public class SearchNestedScrollingFragmentAdapter extends FragmentStatePagerAdapter{

    private List<SearchNestedScrollingDemoFragment> fragments;

    public SearchNestedScrollingFragmentAdapter(FragmentManager fm, List<SearchNestedScrollingDemoFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments!=null && position < fragments.size()?fragments.get(position):null;
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.size():0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "test"+position;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object.getClass().getName().equals(SearchNestedScrollingDemoFragment.class.getName())){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}

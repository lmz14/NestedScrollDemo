package com.lmz.viewdemo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lmz.viewdemo.fragment.NestedScrollingDemoFragment;
import com.lmz.viewdemo.fragment.SearchNestedScrollingDemoFragment;

import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class SearchNestedScrollingFragmentAdapter extends BaseFragmentAdapter<SearchNestedScrollingDemoFragment>{


    public SearchNestedScrollingFragmentAdapter(FragmentManager fm, List<SearchNestedScrollingDemoFragment> fragments) {
        super(fm,fragments);
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

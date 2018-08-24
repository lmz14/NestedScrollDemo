package com.lmz.viewdemo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lmz.viewdemo.fragment.NestedScrollingDemoFragment;

import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class NestedScrollingFragmentAdapter extends BaseFragmentAdapter<NestedScrollingDemoFragment>{

    public NestedScrollingFragmentAdapter(FragmentManager fm,List<NestedScrollingDemoFragment> fragments) {
        super(fm,fragments);
        this.fragments = fragments;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "test"+position;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object.getClass().getName().equals(NestedScrollingDemoFragment.class.getName())){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}

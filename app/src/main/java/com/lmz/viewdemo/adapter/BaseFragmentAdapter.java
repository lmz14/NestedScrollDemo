package com.lmz.viewdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/24
 * @description
 */
public abstract class BaseFragmentAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    protected List<T> fragments;

    public BaseFragmentAdapter(FragmentManager fm, List<T> fragments) {
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

}

package com.rikkathewrold.rikkamusic.main.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rikkathewrold.rikkamusic.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 多Fragment切换、高复用的PagerAdapter
 */
public class MultiFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "MultiFragmentPagerAdapt";

    private List<BaseFragment> fragments = new ArrayList<>();

    public MultiFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void init(List<BaseFragment> fragmentList) {
        fragments.clear();
        fragments.addAll(fragmentList);
    }

    @Override
    public Fragment getItem(int i) {
        if (fragments != null && i < fragments.size()) {
            return fragments.get(i);
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (getItem(position) instanceof BaseFragment) {
            return ((BaseFragment) getItem(position)).getTitle();
        }
        return super.getPageTitle(position);
    }
}

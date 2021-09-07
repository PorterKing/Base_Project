package com.porterking.commonlibrary.ui;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Poterking on 19-7-9.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] mFragments;

    public ViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

}

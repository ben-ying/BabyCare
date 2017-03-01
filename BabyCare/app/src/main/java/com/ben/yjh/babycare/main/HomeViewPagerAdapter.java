package com.ben.yjh.babycare.main;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.setting.SettingFragment;

import java.util.Stack;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventListFragment.newInstance();
            case 1:
                return SettingFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page" + position;
    }
}

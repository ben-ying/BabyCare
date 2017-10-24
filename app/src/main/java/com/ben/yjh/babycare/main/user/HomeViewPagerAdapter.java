package com.ben.yjh.babycare.main.user;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ben.yjh.babycare.base.BaseFragment;

import java.util.List;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<BaseFragment> mFragments;

    public HomeViewPagerAdapter(Context context, FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.mContext = context;
        this.mFragments = fragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle(mContext);
    }
}

package com.ben.yjh.babycare.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;

import java.util.List;

public class ImagePagerActivity extends BaseActivity {

    private ViewPager mViewPager;
    private List<String> mUrls;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurrentPosition = getIntent().getIntExtra(Constants.POSITION, 0);
        mUrls = getIntent().getStringArrayListExtra(Constants.IMAGE_URLS);
        ImageViewpagerAdapter pagerAdapter = new ImageViewpagerAdapter(this, mUrls);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
    }

    @Override
    public void onClick(View v) {

    }
}

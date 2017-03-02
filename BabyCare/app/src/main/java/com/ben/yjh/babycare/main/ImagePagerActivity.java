package com.ben.yjh.babycare.main;

import android.annotation.SuppressLint;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;

public class ImagePagerActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageViewpagerAdapter pagerAdapter = new ImageViewpagerAdapter(this, "1,1,1");
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}

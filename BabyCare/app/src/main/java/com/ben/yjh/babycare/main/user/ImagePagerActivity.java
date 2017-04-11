package com.ben.yjh.babycare.main.user;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerActivity extends BaseActivity {

    private static final int UI_ANIMATION_DELAY = 300;
    private Handler mHideHandler = new Handler();

    private ViewPager mViewPager;
    private List<String> mUrls;
    private String mCurrentUrl;
    private View mRootView;
    private final Runnable mShowStatusBarRunnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable mHideStatusBarRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mRootView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        mRootView = findViewById(R.id.content_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurrentUrl = getIntent().getStringExtra(Constants.IMAGE_URL);
        mUrls = getIntent().getStringArrayListExtra(Constants.IMAGE_URLS);
        if (mUrls == null || mUrls.size() == 0) {
            mUrls = new ArrayList<>();
            mUrls.add(mCurrentUrl);
        }
        ImageViewpagerAdapter pagerAdapter = new ImageViewpagerAdapter(this, mUrls);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mUrls.indexOf(mCurrentUrl));
    }

    @Override
    protected void onResume() {
        super.onResume();
        hide();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.removeCallbacks(mShowStatusBarRunnable);
        mHideHandler.postDelayed(mHideStatusBarRunnable, UI_ANIMATION_DELAY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}

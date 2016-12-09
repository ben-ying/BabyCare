package com.ben.yjh.babycare.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.util.ScalePageTransformer;
import com.ben.yjh.babycare.widget.ClipViewPager;

public class MainActivity extends AppCompatActivity {

    private ClipViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ClipViewPager) findViewById(R.id.viewpager);
        mViewPager.setSpeedScroller(300);
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(20);

        findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
    }
}

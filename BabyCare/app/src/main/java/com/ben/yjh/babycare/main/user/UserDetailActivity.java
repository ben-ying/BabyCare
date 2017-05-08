package com.ben.yjh.babycare.main.user;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends BaseActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomeViewPagerAdapter mPagerAdapter;
    private EventListFragment mEventListFragment;
    private UserDetailFragment mUserDetailFragment;
    private ImageView mProfileImageView;
    private TextView mNameTextView;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mUserId = getIntent().getIntExtra(Constants.USER_ID, Constants.INVALID_VALUE);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        List<BaseFragment> fragments = new ArrayList<>();
        mUserDetailFragment = UserDetailFragment.newInstance();
        mEventListFragment = EventListFragment.newInstance(mUserId);
        fragments.add(mUserDetailFragment);
        fragments.add(mEventListFragment);
        mPagerAdapter = new HomeViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mTabLayout.setupWithViewPager(mViewPager);
        mProfileImageView = (ImageView) findViewById(R.id.img_profile);
        mProfileImageView.setOnClickListener(this);
        mNameTextView = ((TextView) findViewById(R.id.tv_name));

        getUserDetail();
    }

    private void getUserDetail() {
        new UserTaskHandler(this).getUserDetail(User.getUser().getToken(), mUserId,
                new HttpResponseInterface<User>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(User classOfT) {
                        user = classOfT;
                        MyApplication.getInstance(UserDetailActivity.this).displayImage(
                                user.getProfile(), mProfileImageView, ImageUtils
                                        .getProfileImageOptions(UserDetailActivity.this), false);
                        mNameTextView.setText(user.getBabyName());
                        ((AppBarLayout) findViewById(R.id.app_bar_scrolling))
                                .addOnOffsetChangedListener(
                                        new AppBarLayout.OnOffsetChangedListener() {
                                    int scrollRange = Constants.INVALID_VALUE;

                                    @Override
                                    public void onOffsetChanged(
                                            AppBarLayout appBarLayout, int verticalOffset) {
                                        if (scrollRange == Constants.INVALID_VALUE) {
                                            scrollRange = appBarLayout.getTotalScrollRange();
                                        }
                                        if (scrollRange + verticalOffset <= toolbar.getHeight()) {
                                            ((CollapsingToolbarLayout) findViewById(
                                                    R.id.toolbar_layout))
                                                    .setTitle(user.getBabyName());
                                            findViewById(R.id.profile_layout)
                                                    .setVisibility(View.GONE);
                                        } else {
                                            ((CollapsingToolbarLayout) findViewById(
                                                    R.id.toolbar_layout)).setTitle("");
                                            findViewById(R.id.profile_layout)
                                                    .setVisibility(View.VISIBLE);
                                            findViewById(R.id.profile_layout).setAlpha(
                                                    (scrollRange - Math.abs(verticalOffset)
                                                            - toolbar.getHeight()) / (float)
                                                            (scrollRange - toolbar.getHeight()));
                                        }
                                    }
                                });
                        mUserDetailFragment.init();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {

                    }

                    @Override
                    public void onHttpError(String error) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
                    break;
                case Constants.COMMENT_REQUEST_CODE:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}

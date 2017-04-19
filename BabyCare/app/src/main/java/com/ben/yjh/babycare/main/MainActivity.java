package com.ben.yjh.babycare.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.main.event.AddEventActivity;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.left.FeedbackActivity;
import com.ben.yjh.babycare.main.left.SettingActivity;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.HomeViewPagerAdapter;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private HomeViewPagerAdapter mPagerAdapter;
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;
    private EventListFragment mEventListFragment;
    private Runnable mPendingRunnable;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setStatusBarMargin(R.id.cl_layout);

        List<BaseFragment> fragments = new ArrayList<>();
        mEventListFragment = EventListFragment.newInstance(true);
        fragments.add(mEventListFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new HomeViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mTabLayout.setupWithViewPager(mViewPager);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
        if (hasSoftNavBar()) {
            // has navigation bar in bottom
            layoutParams.setMargins((int) getResources().getDimension(R.dimen.fab_margin),
                    (int) getResources().getDimension(R.dimen.fab_margin),
                    (int) getResources().getDimension(R.dimen.fab_margin),
                    (int) getResources().getDimension(R.dimen.fab_margin_with_navigation));
        } else {
            layoutParams.setMargins((int) getResources().getDimension(R.dimen.fab_margin),
                    (int) getResources().getDimension(R.dimen.fab_margin),
                    (int) getResources().getDimension(R.dimen.fab_margin),
                    (int) getResources().getDimension(R.dimen.fab_margin));
        }
//        mFab.setLayoutParams(layoutParams );

        mFab.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFab.show();
                } else {
                    mFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                getActionBar().setTitle(mTitle);
//                invalidateOptionsMenu();

                if (mPendingRunnable != null) {
                    mHandler.post(mPendingRunnable);
                    mPendingRunnable = null;
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initNavigationView();
    }

    private void initNavigationView() {
        user = User.getUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.design_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView nameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        nameTextView.setText(user.getBabyName());
        TextView emailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        emailTextView.setText(user.getEmail());
        ImageView profileImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_profile);
        MyApplication.displayImage(user.getProfile(),
                profileImageView, ImageUtils.getProfileImageOptions(this), false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_personal_info:
                        intent = new Intent(MainActivity.this, UserInfoActivity.class);
                        startActivityForResult(intent, Constants.USER_INFO_REQUEST_CODE);
                        break;
                    case R.id.nav_feedback:
//                        intent = new Intent(Intent.ACTION_SENDTO);
//                        intent.setData(Uri.parse("mailto:bensbabycare@163.com"));
//                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
//                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_message));
//                        if (intent.resolveActivity(getPackageManager()) != null) {
//                            startActivity(intent);
//                        } else {
                            intent = new Intent(MainActivity.this, FeedbackActivity.class);
                            startActivity(intent);
//                        }
                        break;
                    case R.id.nav_message:
                        break;
                    case R.id.nav_share:
                        int stringId = getApplicationInfo().labelRes;
                        String appName = stringId == 0 ? getApplicationInfo()
                                .nonLocalizedLabel.toString() : getString(stringId);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(
                                getString(R.string.share_message), appName, getPackageName()));
                        sendIntent.setType("text/plain");
                        startActivityForResult(sendIntent, Constants.SHARE_APP_REQUEST_CODE);
                        break;
                    case R.id.nav_setting:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivityForResult(intent, Constants.SETTING_REQUEST_CODE);
                        break;
                }
            }
        };

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ADD_EVENT_REQUEST_CODE:
                    break;
                case Constants.USER_INFO_REQUEST_CODE:
                    initNavigationView();
                    break;
            }
        }

        if (mEventListFragment != null) {
            mEventListFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, AddEventActivity.class);
                startActivityForResult(intent, Constants.ADD_EVENT_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFab.performClick();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

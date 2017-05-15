package com.ben.yjh.babycare.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.ben.yjh.babycare.main.event.MediaRecorderActivity;
import com.ben.yjh.babycare.main.left.FeedbackActivity;
import com.ben.yjh.babycare.main.left.SettingActivity;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.HomeViewPagerAdapter;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.VideoConfig;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.VideoUtils;

import java.util.ArrayList;
import java.util.List;

import mabeijianxi.camera.model.MediaRecorderConfig;

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

        initToolbar(0, 0);
        List<BaseFragment> fragments = new ArrayList<>();
        mEventListFragment = EventListFragment.newInstance(user.getUserId());
        fragments.add(mEventListFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new HomeViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mTabLayout.setupWithViewPager(mViewPager);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
//        if (hasSoftNavBar()) {
//            // has navigation bar in bottom
//            layoutParams.setMargins((int) getResources().getDimension(R.dimen.fab_margin),
//                    (int) getResources().getDimension(R.dimen.fab_margin),
//                    (int) getResources().getDimension(R.dimen.fab_margin),
//                    (int) getResources().getDimension(R.dimen.fab_margin_with_navigation));
//        } else {
//            layoutParams.setMargins((int) getResources().getDimension(R.dimen.fab_margin),
//                    (int) getResources().getDimension(R.dimen.fab_margin),
//                    (int) getResources().getDimension(R.dimen.fab_margin),
//                    (int) getResources().getDimension(R.dimen.fab_margin));
//        }
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

    @Override
    protected void onResume() {
        super.onResume();
        mFab.show();
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
        MyApplication.getInstance(this).displayImage(user.getProfile(),
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
                        intent = new Intent(MainActivity.this, FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_message:
                        break;
                    case R.id.nav_share:
                        share();
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

    private void share() {
        int stringId = getApplicationInfo().labelRes;
        String appName = stringId == 0 ? getApplicationInfo()
                .nonLocalizedLabel.toString() : getString(stringId);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
//                        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(
//                                "android.resource://" + getPackageName() + "/mipmap/boy"));
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(
                getString(R.string.share_message), appName, getPackageName()));
        startActivityForResult(Intent.createChooser(sendIntent,
                getString(R.string.share_to)), Constants.SHARE_APP_REQUEST_CODE);
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

    public void showTypeOptions() {
        if (verifyStoragePermissions()) {
            String[] array = getResources().getStringArray(R.array.event_type_choices);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.media_type)
                    .setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent;
                            switch (which) {
                                case 0:
                                    intent = new Intent(MainActivity.this, AddEventActivity.class);
                                    startActivityForResult(intent, Constants.ADD_EVENT_REQUEST_CODE);
                                    break;
                                case 1:
                                    VideoConfig videoConfig = VideoUtils.getVideoConfig(
                                            MainActivity.this, user.getUsername());
                                    MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
//                                            .doH264Compress(compressMode)
//                                            .setMediaBitrateConfig(recordMode)
                                            .smallVideoWidth(videoConfig.getWidth())
                                            .smallVideoHeight(videoConfig.getWidth())
                                            .recordTimeMax(videoConfig.getMaxTime())
                                            .recordTimeMin(videoConfig.getMinTime())
                                            .maxFrameRate(videoConfig.getMaxFrameRate())
                                            .captureThumbnailsTime(videoConfig.getCaptureThumbnailsTime())
                                            .build();
                                    intent = new Intent(MainActivity.this, MediaRecorderActivity.class);
                                    intent.putExtra(Constants.VIDEO_CONFIG, config);
                                    startActivityForResult(intent, Constants.VIDEO_REQUEST_CODE);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                showTypeOptions();
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
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                // back like home button
                moveTaskToBack(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

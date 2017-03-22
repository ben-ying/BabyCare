package com.ben.yjh.babycare.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.ben.yjh.babycare.main.event.AddEventActivity;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.setting.SettingFragment;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.util.AlertUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setStatusBarMargin(R.id.cl_layout);

        babyUser = BabyUser.getBabyUser();
        if (babyUser == null) {
            logout();
        }

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(EventListFragment.newInstance());
        fragments.add(SettingFragment.newInstance());
        fragments.add(EventListFragment.newInstance());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragments);
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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.design_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView nameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        nameTextView.setText(babyUser.getBabyName());
        TextView emailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        emailTextView.setText(babyUser.getEmail());
        ImageView profileImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_profile);
        MyApplication.displayImage(babyUser.getProfile(),
                profileImageView, ImageUtils.getProfileImageOptions(this), false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.nav_personal_info:
                intent = new Intent(this, PersonalInfoActivity.class);
                startActivityForResult(intent, Constants.SETTING_REQUEST_CODE);
                break;
//            case R.id.nav_gallery:
//                break;
            case R.id.nav_feedback:
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
                startActivity(sendIntent);
                break;
//            case R.id.nav_send:
//                break;
            case R.id.nav_logout:
                AlertUtils.showConfirmDialog(this,
                        R.string.logout_message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        });
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        intent = new Intent(this, AddEventActivity.class);
        intent.putExtra(Constants.IMAGE_URI, "http://");
        startActivityForResult(intent, Constants.ADD_EVENT_REQUEST_CODE);
//        switch (v.getId()) {
//            case R.id.fab:
//                showImageOptions(R.string.add_event);
//        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, data.getData(),
                            getResources().getInteger(R.integer.event_width),
                            getResources().getInteger(R.integer.event_height));
                    break;
                case Constants.GALLERY_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, data.getData(),
                            getResources().getInteger(R.integer.event_width),
                            getResources().getInteger(R.integer.event_height));
                    break;
                case Constants.CROP_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData() == null ? ImageUtils.getTempUri() : data.getData();
                    if (uri != null) {
                        Intent intent = new Intent(this, AddEventActivity.class);
                        intent.putExtra(Constants.IMAGE_URI, uri.toString());
                        startActivityForResult(intent, Constants.ADD_EVENT_REQUEST_CODE);
                    }
                    break;
                case Constants.ADD_EVENT_REQUEST_CODE:
                    break;
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

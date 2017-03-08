package com.ben.yjh.babycare.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.login.LoginActivity;
import com.ben.yjh.babycare.login.RegisterActivity;
import com.ben.yjh.babycare.main.event.AddEventActivity;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.setting.SettingFragment;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private HomeViewPagerAdapter mPagerAdapter;
    private FloatingActionButton mFab;
    private TabLayout mTabLayout;
    private BabyUser mBabyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<BabyUser> babyUsers = BabyUser.find(BabyUser.class, "is_login = ?", "1");
        if (babyUsers.size() == 1 && !babyUsers.get(0).getToken().isEmpty()) {
            mBabyUser = babyUsers.get(0);
        } else {
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
        nameTextView.setText(mBabyUser.getBabyName());
        TextView emailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        emailTextView.setText(mBabyUser.getEmail());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
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

    private void logout() {
        if (mBabyUser != null) {
            mBabyUser.setLogin(false);
            mBabyUser.setToken(null);
            mBabyUser.save();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.fab:
                intent = new Intent(this, AddEventActivity.class);
                startActivityForResult(intent, Constants.ADD_EVENT_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ADD_EVENT_REQUEST_CODE) {
                // todo
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

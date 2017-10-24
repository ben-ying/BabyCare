package com.ben.yjh.babycare.main.left;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.glide.GlideUtils;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.user.HomeViewPagerAdapter;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends BaseActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomeViewPagerAdapter mPagerAdapter;
    private EventListFragment mEventListFragment;
    private PersonalInfoFragment mPersonalInfoFragment;
    private RedEnvelopeFragment mRedEnvelopeFragment;
    private ImageView mProfileImageView;
    private TextView mNameTextView;
    private String mProfileBase64;
    private Uri mCameraUri;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initToolbar(0);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.hide();
        List<BaseFragment> fragments = new ArrayList<>();
        mPersonalInfoFragment = PersonalInfoFragment.newInstance();
        mEventListFragment = EventListFragment.newInstance(user.getUserId());
        mRedEnvelopeFragment = RedEnvelopeFragment.newInstance();
        fragments.add(mPersonalInfoFragment);
        fragments.add(mEventListFragment);
        fragments.add(mRedEnvelopeFragment);
        mPagerAdapter = new HomeViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mTabLayout.setupWithViewPager(mViewPager);
        mProfileImageView = (ImageView) findViewById(R.id.img_profile);
        mProfileImageView.setOnClickListener(this);
        GlideUtils.displayCircleImage(this, mProfileImageView, user.getProfile(),
                SharedPreferenceUtils.isGirl(this) ? R.mipmap.girl : R.mipmap.boy);
        mNameTextView = ((TextView) findViewById(R.id.tv_name));
        mNameTextView.setText(user.getBabyName());
        ((AppBarLayout) findViewById(R.id.app_bar_scrolling))
                .addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = Constants.INVALID_VALUE;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == Constants.INVALID_VALUE) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset <= toolbar.getHeight()) {
                    ((CollapsingToolbarLayout) findViewById(
                            R.id.toolbar_layout)).setTitle(user.getBabyName());
                    findViewById(R.id.profile_layout).setVisibility(View.GONE);
                } else {
                    ((CollapsingToolbarLayout) findViewById(
                            R.id.toolbar_layout)).setTitle(getString(R.string.empty));
                    findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.profile_layout).setAlpha(
                            (scrollRange - Math.abs(verticalOffset) - toolbar.getHeight())
                                    / (float) (scrollRange - toolbar.getHeight()));
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    mFab.show();
                } else {
                    mFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.img_profile:
                mCameraUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        Constants.PROFILE_IMAGE_PREFIX + System.currentTimeMillis() + ".jpg"));
                showImageOptions(R.string.upload_picture_option, mCameraUri);
                break;
        }
    }

    public boolean verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, mCameraUri);
                    break;
                case Constants.GALLERY_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, data.getData());
                    break;
                case Constants.AVIARY_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData() == null ? ImageUtils.getTempUri() : data.getData();
                    if (uri != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(uri));
                            mProfileBase64 = ImageUtils.getBase64FromBitmap(bitmap);
                            GlideUtils.displayCircleImage(this, mProfileImageView, uri.toString(),
                                    SharedPreferenceUtils.isGirl(this) ? R.mipmap.girl : R.mipmap.boy);
                            mPersonalInfoFragment.editPersonalInfoTask(R.id.img_profile, mProfileBase64);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            mProfileBase64 = "";
                        }
                    }
                    break;
                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
                    break;
                case Constants.COMMENT_REQUEST_CODE:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mProfileImageView.performClick();
            }
        }
    }
}

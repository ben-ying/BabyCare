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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.main.event.CommentActivity;
import com.ben.yjh.babycare.main.event.EventAdapter;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.user.HomeViewPagerAdapter;
import com.ben.yjh.babycare.main.user.ImagePagerActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

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
    private ImageView mProfileImageView;
    private TextView mNameTextView;
    private boolean mUpdate;
    private String mProfileBase64;
    private Uri mCameraUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        List<BaseFragment> fragments = new ArrayList<>();
        mPersonalInfoFragment = PersonalInfoFragment.newInstance();
        mEventListFragment = EventListFragment.newInstance(false);
        fragments.add(mPersonalInfoFragment);
        fragments.add(mEventListFragment);
        mPagerAdapter = new HomeViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mTabLayout.setupWithViewPager(mViewPager);
        mProfileImageView = (ImageView) findViewById(R.id.img_profile);
        mProfileImageView.setOnClickListener(this);
        MyApplication.displayImage(user.getProfile(),
                mProfileImageView, ImageUtils.getProfileImageOptions(this), false);
        mNameTextView = ((TextView) findViewById(R.id.tv_name));
        mNameTextView.setText(user.getBabyName());
        ((AppBarLayout) findViewById(R.id.app_bar_scrolling))
                .addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset <= toolbar.getHeight()) {
                    ((CollapsingToolbarLayout) findViewById(
                            R.id.toolbar_layout)).setTitle(user.getBabyName());
                    findViewById(R.id.profile_layout).setVisibility(View.GONE);
                } else {
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle("");
                    findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.profile_layout).setAlpha(
                            (scrollRange - Math.abs(verticalOffset) - toolbar.getHeight())
                                    / (float) (scrollRange - toolbar.getHeight()));
                }
            }
        });
    }

    private void getUserDetail() {
        new UserTaskHandler(this).getUserDetail(user.getToken(), user.getUserId(),
                new HttpResponseInterface<User>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(User classOfT) {
                        user = classOfT;
                        mPersonalInfoFragment.init();
                        mEventListFragment.init();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {

                    }

                    @Override
                    public void onHttpError(String error) {

                    }
                });
    }

    protected void editPersonalInfoTask(final int id, final String value) {
        String key = null;
        switch (id) {
            case R.id.item_baby_name:
                key = Constants.BABY_NAME;
                break;
            case R.id.item_email:
                key = Constants.EMAIL;
                break;
            case R.id.item_phone:
                key = Constants.PHONE;
                break;
            case R.id.item_gender:
                key = Constants.GENDER;
                break;
            case R.id.item_birth:
                key = Constants.BIRTHDAY;
                break;
            case R.id.item_hobbies:
                key = Constants.HOBBIES;
                break;
            case R.id.img_profile:
                key = Constants.BASE64;
                break;
        }

        if (key != null) {
            new UserTaskHandler(this).editUserInfo(user.getUserId(), key, value, user.getToken(),
                    new HttpResponseInterface<User>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(User classOfT) {
                            user = classOfT;
                            user.save();
                            mUpdate = true;
                            mPersonalInfoFragment.init();
                            mEventListFragment.init();
                        }

                        @Override
                        public void onFailure(HttpBaseResult result) {

                        }

                        @Override
                        public void onHttpError(String error) {

                        }
                    });
        }
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
                            MyApplication.displayImage(uri.toString(),
                                    mProfileImageView, ImageUtils.getProfileImageOptions(this), true);
                            editPersonalInfoTask(R.id.img_profile, mProfileBase64);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            mProfileBase64 = "";
                        }
                    }
                    break;
                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
                    getUserDetail();
                    break;
                case Constants.COMMENT_REQUEST_CODE:
                    getUserDetail();
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

    @Override
    public void finish() {
        if (mUpdate) {
            Intent intent = getIntent();
            intent.putExtra(Constants.BABY_USER, user);
            setResult(RESULT_OK, intent);
        }

        super.finish();
    }
}

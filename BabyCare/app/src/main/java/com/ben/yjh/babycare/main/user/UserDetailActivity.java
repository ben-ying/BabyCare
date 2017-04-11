package com.ben.yjh.babycare.main.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.widget.ItemDetail;
import com.ben.yjh.babycare.widget.ItemInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

public class UserDetailActivity extends BaseActivity {

    private ItemDetail mEmailItem;
    private ItemDetail mPhoneItem;
    private ItemDetail mGenderItem;
    private ItemDetail mBirthItem;
    private ItemDetail mHobbiesItem;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);

        mEmailItem = (ItemDetail) findViewById(R.id.item_email);
        mPhoneItem = (ItemDetail) findViewById(R.id.item_phone);
        mGenderItem = (ItemDetail) findViewById(R.id.item_gender);
        mBirthItem = (ItemDetail) findViewById(R.id.item_birth);
        mHobbiesItem = (ItemDetail) findViewById(R.id.item_hobbies);

        getUserDetail();
    }

    private void getUserDetail() {
        new UserTaskHandler(this).getUserDetail(user.getToken(),
                getIntent().getIntExtra(Constants.USER_ID, 0), new HttpResponseInterface<User>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(User classOfT) {
                mUser = classOfT;
                setValues();
                toolbar.setTitle(mUser.getUsername());
            }

            @Override
            public void onFailure(HttpBaseResult result) {

            }

            @Override
            public void onHttpError(String error) {

            }
        });
    }

    private void setValues() {
        mEmailItem.setValue(R.string.email, mUser.getEmail());
        mPhoneItem.setValue(R.string.phone, mUser.getPhone());
        mGenderItem.setValue(R.string.gender, mUser.getGenderValue(this));
        mBirthItem.setValue(R.string.birth, mUser.getBirth());
        mHobbiesItem.setValue(R.string.hobbies, mUser.getHobbies());
        MyApplication.displayImage(mUser.getProfile(),
                (ImageView) findViewById(R.id.img_profile),
                ImageUtils.getProfileImageOptions(this), false);
        ((TextView) findViewById(R.id.tv_name)).setText(mUser.getBabyName());
    }


    @Override
    public void onClick(final View v) {
    }
}

package com.ben.yjh.babycare.main.event;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aviary.android.feather.common.AviaryIntent;
import com.aviary.android.feather.sdk.FeatherActivity;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.main.GalleryActivity;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

public class AddEventActivity extends BaseActivity {

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private ImageView mImageView;
    private String mTitle;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);
        mTitleEditText = (EditText) findViewById(R.id.et_title);
        mContentEditText = (EditText) findViewById(R.id.et_content);
        mImageView = (ImageView) findViewById(R.id.img_event);

        findViewById(R.id.btn_add).setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.GALLERY_REQUEST_CODE:
                    if (data != null) {
                        String imageUrl = data.getStringExtra(Constants.IMAGE_URL);
                        if (imageUrl != null) {
                            MyApplication.displayImage(Uri.fromFile(new File(imageUrl)).toString(),
                                    mImageView, ImageUtils.getEventImageOptions(), true, new ImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String s, View view) {

                                        }

                                        @Override
                                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                            mImageView.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onLoadingCancelled(String s, View view) {

                                        }
                                    });
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                mTitle = mTitleEditText.getText().toString();
                mContent = mContentEditText.getText().toString();
                if (mTitle.isEmpty()) {
                    mTitleEditText.setError(getString(R.string.empty_title));
                }
                if (mContent.isEmpty()) {
                    mContentEditText.setError(getString(R.string.empty_content));
                }
                // todo
                break;
            case R.id.img_event:
//                mCameraUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
//                        Constants.EVENT_IMAGE_PREFIX + System.currentTimeMillis() + ".jpg"));
//                showImageOptions(R.string.add_event, mCameraUri);
                if (verifyStoragePermissions()) {
                    Intent intent = new Intent(this, GalleryActivity.class);
                    startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mImageView.performClick();
            }
        }
    }
}

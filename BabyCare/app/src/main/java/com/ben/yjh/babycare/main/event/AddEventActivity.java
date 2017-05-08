package com.ben.yjh.babycare.main.event;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.user.GalleryActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddEventActivity extends BaseActivity {

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private ImageView mImageView;
    private String mTitle;
    private String mContent;
    private String mBase64Image;
    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mTitleEditText = (EditText) findViewById(R.id.et_title);
        mContentEditText = (EditText) findViewById(R.id.et_content);
        mImageView = (ImageView) findViewById(R.id.img_event);

        findViewById(R.id.btn_add).setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    private void postEventTask() {
        List<String> base64Images = new ArrayList<>();
        if (mBase64Image != null) {
            base64Images.add(mBase64Image);
        }
        new EventTaskHandler(this, user.getToken()).addEvent(user.getUserId(),
                mTitle, mContent, base64Images, new HttpResponseInterface<Event>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Event classOfT) {
                        Log.d("", "");
                        classOfT.setImage1(mImageUrl);
                        classOfT.save();
                        Intent intent = getIntent();
                        intent.putExtra(Constants.EVENT, classOfT);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }

    private void setImage(Intent intent) {
        if (intent != null) {
            mImageUrl = intent.getStringExtra(Constants.IMAGE_URL);
            if (mImageUrl != null) {
                MyApplication.getInstance(this).displayImage(Uri.fromFile(new File(mImageUrl)).toString(),
                        mImageView, ImageUtils.getEventImageOptions(
                                AddEventActivity.this), true, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                mImageView.setImageBitmap(bitmap);
                                mBase64Image = ImageUtils.getBase64FromBitmap(bitmap);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.GALLERY_REQUEST_CODE:
                    setImage(data);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                mTitle = mTitleEditText.getText().toString().trim();
                mContent = mContentEditText.getText().toString().trim();
                if (!mContent.isEmpty() || mBase64Image != null) {
                    postEventTask();
                }
                break;
            case R.id.img_event:
                Intent intent = new Intent(AddEventActivity.this, GalleryActivity.class);
                startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
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

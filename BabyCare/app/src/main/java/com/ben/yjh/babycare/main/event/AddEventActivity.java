package com.ben.yjh.babycare.main.event;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

public class AddEventActivity extends BaseActivity {

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private ImageView mImageView;
    private String mTitle;
    private String mContent;
    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setTitle(R.string.add_event);
        mTitleEditText = (EditText) findViewById(R.id.et_title);
        mContentEditText = (EditText) findViewById(R.id.et_content);
        mImageView = (ImageView) findViewById(R.id.img_event);
        mImageUrl = getIntent().getStringExtra(Constants.IMAGE_URI);
        MyApplication.displayImage(mImageUrl, mImageView, ImageUtils.getEventImageOptions(this), true);

        findViewById(R.id.btn_add).setOnClickListener(this);
        mImageView.setOnClickListener(this);
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
                showImageOptions(R.string.add_event);
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

package com.ben.yjh.babycare.main.event;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;

public class AddEventActivity extends BaseActivity {

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private ImageView mImageView;
    private String mTitle;
    private String mContent;
    private String mImageUrl;
    private Uri mCameraUri;

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
        mImageUrl = getIntent().getStringExtra(Constants.IMAGE_URI);
        MyApplication.displayImage(mImageUrl, mImageView, ImageUtils.getEventImageOptions(), true);

        findViewById(R.id.btn_add).setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    Intent newIntent = new Intent(this, FeatherActivity.class);
                    // high resolution
//                    newIntent.putExtra(com.aviary.android.feather.library
//                            .Constants.EXTRA_IN_HIRES_MEGAPIXELS, MegaPixels.Mp5.ordinal());
                    newIntent.setData(mCameraUri);
                    newIntent.putExtra(AviaryIntent.EXTRA_API_KEY_SECRET, Constants.AVIARY_PICTURE_REQUEST_CODE);
                    startActivityForResult(newIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);
                    break;
                case Constants.GALLERY_PICTURE_REQUEST_CODE:
                    Intent anewIntent = new Intent(this, FeatherActivity.class);
                    // high resolution
//                newIntent.putExtra( com.aviary.android.feather.library
//                        .Constants.EXTRA_IN_HIRES_MEGAPIXELS, MegaPixels.Mp5.ordinal() );
                    anewIntent.setData(data.getData());
                    anewIntent.putExtra(AviaryIntent.EXTRA_API_KEY_SECRET, Constants.AVIARY_API_KEY_SECRET);
                    startActivityForResult(anewIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);
                    break;
                case Constants.AVIARY_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData();
                    Bundle extra = data.getExtras();
                    if (null != extra) {
                        // image has been changed by the user?
                        boolean changed = extra.getBoolean(com.aviary.android.feather.library
                                .Constants.EXTRA_OUT_BITMAP_CHANGED);
                    }
                    MyApplication.displayImage(Uri.fromFile(new File(uri.toString())).toString(),
                            mImageView, ImageUtils.getEventImageOptions(), false);
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
//                mCameraUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
//                        Constants.EVENT_IMAGE_PREFIX + System.currentTimeMillis() + ".jpg"));
//                showImageOptions(R.string.add_event, mCameraUri);
                Intent intent = new Intent(this, GalleryActivity.class);
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

package com.ben.yjh.babycare.main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import com.aviary.android.feather.common.AviaryIntent;
import com.aviary.android.feather.sdk.FeatherActivity;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity implements GalleryAdapter.GalleryInterface {

    private GalleryAdapter mAdapter;
    private ArrayList<String> mUrls;
    private Uri mCameraUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.select_image);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        String[] columns1 = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns1, null, null, orderBy + " DESC");
        int count = cursor.getCount();
        mUrls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            mUrls.add(Uri.fromFile(new File(cursor.getString(dataColumnIndex))).toString());
        }

        mAdapter = new GalleryAdapter(this, mUrls, this);
        gridView.setAdapter(mAdapter);
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void intent2Gallery(String url) {
        Intent newIntent = new Intent(this, FeatherActivity.class);
//        high resolution
//        newIntent.putExtra(com.aviary.android.feather.library
//                .Constants.EXTRA_IN_HIRES_MEGAPIXELS, MegaPixels.Mp5.ordinal());
        newIntent.setData(Uri.parse(url));
        newIntent.putExtra(AviaryIntent.EXTRA_API_KEY_SECRET, Constants.AVIARY_PICTURE_REQUEST_CODE);
        startActivityForResult(newIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);
    }

    @Override
    public void intent2Camera() {
        mCameraUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                Constants.EVENT_IMAGE_PREFIX + System.currentTimeMillis() + ".jpg"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
        startActivityForResult(intent, Constants.CAMERA_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    Intent newIntent = new Intent(this, FeatherActivity.class);
                    // high resolution
                    // newIntent.putExtra(com.aviary.android.feather.library
                    //       .Constants.EXTRA_IN_HIRES_MEGAPIXELS, MegaPixels.Mp5.ordinal());
                    newIntent.setData(mCameraUri);
                    newIntent.putExtra(AviaryIntent.EXTRA_API_KEY_SECRET, Constants.AVIARY_PICTURE_REQUEST_CODE);
                    startActivityForResult(newIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);
                    break;
                case Constants.AVIARY_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData();
                    Bundle extra = data.getExtras();
                    if (null != extra) {
                        // image has been changed by the user?
                        boolean changed = extra.getBoolean(com.aviary.android.feather.library
                                .Constants.EXTRA_OUT_BITMAP_CHANGED);
                    }
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra(Constants.IMAGE_URL, uri.toString());
                    finish();
                    break;
                case Constants.ADD_EVENT_REQUEST_CODE:
                    break;
            }
        }
    }
}

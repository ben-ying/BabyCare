package com.ben.yjh.babycare.main.user;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;

import com.aviary.android.feather.common.AviaryIntent;
import com.aviary.android.feather.sdk.FeatherActivity;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends BaseActivity implements GalleryAdapter.GalleryInterface {

    private GalleryAdapter mAdapter;
    private ArrayList<String> mUrls;
    private Uri mCameraUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initToolbar(R.string.select_image);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        String[] columns1 = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns1, null, null, orderBy + " DESC");
        if (cursor != null) {
            int count = cursor.getCount();
            mUrls = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                File file = new File(cursor.getString(dataColumnIndex));
                if (file.length() != 0) {
                    mUrls.add(Uri.fromFile(file).toString());
                }
            }

            mAdapter = new GalleryAdapter(this, mUrls, this);
            gridView.setAdapter(mAdapter);
            cursor.close();
        }
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
//        Intent newIntent = new Intent(this, FeatherActivity.class);
////        high resolution
////        newIntent.putExtra(com.aviary.android.feather.library
////                .Constants.EXTRA_IN_HIRES_MEGAPIXELS, MegaPixels.Mp5.ordinal());
//        newIntent.setData(Uri.parse(url));
//        newIntent.putExtra(AviaryIntent.EXTRA_API_KEY_SECRET, Constants.AVIARY_PICTURE_REQUEST_CODE);
//        startActivityForResult(newIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);

        Uri uri = Uri.parse(url);
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        intent.putExtra(Constants.IMAGE_URL, uri.getPath());
        finish();
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
//                    Intent newIntent = new Intent(this, FeatherActivity.class);
//                    // high resolution
//                    // newIntent.putExtra(com.aviary.android.feather.library
//                    //       .Constants.EXTRA_IN_HIRES_MEGAPIXELS, MegaPixels.Mp5.ordinal());
//                    newIntent.setData(mCameraUri);
//                    newIntent.putExtra(AviaryIntent.EXTRA_API_KEY_SECRET, Constants.AVIARY_PICTURE_REQUEST_CODE);
//                    startActivityForResult(newIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra(Constants.IMAGE_URL, mCameraUri.getPath());
                    finish();
                    break;
                case Constants.AVIARY_PICTURE_REQUEST_CODE:
//                    Uri uri = data.getData();
//                    Bundle extra = data.getExtras();
//                    if (null != extra) {
//                        // image has been changed by the user?
//                        boolean changed = extra.getBoolean(com.aviary.android.feather.library
//                                .Constants.EXTRA_OUT_BITMAP_CHANGED);
//                    }
//                    Intent intent = getIntent();
//                    setResult(RESULT_OK, intent);
//                    intent.putExtra(Constants.IMAGE_URL, uri.toString());
//                    finish();
                    break;
                case Constants.ADD_EVENT_REQUEST_CODE:
                    break;
            }
        }
    }
}

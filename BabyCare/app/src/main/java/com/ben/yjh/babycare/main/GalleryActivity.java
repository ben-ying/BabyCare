package com.ben.yjh.babycare.main;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity {

    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        user = User.getBabyUser();
        if (user == null) {
            logout();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.select_image);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        String[] columns = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA};
        String orderBy = MediaStore.Images.Thumbnails._ID;
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
        int count = cursor.getCount();
        List<Bitmap> bitmaps = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int id = cursor.getInt(columnIndex);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
//            bitmaps.add(MediaStore.Images.Thumbnails.getThumbnail(
//                    getApplicationContext().getContentResolver(), id,
//                    MediaStore.Images.Thumbnails.MICRO_KIND, null));
            urls.add(Uri.fromFile(new File(cursor.getString(dataColumnIndex))).toString());

        }
        Log.d("TEST", "time: " + (System.currentTimeMillis() - time));
        mAdapter = new GalleryAdapter(GalleryActivity.this, urls);
        gridView.setAdapter(mAdapter);
        cursor.close();
    }

    class GenerateBitmap extends AsyncTask<Void, Void, Bitmap> {
        Cursor cursor;
        int position;

        public GenerateBitmap(Cursor cursor, int position) {
            this.cursor = cursor;
            this.position = position;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), position,
                    MediaStore.Images.Thumbnails.MINI_KIND, null);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            mAdapter.addItem(bitmap, position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
}

package com.ben.yjh.babycare.main.left;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.BaseTaskHandler;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.NavigationTaskHandler;
import com.ben.yjh.babycare.main.GalleryActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends BaseActivity {

    public static final int MAX_IMAGE_SIZE = 3;
    public static String DEFAULT_ADD_IMAGE = "drawable://" + R.drawable.ic_add_off;

    private GridView mGridView;
    private EditText mDescriptionEditText;
    private String mDescription;
    private ArrayList<String> mImageUrls;
    private ArrayList<String> mBase64Images;
    private int mCurrentPosition;
    private FeedbackImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.feedback);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);

        findViewById(R.id.btn_send).setOnClickListener(this);
        mDescriptionEditText = (EditText) findViewById(R.id.et_feedback);
        mImageUrls = new ArrayList<>();
        mBase64Images = new ArrayList<>();
        mImageUrls.add(DEFAULT_ADD_IMAGE);
        mGridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new FeedbackImageAdapter(this, mImageUrls);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPosition = position;
                if (verifyStoragePermissions()) {
                    Intent intent = new Intent(FeedbackActivity.this, GalleryActivity.class);
                    startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
                }
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!mImageUrls.get(position).equals(DEFAULT_ADD_IMAGE)) {
                    String[] array = getResources().getStringArray(R.array.feedback_image_choices);
                    new AlertDialog.Builder(FeedbackActivity.this)
                            .setItems(array, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            mCurrentPosition = position;
                                            if (verifyStoragePermissions()) {
                                                Intent intent = new Intent(FeedbackActivity.this, GalleryActivity.class);
                                                startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
                                            }
                                            break;
                                        case 1:
                                            mImageUrls.remove(position);
                                            mImageUrls.remove(DEFAULT_ADD_IMAGE);
                                            mImageUrls.add(DEFAULT_ADD_IMAGE);
                                            mAdapter.setImageUrls(mImageUrls);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .show();
                }
                return true;
            }
        });
    }

    private void sendFeedback() {
        new NavigationTaskHandler(this, user.getToken()).sendFeedback(mDescription,
                mBase64Images, new HttpResponseInterface<HttpBaseResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(HttpBaseResult classOfT) {
                        Toast.makeText(FeedbackActivity.this,
                                classOfT.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(FeedbackActivity.this, GalleryActivity.class);
                startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.GALLERY_REQUEST_CODE:
                    if (data != null) {
                        String url = data.getStringExtra(Constants.IMAGE_URL);
                        if (url != null) {
                            Uri uri = Uri.fromFile(new File(url));
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(
                                        getContentResolver().openInputStream(uri));
                                mBase64Images.add(ImageUtils.getBase64FromBitmap(bitmap));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            mImageUrls.set(mCurrentPosition, uri.toString());
                            if (mImageUrls.size() < MAX_IMAGE_SIZE) {
                                mImageUrls.remove(DEFAULT_ADD_IMAGE);
                                mImageUrls.add(DEFAULT_ADD_IMAGE);
                            }
                            mAdapter.setImageUrls(mImageUrls);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                mDescription = mDescriptionEditText.getText().toString().trim();
                if (mBase64Images.size() > 0 || !mDescription.isEmpty()) {
                    sendFeedback();
                }
                break;
        }
    }
}

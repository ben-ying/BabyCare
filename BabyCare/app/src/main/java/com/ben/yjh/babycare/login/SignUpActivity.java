package com.ben.yjh.babycare.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.model.Baby;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.io.FileNotFoundException;
import java.util.List;

public class SignUpActivity extends BaseActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private EditText mUsernameEditText;
    private EditText mBabyNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private String mUsername;
    private String mBabyName;
    private String mEmail;
    private String mPassword;
    private String mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mBabyNameEditText = (EditText) findViewById(R.id.et_baby_name);
        mEmailEditText = (EditText) findViewById(R.id.et_email);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.et_confirm_password);
        findViewById(R.id.tv_link_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.profile_layout).setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private boolean isValid() {
        mUsername = mUsernameEditText.getText().toString().trim();
        mBabyName = mBabyNameEditText.getText().toString().trim();
        mEmail = mEmailEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString().trim();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if (mUsername.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_username);
            return false;
        }
        if (mBabyName.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_baby_name);
            return false;
        }
        if (mEmail.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_email);
            return false;
        }
        if (mPassword.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_password);
            return false;
        }
        if (mPassword.length() < 6) {
            AlertUtils.showAlertDialog(this, R.string.invalid_password);
            return false;
        }
        if (!mPassword.equals(mConfirmPassword)) {
            AlertUtils.showAlertDialog(this, R.string.invalid_confirm_password);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            AlertUtils.showAlertDialog(this, R.string.invalid_email);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findViewById(R.id.profile_layout).performClick();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(SignUpActivity.this, data.getData());
                    break;
                case Constants.GALLERY_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(SignUpActivity.this, data.getData());
                    break;
                case Constants.CROP_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(uri));
                            if (bitmap != null) {
                                findViewById(R.id.profile_layout).setBackground(
                                        new BitmapDrawable(getResources(), bitmap));
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_link_login:
                onBackPressed();
                break;
            case R.id.btn_register:
                if (isValid()) {
                    new UserTaskHandler(this).register(mUsername, mBabyName, mPassword, mEmail,
                            new HttpResponseInterface<Baby>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(Baby classOfT) {
                            List<Baby> babies = Baby.listAll(Baby.class);
                            for (Baby baby : babies) {
                                baby.setLogin(false);
                                baby.save();
                            }
                            classOfT.setLogin(true);
                            classOfT.save();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(HttpBaseResult result) {
                        }

                        @Override
                        public void onHttpError(String error) {
                        }
                    });
                }
                break;
            case R.id.profile_layout:
                if (verifyStoragePermissions()) {
                    String[] array = getResources().getStringArray(R.array.picture_choices);
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle(R.string.upload_picture_option)
                            .setItems(array, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = null;
                                    switch (which) {
                                        case 0:
                                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, Constants.CAMERA_PICTURE_REQUEST_CODE);
                                            break;
                                        case 1:
                                            intent = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(intent, Constants.GALLERY_PICTURE_REQUEST_CODE);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .show();
                }
                break;
        }
    }
}

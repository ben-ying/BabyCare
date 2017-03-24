package com.ben.yjh.babycare.login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.UserHistory;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class RegisterActivity extends BaseActivity {

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
    private ImageButton mProfileButton;
    private String mProfileBase64;
    private Uri mCameraUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.register);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mBabyNameEditText = (EditText) findViewById(R.id.et_baby_name);
        mEmailEditText = (EditText) findViewById(R.id.et_email);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.et_confirm_password);
        mProfileButton = (ImageButton) findViewById(R.id.ib_profile);
        mProfileButton.setOnClickListener(this);
        TextView loginTextView = (TextView) findViewById(R.id.tv_link_login);
        loginTextView.setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
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
                mProfileButton.performClick();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, mCameraUri);
                    break;
                case Constants.GALLERY_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, data.getData());
                    break;
                case Constants.AVIARY_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData() == null ? ImageUtils.getTempUri() : data.getData();
                    if (uri != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(uri));
                            mProfileBase64 = ImageUtils.getBase64FromBitmap(bitmap);
                            MyApplication.displayImage(uri.toString(),
                                    mProfileButton, ImageUtils.getEventImageOptions(), true);
                            findViewById(R.id.tv_add_profile).setVisibility(View.GONE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            mProfileBase64 = "";
                        }
                    }
                    break;
            }
        }
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
                            mProfileBase64, new HttpResponseInterface<BabyUser>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(BabyUser classOfT) {
                            classOfT.save();
                            UserHistory.saveUserHistory(mUsername, classOfT);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
            case R.id.ib_profile:
                mCameraUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                Constants.PROFILE_IMAGE_PREFIX + System.currentTimeMillis() + ".jpg"));
                showImageOptions(R.string.upload_picture_option, mCameraUri);
                break;
        }
    }
}

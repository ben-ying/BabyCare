package com.ben.yjh.babycare.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.UserHistory;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;
import com.ben.yjh.babycare.util.SystemUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView mUsernameEditText;
    private EditText mPasswordEditText;
    private String mUsername;
    private String mPassword;
    private ImageView mGenderImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.login);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_link_signup).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        mGenderImageView = (ImageView) findViewById(R.id.img_profile);
        mGenderImageView.setOnClickListener(this);
        List<BabyUser> babyUsers = BabyUser.find(BabyUser.class, "is_login = ?", "1");
        if (babyUsers.size() > 0) {
            if (babyUsers.get(0).getToken().isEmpty()) {
                mGenderImageView.setClickable(false);
                mGenderImageView.setEnabled(false);
                MyApplication.getImageLoader(this).displayImage(babyUsers.get(0).getProfile(),
                        mGenderImageView, ImageUtils.getProfileImageOptions(this), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                mGenderImageView.setClickable(true);
                                mGenderImageView.setEnabled(true);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                mGenderImageView.setClickable(false);
                                mGenderImageView.setEnabled(false);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                mGenderImageView.setClickable(true);
                                mGenderImageView.setEnabled(true);
                            }
                        });
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            mGenderImageView.setClickable(true);
            mGenderImageView.setEnabled(true);
            mGenderImageView.setImageResource(
                    SharedPreferenceUtils.isGirl(this) ? R.mipmap.girl : R.mipmap.boy);
        }
        mUsernameEditText = (AutoCompleteTextView) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);

        List<String> autoStrings = new ArrayList<>();
        List<UserHistory> userHistories = UserHistory.listAll(UserHistory.class);
        for (int i = 0; i < userHistories.size() && i < 5; i++) {
            autoStrings.add(userHistories.get(i).getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_dropdown, autoStrings);
        mUsernameEditText.setAdapter(adapter);
        mUsernameEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUsernameEditText.clearFocus();
                mPasswordEditText.setText("");
                mPasswordEditText.requestFocus();
                SystemUtils.showKeyboard(LoginActivity.this, mPasswordEditText);

                List<UserHistory> userHistories = UserHistory.find(UserHistory.class,
                        "username = ?", mUsernameEditText.getText().toString());
                if (userHistories.size() > 0) {
                    mGenderImageView.setClickable(false);
                    mGenderImageView.setEnabled(false);
                    MyApplication.getImageLoader(LoginActivity.this).displayImage(userHistories.get(0).getProfile(),
                            mGenderImageView, ImageUtils.getProfileImageOptions(LoginActivity.this,
                                    userHistories.get(0).getGender() == 1 ? R.mipmap.girl : R.mipmap.boy));
                }
            }
        });
    }

    private boolean isValid() {
        mUsername = mUsernameEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString().trim();

        if (mUsername.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_username);
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

        return true;
    }

    private void loginTask() {
        new UserTaskHandler(this).login(mUsername, mPassword,
                new HttpResponseInterface<BabyUser>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(BabyUser classOfT) {
                        classOfT.save();
                        UserHistory.saveUserHistory(mUsername, classOfT);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void sendVerifyCodeTask(final String email) {
        new UserTaskHandler(this).sendVerifyCode(email,
                new HttpResponseInterface<HttpBaseResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(HttpBaseResult classOfT) {
//                        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
//                        intent.putExtra(Constants.EMAIL, email);
//                        startActivityForResult(intent, Constants.RESET_PASSWORD_REQUEST_CODE);
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
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btn_login:
                if (isValid()) {
                    loginTask();
                }
                break;
            case R.id.tv_link_signup:
                intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, Constants.SIGN_UP_REQUEST_CODE);
                break;
            case R.id.img_profile:
                if (SharedPreferenceUtils.isGirl(this)) {
                    mGenderImageView.setImageResource(R.mipmap.boy);
                    SharedPreferenceUtils.saveGender(this, false);
                } else {
                    mGenderImageView.setImageResource(R.mipmap.girl);
                    SharedPreferenceUtils.saveGender(this, true);
                }
                break;
            case R.id.tv_forgot_password:
                final View view = LayoutInflater.from(this).inflate(R.layout.dialog_email, null);
                final AlertDialog dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                        .setMessage(R.string.dialog_forgot_password)
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create();
                dialog.setCancelable(true);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText emailEditText = (EditText) view.findViewById(R.id.et_email);
                        String sentEmail = emailEditText.getText().toString();
//                        if (sentEmail.isEmpty()) {
//                            emailEditText.setError(getString(R.string.empty_email));
//                        } else if (!Patterns.EMAIL_ADDRESS.matcher(sentEmail).matches()) {
//                            emailEditText.setError(getString(R.string.invalid_email));
//                        } else {
                            // TODO: 3/10/17
//                                    sendVerifyCodeTask(sentEmail);
                            dialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                            intent.putExtra(Constants.EMAIL, sentEmail);
                            startActivityForResult(intent, Constants.RESET_PASSWORD_REQUEST_CODE);
//                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.SIGN_UP_REQUEST_CODE:
                    mUsernameEditText.setText(data.getStringExtra(Constants.EMAIL));
                    mPasswordEditText.setText(data.getStringExtra(Constants.PASSWORD));
                    break;
                case Constants.RESET_PASSWORD_REQUEST_CODE:
                    mUsernameEditText.setText(data.getStringExtra(Constants.EMAIL));
                    mPasswordEditText.setText(data.getStringExtra(Constants.PASSWORD));
                    break;
            }
        }
    }
}

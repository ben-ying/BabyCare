package com.ben.yjh.babycare.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseAllActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.UserHistory;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;
import com.ben.yjh.babycare.util.SystemUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseAllActivity {

    private AutoCompleteTextView mUsernameEditText;
    private EditText mPasswordEditText;
    private String mUsername;
    private String mPassword;
    private ImageView mGenderImageView;
    private int mUserIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.login);
        mUsername = getIntent().getStringExtra(Constants.USERNAME);
        mPassword = getIntent().getStringExtra(Constants.PASSWORD);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_link_signup).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        mGenderImageView = (ImageView) findViewById(R.id.img_profile);
        mGenderImageView.setOnClickListener(this);
        List<User> users = User.find(User.class, "is_login = ?", "1");
        List<UserHistory> userHistories = UserHistory.listAll(UserHistory.class);
        if (users.size() > 0) {
            user = users.get(0);
            for (int i = 0; i < userHistories.size(); i++) {
                if (userHistories.get(i).getUsername().equals(user.getUsername())) {
                    mUserIndex = i;
                }
            }
            if (users.get(0).getToken().isEmpty()) {
                if (users.get(0).getProfile() != null) {
                    MyApplication.getInstance(LoginActivity.this).displayImage(users.get(0).getProfile(),
                            mGenderImageView, ImageUtils.getProfileImageOptions(this), false);
                } else {
                    mGenderImageView.setImageResource(
                            SharedPreferenceUtils.isGirl(this) ? R.mipmap.girl : R.mipmap.boy);
                }
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            mGenderImageView.setImageResource(
                    SharedPreferenceUtils.isGirl(this) ? R.mipmap.girl : R.mipmap.boy);
        }
        mUsernameEditText = (AutoCompleteTextView) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        if (mUsername != null) {
            mUsernameEditText.setText(mUsername);
            mUsernameEditText.setSelection(mUsername.length());
        }
        if (mPassword != null) {
            mPasswordEditText.setText(mPassword);
        }
        List<String> autoStrings = new ArrayList<>();
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
                    MyApplication.getInstance(LoginActivity.this).displayImage(
                            userHistories.get(0).getProfile(), mGenderImageView, ImageUtils.getProfileImageOptions(
                                    userHistories.get(0).getGender() == 1 ? R.mipmap.girl : R.mipmap.boy), false);
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
        if (mPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            AlertUtils.showAlertDialog(this, R.string.invalid_password);
            return false;
        }

        return true;
    }

    private void loginTask() {
        new UserTaskHandler(this).login(mUsername, mPassword,
                new HttpResponseInterface<User>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(User classOfT) {
                        classOfT.save();
                        UserHistory.saveUserHistory(classOfT);
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

    private void sendVerifyCodeTask(final String email, final AlertDialog dialog) {
        new UserTaskHandler(this).sendVerifyCode(email,
                new HttpResponseInterface<HttpBaseResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(HttpBaseResult classOfT) {
                        dialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                        intent.putExtra(Constants.EMAIL, email);
                        startActivityForResult(intent, Constants.RESET_PASSWORD_REQUEST_CODE);
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
                onClickProfile();
                break;
            case R.id.tv_forgot_password:
                onForgotPassword();
                break;
        }
    }

    private void onForgotPassword() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
        final EditText emailEditText = (EditText) view.findViewById(R.id.et_value);
        emailEditText.setHint(R.string.email);
        emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
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
                String sentEmail = emailEditText.getText().toString();
                if (sentEmail.isEmpty()) {
                    emailEditText.setError(getString(R.string.empty_email));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(sentEmail).matches()) {
                    emailEditText.setError(getString(R.string.invalid_email));
                } else {
                    sendVerifyCodeTask(sentEmail, dialog);
                }
            }
        });
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

    private void onClickProfile() {
        List<UserHistory> userHistories = UserHistory.listAll(UserHistory.class);
        if (user == null || userHistories.size() == 0) {
            if (SharedPreferenceUtils.isGirl(this)) {
                mGenderImageView.setImageResource(R.mipmap.boy);
                SharedPreferenceUtils.saveGender(this, false);
            } else {
                mGenderImageView.setImageResource(R.mipmap.girl);
                SharedPreferenceUtils.saveGender(this, true);
            }
        } else {
            if (userHistories.size() == 1) {
                if (userHistories.get(0).getProfile() == null) {
                    if (SharedPreferenceUtils.isGirl(this)) {
                        mGenderImageView.setImageResource(R.mipmap.boy);
                        SharedPreferenceUtils.saveGender(this, false);
                    } else {
                        mGenderImageView.setImageResource(R.mipmap.girl);
                        SharedPreferenceUtils.saveGender(this, true);
                    }
                } else {
                    mGenderImageView.setClickable(true);
                    mGenderImageView.setEnabled(true);
                }
            } else {
                if (mUserIndex + 1 < userHistories.size()) {
                    mUserIndex++;
                } else {
                    mUserIndex = 0;
                }
                String profile = userHistories.get(mUserIndex).getProfile();
                if (profile != null) {
                    MyApplication.getInstance(LoginActivity.this).displayImage(profile,
                            mGenderImageView, ImageUtils.getProfileImageOptions(this), false);
                } else {
                    mGenderImageView.setImageResource(userHistories.get(
                            mUserIndex).getGender() == 0 ? R.mipmap.boy : R.mipmap.girl);
                }
                mUsernameEditText.setText(userHistories.get(mUserIndex).getUsername());
                mUsernameEditText.clearFocus();
                mPasswordEditText.setText("");
                mPasswordEditText.requestFocus();
            }
        }
    }
}

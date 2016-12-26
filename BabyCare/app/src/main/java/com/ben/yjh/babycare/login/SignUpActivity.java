package com.ben.yjh.babycare.login;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.AlertUtils;

public class SignUpActivity extends BaseActivity {

    private EditText mUsernameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private String mUsername;
    private String mEmail;
    private String mPassword;
    private String mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mEmailEditText = (EditText) findViewById(R.id.et_email);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.et_confirm_password);
        findViewById(R.id.tv_link_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    private boolean isValid() {
        mUsername = mUsernameEditText.getText().toString().trim();
        mEmail = mEmailEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString().trim();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if (mUsername.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_username);
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
        if (!Patterns.EMAIL_ADDRESS.matcher(mUsername).matches()) {
            AlertUtils.showAlertDialog(this, R.string.invalid_email);
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
                    // todo register
                }
                break;
        }
    }
}

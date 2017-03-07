package com.ben.yjh.babycare.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;
import com.ben.yjh.babycare.util.SystemUtils;

import java.util.List;

public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView mUsernameEditText;
    private EditText mPasswordEditText;
    private String mUsername;
    private String mPassword;
    private boolean mIsGirl;
    private ImageView mGenderImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_link_signup).setOnClickListener(this);
        mGenderImageView = (ImageView) findViewById(R.id.iv_gender);
        mGenderImageView.setOnClickListener(this);
        mGenderImageView.setImageResource(SharedPreferenceUtils.isGirl(this) ? R.mipmap.girl : R.mipmap.boy);
        mUsernameEditText = (AutoCompleteTextView) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);

        List<String> autoStrings = SharedPreferenceUtils.getUsernameHistory(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_dropdown, autoStrings);
        mUsernameEditText.setAdapter(adapter);
        mUsernameEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUsernameEditText.clearFocus();
                mPasswordEditText.setText("");
                mPasswordEditText.requestFocus();
                SystemUtils.showKeyboard(LoginActivity.this, mPasswordEditText);
            }
        });
    }

    private boolean isValid() {
        mUsername = mUsernameEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString().trim();
//        SharedPreferenceUtils.saveUsernameHistory(this, mUsername);

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
        if (!Patterns.EMAIL_ADDRESS.matcher(mUsername).matches()) {
            AlertUtils.showAlertDialog(this, R.string.invalid_email);
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btn_login:
                if (isValid()) {
                    // todo login
                }
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.tv_link_signup:
                intent = new Intent(this, SignUpActivity.class);
                startActivityForResult(intent, Constants.SIGN_UP_REQUEST_CODE);
                break;
            case R.id.iv_gender:
                if (SharedPreferenceUtils.isGirl(this)) {
                    mGenderImageView.setImageResource(R.mipmap.boy);
                    SharedPreferenceUtils.saveGender(this, false);
                } else {
                    mGenderImageView.setImageResource(R.mipmap.girl);
                    SharedPreferenceUtils.saveGender(this, true);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SIGN_UP_REQUEST_CODE) {
                mUsernameEditText.setText(data.getStringExtra(Constants.EMAIL));
                mPasswordEditText.setText(data.getStringExtra(Constants.PASSWORD));
            }
        }
    }
}

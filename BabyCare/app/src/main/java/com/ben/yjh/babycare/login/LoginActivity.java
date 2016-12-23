package com.ben.yjh.babycare.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.util.Constants;

public class LoginActivity extends BaseActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_link_signup).setOnClickListener(this);
        mUsernameEditText = (EditText) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btn_login:
                break;
            case R.id.tv_link_login:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, Constants.SIGN_UP_REQUEST_CODE);
                break;
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

package com.ben.yjh.babycare.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.tv_link_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_link_login:
                onBackPressed();
                break;
            case R.id.btn_register:
                break;
        }
    }
}

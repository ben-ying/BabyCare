package com.ben.yjh.babycare.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class ResetPasswordActivity extends BaseActivity {

    private static final int COUNTDOWN_SECONDS = 60;
    private static final String RESET_PASSWORD = "reset_password";

    private int mCount = COUNTDOWN_SECONDS;
    private Timer mTimer;
    private TimerTask mTask;
    private TextView mVerifyCodeTextView;
    private boolean mIsResetPassword;
    private String mEmail;
    private Button mConfirmButton;
    private EditText mVerifyCodeEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private String mVerifyCode;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mIsResetPassword = getIntent().getBooleanExtra(RESET_PASSWORD, false);
        mEmail = getIntent().getStringExtra(Constants.EMAIL);
        setTitle(mIsResetPassword ? R.string.reset_password : R.string.forgot_password_title);
        mVerifyCodeTextView = (TextView) findViewById(R.id.tv_resend_verify_code);
        mVerifyCodeTextView.setOnClickListener(this);
        mConfirmButton = (Button) findViewById(R.id.btn_change_password);
        mConfirmButton.setOnClickListener(this);
        mVerifyCodeEditText = (EditText) findViewById(R.id.et_verify_code);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.et_confirm_password);

        startTimer();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mCount > 0) {
                        mVerifyCodeTextView.setText(String.format(
                                getString(R.string.countdown_resend_message), mCount));
                        mCount--;
                    } else {
                        mVerifyCodeTextView.setText(
                                getResources().getString(R.string.click_resend_verify_code));
                        mVerifyCodeTextView.setEnabled(true);
                        stopTimer();
                        mCount = COUNTDOWN_SECONDS;
                    }
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private void startTimer() {
        stopTimer();
        mVerifyCodeTextView.setEnabled(false);
        mTimer = new Timer();
        mTask = new TimerTask() {
            public void run() {
                if (!mVerifyCodeTextView.isEnabled()) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        };
        mTimer.schedule(mTask, 0, 1000);
    }


    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private void sendVerifyCodeTask() {
        new UserTaskHandler(this).sendVerifyCode(mEmail,
                new HttpResponseInterface<HttpBaseResult>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(HttpBaseResult classOfT) {
                        Toast.makeText(ResetPasswordActivity.this, getResources().getString(
                                R.string.sent_verify_code_message), Toast.LENGTH_LONG).show();
                        startTimer();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }

    private void resetPassword() {
        new UserTaskHandler(this).resetPassword(mEmail, mVerifyCode, mPassword,
                new HttpResponseInterface<HttpBaseResult>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(HttpBaseResult classOfT) {
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.USERNAME, mEmail);
                        intent.putExtra(Constants.PASSWORD, mPassword);
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

    private boolean isValid() {
        mVerifyCode = mVerifyCodeEditText.getText().toString().trim();
        mPassword = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if (mVerifyCode.isEmpty()) {
            AlertUtils.showAlertDialog(this, R.string.empty_verify_code);
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
        if (!mPassword.equals(confirmPassword)) {
            AlertUtils.showAlertDialog(this, R.string.invalid_confirm_password);
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend_verify_code:
                sendVerifyCodeTask();
                break;
            case R.id.btn_change_password:
                if (isValid()) {
                    resetPassword();
                }
                break;
        }
    }
}

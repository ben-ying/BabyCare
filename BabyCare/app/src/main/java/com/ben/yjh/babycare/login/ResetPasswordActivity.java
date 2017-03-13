package com.ben.yjh.babycare.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

public class ResetPasswordActivity extends BaseActivity {

    private static final int COUNTDOWN_SECONDS = 10;
    private static final String RESET_PASSWORD = "reset_password";

    private int mCount = COUNTDOWN_SECONDS;
    private Timer mTimer;
    private TimerTask mTask;
    private TextView mVerifyCodeTextView;
    private boolean mIsResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mIsResetPassword = getIntent().getBooleanExtra(RESET_PASSWORD, false);
        setTitle(mIsResetPassword ? R.string.forgot_password_title : R.string.reset_password);
        mVerifyCodeTextView = (TextView) findViewById(R.id.tv_resend_verify_code);
        mVerifyCodeTextView.setOnClickListener(this);

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
        Toast.makeText(this, getResources().getString(
                R.string.sent_verify_code_message), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend_verify_code:
                startTimer();
                break;
        }
    }
}

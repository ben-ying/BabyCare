package com.ben.yjh.babycare.base;

import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.ben.yjh.babycare.model.User;

public abstract class BaseActivity extends BaseAllActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        user = User.getUser();
        if (user == null) {
            logout();
        }
    }
}

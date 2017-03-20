package com.ben.yjh.babycare.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ben.yjh.babycare.login.LoginActivity;
import com.ben.yjh.babycare.model.BabyUser;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {

    public Toolbar toolbar;
    public BabyUser babyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void initToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        tvTitle = (GothamTextView) findViewById(R.id.tv_toolbar_title);
//
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            setTitle("");
//            toolbar.setNavigationOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
//    }

    public void setStatusBarMargin(int resId) {
        View view = findViewById(resId);
        view.setPadding(view.getPaddingLeft(), getStatusBarHeight(),
                view.getPaddingRight(), view.getPaddingBottom());
    }

    public int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        } else {
            return (int) Math.ceil((Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.M ? 24 : 25) * getResources().getDisplayMetrics().density);
        }
    }

    public boolean hasSoftNavBar() {
        int id = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return getResources().getBoolean(id);
        } else {
            boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !hasMenuKey && !hasBackKey;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void replaceFragment(BaseFragment fragment, int resId, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.screen_left_out, R.anim.screen_right_in,
//                R.anim.screen_left_in, R.anim.screen_right_out);
        transaction.replace(resId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    public void replaceFragment(BaseFragment fragment, int resId) {
        replaceFragment(fragment, resId, true);
    }

    public void logout() {
        if (babyUser != null) {
            babyUser.setLogin(false);
            babyUser.setToken(null);
            babyUser.save();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

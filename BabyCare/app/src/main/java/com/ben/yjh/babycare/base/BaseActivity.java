package com.ben.yjh.babycare.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;

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

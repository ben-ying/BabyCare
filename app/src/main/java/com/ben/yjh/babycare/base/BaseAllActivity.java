package com.ben.yjh.babycare.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.login.LoginActivity;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;

public abstract class BaseAllActivity extends AppCompatActivity implements OnClickListener {

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public Toolbar toolbar;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(int navigationId, int title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            if (title != 0) {
                getSupportActionBar().setTitle(title);
            } else {
                getSupportActionBar().setTitle(R.string.empty);
            }
        }

        if (navigationId != 0) {
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbar.setNavigationIcon(navigationId);
        }
    }

    public void initToolbar(int title) {
        initToolbar(R.mipmap.ic_arrow_back_white_24dp, title);
    }

    public boolean verifyStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

            return false;
        }

        return true;
    }

    public void showImageOptions(int title, final Uri cameraUri) {
        if (verifyStoragePermissions()) {
            String[] array = getResources().getStringArray(R.array.picture_choices);
            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = null;
                            switch (which) {
                                case 0:
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                                    startActivityForResult(intent, Constants.CAMERA_PICTURE_REQUEST_CODE);
                                    break;
                                case 1:
                                    intent = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent, Constants.GALLERY_PICTURE_REQUEST_CODE);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
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
        if (user != null) {
            user.setLogin(false);
            user.setToken(null);
            user.save();
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

package com.ben.yjh.babycare.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.login.UserTaskHandler;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.ItemInfo;

public class PersonalActivity extends BaseActivity {

    private ItemInfo mUserNameItem;
    private ItemInfo mBabyNameItem;
    private ItemInfo mEmailItem;
    private ItemInfo mPhoneItem;
    private ItemInfo mGenderItem;
    private ItemInfo mBirthItem;
    private ItemInfo mZoneItem;
    private ItemInfo mHobbiesItem;
    private boolean mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.user_info);

        babyUser = BabyUser.getBabyUser();
        if (babyUser == null) {
            logout();
        }

        mUserNameItem = (ItemInfo) findViewById(R.id.item_username);
        mBabyNameItem = (ItemInfo) findViewById(R.id.item_baby_name);
        mEmailItem = (ItemInfo) findViewById(R.id.item_email);
        mPhoneItem = (ItemInfo) findViewById(R.id.item_phone);
        mGenderItem = (ItemInfo) findViewById(R.id.item_gender);
        mBirthItem = (ItemInfo) findViewById(R.id.item_birth);
        mZoneItem = (ItemInfo) findViewById(R.id.item_zone);
        mHobbiesItem = (ItemInfo) findViewById(R.id.item_hobbies);
        mUserNameItem.setEnabled(false);
        mBabyNameItem.setOnClickListener(this);
        mEmailItem.setOnClickListener(this);
        mPhoneItem.setOnClickListener(this);
        mGenderItem.setOnClickListener(this);
        mBirthItem.setOnClickListener(this);
        mZoneItem.setOnClickListener(this);
        mHobbiesItem.setOnClickListener(this);

        mBabyNameItem.setOnClickListener(this);
        mEmailItem.setOnClickListener(this);
        mPhoneItem.setOnClickListener(this);
        mGenderItem.setOnClickListener(this);
        mBirthItem.setOnClickListener(this);
        mZoneItem.setOnClickListener(this);
        mHobbiesItem.setOnClickListener(this);

        setValues();
    }

    private void setValues() {
        mUserNameItem.setValue(R.string.username, babyUser.getUsername());
        mBabyNameItem.setValue(R.string.baby_name, babyUser.getBabyName());
        mEmailItem.setValue(R.string.email, babyUser.getEmail());
        mPhoneItem.setValue(R.string.phone, babyUser.getPhone());
        mGenderItem.setValue(R.string.gender, babyUser.getGenderValue(this));
        mBirthItem.setValue(R.string.birth, babyUser.getBirth());
        mZoneItem.setValue(R.string.zone, babyUser.getZone());
        mHobbiesItem.setValue(R.string.hobbies, babyUser.getHobbies());
    }

    private void editPersonalInfoTask(final int id, final String value) {
        String key = null;
        switch (id) {
            case R.id.item_baby_name:
                key = Constants.USERNAME;
                break;
            case R.id.item_email:
                key = Constants.EMAIL;
                break;
            case R.id.item_phone:
                key = Constants.PHONE;
                break;
            case R.id.item_gender:
                key = Constants.GENDER;
                break;
            case R.id.item_birth:
                key = Constants.BIRTHDAY;
                break;
            case R.id.item_zone:
                key = Constants.ZONE;
                break;
            case R.id.item_hobbies:
                key = Constants.HOBBIES;
                break;
        }

        if (key != null) {
            new UserTaskHandler(this).editUserInfo(key, value,
                    new HttpResponseInterface<HttpBaseResult>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(HttpBaseResult classOfT) {
                    switch (id) {
                        case R.id.item_baby_name:
                            babyUser.setBabyName(value);
                            break;
                        case R.id.item_email:
                            babyUser.setEmail(value);
                            break;
                        case R.id.item_phone:
                            babyUser.setPhone(value);
                            break;
                        case R.id.item_gender:
                            babyUser.setGenderStr(PersonalActivity.this, value);
                            break;
                        case R.id.item_birth:
                            babyUser.setBirth(value);
                            break;
                        case R.id.item_zone:
                            babyUser.setZone(value);
                            break;
                        case R.id.item_hobbies:
                            babyUser.setHobbies(value);
                            break;
                    }

                    babyUser.save();
                    mUpdate = true;
                }

                @Override
                public void onFailure(HttpBaseResult result) {

                }

                @Override
                public void onHttpError(String error) {

                }
            });
        }
    }

    @Override
    public void onClick(final View v) {
        if (v instanceof ItemInfo) {
            ItemInfo itemInfo = (ItemInfo) v;
            final View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
            final EditText editText = (EditText) view.findViewById(R.id.et_value);
            editText.setHint(null);
            editText.setText(itemInfo.getValue());
            editText.setSelection(editText.getText().length());
            final AlertDialog dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle(itemInfo.getTitle())
                    .setView(view)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.setCancelable(true);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = v.getId();
                    String value = editText.getText().toString().trim();
                    if (id == R.id.item_email || id == R.id.item_baby_name) {
                        if (value.isEmpty()) {
                            editText.setError(getString(R.string.empty_email));
                        } else if (v.getId() == R.id.item_email &&
                                !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                            editText.setError(getString(R.string.invalid_email));
                        } else {
                            dialog.dismiss();
                            editPersonalInfoTask(id, value);
                        }
                    } else {
                        dialog.dismiss();
                        editPersonalInfoTask(id, value);
                    }
                }
            });
        }
    }

    @Override
    public void finish() {
        if (mUpdate) {
            Intent intent = getIntent();
            intent.putExtra(Constants.BABY_USER, babyUser);
            setResult(RESULT_OK, intent);
        }

        super.finish();
    }

}

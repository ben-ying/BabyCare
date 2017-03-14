package com.ben.yjh.babycare.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.login.RegisterActivity;
import com.ben.yjh.babycare.login.UserTaskHandler;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.widget.ItemInfo;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PersonalInfoActivity extends BaseActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ItemInfo mBabyNameItem;
    private ItemInfo mEmailItem;
    private ItemInfo mPhoneItem;
    private ItemInfo mGenderItem;
    private ItemInfo mBirthItem;
    private ItemInfo mHobbiesItem;
    private boolean mUpdate;
    private FloatingActionButton mFab;
    private String mProfileBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        babyUser = BabyUser.getBabyUser();
        if (babyUser == null) {
            logout();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(babyUser.getUsername());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mBabyNameItem = (ItemInfo) findViewById(R.id.item_baby_name);
        mEmailItem = (ItemInfo) findViewById(R.id.item_email);
        mPhoneItem = (ItemInfo) findViewById(R.id.item_phone);
        mGenderItem = (ItemInfo) findViewById(R.id.item_gender);
        mBirthItem = (ItemInfo) findViewById(R.id.item_birth);
        mHobbiesItem = (ItemInfo) findViewById(R.id.item_hobbies);
        mFab = (FloatingActionButton) findViewById(R.id.fab_scrolling);
        mBabyNameItem.setOnClickListener(this);
        mEmailItem.setOnClickListener(this);
        mPhoneItem.setOnClickListener(this);
        mGenderItem.setOnClickListener(this);
        mBirthItem.setOnClickListener(this);
        mHobbiesItem.setOnClickListener(this);
        mFab.setOnClickListener(this);

        setValues();
    }

    private void setValues() {
        mBabyNameItem.setValue(R.string.baby_name, babyUser.getBabyName(), R.string.edit_baby_name);
        mEmailItem.setValue(R.string.email, babyUser.getEmail(), R.string.edit_email);
        mPhoneItem.setValue(R.string.phone, babyUser.getPhone(), R.string.edit_phone);
        mGenderItem.setValue(R.string.gender, babyUser.getGenderValue(this), R.string.male);
        mBirthItem.setValue(R.string.birth, babyUser.getBirth(), R.string.edit_birth);
        mHobbiesItem.setValue(R.string.hobbies, babyUser.getHobbies(), R.string.edit_hobbies);
        MyApplication.getImageLoader(this).displayImage(babyUser.getProfile(),
                mFab, ImageUtils.getProfileImageOptions(this));
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
                                    babyUser.setGenderStr(PersonalInfoActivity.this, value);
                                    break;
                                case R.id.item_birth:
                                    babyUser.setBirth(value);
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
        final int id = v.getId();

        switch (id) {
            case R.id.item_baby_name:
            case R.id.item_email:
            case R.id.item_phone:
            case R.id.item_hobbies:
                ItemInfo itemInfo = (ItemInfo) v;
                final View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_value);
                editText.setText(itemInfo.getValue());
                editText.setSelection(editText.getText().length());
                if (id == R.id.item_email) {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                } else if (id == R.id.item_phone) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (id == R.id.item_hobbies) {
                    editText.setLines(5);
                }
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
                        String value = editText.getText().toString().trim();
                        if (id == R.id.item_email || id == R.id.item_baby_name) {
                            if (value.isEmpty()) {
                                editText.setError(getString(R.string.empty_email));
                            } else if (id == R.id.item_email &&
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
                break;
            case R.id.item_gender:
                String[] array = getResources().getStringArray(R.array.gender_choices);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.gender)
                        .setItems(array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = null;
                                switch (which) {
                                    case 0:
                                        editPersonalInfoTask(id, "0");
                                        break;
                                    case 1:
                                        editPersonalInfoTask(id, "1");
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.item_birth:
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                editPersonalInfoTask(id, year + "-" + monthOfYear + "-" + dayOfMonth);
                            }
                        }
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH));
                c.add(Calendar.YEAR, -30);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                c.add(Calendar.YEAR, 31);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.fab_scrolling:
                if (verifyStoragePermissions()) {
                    String[] profileArray = getResources().getStringArray(R.array.picture_choices);
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle(R.string.upload_picture_option)
                            .setItems(profileArray, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = null;
                                    switch (which) {
                                        case 0:
                                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent, Constants.CAMERA_PICTURE_REQUEST_CODE);
                                            break;
                                        case 1:
                                            intent = new Intent(Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CAMERA_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, data.getData());
                    break;
                case Constants.GALLERY_PICTURE_REQUEST_CODE:
                    ImageUtils.cropPicture(this, data.getData());
                    break;
                case Constants.CROP_PICTURE_REQUEST_CODE:
                    Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(uri));
                            mProfileBase64 = ImageUtils.getBase64FromBitmap(bitmap);
                            MyApplication.getImageLoader(this).displayImage(uri.toString(),
                                    mFab, ImageUtils.getProfileImageOptions(this));
                            findViewById(R.id.tv_add_profile).setVisibility(View.GONE);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            mProfileBase64 = "";
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFab.performClick();
            }
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

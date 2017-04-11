package com.ben.yjh.babycare.main.left;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.main.event.CommentActivity;
import com.ben.yjh.babycare.main.event.EventAdapter;
import com.ben.yjh.babycare.main.event.EventListFragment;
import com.ben.yjh.babycare.main.user.HomeViewPagerAdapter;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends BaseActivity
        implements EventAdapter.EventRecyclerViewInterface {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomeViewPagerAdapter mPagerAdapter;
    private EventListFragment mEventListFragment;
    private PersonalInfoFragment mPersonalInfoFragment;
    private ImageView mProfileImageView;
    private TextView mNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = new ArrayList<>();
        mPersonalInfoFragment = PersonalInfoFragment.newInstance();
        mEventListFragment = EventListFragment.newInstance();
        fragments.add(mPersonalInfoFragment);
        fragments.add(mEventListFragment);
        mPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        mTabLayout.setupWithViewPager(mViewPager);
        mProfileImageView = (ImageView) findViewById(R.id.img_profile);
        mProfileImageView.setOnClickListener(this);
        MyApplication.displayImage(user.getProfile(),
                mProfileImageView, ImageUtils.getProfileImageOptions(this), false);
        ((TextView) findViewById(R.id.tv_name)).setText(user.getBabyName());
        ((AppBarLayout) findViewById(R.id.app_bar_scrolling))
                .addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShowTitle = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(user.getUsername());
                    isShowTitle = true;
                } else if (isShowTitle) {
                    ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle("");
                    isShowTitle = true;
                }
            }
        });
    }

    private void getUserDetail() {
        new UserTaskHandler(this).getUserDetail(user.getToken(), user.getUserId(),
                new HttpResponseInterface<User>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(User classOfT) {
                        user = classOfT;
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {

                    }

                    @Override
                    public void onHttpError(String error) {

                    }
                });
    }


    @Override
    public void onClick(final View v) {
        final int id = v.getId();

//        switch (id) {
//            case R.id.item_baby_name:
//            case R.id.item_email:
//            case R.id.item_phone:
//            case R.id.item_hobbies:
//                ItemInfo itemInfo = (ItemInfo) v;
//                final View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
//                final EditText editText = (EditText) view.findViewById(R.id.et_value);
//                editText.setText(itemInfo.getValue());
//                if (id == R.id.item_email) {
//                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//                } else if (id == R.id.item_phone) {
//                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                }
//                editText.setSelection(editText.getText().length());
//                final AlertDialog dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
//                        .setTitle(itemInfo.getTitle())
//                        .setView(view)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, null)
//                        .create();
//                dialog.setCancelable(true);
//                dialog.show();
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String value = editText.getText().toString().trim();
//                        if (id == R.id.item_email || id == R.id.item_baby_name) {
//                            if (value.isEmpty()) {
//                                editText.setError(getString(R.string.empty_email));
//                            } else if (id == R.id.item_email &&
//                                    !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
//                                editText.setError(getString(R.string.invalid_email));
//                            } else {
//                                dialog.dismiss();
//                                editPersonalInfoTask(id, value);
//                            }
//                        } else {
//                            dialog.dismiss();
//                            editPersonalInfoTask(id, value);
//                        }
//                    }
//                });
//                break;
//            case R.id.item_gender:
//                String[] array = getResources().getStringArray(R.array.gender_choices);
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.gender)
//                        .setItems(array, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = null;
//                                switch (which) {
//                                    case 0:
//                                        editPersonalInfoTask(id, "0");
//                                        break;
//                                    case 1:
//                                        editPersonalInfoTask(id, "1");
//                                        break;
//                                    default:
//                                        break;
//                                }
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, null)
//                        .show();
//                break;
//            case R.id.item_birth:
//                Calendar c = Calendar.newInstance();
//                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                editPersonalInfoTask(id, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//                            }
//                        }
//                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
//                        .get(Calendar.DAY_OF_MONTH));
//                c.add(Calendar.YEAR, -30);
//                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
//                c.add(Calendar.YEAR, 31);
//                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
//                datePickerDialog.show();
//                break;
//            case R.id.img_profile:
//                mCameraUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
//                        Constants.PROFILE_IMAGE_PREFIX + System.currentTimeMillis() + ".jpg"));
//                showImageOptions(R.string.upload_picture_option, mCameraUri);
//                break;
//        }
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Constants.CAMERA_PICTURE_REQUEST_CODE:
//                    ImageUtils.cropPicture(this, mCameraUri);
//                    break;
//                case Constants.GALLERY_PICTURE_REQUEST_CODE:
//                    ImageUtils.cropPicture(this, data.getData());
//                    break;
//                case Constants.AVIARY_PICTURE_REQUEST_CODE:
//                    Uri uri = data.getData() == null ? ImageUtils.getTempUri() : data.getData();
//                    if (uri != null) {
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(
//                                    getContentResolver().openInputStream(uri));
//                            mProfileBase64 = ImageUtils.getBase64FromBitmap(bitmap);
//                            MyApplication.displayImage(uri.toString(),
//                                    mProfileImageView, ImageUtils.getProfileImageOptions(this), true);
//                            editPersonalInfoTask(R.id.img_profile, mProfileBase64);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                            mProfileBase64 = "";
//                        }
//                    }
//                    break;
//                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
//                    getUserDetail();
//                    break;
//                case Constants.COMMENT_REQUEST_CODE:
//                    getUserDetail();
//                    break;
//            }
//        }
//    }

    @Override
    public void showImageDetail(int position) {
//        List<Event> events = mAdapter.getEvents();
//        Intent intent = new Intent(this, ImagePagerActivity.class);
//        intent.putExtra(Constants.IMAGE_URL, events.get(position).getImage1());
//        startActivityForResult(intent, Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE);
    }

    @Override
    public void intent2CommentList() {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivityForResult(intent, Constants.COMMENT_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mProfileImageView.performClick();
//            }
//        }
    }

    @Override
    public void finish() {
//        if (mUpdate) {
//            Intent intent = getIntent();
//            intent.putExtra(Constants.BABY_USER, user);
//            setResult(RESULT_OK, intent);
//        }

        super.finish();
    }
}

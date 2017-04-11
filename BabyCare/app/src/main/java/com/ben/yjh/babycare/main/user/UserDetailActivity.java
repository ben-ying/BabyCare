package com.ben.yjh.babycare.main.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.widget.ItemDetail;

import java.util.List;

public class UserDetailActivity extends BaseActivity
        implements EventAdapter.EventRecyclerViewInterface {

    private ItemDetail mEmailItem;
    private ItemDetail mPhoneItem;
    private ItemDetail mGenderItem;
    private ItemDetail mBirthItem;
    private ItemDetail mHobbiesItem;
    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);

        mEmailItem = (ItemDetail) findViewById(R.id.item_email);
        mPhoneItem = (ItemDetail) findViewById(R.id.item_phone);
        mGenderItem = (ItemDetail) findViewById(R.id.item_gender);
        mBirthItem = (ItemDetail) findViewById(R.id.item_birth);
        mHobbiesItem = (ItemDetail) findViewById(R.id.item_hobbies);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        getUserDetail();
    }

    private void getUserDetail() {
        new UserTaskHandler(this).getUserDetail(user.getToken(),
                getIntent().getIntExtra(Constants.USER_ID, 0), new HttpResponseInterface<User>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(User classOfT) {
                mUser = classOfT;
                setValues();
                toolbar.setTitle(mUser.getUsername());
//                if (mAdapter == null) {
//                    mAdapter = new EventAdapter(UserDetailActivity.this,
//                            mUser, mUser.getEvents(), UserDetailActivity.this);
//                } else {
//                    mAdapter.setData(mUser.getEvents());
//                }
//                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(HttpBaseResult result) {

            }

            @Override
            public void onHttpError(String error) {

            }
        });
    }

    private void setValues() {
        mEmailItem.setValue(R.string.email, mUser.getEmail());
        mPhoneItem.setValue(R.string.phone, mUser.getPhone());
        mGenderItem.setValue(R.string.gender, mUser.getGenderValue(this));
        mBirthItem.setValue(R.string.birth, mUser.getBirth());
        mHobbiesItem.setValue(R.string.hobbies, mUser.getHobbies());
        MyApplication.displayImage(mUser.getProfile(),
                (ImageView) findViewById(R.id.img_profile),
                ImageUtils.getProfileImageOptions(this), false);
        ((TextView) findViewById(R.id.tv_name)).setText(mUser.getBabyName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
                    break;
                case Constants.COMMENT_REQUEST_CODE:
                    break;
            }

            getUserDetail();
        }
    }

    @Override
    public void showImageDetail(int position) {
        List<Event> events = mAdapter.getEvents();
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(Constants.IMAGE_URL, events.get(position).getImage1());
        startActivityForResult(intent, Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE);
    }

    @Override
    public void intent2CommentList() {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivityForResult(intent, Constants.COMMENT_REQUEST_CODE);
    }

    @Override
    public void onClick(final View v) {
    }
}

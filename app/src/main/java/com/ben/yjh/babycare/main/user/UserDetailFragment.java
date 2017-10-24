package com.ben.yjh.babycare.main.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.ItemDetail;


public class UserDetailFragment extends BaseFragment {

    private ItemDetail mUsernameItem;
    private ItemDetail mBabyNameItem;
    private ItemDetail mEmailItem;
    private ItemDetail mPhoneItem;
    private ItemDetail mGenderItem;
    private ItemDetail mBirthItem;
    private ItemDetail mHobbiesItem;

    public static UserDetailFragment newInstance() {
        Bundle args = new Bundle();
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        mUsernameItem = (ItemDetail) view.findViewById(R.id.item_username);
        mBabyNameItem = (ItemDetail) view.findViewById(R.id.item_baby_name);
        mEmailItem = (ItemDetail) view.findViewById(R.id.item_email);
        mPhoneItem = (ItemDetail) view.findViewById(R.id.item_phone);
        mGenderItem = (ItemDetail) view.findViewById(R.id.item_gender);
        mBirthItem = (ItemDetail) view.findViewById(R.id.item_birth);
        mHobbiesItem = (ItemDetail) view.findViewById(R.id.item_hobbies);

        return view;
    }

    @Override
    public void init() {
        user = activity.user;
        mUsernameItem.setValue(R.string.username, user.getUsername());
        mBabyNameItem.setValue(R.string.baby_name, user.getBabyName());
        mEmailItem.setValue(R.string.email, user.getEmail());
        mPhoneItem.setValue(R.string.phone, user.getPhone());
        mGenderItem.setValue(R.string.gender, user.getGenderValue(activity));
        mBirthItem.setValue(R.string.birth, user.getBirth());
        mHobbiesItem.setValue(R.string.hobbies, user.getHobbies());
        mBabyNameItem.setValue(R.string.baby_name, user.getBabyName());
        mEmailItem.setValue(R.string.email, user.getEmail());
        mPhoneItem.setValue(R.string.phone, user.getPhone());
        mGenderItem.setValue(R.string.gender, user.getGenderValue(activity));
        mBirthItem.setValue(R.string.birth, user.getBirth());
        mHobbiesItem.setValue(R.string.hobbies, user.getHobbies());
    }

    @Override
    public void onClick(final View v) {
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
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getResources().getString(R.string.basic_info);
    }
}

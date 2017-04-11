package com.ben.yjh.babycare.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ben.yjh.babycare.model.User;

public abstract class BaseFragment extends Fragment implements OnClickListener {

    public BaseActivity activity;
    public User user;
    public View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activity = (BaseActivity) getActivity();
        user = activity.user;
        rootView = initView(inflater, container, savedInstanceState);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState);

    public abstract void init();

    public abstract String getTitle(Context context);
}

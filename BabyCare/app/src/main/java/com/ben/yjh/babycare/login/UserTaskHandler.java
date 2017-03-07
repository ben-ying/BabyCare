package com.ben.yjh.babycare.login;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.android.volley.Request;
import com.ben.yjh.babycare.http.BaseTaskHandler;
import com.ben.yjh.babycare.http.HttpPostTask;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.BabyResult;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UserTaskHandler extends BaseTaskHandler {

    private static final String URL_USERS = "users/";

    public UserTaskHandler(Context context) {
        super(context);
    }

    public void register(String username, String babyName, String password, String email,
                            HttpResponseInterface<BabyResult> httpResponseInterface) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("baby_name", babyName);
            map.put("password", password);
            map.put("email", email);
            map.put("gender", SharedPreferenceUtils.isGirl(context) ? "1" : "0");
            map.put("zone", TimeZone.getDefault().getID());
            new HttpPostTask(context).startTask(
                    URL_USERS, Request.Method.POST, map, BabyResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

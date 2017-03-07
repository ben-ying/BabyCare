package com.ben.yjh.babycare.login;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.ben.yjh.babycare.http.BaseTaskHandler;
import com.ben.yjh.babycare.http.HttpPostTask;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.BabyResult;
import com.ben.yjh.babycare.model.BaseResult;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class UserTaskHandler extends BaseTaskHandler {

    private static final String URL_USERS = "users/";

    public UserTaskHandler(Context context) {
        super(context);
    }

    public void register(String username, String babyName, String password, String email,
                            HttpResponseInterface<BabyResult> httpResponseInterface) {

        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("username", username);
            bodyObject.put("baby_name", babyName);
            bodyObject.put("password", password);
            bodyObject.put("email", email);
            bodyObject.put("gender", SharedPreferenceUtils.isGirl(context) ? 1 : 0);
            bodyObject.put("zone", TimeZone.getDefault().getID());
            new HttpPostTask(context).startTask(
                    URL_USERS, bodyObject, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

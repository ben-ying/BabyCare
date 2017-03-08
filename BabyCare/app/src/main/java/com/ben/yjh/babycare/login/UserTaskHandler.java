package com.ben.yjh.babycare.login;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.http.BaseTaskHandler;
import com.ben.yjh.babycare.http.HttpPostTask;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.Baby;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;

import org.json.JSONObject;


public class UserTaskHandler extends BaseTaskHandler {

    private static final String URL_USERS = "users/";
    private static final String URL_USER_LOGIN = "user/login";

    public UserTaskHandler(Context context) {
        super(context);
    }

    public void register(String username, String babyName, String password, String email,
                            HttpResponseInterface<Baby> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("username", username);
            bodyObject.put("baby_name", babyName);
            bodyObject.put("password", password);
            bodyObject.put("email", email);
            bodyObject.put("gender", SharedPreferenceUtils.isGirl(context) ? 1 : 0);
            new HttpPostTask(context).startTask(URL_USERS, Request.Method.POST,
                    bodyObject, Baby.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password,
                         HttpResponseInterface<Baby> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("username", username);
            bodyObject.put("password", password);
            new HttpPostTask(context).startTask(URL_USER_LOGIN, Request.Method.POST,
                    bodyObject, Baby.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

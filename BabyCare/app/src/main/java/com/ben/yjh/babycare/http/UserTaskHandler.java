package com.ben.yjh.babycare.http;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.http.BaseTaskHandler;
import com.ben.yjh.babycare.http.HttpPostTask;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.MD5Utils;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;

import org.json.JSONObject;


public class UserTaskHandler extends BaseTaskHandler {

    private static final String URL_USERS = "users/";
    private static final String URL_USER_LOGIN = "user/login";
    private static final String URL_SEND_VERIFY_CODE = "user/send_verify_code";
    private static final String URL_EDIT_USER_INFO = "user/edit_user_info";

    public UserTaskHandler(Context context) {
        super(context);
    }

    public void register(String username, String babyName, String password, String email, String base64,
                            HttpResponseInterface<BabyUser> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("username", username);
            bodyObject.put("baby_name", babyName);
            bodyObject.put("password", MD5Utils.getMD5ofStr(
                    context.getString(R.string.md5_code) + password).toLowerCase());
            bodyObject.put("email", email);
            bodyObject.put("base64", base64);
            bodyObject.put("gender", SharedPreferenceUtils.isGirl(context) ? 1 : 0);
            new HttpPostTask(context).startTask(URL_USERS, Request.Method.POST,
                    bodyObject, BabyUser.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password,
                         HttpResponseInterface<BabyUser> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("username", username);
            bodyObject.put("password", MD5Utils.getMD5ofStr(
                    context.getString(R.string.md5_code) + password).toLowerCase());
            new HttpPostTask(context).startTask(URL_USER_LOGIN, Request.Method.POST,
                    bodyObject, BabyUser.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVerifyCode(String email, HttpResponseInterface<HttpBaseResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("email", email);
            new HttpPostTask(context).startTask(URL_SEND_VERIFY_CODE, Request.Method.POST,
                    bodyObject, BabyUser.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editUserInfo(String key, String value,
                             HttpResponseInterface<HttpBaseResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put(key, value);
            new HttpPostTask(context).startTask(URL_EDIT_USER_INFO, Request.Method.POST,
                    bodyObject, BabyUser.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
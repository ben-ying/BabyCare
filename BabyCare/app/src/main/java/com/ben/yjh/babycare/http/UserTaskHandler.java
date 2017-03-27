package com.ben.yjh.babycare.http;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.MD5Utils;
import com.ben.yjh.babycare.util.SharedPreferenceUtils;

import org.json.JSONObject;


public class UserTaskHandler extends BaseTaskHandler {

    private static final String URL_USERS = "users/";
    private static final String URL_USER_LOGIN = "user/login";
    private static final String URL_SEND_VERIFY_CODE = "user/send_verify_code";

    public UserTaskHandler(Context context) {
        super(context);
    }

    public void register(String username, String babyName, String password, String email, String base64,
                            HttpResponseInterface<User> httpResponseInterface) {
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
                    bodyObject, User.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password,
                         HttpResponseInterface<User> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("username", username);
            bodyObject.put("password", MD5Utils.getMD5ofStr(
                    context.getString(R.string.md5_code) + password).toLowerCase());
            new HttpPostTask(context).startTask(URL_USER_LOGIN, Request.Method.POST,
                    bodyObject, User.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVerifyCode(String email, HttpResponseInterface<HttpBaseResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("email", email);
            new HttpPostTask(context).startTask(URL_SEND_VERIFY_CODE, Request.Method.POST,
                    bodyObject, HttpBaseResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editUserInfo(int userId, String key, String value, String token,
                             HttpResponseInterface<User> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put(key, value);
            bodyObject.put("token", token);
            new HttpPostTask(context).startTask(URL_USERS + userId, Request.Method.PUT,
                    bodyObject, User.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

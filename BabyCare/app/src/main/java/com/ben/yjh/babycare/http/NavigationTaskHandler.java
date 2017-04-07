package com.ben.yjh.babycare.http;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.HttpBaseResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NavigationTaskHandler extends BaseTaskHandler {

    private static final String URL_SEND_FEEDBACK = "feedback/";

    private String mToken;

    public NavigationTaskHandler(Context context, String token) {
        super(context);
        this.mToken = token;
    }

    public void sendFeedback(String description, ArrayList<String> base64Images,
                             HttpResponseInterface<HttpBaseResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("description", description);
            bodyObject.put("token", mToken);
            if (base64Images.size() > 0) {
                JSONArray jsonArray = new JSONArray();
                for (String base64 : base64Images) {
                    jsonArray.put(base64);
                }
                bodyObject.put("base64_images", jsonArray);
            }
            new HttpPostTask(context).startTask(URL_SEND_FEEDBACK, Request.Method.POST,
                    bodyObject, HttpBaseResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

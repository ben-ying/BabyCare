package com.ben.yjh.babycare.http;


import android.content.Context;

import com.android.volley.Request;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.RedEnvelope;
import com.ben.yjh.babycare.model.RedEnvelopesResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class NavigationTaskHandler extends BaseTaskHandler {

    public static final String URL_ABOUT_US = "about_us/";
    private static final String URL_SEND_FEEDBACK = "feedback/";
    private static final String URL_GET_RED_ENVELOPES = "envelopes/";

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

    public void getRedEnvelopes(String userId,
                                HttpResponseInterface<RedEnvelopesResult> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            new HttpPostTask(context, false).startTask(URL_GET_RED_ENVELOPES +
                            "?token=" + mToken + "&user_id=" + userId, Request.Method.GET,
                    bodyObject, RedEnvelopesResult.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRedEnvelope(String from, String money, String remark,
                               HttpResponseInterface<RedEnvelope> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("money_from", from);
            bodyObject.put("money", money);
            bodyObject.put("remark", remark);
            bodyObject.put("token", mToken);
            new HttpPostTask(context).startTask(URL_GET_RED_ENVELOPES,
                    Request.Method.POST, bodyObject, RedEnvelope.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRedEnvelope(int redEnvelopeId,
                                  HttpResponseInterface<RedEnvelope> httpResponseInterface) {
        try {
            JSONObject bodyObject = new JSONObject();
            bodyObject.put("token", mToken);
            new HttpPostTask(context).startTask(URL_GET_RED_ENVELOPES + redEnvelopeId,
                    Request.Method.DELETE, bodyObject, RedEnvelope.class, true, httpResponseInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

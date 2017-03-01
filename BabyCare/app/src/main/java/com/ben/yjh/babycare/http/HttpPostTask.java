package com.ben.yjh.babycare.http;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.HttpUtils;
import com.ben.yjh.babycare.util.HttpsTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HttpPostTask {

    private static final String DOMAIN = "http://116.62.47.105/babycare";
    private static final String TAG_JSON_OBJ = "tag_json_obj";
    private static final String VERSION = "1.0.0";

    private Context mContext;
    private ProgressBar mProgressBar;

    private void showProgress() {
        mProgressBar = (ProgressBar) ((Activity) mContext).findViewById(R.id.progress_bar);
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        try {
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpPostTask(Context context) {
        this(context, true);
    }

    public HttpPostTask(Context context, boolean showProgressBar) {
        this.mContext = context;
        if (showProgressBar) {
            showProgress();
        }
        HttpsTrustManager.allowAllSSL();
    }

    public <T> void startTask(final String url, JSONObject bodyObject,
                              final HttpResponseInterface httpResponseInterface) {
        startTask(url, bodyObject, HttpResult.class, httpResponseInterface);
    }

    public <T> void startTask(final String url, final JSONObject bodyObject, final Class<T> classOfT,
                              final HttpResponseInterface httpResponseInterface) {
        startTask(url, bodyObject, classOfT, true, httpResponseInterface);
    }

    public <T> void startTask(final String url, final JSONObject bodyObject,
                              final Class<T> classOfT, final boolean showErrorDialog,
                              final HttpResponseInterface httpResponseInterface) {
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        final JSONObject jsonObject = getJsonObject(bodyObject);

        if (httpResponseInterface != null) {
            httpResponseInterface.onStart();
        }

        Log.d("HTTP", "params: " + jsonObject);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DOMAIN + url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("HTTP", "url: " + DOMAIN + url);
                        Log.d("HTTP", "response: " + response);
                        hideProgress();
                        if (httpResponseInterface != null) {
                            HttpResult httpResponse = HttpUtils.getJsonData(response.toString(), HttpResult.class);
                            if (httpResponse.isSuccess()) {
                                httpResponseInterface.onSuccess(httpResponse.getBody());
                            } else {
                                httpResponseInterface.onFailure(httpResponse);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgress();
                    if (httpResponseInterface != null) {
                        httpResponseInterface.onHttpError(error.getMessage());
                        if (error.getMessage() != null && !error.getMessage().trim().isEmpty()) {
                            Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }
        };


        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MyApplication.getInstance(mContext).addToRequestQueue(request, TAG_JSON_OBJ);
    }

    private JSONObject getJsonObject(JSONObject bodyObject) {

        JSONObject jsonObj = new JSONObject();
        try {
            JSONObject jsonSystem = new JSONObject();
            jsonSystem.put("app_version", VERSION);
            jsonSystem.put("system_version", "android-" + Build.VERSION.RELEASE);
            jsonSystem.put("phone_model", getDeviceName());
            jsonObj.put("body", bodyObject);
            jsonObj.put("system", jsonSystem);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}


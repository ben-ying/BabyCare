package com.ben.yjh.babycare.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ben.yjh.babycare.BuildConfig;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.login.LoginActivity;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.HttpUtils;
import com.ben.yjh.babycare.util.HttpsTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TimeZone;

public class HttpPostTask {

//    private static final String DOMAIN = "http://www.bensbabycare.com";
     public static final String DOMAIN = "http://192.168.31.130:8000";
    // public static final String DOMAIN = "http://116.62.47.105";
    public static final String URL = DOMAIN + "/webservice/";
    private static final String TAG_JSON_OBJ = "tag_json_obj";
    private static final String VERSION = "1.0.0";

    private Context mContext;
    private ProgressBar mProgressBar;

    private void showProgress() {
        mProgressBar = (ProgressBar) ((Activity) mContext).findViewById(R.id.progressBar);
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

    HttpPostTask(Context context) {
        this(context, true);
    }

    HttpPostTask(Context context, boolean showProgressBar) {
        this.mContext = context;
        if (showProgressBar) {
            showProgress();
        }
        HttpsTrustManager.allowAllSSL();
    }

    <T> void startTask(final String url, final int method, final JSONObject jsonObject,
                       final Class<T> classOfT, final boolean showErrorDialog,
                       final HttpResponseInterface httpResponseInterface) {
        try {
            jsonObject.put("zone", TimeZone.getDefault().getID());
            jsonObject.put("app_version", VERSION);
            jsonObject.put("system_version", "android-" + Build.VERSION.RELEASE);
            jsonObject.put("phone_model", getDeviceName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (httpResponseInterface != null) {
            httpResponseInterface.onStart();
        }
        if (BuildConfig.DEBUG) {
            Log.e("HTTP", "url: " + URL + url);
            Log.e("HTTP", "params: " + jsonObject);
        }

        JsonObjectRequest request = new JsonObjectRequest(method, URL +
                url.replace(HttpPostTask.URL, ""), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (BuildConfig.DEBUG) {
                    Log.e("HTTP", "response: " + response);
                }
                hideProgress();
                if (httpResponseInterface != null) {
                    HttpBaseResult httpResponse = HttpUtils.getJsonData(
                            response.toString(), HttpBaseResult.class);
                    if (httpResponse.isSuccess()) {
                        onSuccess(response, httpResponse, classOfT, httpResponseInterface);
                    } else {
                        onFailure(url, method, showErrorDialog,
                                jsonObject, httpResponse, classOfT, httpResponseInterface);
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onError(error, httpResponseInterface);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }


        };

        int socketTimeout = 20000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MyApplication.getInstance(mContext).addToRequestQueue(request, TAG_JSON_OBJ);
    }

    private <T> void onSuccess(JSONObject response, HttpBaseResult httpResponse,
                               Class<T> classOfT, HttpResponseInterface httpResponseInterface) {
        try {
            if (classOfT == HttpBaseResult.class) {
                httpResponseInterface.onSuccess(httpResponse);
            } else {
                httpResponseInterface.onSuccess(HttpUtils.getJsonData(
                        response.getJSONObject("result"), classOfT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private <T> void onFailure(final String url, final int method, final boolean showErrorDialog,
                               final JSONObject jsonObject, HttpBaseResult httpResponse,
                               final Class<T> classOfT, final HttpResponseInterface httpResponseInterface) {
        httpResponseInterface.onFailure(httpResponse);
        if (httpResponse.getCode() == Constants.INVALID_ACCESS_TOKEN) {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setMessage(httpResponse.getMessage());
                builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                User user = User.getUser();
                                if (user != null) {
                                    user.setLogin(false);
                                    user.setToken(null);
                                    user.save();
                                }
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        });
                builder.create().show();
            }
        } else if (showErrorDialog) {
            if (!((Activity) mContext).isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setMessage(httpResponse.getMessage());
                builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.setNegativeButton(R.string.retry,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startTask(url, method, jsonObject,
                                        classOfT, true, httpResponseInterface);
                            }
                        });
                builder.create().show();
            }
        }
    }

    private void onError(VolleyError error, HttpResponseInterface httpResponseInterface) {
        hideProgress();
        // report dialog and send email
        if (httpResponseInterface != null) {
            httpResponseInterface.onHttpError(error.getMessage());
            if (error.getMessage() != null && !error.getMessage().trim().isEmpty()) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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


package com.ben.yjh.babycare.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.HttpUtils;
import com.ben.yjh.babycare.util.HttpsTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class HttpPostTask {

    //    private static final String DOMAIN = "http://116.62.47.105/babycare";
    private static final String DOMAIN = "http://192.168.1.131:8000/babycare/";
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

    public <T> void startTask(final String url, final int method, final JSONObject jsonObject,
                              final Class<T> classOfT, final boolean showErrorDialog,
                              final HttpResponseInterface httpResponseInterface) {
//        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024);
//        Network network = new BasicNetwork(new HurlStack());
//        final JSONObject jsonObject = getJsonObject(bodyObject);
//
        if (httpResponseInterface != null) {
            httpResponseInterface.onStart();
        }

        try {
            jsonObject.put("app_version", VERSION);
            jsonObject.put("system_version", "android-" + Build.VERSION.RELEASE);
            jsonObject.put("phone_model", getDeviceName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(method, DOMAIN + url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("HTTP", "url: " + DOMAIN + url);
                        Log.d("HTTP", "response: " + response);
                        hideProgress();
                        if (httpResponseInterface != null) {
                            HttpBaseResult httpResponse = HttpUtils.getJsonData(
                                    response.toString(), HttpBaseResult.class);
                            if (httpResponse.isSuccess()) {
                                try {
                                    httpResponseInterface.onSuccess(
                                            HttpUtils.getJsonData(response.getJSONObject("result"), classOfT));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                httpResponseInterface.onFailure(httpResponse);
                                if (showErrorDialog) {
                                    if (!((Activity) mContext).isFinishing()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                                                .setMessage(httpResponse.getMessage());
                                        builder.setNegativeButton(R.string.ok,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                        builder.setPositiveButton(R.string.retry,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startTask(url, method, jsonObject,
                                                                classOfT, showErrorDialog, httpResponseInterface);
                                                    }
                                                });
                                        builder.create().show();
                                    }
                                }
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

        int socketTimeout = 20000;// 20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MyApplication.getInstance().addToRequestQueue(request, TAG_JSON_OBJ);
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


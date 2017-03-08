package com.ben.yjh.babycare.application;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ben.yjh.babycare.util.CrashHandler;
import com.ben.yjh.babycare.util.LruBitmapCache;
import com.ben.yjh.babycare.widget.VolleySingleton;
import com.orm.SugarApp;


public class MyApplication extends SugarApp {
    private static MyApplication mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }
}
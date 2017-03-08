package com.ben.yjh.babycare.application;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.ben.yjh.babycare.widget.VolleySingleton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;


public class MyApplication extends SugarApp {

    private static MyApplication mInstance;
    private static Context mAppContext;
    private static ImageLoader sImageLoader;

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

    public static ImageLoader getImageLoader(Context context) {
        if (sImageLoader == null) {
            sImageLoader = ImageLoader.getInstance();
            sImageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
        }

        return sImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }
}
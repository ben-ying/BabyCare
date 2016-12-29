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
import com.ben.yjh.babycare.util.CrashHandler;
import com.ben.yjh.babycare.util.LruBitmapCache;
import com.orm.SugarApp;

public class MyApplication extends SugarApp {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static MyApplication sInstance;
    private static Context sContext;

    public MyApplication() {
    }

    private MyApplication(Context context) {
        mRequestQueue = getRequestQueue();
        sContext = context;
        mImageLoader = new ImageLoader(mRequestQueue,
                new LruBitmapCache(LruBitmapCache.getCacheSize(context)));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }

    public ImageLoader getImageLoader(Context context) {
        return mImageLoader;
    }

    public static synchronized MyApplication getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MyApplication(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
            // Instantiate the cache
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        getRequestQueue().add(req);
    }
}
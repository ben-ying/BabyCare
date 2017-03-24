package com.ben.yjh.babycare.application;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.ben.yjh.babycare.widget.VolleySingleton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.orm.SugarApp;


public class MyApplication extends SugarApp {

    private static MyApplication sInstance;
    private static Context sAppContext;
    private static ImageLoader sImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sAppContext = getApplicationContext();

        // aviary

    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sAppContext;
    }


    private static ImageLoader getImageLoader(Context context) {
        if (sImageLoader == null) {
            sImageLoader = ImageLoader.getInstance();
            sImageLoader.init(ImageLoaderConfiguration.createDefault(context.getApplicationContext()));
        }

        return sImageLoader;
    }

    public static void displayImage(String uri, ImageView imageView,
                                    DisplayImageOptions options, boolean removeCache) {
        displayImage(uri, imageView, options, removeCache, null);
    }

    public static void displayImage(String uri, ImageView imageView,
                                    DisplayImageOptions options, boolean removeCache,
                                    ImageLoadingListener listener) {
        if (removeCache) {
            DiskCacheUtils.removeFromCache(uri, MyApplication.getImageLoader(sAppContext).getDiskCache());
            MemoryCacheUtils.removeFromCache(uri, MyApplication.getImageLoader(sAppContext).getMemoryCache());
        }

        getImageLoader(sAppContext).displayImage(uri, imageView, options, listener);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }
}
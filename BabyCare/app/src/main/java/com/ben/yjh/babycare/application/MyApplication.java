package com.ben.yjh.babycare.application;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.aviary.android.feather.common.AviaryIntent;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.VolleySingleton;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.orm.SugarApp;

import java.io.File;


public class MyApplication extends SugarApp {

    private static MyApplication sInstance;
    private static Context sAppContext;
    private static ImageLoader sImageLoader;
    private static ImageLoader sThumbnailImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sAppContext = getApplicationContext();

        // aviary
        Intent cdsIntent = AviaryIntent.createCdsInitIntent(
                getBaseContext(), Constants.AVIARY_API_KEY_SECRET, null);
        startService(cdsIntent);
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
            File cacheDir = StorageUtils.getCacheDirectory(context);
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                    .diskCacheExtraOptions(480, 800, null)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(30 * 1024 * 1024))
                    .memoryCacheSize(30 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                    .diskCacheSize(200 * 1024 * 1024)
                    .diskCacheFileCount(300)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(context)) // default
                    .imageDecoder(new BaseImageDecoder(false)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                    .writeDebugLogs()
                    .build();
            sImageLoader.init(config);
        }

        return sImageLoader;
    }

    private static ImageLoader getThumbnailImageLoader(Context context) {
        if (sThumbnailImageLoader == null) {
            sThumbnailImageLoader = ImageLoader.getInstance();
            File cacheDir = StorageUtils.getCacheDirectory(context);
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(200, 200)
                    .diskCacheExtraOptions(200, 200, null)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(30 * 1024 * 1024))
                    .memoryCacheSize(30 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                    .diskCacheSize(200 * 1024 * 1024)
                    .diskCacheFileCount(300)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(context)) // default
                    .imageDecoder(new BaseImageDecoder(false)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                    .writeDebugLogs()
                    .build();
            sThumbnailImageLoader.init(config);
        }

        return sThumbnailImageLoader;
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

    public static void displayThumbnailImage(String uri, ImageView imageView,
                                    DisplayImageOptions options, boolean removeCache,
                                    ImageLoadingListener listener) {
        if (removeCache) {
            DiskCacheUtils.removeFromCache(uri, MyApplication.getThumbnailImageLoader(sAppContext).getDiskCache());
            MemoryCacheUtils.removeFromCache(uri, MyApplication.getThumbnailImageLoader(sAppContext).getMemoryCache());
        }

        getThumbnailImageLoader(sAppContext).displayImage(uri, imageView, options, listener);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        VolleySingleton.getInstance().getRequestQueue().add(req);
    }
}
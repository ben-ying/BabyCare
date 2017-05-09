package com.ben.yjh.babycare.application;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.aviary.android.feather.common.AviaryIntent;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.volley.VolleySingleton;
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

import mabeijianxi.camera.VCamera;


public class MyApplication extends SugarApp {

    private static MyApplication sInstance;

    private Context mContext;
    private ImageLoader mImageLoader;
    private ImageLoader mThumbnailImageLoader;
    private ImageLoader mTinyImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        // aviary
        Intent cdsIntent = AviaryIntent.createCdsInitIntent(
                getBaseContext(), Constants.AVIARY_API_KEY_SECRET, null);
        startService(cdsIntent);

//        small-video-recorder
        initSmallVideo(this);
    }

    public MyApplication() {
    }

    public MyApplication(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static MyApplication getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MyApplication(context);
        }
        return sInstance;
    }

    private ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
            File cacheDir = StorageUtils.getCacheDirectory(mContext);
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
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
                    .imageDownloader(new BaseImageDownloader(mContext)) // default
                    .imageDecoder(new BaseImageDecoder(false)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                    .writeDebugLogs()
                    .build();
            mImageLoader.init(config);
        }

        return mImageLoader;
    }

    private ImageLoader getThumbnailImageLoader() {
        if (mThumbnailImageLoader == null) {
            mThumbnailImageLoader = ImageLoader.getInstance();
            File cacheDir = StorageUtils.getCacheDirectory(mContext);
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                    .memoryCacheExtraOptions(150, 150)
                    .diskCacheExtraOptions(150, 150, null)
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
                    .imageDownloader(new BaseImageDownloader(mContext)) // default
                    .imageDecoder(new BaseImageDecoder(false)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                    .writeDebugLogs()
                    .build();
            mThumbnailImageLoader.init(config);
        }

        return mThumbnailImageLoader;
    }

    private ImageLoader getTinyImageLoader() {
        if (mTinyImageLoader == null) {
            mTinyImageLoader = ImageLoader.getInstance();
            File cacheDir = StorageUtils.getCacheDirectory(mContext);
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                    .memoryCacheExtraOptions(50, 50)
                    .diskCacheExtraOptions(50, 50, null)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                    .memoryCacheSize(20 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                    .diskCacheSize(100 * 1024 * 1024)
                    .diskCacheFileCount(100)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    .imageDownloader(new BaseImageDownloader(mContext)) // default
                    .imageDecoder(new BaseImageDecoder(false)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                    .writeDebugLogs()
                    .build();
            mTinyImageLoader.init(config);
        }

        return mTinyImageLoader;
    }

    public void displayImage(String uri, ImageView imageView,
                                    DisplayImageOptions options, boolean removeCache) {
        displayImage(uri, imageView, options, removeCache, null);
    }

    public void displayImage(String uri, ImageView imageView,
                                    DisplayImageOptions options, boolean removeCache,
                                    ImageLoadingListener listener) {
        if (removeCache) {
            DiskCacheUtils.removeFromCache(uri, ImageLoader.getInstance().getDiskCache());
            MemoryCacheUtils.removeFromCache(uri, ImageLoader.getInstance().getMemoryCache());
        }

        getImageLoader().displayImage(uri, imageView, options, listener);
    }

    public void displayThumbnailImage(String uri, ImageView imageView,
                                      DisplayImageOptions options, ImageLoadingListener listener) {
        getThumbnailImageLoader().displayImage(uri, imageView, options, listener);
    }

    public void displayTinyImage(String uri,
                                        ImageView imageView, DisplayImageOptions options) {
        getTinyImageLoader().displayImage(uri, imageView, options);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        VolleySingleton.getInstance(mContext).getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        VolleySingleton.getInstance(mContext).getRequestQueue().add(req);
    }

    public static void initSmallVideo(Context context) {
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                VCamera.setVideoCachePath(dcim + Constants.VIDEO_DIR);
//            } else {
//                VCamera.setVideoCachePath(dcim.getPath().replace(
//                        "/sdcard/", "/sdcard-ext/") + Constants.VIDEO_DIR);
//            }
//        } else {
            VCamera.setVideoCachePath(dcim + Constants.VIDEO_DIR);
//        }
        VCamera.setDebugMode(true);
        VCamera.initialize(context);
    }
}
package com.ben.yjh.babycare.application;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Request;
import com.aviary.android.feather.common.AviaryIntent;
import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.Utils;
import com.ben.yjh.babycare.widget.volley.VolleySingleton;
import com.bumptech.glide.request.target.ViewTarget;
import com.orm.SugarApp;

import mabeijianxi.camera.VCamera;


public class MyApplication extends SugarApp {

    private static MyApplication sInstance;

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // avoid java.lang.IllegalArgumentException:
        // You must not call setTag() on a view Glide is targeting
        ViewTarget.setTagId(R.id.glide_loader);
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


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "MyApplication" : tag);
        VolleySingleton.getInstance(mContext).getRequestQueue().add(req);
    }

    public static void initSmallVideo(Context context) {
//        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                VCamera.setVideoCachePath(dcim + Constants.VIDEO_DIR);
//            } else {
//                VCamera.setVideoCachePath(dcim.getPath().replace(
//                        "/sdcard/", "/sdcard-ext/") + Constants.VIDEO_DIR);
//            }
//        } else {
            String dir = Utils.getVideoCacheDir(context).getAbsolutePath();
            VCamera.setVideoCachePath(dir.endsWith(",") ? dir : dir + "/");
//        }
        VCamera.setDebugMode(true);
        VCamera.initialize(context);
    }
}
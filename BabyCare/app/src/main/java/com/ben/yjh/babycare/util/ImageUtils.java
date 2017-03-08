package com.ben.yjh.babycare.util;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageUtils {

    public static void cropPicture(Activity activity, Uri uri) {
        cropPicture(activity, uri, 200, 200);
    }

    public static void cropPicture(Activity activity, Uri uri, int width, int height) {
        if (uri == null) return;

        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", width);
            cropIntent.putExtra("outputY", height);
            cropIntent.putExtra("return-data", false);
            activity.startActivityForResult(cropIntent, Constants.CROP_PICTURE_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(activity, R.string.not_support_crop, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static DisplayImageOptions getProfileImageOptions(Context context) {
        int defaultIconId = SharedPreferenceUtils.isGirl(context) ? R.mipmap.girl : R.mipmap.boy;

        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(180))
                .showImageOnLoading(defaultIconId)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .cacheInMemory(true)
                .cacheOnDisc()
                .build();
    }
}

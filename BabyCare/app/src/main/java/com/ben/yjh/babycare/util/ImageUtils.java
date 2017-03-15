package com.ben.yjh.babycare.util;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static void cropPicture(Activity activity, Uri uri) {
        cropPicture(activity, uri, 300, 300);
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
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            activity.startActivityForResult(cropIntent, Constants.CROP_PICTURE_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(activity, R.string.not_support_crop, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static final Uri getTempUri() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath(), "temp.jpg"));
    }

    public static Bitmap getCircleBitmap(Context context, Bitmap baseBitmap) {
        Paint p = new Paint();
        p.setAntiAlias(true); //去锯齿
        p.setStyle(Paint.Style.FILL);
        Canvas canvas = new Canvas(baseBitmap); //bitmap就是我们原来的图,比如头像
        //SRC 为我们要画到目标图上的图即原图，DST为目标图，
        // 这里的目标图为资源图片（SRC就是那个圆，DST就是baseBitmap）
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER)); //将DST放在圆底背景图上
        int radius = baseBitmap.getWidth(); //假设图片是正方形的
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, p); //r=radius/2, 圆心(r,r)

        return baseBitmap;
    }

    public static DisplayImageOptions getProfileImageOptions(Context context) {
        int defaultIconId = SharedPreferenceUtils.isGirl(context) ? R.mipmap.girl : R.mipmap.boy;

        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(180))
                .showImageOnLoading(defaultIconId)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public static DisplayImageOptions getProfileImageOptions(Context context, int defaultIconId) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(180))
                .showImageOnLoading(defaultIconId)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .cacheInMemory(true)
                .cacheOnDisc()
                .build();
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream out = null;
            try {
                out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.flush();
                out.close();
                byte[] imgBytes = out.toByteArray();
                return Base64.encodeToString(imgBytes, Base64.DEFAULT);
            } catch (Exception e) {
                return null;
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return "";
        }
    }
}

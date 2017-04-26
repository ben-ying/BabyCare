package com.ben.yjh.babycare.util;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

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
            cropIntent.putExtra("aspectX", width);
            cropIntent.putExtra("aspectY", height);
            cropIntent.putExtra("outputX", width);
            cropIntent.putExtra("outputY", height);
            cropIntent.putExtra("return-data", false);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            activity.startActivityForResult(cropIntent, Constants.AVIARY_PICTURE_REQUEST_CODE);
        } catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(activity, R.string.not_support_crop, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static Uri getTempUri() {
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
                .showImageOnLoading(0)
                .showImageOnFail(defaultIconId)
                .showImageForEmptyUri(defaultIconId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public static DisplayImageOptions getTinyProfileImageOptions(Context context) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(
                        (int) context.getResources().getDimension(R.dimen.image_radius)))
                .showImageOnLoading(R.drawable.ic_profile)
                .showImageOnFail(R.drawable.ic_profile)
                .showImageForEmptyUri(R.drawable.ic_profile)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public static DisplayImageOptions getGalleryOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(0)
                .showImageOnFail(0)
                .showImageForEmptyUri(0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public static DisplayImageOptions getEventImageOptions(Context context) {
        return new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(
                        (int) context.getResources().getDimension(R.dimen.image_radius)))
                .showImageOnLoading(0)
                .showImageOnFail(0)
                .showImageForEmptyUri(0)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public static DisplayImageOptions getProfileImageOptions(int defaultIconId) {
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
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return "";
        }
    }

    public static Drawable scaleImage(Context context, Drawable image, float scaleFactor) {
        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable) image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        image = new BitmapDrawable(context.getResources(), bitmapResized);

        return image;
    }

    public static Drawable scale2IconSize(Context context, Drawable image) {
        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        int size = ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getLauncherLargeIconSize();
        Bitmap b = ((BitmapDrawable) image).getBitmap();


        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, size, size, true);

        image = new BitmapDrawable(context.getResources(), bitmapResized);

        return image;
    }
}

package com.ben.yjh.babycare.util;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.ben.yjh.babycare.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static String getBase64FromFile(String path) {
        File tempFile = new File(path);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        byte[] buffer = new byte[10240];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException|NullPointerException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(output.toByteArray(), Base64.DEFAULT);
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

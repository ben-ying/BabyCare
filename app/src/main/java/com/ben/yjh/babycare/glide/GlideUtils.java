package com.ben.yjh.babycare.glide;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;

import java.util.Random;


public class GlideUtils {
    public static final int CIRCLE_SMALL_PROFILE = 15;

    public static void displayImage(Context context, ImageView imageView, String url) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(context.getResources().getInteger(R.integer.glide_version)))
                .into(imageView);
    }

    public static void displayImage(Context context, ImageView imageView,
                                    String url, int placeholder) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(context.getResources().getInteger(R.integer.glide_version)))
                .into(imageView);
    }

    public static void displayImage(Context context, ImageView imageView,
                                    String url, ColorDrawable colorDrawable) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(colorDrawable)
                .error(colorDrawable)
                .fallback(colorDrawable)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(context.getResources().getInteger(R.integer.glide_version)))
                .into(imageView);
    }

    public static void displayRoundedImage(Context context, ImageView imageView,
                                           String url, int placeholder, int radius) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(context.getResources().getInteger(R.integer.glide_version)))
                .transform(new RoundedCornersTransformation(context, radius, 0))
                .into(imageView);
    }

    public static void displayCircleImage(Context context, ImageView imageView,
                                          String url, int placeholder) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .signature(new ObjectKey(context.getResources().getInteger(R.integer.glide_version)))
                .into(imageView);
    }
}

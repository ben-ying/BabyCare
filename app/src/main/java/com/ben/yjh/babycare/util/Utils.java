package com.ben.yjh.babycare.util;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ben.yjh.babycare.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static float dpToPixels(Context context, int dp) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager keyboard = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(editText, 0);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getDateStr(Context context, Date date) {
        Calendar calendar = Calendar.getInstance();
        long time = date.getTime();
        long decimalMilliSeconds = calendar.getTimeInMillis() - date.getTime();

        if (DateUtils.isToday(time)) {
            if (decimalMilliSeconds / 1000 < 60) {
                return context.getString(R.string.seconds_ago);
            } else if (decimalMilliSeconds / 1000 < 60 * 60) {
                return ((int) decimalMilliSeconds / 1000 / 60) + context.getString(R.string.minutes_ago);
            } else if (decimalMilliSeconds / 1000 < 60 * 60 * 24) {
                return ((int) decimalMilliSeconds / 1000 / 60 / 60) + context.getString(R.string.hours_ago);
            }
        } else if (isYesterday(time)) {
            return context.getString(R.string.yesterday);
        } else if (isTheDayBeforeYesterday(time)) {
            return context.getString(R.string.the_day_before_yesterday);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return format.format(date);
    }

    public static boolean isYesterday(long time) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        now.add(Calendar.DATE, -1);

        return now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE);
    }

    public static boolean isTheDayBeforeYesterday(long time) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        now.add(Calendar.DATE, -2);

        return now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE);
    }

    public static String getFormatDate(Context context, String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date utcDate = format.parse(dateStr);
            format.setTimeZone(TimeZone.getDefault());
            String localDate = format.format(utcDate);
            Date date = format.parse(localDate);
            return Utils.getDateStr(context, date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void changeStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(color));
    }

    public static File getVideoCacheDir(Context context) {
        return new File(context.getExternalCacheDir(), "video-cache");
    }

    public static void cleanVideoCacheDir(Context context) throws IOException {
        File videoCacheDir = getVideoCacheDir(context);
        cleanDirectory(videoCacheDir);
    }

    private static void cleanDirectory(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        File[] contentFiles = file.listFiles();
        if (contentFiles != null) {
            for (File contentFile : contentFiles) {
                delete(contentFile);
            }
        }
    }

    private static void delete(File file) throws IOException {
        if (file.isFile() && file.exists()) {
            deleteOrThrow(file);
        } else {
            cleanDirectory(file);
            deleteOrThrow(file);
        }
    }

    private static void deleteOrThrow(File file) throws IOException {
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                throw new IOException(String.format("File %s can't be deleted", file.getAbsolutePath()));
            }
        }
    }
}

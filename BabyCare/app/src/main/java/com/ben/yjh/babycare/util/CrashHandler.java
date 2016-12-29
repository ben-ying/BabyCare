package com.ben.yjh.babycare.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import com.ben.yjh.babycare.R;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    private static CrashHandler sInstance;

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            sInstance = new CrashHandler();
        }
        return sInstance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Log.d("", "");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    public boolean handleException(Throwable ex) {
        if (ex == null || mContext == null) {
            return false;
        }
        final String crashReport = getCrashReport(mContext, ex);
        new Thread() {
            public void run() {
                Looper.prepare();
                sendAppCrashReport(mContext, crashReport);
                Looper.loop();
            }

        }.start();

        return true;
    }

    private void sendAppCrashReport(final Context context, final String crashReport) {
        // todo send to server
    }


    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = getPackageInfo(context);
        StringBuilder exceptionStr = new StringBuilder();
        exceptionStr.append("User:" + SharedPreferenceUtils.getUsername(context));
        exceptionStr.append("\nVersion:" + pinfo.versionName + "(" + pinfo.versionCode + ")");
        exceptionStr.append("\nclientModel:" + Utils.getDeviceName());
        exceptionStr.append("\nAndroid:" + android.os.Build.VERSION.RELEASE + android.os.Build.MODEL);
        exceptionStr.append("\nException:" + ex.getMessage());
        StackTraceElement[] elements = ex.getStackTrace();
        for (StackTraceElement element : elements) {
            exceptionStr.append("\n" + element.toString());
        }
        return exceptionStr.toString();
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }
}

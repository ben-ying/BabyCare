package com.ben.yjh.babycare.util;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager keyboard = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(editText, 0);
        }
    }
}

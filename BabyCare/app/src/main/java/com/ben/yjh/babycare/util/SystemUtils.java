package com.ben.yjh.babycare.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SystemUtils {
    public static void showKeyboard(Activity activity, EditText editText) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager keyboard = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(editText, 0);
        }
    }
}

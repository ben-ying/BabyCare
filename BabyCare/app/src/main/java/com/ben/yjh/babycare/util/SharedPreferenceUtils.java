package com.ben.yjh.babycare.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedPreferenceUtils {

    private static final String USERNAME_HISTORY_NAME = "username_history_name";
    private static final String USERNAME_HISTORY_KEY = "username_history_key";
    private static final String USERNAME_NAME = "username_name";
    private static final String USERNAME_KEY = "username_key";

    public static void saveUsername(Context context, String username) {
        SharedPreferenceUtils.saveSharedPreference(
                context, USERNAME_NAME, USERNAME_KEY, username);
    }

    public static String getUsername(Context context) {
        return context.getSharedPreferences(
                USERNAME_NAME, Context.MODE_PRIVATE).getString(USERNAME_KEY, "");
    }

    public static void saveUsernameHistory(Context context, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                USERNAME_HISTORY_NAME, Context.MODE_PRIVATE).edit();
        List<String> usernameHistory = getUsernameHistory(context);

        if (usernameHistory.size() == 0) {
            editor.putString(USERNAME_HISTORY_KEY, value);
        } else {
            String usernameArrayStr = "";
            for (String username : usernameHistory) {
                if (!username.trim().isEmpty() && !value.trim().isEmpty()) {
                    if (!username.equals(value)) {
                        if (usernameArrayStr.isEmpty()) {
                            usernameArrayStr += username;
                        } else {
                            usernameArrayStr += "," + username;
                        }
                    }
                }
            }
            editor.putString(USERNAME_HISTORY_KEY, usernameArrayStr + "," + value);
        }
        editor.apply();
    }

    public static List<String> getUsernameHistory(Context context) {
        String usernameHistory = context.getSharedPreferences(
                USERNAME_HISTORY_NAME, Context.MODE_PRIVATE).getString(USERNAME_HISTORY_KEY, "");
        List<String> autoStrings = new ArrayList<>();
        if (usernameHistory.contains(",")) {
            String[] usernameArray = usernameHistory.split(",");
            for (String username : usernameArray) {
                if (!username.trim().isEmpty()) {
                    autoStrings.add(0, username);
                }
            }
        } else if (!usernameHistory.trim().isEmpty()) {
            autoStrings.add(usernameHistory);
        }

        return autoStrings;
    }

    public static boolean saveSharedPreference(Context context, String name, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean saveSharedPreference(Context context, String name, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean saveSharedPreference(Context context, String name, String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static boolean saveSharedPreference(Context context, String name, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static String getSharedPreferenceString(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getString(key, null);
    }

    public static String getSharedPreferenceString(Context context, String name, String key, String defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getString(key, defaultValue);
    }


    public static int getSharedPreferenceInt(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getInt(key, -1);
    }

    public static int getSharedPreferenceInt(Context context, String name, String key, int defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getInt(key, defaultValue);
    }

    public static long getSharedPreferenceLong(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        try {
            return savedPreference.getLong(key, -1);
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public static long getSharedPreferenceLong(Context context, String name, String key, long defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        try {
            return savedPreference.getLong(key, defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public static boolean getSharedPreferenceBoolean(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getBoolean(key, false);
    }

    public static boolean getSharedPreferenceBoolean(Context context, String name, String key, boolean defaultValue) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getBoolean(key, defaultValue);
    }

    public static boolean removeSharedPreference(Context context, String name, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean existSharedPreferenceKey(Context context, String name, String key) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.contains(key);
    }

    public static int getSharedPreferenceSize(Context context, String name) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getAll().size();
    }

    public static Map<String, ?> getSharedPreferenceAll(Context context, String name) {
        SharedPreferences savedPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return savedPreference.getAll();
    }

    public static void setSharedPreferencesFlag(Context context, String title, boolean content) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(title, content);
        editor.apply();
    }
}

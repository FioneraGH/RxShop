package com.centling.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.centling.BaseApplication;

@SuppressWarnings("unused")
public class SPUtil {
    private static SharedPreferences mSharedPreferences;

    private static void init() {
        if (mSharedPreferences == null) {
            mSharedPreferences = BaseApplication.getInstance().getSharedPreferences("SweepingRobot",
                    Context.MODE_PRIVATE);
        }
    }

    public static void setInt(String key, int value) {
        if (mSharedPreferences == null) {
            init();
        }
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        if (mSharedPreferences == null) {
            init();
        }
        return getInt(key, -1);
    }

    public static int getInt(String key, int defaultValue) {
        if (mSharedPreferences == null) {
            init();
        }
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public static void setFloat(String key, float value) {
        if (mSharedPreferences == null) {
            init();
        }
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public static Float getFloat(String key) {
        if (mSharedPreferences == null) {
            init();
        }
        return getFloat(key, -1f);
    }

    public static Float getFloat(String key, float defaultValue) {
        if (mSharedPreferences == null) {
            init();
        }
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        if (mSharedPreferences == null) {
            init();
        }
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        if (mSharedPreferences == null) {
            init();
        }
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        if (mSharedPreferences == null) {
            init();
        }
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void setString(String key, String value) {
        if (mSharedPreferences == null) {
            init();
        }
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        if (mSharedPreferences == null) {
            init();
        }
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        if (mSharedPreferences == null) {
            init();
        }
        return mSharedPreferences.getString(key, defaultValue);
    }

    public static void remove(String key) {
        if (mSharedPreferences == null) {
            init();
        }
        mSharedPreferences.edit().remove(key).apply();
    }

    static void clearAll() {
        if (mSharedPreferences == null) {
            init();
        }
        mSharedPreferences.edit().clear().apply();
    }
}

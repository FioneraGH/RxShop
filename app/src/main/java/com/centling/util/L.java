package com.centling.util;

import android.text.TextUtils;
import android.util.Log;

import com.centling.BuildConfig;

import java.util.Locale;

/**
 * L
 * Created by fionera on 15-12-6.
 */

public class L {
    private static boolean isDebug = BuildConfig.DEBUG;

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(Locale.CHINA, tag, callerClazzName, caller.getMethodName(),
                            caller.getLineNumber());
        String customTagPrefix = "l_log";
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (!isDebug) {
            return;
        }
        String tag = generateTag();
        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!isDebug) {
            return;
        }
        String tag = generateTag();
        Log.d(tag, content, tr);
    }

    public static void e(String content) {
        if (!isDebug) {
            return;
        }
        String tag = generateTag();
        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!isDebug) {
            return;
        }
        String tag = generateTag();
        Log.e(tag, content, tr);
    }
}

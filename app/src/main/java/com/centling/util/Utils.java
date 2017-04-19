package com.centling.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.centling.BaseApplication;

public class Utils {
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) BaseApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean checkArchSupported() {
        /*
        check if supported by device
         */
        boolean isSupported = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (String supportedAbi : Build.SUPPORTED_ABIS) {
                if (TextUtils.equals("armeabi", supportedAbi) || TextUtils.equals("armeabi-v7a",
                        supportedAbi)) {
                    isSupported = true;
                    break;
                }
            }
        } else {
            // noinspection deprecation
            if (TextUtils.equals("armeabi", Build.CPU_ABI) || TextUtils.equals("armeabi-v7a",
                    Build.CPU_ABI)) {
                isSupported = true;
            }
        }
        return isSupported;
    }

    // 获取当前应用的版本名称
    public static String getVersionName() {
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    BaseApplication.getInstance().getPackageName(), 0);
            String version = packInfo.versionName;
            if (!TextUtils.isEmpty(version)) {
                return version;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    // 获取当前应用的版本序号
    public static int getVersionCode() {
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    BaseApplication.getInstance().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI() {
        String uniqueCode = "";
        try {
            TelephonyManager tm = ((TelephonyManager) BaseApplication.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE));
            uniqueCode = tm.getDeviceId();
            if (uniqueCode == null) {
                uniqueCode = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uniqueCode;
    }
}

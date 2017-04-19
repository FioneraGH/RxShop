package com.centling;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.centling.perf.EventIndex;
import com.centling.util.CrashHandler;
import com.centling.util.L;
import com.tspoon.traceur.Traceur;
import com.umeng.socialize.PlatformConfig;

import org.greenrobot.eventbus.EventBus;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import rx_activity_result2.RxActivityResult;

/**
 * BaseApplication
 * Created by fionera on 17-2-6 in AndroidDemo.
 */

public class BaseApplication
        extends Application {
    protected static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    public static float scaledDensity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (!adjustMainProcess()) {
            return;
        }

        L.d("Application Start Time:" + System.currentTimeMillis());

        getDisplayMetrics();

        BGASwipeBackManager.getInstance().init(this);

        RxActivityResult.register(this);

        EventBus.builder().addIndex(new EventIndex()).installDefaultEventBus();

        if (!BuildConfig.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
            Traceur.disableLogging();
        } else {
            Traceur.enableLogging();
        }

        initUmeng();
    }

    public boolean adjustMainProcess() {
        ActivityManager activityManager = (ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == android.os.Process.myPid()) {
                L.d("Current Process:" + appProcess.processName + " -> " + getPackageName());
                return TextUtils.equals(appProcess.processName, getPackageName());
            }
        }
        return false;
    }

    private void initUmeng() {
        PlatformConfig.setWeixin("wx7592af40381460a8", "d4624c36b6795d1d99dcf0547af5443d");//honny
        PlatformConfig.setSinaWeibo("260277561", "c54917be16e572cb8d7629b96adcd05f");//honny
        PlatformConfig.setQQZone("1105253166", "y58O2JPrlOkbz1oB"); //honny
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    private void getDisplayMetrics() {
        DisplayMetrics metric = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDensity = metric.density;
        scaledDensity = metric.scaledDensity;
    }
}

package com.centling;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.perf.EventIndex;
import com.centling.util.CrashHandler;
import com.tspoon.traceur.Traceur;
import com.umeng.socialize.PlatformConfig;

import org.greenrobot.eventbus.EventBus;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import rx_activity_result2.RxActivityResult;
import timber.log.Timber;

/**
 * BaseApplication
 * Created by fionera on 17-2-6 in AndroidDemo.
 */

public class BaseApplication
        extends com.fionera.base.BaseApplication {
    protected static BaseApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!adjustMainProcess()) {
            return;
        }

        Timber.d("Application Start Time:%s", System.currentTimeMillis());

        BGASwipeBackManager.getInstance().init(this);

        RxActivityResult.register(this);

        EventBus.builder().addIndex(new EventIndex()).installDefaultEventBus();

        if (!BuildConfig.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
            Timber.plant(new CrashReportingTree());
            Traceur.disableLogging();
        } else {
            Timber.plant(new Timber.DebugTree());
            Traceur.enableLogging();
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);

        initUmeng();
    }

    public boolean adjustMainProcess() {
        ActivityManager activityManager = (ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == android.os.Process.myPid()) {
                Timber.d("Current Process:%s->%s", appProcess.processName, getPackageName());
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

    private static class CrashReportingTree
            extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Log.e(tag, message);

            if (t != null) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}

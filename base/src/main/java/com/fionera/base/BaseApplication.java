package com.fionera.base;

import android.app.Application;
import android.util.DisplayMetrics;

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
    public void onCreate() {
        super.onCreate();
        instance = this;

        getDisplayMetrics();
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

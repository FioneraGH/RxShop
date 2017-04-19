package com.centling.util;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class CrashHandler
        implements UncaughtExceptionHandler {
    private Context context;
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return new CrashHandler();
    }

    public void init(Context context) {
        this.context = context;
        /*
        retain origin
         */
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                File cacheDir = context.getExternalCacheDir();
                if (cacheDir != null) {
                    File file = new File(cacheDir.getAbsolutePath(), "traces.txt");
                    PrintWriter printWriter = new PrintWriter(file.getAbsolutePath());
                    ex.printStackTrace(printWriter);
                }
                Thread.sleep(1000);
            } catch (InterruptedException | FileNotFoundException e) {
                e.printStackTrace();
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ex.printStackTrace();
        return true;
    }
}
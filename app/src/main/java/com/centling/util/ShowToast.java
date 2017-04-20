package com.centling.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.centling.BaseApplication;

/**
 * ShowToast
 * Created by fionera on 15-12-6.
 */
public class ShowToast {
    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void show(Object info) {

        if (info == null) {
            return;
        }

        if (info.toString().startsWith("java") || info.toString().startsWith("unexpect") || info
                .toString().startsWith("syntax")) {
            info = "参数错误";
        }

        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getInstance(), info.toString(),
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(info.toString());
        }
        toast.show();
    }
}

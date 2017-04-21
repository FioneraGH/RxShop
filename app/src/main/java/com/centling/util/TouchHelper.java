package com.centling.util;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * TouchHelper
 * Created by fionera on 16-2-2.
 */

public class TouchHelper {

    public static void expandViewTouchDelegate(final View view, final int top, final int bottom,
                                               final int left, final int right) {

        ((View) view.getParent()).post(() -> {
            Rect bounds = new Rect();
            view.setEnabled(true);
            view.getHitRect(bounds);

            bounds.top -= top;
            bounds.bottom += bottom;
            bounds.left -= left;
            bounds.right += right;

            TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

            if (View.class.isInstance(view.getParent())) {
                ((View) view.getParent()).setTouchDelegate(touchDelegate);
            }
        });
    }
}

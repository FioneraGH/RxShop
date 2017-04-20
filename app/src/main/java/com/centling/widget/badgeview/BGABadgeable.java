package com.centling.widget.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewParent;

public interface BGABadgeable {

    void showCirclePointBadge();

    void showDrawableBadge(Bitmap bitmap);

    void showTextBadge(String badgeText);

    void hiddenBadge();

    /**
     * 调用父类的onTouchEvent方法
     *
     * @param event
     * @return
     */
    boolean callSuperOnTouchEvent(MotionEvent event);

    int getWidth();

    int getHeight();

    void postInvalidate();

    ViewParent getParent();

    int getId();

    boolean getGlobalVisibleRect(Rect r);

    Context getContext();
}
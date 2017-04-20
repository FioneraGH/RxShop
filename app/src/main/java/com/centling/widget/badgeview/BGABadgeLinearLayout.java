package com.centling.widget.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class BGABadgeLinearLayout extends LinearLayout implements BGABadgeable {

    private BGABadgeViewHelper bgaBadgeViewHelper;

    public BGABadgeLinearLayout(Context context) {
        this(context, null);
    }

    public BGABadgeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        bgaBadgeViewHelper = new BGABadgeViewHelper(this, context, attrs, BGABadgeViewHelper.BadgeGravity.RightTop);
    }

    @Override
    public boolean callSuperOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        bgaBadgeViewHelper.drawBadge(canvas);
    }

    @Override
    public void showCirclePointBadge() {
        bgaBadgeViewHelper.showCirclePointBadge();
    }

    @Override
    public void showDrawableBadge(Bitmap bitmap) {
        bgaBadgeViewHelper.showDrawable(bitmap);
    }

    @Override
    public void showTextBadge(String badgeText) {
        bgaBadgeViewHelper.showTextBadge(badgeText);
    }

    @Override
    public void hiddenBadge() {
        bgaBadgeViewHelper.hiddenBadge();
    }

}
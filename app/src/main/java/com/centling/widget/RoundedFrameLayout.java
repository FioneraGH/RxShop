package com.centling.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.centling.util.DisplayUtil;


/**
 * RoundedFrameLayout
 * Created by fionera on 16-6-26.
 */
public class RoundedFrameLayout
        extends FrameLayout {

    private final int RADIUS = DisplayUtil.dp2px(2f);

    public RoundedFrameLayout(Context context) {
        this(context, null);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG));
        canvas.clipPath(roundPath);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

    private Path roundPath = new Path();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        roundPath.reset();
        roundPath.addRoundRect(new RectF(0, 0, w, h), RADIUS, RADIUS, Path.Direction.CW);
        roundPath.close();
    }
}

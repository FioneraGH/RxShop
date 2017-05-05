package com.fionera.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.fionera.base.R;

public class FlexibleImageView extends AppCompatImageView {
    private int mProportionHeight;
    private int mProportionWidth;

    public FlexibleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.FlexibleImageView);
        mProportionHeight = typedArray.getInt(R.styleable.FlexibleImageView_proportion_height, 0);
        mProportionWidth = typedArray.getInt(R.styleable.FlexibleImageView_proportion_width, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mProportionHeight != 0 && mProportionWidth != 0) {
            mHeight = mWidth * mProportionHeight / mProportionWidth;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setScaleType(ScaleType.CENTER_CROP);
    }
}

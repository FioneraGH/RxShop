package com.centling.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centling.R;

@SuppressWarnings("unused")
public class TitleBar
        extends LinearLayout {

    private TextView mTvTitle;
    private Toolbar mToolbar;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.layout_title_bar, this, true);
        mToolbar = (Toolbar) view.getChildAt(0);

        mTvTitle = (TextView) view.findViewById(R.id.title_bar_title);
    }

    public void setTitleBarText(String title) {
        mTvTitle.setText(title);
    }

    public void setTitleBarTextColor(int resId) {
        mTvTitle.setTextColor(resId);
    }

    public void setTitleBarBackgroundColor(int resId) {
        setBackgroundColor(ContextCompat.getColor(getContext(), resId));
    }

    public void setTitleBarTitleClick(OnClickListener onTitleClickListener) {
        mTvTitle.setOnClickListener(onTitleClickListener);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}

package com.centling.activity;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.centling.R;
import com.centling.util.DisplayUtil;
import com.centling.widget.TitleBar;

public abstract class TitleBarActivity
        extends BaseActivity {
    public TitleBar mTitleBar;
    private View mTitleBarLine;
    private RelativeLayout mLayoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutContent = (RelativeLayout) View.inflate(mContext, R.layout.layout_title_bar_content,
                null);
        mTitleBar = (TitleBar) mLayoutContent.findViewById(R.id.tb_common_title_bar);
        mTitleBarLine = mLayoutContent.findViewById(R.id.v_common_title_bar_split_line);

        Toolbar toolbar = mTitleBar.getToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(v -> ActivityCompat.finishAfterTransition(mActivity));
        toolbar.setPadding(-DisplayUtil.dp2px(8.0f), toolbar.getPaddingTop(),
                toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
    }

    @Override
    public View findViewById(int id) {
        View view = mLayoutContent.findViewById(id);
        if (view == null) {
            return super.findViewById(id);
        }
        return view;
    }

    @Override
    public void setContentView(int layoutResID) {
        View mContent = getLayoutInflater().inflate(layoutResID, mLayoutContent, false);
        mContent.setPadding(mContent.getPaddingLeft(), DisplayUtil.dp2px(50) + (int) getResources()
                        .getDimension(R.dimen.statusbar_view_height), mContent.getPaddingRight(),
                mContent.getPaddingBottom());
        mLayoutContent.addView(mContent, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mTitleBar.bringToFront();
        mTitleBarLine.bringToFront();
        super.setContentView(mLayoutContent);
    }

    public void setContentViewWithoutPadding(int layoutResID) {
        View mContent = getLayoutInflater().inflate(layoutResID, mLayoutContent, false);
        mLayoutContent.addView(mContent, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mTitleBar.bringToFront();
        mTitleBarLine.bringToFront();
        super.setContentView(mLayoutContent);
    }

    @Override
    public void setContentView(View view) {
        view.setPadding(view.getPaddingLeft(),
                view.getPaddingTop() + DisplayUtil.dp2px(50) + (int) getResources()
                        .getDimension(R.dimen.statusbar_view_height), view.getPaddingRight(),
                view.getPaddingBottom());
        mLayoutContent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mTitleBar.bringToFront();
        mTitleBarLine.bringToFront();
        super.setContentView(mLayoutContent);
    }

    public void setContentViewWithoutPadding(View view) {
        mLayoutContent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mTitleBar.bringToFront();
        mTitleBarLine.bringToFront();
        super.setContentView(mLayoutContent);
    }

    public void setTitleBarText(String title) {
        setTitleBarText(title, false);
    }

    public void setTitleBarText(String title, boolean needLine) {
        mTitleBar.setTitleBarText(title);
        mTitleBarLine.setVisibility(needLine ? View.VISIBLE : View.GONE);
    }

    public void setTitleBarTextColor(int resId) {
        mTitleBar.setTitleBarTextColor(resId);
    }

    public void setTitleBarBackgroundColor(int resId) {
        mTitleBar.setTitleBarBackgroundColor(resId);
    }
}

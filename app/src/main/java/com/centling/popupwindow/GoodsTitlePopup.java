package com.centling.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.centling.R;
import com.centling.activity.BaseActivity;
import com.fionera.base.util.DisplayUtil;

public class GoodsTitlePopup
        extends PopupWindow
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (listener != null) {
            switch ((int) v.getTag()) {
                case 1:
                    listener.onItemClick(0);
                    break;
                case 2:
                    listener.onItemClick(1);
                    break;
                case 3:
                    listener.onItemClick(2);
                    break;
                case 4:
                    listener.onItemClick(3);
                    break;
                case 0:
                    listener.onItemClick(4);
                    break;
            }
        }
        dismiss();
    }

    private OnItemClickListener listener;
    private ValueAnimator alphaAnimator;
    private Window window;

    public GoodsTitlePopup(Context context, OnItemClickListener listener) {
        super(context);
        this.listener = listener;
        LinearLayout rootView = (LinearLayout) View.inflate(context, R.layout.popup_goods_title_pop,
                null);
        setContentView(rootView);
        LinearLayoutCompat optionContainer = ((LinearLayoutCompat) rootView.getChildAt(1));
        for (int i = 0, count = optionContainer.getChildCount(); i < count; i++) {
            View view = optionContainer.getChildAt(i);
            view.setTag(i);
            view.setOnClickListener(this);
        }

        setWidth(DisplayUtil.dp2px(150));
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        update();

        window = ((BaseActivity) context).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        alphaAnimator = new ValueAnimator();
        alphaAnimator.setDuration(250);
        alphaAnimator.addUpdateListener(animation -> {
            params.alpha = (float) animation.getAnimatedValue();
            window.setAttributes(params);
        });
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 1.0f;
            window.setAttributes(params);
        }
        alphaAnimator.setFloatValues(1f, 0.8f);
        alphaAnimator.removeAllListeners();
        alphaAnimator.start();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void dismiss() {
        alphaAnimator.setFloatValues(0.8f, 1f);
        alphaAnimator.removeAllListeners();
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.dimAmount = 0.0f;
                    window.setAttributes(params);
                }
            }
        });
        alphaAnimator.start();
        super.dismiss();
    }
}

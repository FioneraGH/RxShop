package com.centling.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.BaseActivity;

public class ChangeAvatarPopup
        extends PopupWindow implements View.OnClickListener {

    private OnDialogListener listener;
    private ValueAnimator alphaAnimator;
    private Window window;

    public ChangeAvatarPopup(Context context, OnDialogListener listener) {
        super(context);
        this.listener = listener;
        View mPopView = View.inflate(context, R.layout.dialog_change_avatar_pop, null);
        setContentView(mPopView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);

        TextView btn_take_photo = (TextView) mPopView.findViewById(R.id.tv_option_first_op);
        TextView btn_choose_photo = (TextView) mPopView.findViewById(R.id.tv_option_second_op);
        btn_take_photo.setOnClickListener(this);
        btn_choose_photo.setOnClickListener(this);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.AnimPopupWindow);
        setBackgroundDrawable(new ColorDrawable(0));

        window = ((BaseActivity) context).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        alphaAnimator = new ValueAnimator();
        alphaAnimator.setDuration(250);
        alphaAnimator.addUpdateListener(animation -> {
            params.alpha = (float) animation.getAnimatedValue();
            window.setAttributes(params);
        });

        update();
    }

    public interface OnDialogListener {

        void onTakePhoto();

        void onChoosePhoto();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_option_first_op:
                listener.onTakePhoto();
                break;
            case R.id.tv_option_second_op:
                listener.onChoosePhoto();
                break;
        }
        dismiss();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 1.0f;
            window.setAttributes(params);
        }
        alphaAnimator.setFloatValues(1f, 0.8f);
        alphaAnimator.removeAllListeners();
        alphaAnimator.start();
        super.showAtLocation(parent, gravity, x, y);
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

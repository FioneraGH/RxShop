package com.centling.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.BaseActivity;

public class ChangeRealNamePopup
        extends PopupWindow implements View.OnClickListener {

    private OnDialogListener listener;
    private ValueAnimator alphaAnimator;
    private EditText et_realname;
    private Window window;

    public void setRealname(String realname) {
        et_realname.setText(realname);
    }

    public ChangeRealNamePopup(Context context, OnDialogListener listener) {
        super(context);
        this.listener = listener;
        View mPopView = View.inflate(context, R.layout.dialog_change_realname_pop, null);
        setContentView(mPopView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tv_title = (TextView) mPopView.findViewById(R.id.tv_option_title);
        et_realname = (EditText) mPopView.findViewById(R.id.et_option_change_value);
        TextView tv_confirm = (TextView) mPopView.findViewById(R.id.tv_option_confirm_op);
        TextView tv_cancel = (TextView) mPopView.findViewById(R.id.tv_option_cancel_op);
        tv_title.setText("编辑真实姓名");
        tv_confirm.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setFocusable(true);
        setAnimationStyle(R.style.AnimPopupWindow);
        setBackgroundDrawable(new ColorDrawable(0x00000000));

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
        void onConfirmRealOp(String s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_option_confirm_op:
                listener.onConfirmRealOp(et_realname.getText().toString());
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


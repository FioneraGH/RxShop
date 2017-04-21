package com.centling.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.BaseActivity;

/**
 * SharedPopupWindow
 * Created by Loren on 2016/3/21.
 */
public class SharedPopupWindow
        extends PopupWindow
        implements View.OnClickListener {

    private View.OnClickListener itemOnClick;
    private View mMenuView;
    private ValueAnimator alphaAnimator;
    private Window window;

    @SuppressLint("ClickableViewAccessibility")
    public SharedPopupWindow(Context mContext, View.OnClickListener itemsOnClick) {
        super(mContext);
        this.itemOnClick = itemsOnClick;
        mMenuView = View.inflate(mContext, R.layout.popup_window_shared, null);
        RelativeLayout image_friend = (RelativeLayout) mMenuView.findViewById(R.id.friend_rl);
        RelativeLayout image_sina = (RelativeLayout) mMenuView.findViewById(R.id.sina_rl);
        RelativeLayout image_qq = (RelativeLayout) mMenuView.findViewById(R.id.qq_rl);
        RelativeLayout image_qzone = (RelativeLayout) mMenuView.findViewById(R.id.qzone_rl);
        RelativeLayout image_weixin = (RelativeLayout) mMenuView.findViewById(R.id.weixin_rl);
        TextView btn_cancel = (TextView) mMenuView.findViewById(R.id.shared_btn_cancel);
        btn_cancel.setOnClickListener(v -> dismiss());
        // 设置按钮监听
        image_weixin.setOnClickListener(this);
        image_friend.setOnClickListener(this);
        image_qzone.setOnClickListener(this);
        image_sina.setOnClickListener(this);
        image_qq.setOnClickListener(this);
        // 设置SelectPicPopupWindow的View
        setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(android.R.style.Animation_InputMethod);
        // 实例化一个ColorDrawable颜色为半透明0xb0000000
        ColorDrawable dw = new ColorDrawable(0);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener((v, event) -> {
            int height = mMenuView.findViewById(R.id.pop_layout).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });

        window = ((BaseActivity) mContext).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        alphaAnimator = new ValueAnimator();
        alphaAnimator.setDuration(250);
        alphaAnimator.addUpdateListener(animation -> {
            params.alpha = (float) animation.getAnimatedValue();
            window.setAttributes(params);
        });
        update();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (itemOnClick != null) {
            itemOnClick.onClick(v);
        }
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

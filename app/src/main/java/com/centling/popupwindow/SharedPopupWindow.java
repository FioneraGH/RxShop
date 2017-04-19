package com.centling.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centling.R;

/**
 * SharedPopupWindow
 * Created by Loren on 2016/3/21.
 */
public class SharedPopupWindow
        extends PopupWindow {

    private View mMenuView;

    @SuppressLint("ClickableViewAccessibility")
    public SharedPopupWindow(Context mContext, View.OnClickListener itemsOnClick) {
        super(mContext);
        mMenuView = View.inflate(mContext, R.layout.popup_window_shared, null);
        RelativeLayout image_friend = (RelativeLayout) mMenuView.findViewById(R.id.friend_rl);
        RelativeLayout image_sina = (RelativeLayout) mMenuView.findViewById(R.id.sina_rl);
        RelativeLayout image_qq = (RelativeLayout) mMenuView.findViewById(R.id.qq_rl);
        RelativeLayout image_qzone = (RelativeLayout) mMenuView.findViewById(R.id.qzone_rl);
        RelativeLayout image_weixin = (RelativeLayout) mMenuView.findViewById(R.id.weixin_rl);
        TextView btn_cancel = (TextView) mMenuView.findViewById(R.id.shared_btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        // 设置按钮监听
        image_weixin.setOnClickListener(itemsOnClick);
        image_friend.setOnClickListener(itemsOnClick);
        image_qzone.setOnClickListener(itemsOnClick);
        image_sina.setOnClickListener(itemsOnClick);
        image_qq.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(android.R.style.Animation_InputMethod);
        // 实例化一个ColorDrawable颜色为半透明0xb0000000
        ColorDrawable dw = new ColorDrawable(0xb0000000);
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
    }
}

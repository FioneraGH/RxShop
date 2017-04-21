package com.centling.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.centling.R;
import com.centling.activity.BaseActivity;
import com.centling.databinding.PopBuyLayoutBinding;

import java.text.DecimalFormat;
import java.util.Locale;

public class ChoosePayMethodPopup
        extends PopupWindow
        implements View.OnClickListener {
    private static final int ALIPAY = 0;
    private static final int WXPAY = 1;

    private ValueAnimator alphaAnimator;
    private PopBuyLayoutBinding mPopView;
    private int method;
    private float useGold = 0f;

    private ChoosePayMethodPopup.OnDialogListener listener;
    private Window window;

    public ChoosePayMethodPopup(Context context,
                                @NonNull ChoosePayMethodPopup.OnDialogListener listener) {
        super(context);
        this.listener = listener;
        mPopView = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pop_buy_layout,
                null, false);
        setContentView(mPopView.getRoot());
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);
        setAnimationStyle(android.R.style.Animation_InputMethod);
        setBackgroundDrawable(new ColorDrawable(0));

        mPopView.ivPaymentClose.setOnClickListener(this);
        mPopView.tvPaymentPay.setOnClickListener(this);
        mPopView.llPaymentChoice.setOnItemClick(pos -> method = pos);

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

    private float mPrice = 0.0f;

    public ChoosePayMethodPopup setData(float price, Float totalGold) {
        this.mPrice = price;
        mPopView.tvPaymentPrice.setText(new DecimalFormat("0.00").format(mPrice));
        if (totalGold == null) {
            mPopView.llPaymentGold.setVisibility(View.GONE);
        } else {
            mPopView.llPaymentGold.setVisibility(View.VISIBLE);
            mPopView.tvPaymentGold.setText(String.format(Locale.CHINA, "共有%s金币可用",
                    new DecimalFormat("0.00").format(totalGold)));
            mPopView.etPaymentGold.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        useGold = 0f;
                        mPopView.tvPaymentPrice.setText(new DecimalFormat("0.00").format(mPrice));
                        return;
                    }
                    // 禁止在最高位加0
                    if (s.toString().substring(0, 1).contentEquals("0") && s.length() > 1) {
                        if (!s.toString().substring(1, 2).contentEquals(".")) {
                            s.delete(0, 1);
                        }
                    }
                    // 未输入任何数字时按小数点，前面自动加0
                    if (s.length() == 1) {
                        if (s.toString().contentEquals(".")) {
                            s.insert(0, "0");
                        }
                    }
                    // 如果输入过小数点，禁止再次输入小数点
                    if (s.toString().substring(0, s.length() - 1).contains(".")) {
                        if (s.toString().substring(s.length() - 1, s.length()).contentEquals(".")) {
                            s.delete(s.length() - 1, s.length());
                        }
                        if (s.toString().substring(0, 1).contentEquals(".")) {
                            s.insert(0, "0");
                        }
                    }
                    // 小数点后只能输入两位
                    if (s.toString().contains(".")) {
                        if (s.length() - s.toString().indexOf(".") > 3) {
                            s.delete(s.length() - 1, s.length());
                        }
                    }
                    // 极端情况出现“..”时
                    if (s.toString().contains("..")) {
                        s.replace(s.toString().indexOf(".."), s.length(), "." + s.toString()
                                .substring(s.toString().indexOf("..") + 2, s.length()));
                    }

                    if (!TextUtils.isEmpty(s.toString())) {
                        useGold = Float.valueOf(s.toString());
                        if (useGold > totalGold) {
                            useGold = totalGold;
                            mPopView.etPaymentGold.setText(String.valueOf(useGold));
                        }
                        if (useGold > mPrice) {
                            useGold = mPrice;
                            mPopView.etPaymentGold.setText(String.valueOf(useGold));
                        }
                        mPopView.tvPaymentPrice.setText(
                                new DecimalFormat("0.00").format(mPrice - useGold));
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
        }
        return this;
    }


    public interface OnDialogListener {

        void onTakePhoto(float gold); // 支付宝

        void onChoosePhoto(float gold); // 微信
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_payment_close:
                dismiss();
                break;
            case R.id.tv_payment_pay: {
                switch (method) {
                    case ALIPAY:
                        listener.onTakePhoto(useGold);
                        break;
                    case WXPAY:
                        listener.onChoosePhoto(useGold);
                        break;
                }
            }
        }

        mPopView.etPaymentGold.setText("");
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

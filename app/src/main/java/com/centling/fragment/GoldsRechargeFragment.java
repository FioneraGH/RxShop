package com.centling.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.constant.PaymentConstant;
import com.centling.databinding.FragmentGoldRechargeBinding;
import com.centling.entity.GoldsRuleBean;
import com.centling.http.ApiManager;
import com.centling.payment.ali.AlipayUtils;
import com.centling.payment.wx.WXPayUtil;
import com.centling.popupwindow.ChoosePayMethodPopup;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * GoldsRechargeFragment
 * Created by fionera on 15-11-30.
 */
public class GoldsRechargeFragment
        extends BaseFragment
        implements ChoosePayMethodPopup.OnDialogListener, View.OnClickListener {

    private List<TextView> optionTvs;

    private ChoosePayMethodPopup choosePayMethodPopup;

    private List<GoldsRuleBean.AmountRelusEntity> amountRelusEntityList;
    private SparseIntArray moneyMap = new SparseIntArray();
    private float additionalAmount = 0f;
    private float originAmount = 0f;

    private AlipayUtils alipayUtils;

    private FragmentGoldRechargeBinding mFragmentGoldRechargeBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentGoldRechargeBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_gold_recharge, container, false);
        return mFragmentGoldRechargeBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        alipayUtils = new AlipayUtils(mActivity);
        alipayUtils.setPayCallback(new AlipayUtils.PayCallback() {
            @Override
            public void onSuccess() {
                mActivity.finish();
            }

            @Override
            public void onFailure(String reason) {
                mActivity.finish();
            }
        });

        mFragmentGoldRechargeBinding.etGoldsRechargeCustomAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int j = 0; j < optionTvs.size(); j++) {
                    optionTvs.get(j).setSelected(false);
                }

                if (TextUtils.isEmpty(s.toString())) {
                    mFragmentGoldRechargeBinding.tvGoldsRechargePayAmount.setText("￥0");
                    originAmount = 0f;
                    additionalAmount = 0f;
                    choosePayMethodPopup.setData(originAmount, null);
                    computeRealAmount();
                } else {
                    mFragmentGoldRechargeBinding.tvGoldsRechargePayAmount.setText(
                            String.format(Locale.CHINA, "￥%s", s.toString()));
                    originAmount = Float.parseFloat(s.toString());
                    additionalAmount = 0f;
                    choosePayMethodPopup.setData(originAmount, null);
                    computeRealAmount();
                }
            }
        });

        mFragmentGoldRechargeBinding.tvGoldsRechargeConfirm.setOnClickListener(this);

        getGoldsRules();
    }

    private void init() {
        choosePayMethodPopup = new ChoosePayMethodPopup(mContext, this);

        amountRelusEntityList = new ArrayList<>();
        optionTvs = new ArrayList<>();
        moneyMap.put(0, 2000);
        moneyMap.put(1, 5000);
        moneyMap.put(2, 10000);
        moneyMap.put(3, 20000);
        for (int i = 0; i < mFragmentGoldRechargeBinding.llGoldsRechargeOption1.getChildCount(); i++) {
            optionTvs.add((TextView) mFragmentGoldRechargeBinding.llGoldsRechargeOption1.getChildAt(i));
        }
        for (int i = 0; i < mFragmentGoldRechargeBinding.llGoldsRechargeOption2.getChildCount(); i++) {
            optionTvs.add((TextView) mFragmentGoldRechargeBinding.llGoldsRechargeOption2.getChildAt(i));
        }
        for (int i = 0; i < optionTvs.size(); i++) {
            optionTvs.get(i).setTag(i);
            optionTvs.get(i).setOnClickListener(v -> {
                for (int j = 0; j < optionTvs.size(); j++) {
                    optionTvs.get(j).setSelected(false);
                }
                mFragmentGoldRechargeBinding.etGoldsRechargeCustomAmount.setText(String.valueOf(moneyMap.get((int) v.getTag())));
                v.setSelected(true);
            });
        }
    }

    private void getGoldsRules() {
        showLoading("正在获取金币规则");

        ApiManager.goldRules(new HashMap<>()).compose(bindUntil(FragmentEvent.DESTROY_VIEW)).subscribe(
                goldsRuleBean -> {
                    dismissLoading();
                    amountRelusEntityList.addAll(goldsRuleBean.getAmount_relus());
                }, throwable -> {
                    dismissLoading();
                    ShowToast.show("获取充值规则失败！");
                    mActivity.finish();
                });
    }

    private void computeRealAmount() {
        for (int i = 0; i < amountRelusEntityList.size(); i++) {
            if (originAmount >= Float
                    .parseFloat(amountRelusEntityList.get(i).getRecharge_amount())) {
                additionalAmount = Float.parseFloat(amountRelusEntityList.get(i).getGiven_amount());
                break;
            }
        }
        float sum = originAmount + additionalAmount;
        mFragmentGoldRechargeBinding.tvGoldsRechargeGetAmount.setText(String.format(Locale.CHINA, "￥%s",sum));
    }

    @Override
    public void onTakePhoto(float golds) {
        doRecharge(0,
                Integer.parseInt(mFragmentGoldRechargeBinding.tvGoldsRechargePayAmount.getText().toString().replace("￥", "")),
                Float.parseFloat(
                        mFragmentGoldRechargeBinding.tvGoldsRechargeGetAmount.getText().toString().replace("￥", "")));
    }

    @Override
    public void onChoosePhoto(float golds) {
        doRecharge(1,
                Integer.parseInt(mFragmentGoldRechargeBinding.tvGoldsRechargePayAmount.getText().toString().replace("￥", "")),
                Float.parseFloat(
                        mFragmentGoldRechargeBinding.tvGoldsRechargeGetAmount.getText().toString().replace("￥", "")));
    }

    private void doRecharge(int type, int amount, float finalAmount) {
        showLoading("正在充值金币");
        Map<String, Object> params = new HashMap<>();
        params.put("pdr_amount", amount);
        params.put("account_amount", finalAmount);

        ApiManager.goldRecharge(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(s -> {
            dismissLoading();
            JSONObject rawObj = new JSONObject(s);
            JSONObject resultObj = rawObj.getJSONObject("result");
            JSONArray array = resultObj.getJSONArray("recharge_list");
            String pay_sn = ((JSONObject) array.get(0)).getString("pdr_sn");
            if (0 == type) {
                double pay_amount = Double
                        .parseDouble(((JSONObject) array.get(0)).getString("pdr_amount"));
                getRsaSign(alipayUtils.generateOrderInfo(pay_sn, "金币充值", pay_amount,
                        PaymentConstant.ALI_PAY_CALLBACK_2));
            } else if (1 == type) {
                new WXPayUtil(mContext).pay(pay_sn,
                        Math.round(Float.parseFloat(((JSONObject) array.get(0))
                                .getString("pdr_amount")) * 100), 1, "金币充值");
            }
        },throwable -> {
            dismissLoading();
            ShowToast.show("金币充值失败！");
        });
    }

    private void getRsaSign(String orderInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("orderspec", orderInfo);

        ApiManager.orderSign(params).subscribe(s -> {
            JSONObject rawObj = new JSONObject(s);
            String sign = rawObj.getJSONObject("result").getString("signedstr");
            alipayUtils.pay(orderInfo, sign);
        }, throwable -> ShowDialog.showSelectDialog(mContext, "是否重试", "获取订单签名失败！", "",
                v -> getRsaSign(orderInfo)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_golds_recharge_apply_custom_amount:
                String custom_amount = mFragmentGoldRechargeBinding.etGoldsRechargeCustomAmount.getText().toString();
                if (TextUtils.isEmpty(custom_amount)) {
                    ShowToast.show("请输入金额");
                } else {
                    for (int j = 0; j < optionTvs.size(); j++) {
                        optionTvs.get(j).setSelected(false);
                    }
                    mFragmentGoldRechargeBinding.tvGoldsRechargePayAmount.setText(
                            String.format(Locale.CHINA, "￥%s", custom_amount));
                    originAmount = Float.parseFloat(custom_amount);
                    additionalAmount = 0f;
                    choosePayMethodPopup.setData(originAmount, null);
                    computeRealAmount();
                }
                break;
            case R.id.tv_golds_recharge_confirm:
                showPayPop(choosePayMethodPopup);
                break;
        }
    }

    private void showPayPop(PopupWindow pop) {
        if (0 == originAmount) {
            ShowToast.show("不可充值0金币");
            return;
        }
        pop.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}

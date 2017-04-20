package com.centling.payment.ali;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.alipay.sdk.app.PayTask;
import com.centling.util.ShowToast;

import java.text.DecimalFormat;

/**
 * AlipayUtils
 * Created by fionera on 15-10-20.
 */

public class AlipayUtils {

    private static final String PARTNER = "2088311049661034";
    private static final String SELLER = "honny888@qq.com";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Activity activity;

    private PayCallback callback;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    String resultStatus = payResult.getResultStatus();
                    String resultInfo = payResult.getResult();
                    String memo = payResult.getMemo();

                    if (TextUtils.equals(resultStatus, "9000")) {
                        ShowToast.show("支付成功");
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ShowToast.show("支付结果确认中");
                        } else {
                            ShowToast.show("支付失败");
                            if (callback != null) {
                                callback.onFailure("支付宝支付失败");
                            }
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                }
                default:
                    break;
            }
        }
    };

    public AlipayUtils(Activity activity) {
        this.activity = activity;
    }

    public void setPayCallback(PayCallback payCallback) {
        this.callback = payCallback;
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check() {
        Runnable checkRunnable = () -> {
            // 构造PayTask 对象
            PayTask payTask = new PayTask(activity);
            // 调用查询接口，获取查询结果
            boolean isExist = payTask.checkAccountIfExist();

            Message msg = new Message();
            msg.what = SDK_CHECK_FLAG;
            msg.obj = isExist;
            mHandler.sendMessage(msg);
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    public String generateOrderInfo(String pay_sn, String goodsName, double price, String address) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(activity).setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", (dialog, i) -> {
                        ShowToast.show("请检查配置");
                    }).show();
            return "";
        }

        // 订单
        return getOrderInfo(goodsName, "来自恒尼商城的订单", "" + new DecimalFormat("###0.00").format(price),
                address, pay_sn);
    }

    public void pay(String orderInfo, String sign) {

        //        try {
        //            sign = URLEncoder.encode(sign, "UTF-8");
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";

        Runnable payRunnable = () -> {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(activity);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public String getOrderInfo(String subject, String body, String price, String returnAddress,
                               String pay_sn) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + pay_sn + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + returnAddress + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    public interface PayCallback {
        void onSuccess();

        void onFailure(String reason);
    }
}

package com.centling.payment.wx;

import android.content.Context;
import android.util.Log;

import com.centling.constant.PaymentConstant;
import com.centling.http.ApiManager;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WXPayUtil {

    private IWXAPI api;
    private Context context;

    public WXPayUtil(Context context) {
        api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(PaymentConstant.WX_APP_ID);
        this.context = context;
    }

    public void pay(String paysn, long total_fee, int type, String body) {

        if (api.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
            ShowToast.show("不满足微信支付所需条件");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("body", body);
        params.put("out_trade_no", paysn);
        params.put("spbill_create_ip", NetworkUtil.getIP());
        params.put("total_fee", total_fee);

        ApiManager.orderSignWx(params).subscribe(s -> {
            JSONObject rawObj = new JSONObject(s);
            JSONObject resultObj = rawObj.getJSONObject("result");

            String appId = resultObj.getString("appid");
            String nonceStr = resultObj.getString("noncestr");
            String prepayId = resultObj.getString("prepayid");
            String packageValue = resultObj.getString("package");
            String sign = resultObj.getString("sign");
            String partnerid = resultObj.getString("partnerid");
            long timeStamp = Long.valueOf(resultObj.getString("timestamp"));

            PayReq req = new PayReq();
            req.appId = appId;
            req.nonceStr = nonceStr;
            req.partnerId = partnerid;
            req.prepayId = prepayId;
            req.packageValue = packageValue;
            req.timeStamp = timeStamp + "";
            req.sign = sign;
            req.extData = "";
        }, throwable -> ShowDialog.showSelectDialog(context, "是否重试", "获取订单签名失败！", "",
                v -> pay(paysn, total_fee, type, body)));
    }
}

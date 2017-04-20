package com.centling.payment.wx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.centling.constant.PaymentConstant;
import com.centling.event.OrderRelationEvent;
import com.centling.util.ShowToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity
        extends Activity
        implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(PaymentConstant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (0 == resp.errCode) {
                EventBus.getDefault().post(new OrderRelationEvent.WxPayResult(1,true));
                ShowToast.show("支付成功");
            } else {
                EventBus.getDefault().post(new OrderRelationEvent.WxPayResult(1, false));
                ShowToast.show("支付失败");
            }
        }
        finish();
    }
}
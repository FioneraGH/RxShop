package com.centling.constant;

import com.centling.http.HttpConstants;
import com.centling.util.UserInfoUtil;

public interface PaymentConstant {
    String ALI_PAY_CALLBACK_1 = HttpConstants.IP + "/index" +
            ".php?act=payment_notify&op=ali_notify&key=" + UserInfoUtil.getKey();
    String ALI_PAY_CALLBACK_2 = HttpConstants.IP + "/index" +
            ".php?act=gold_coins&op=notify&key=" + UserInfoUtil.getKey();
    String ALI_PAY_CALLBACK_3 = HttpConstants.IP + "/index" +
            ".php?act=giftcard_cart&op=notify&key=" + UserInfoUtil.getKey();
    String ALI_PAY_CALLBACK_4 = HttpConstants.IP + "/index" +
            ".php?act=pointcart&op=notify&key=" + UserInfoUtil.getKey();
    String ALI_PAY_PRIVATE_KEY = "";

    String WX_APP_ID = "wx7592af40381460a8";
}

package com.centling.http;

/**
 * HttpConstants
 */

public interface HttpConstants {
    String IP = "http://www.honnyshop.com/mobile";

    String BASE_URL = IP + "/index.php/";

    String GOODS_SHARED_URL = "http://www.honnyshop.com/wap/tmpl/share_product.html?goods_id=";

    String HOME_PAGE = "?act=index&op=special&special_id=1&type=app&client=android";

    String CATALOG_LIST = "?act=goods_class&client=android";

    String CATALOG_GOODS_LIST = "?act=goods&op=goods_commonlist&client=android";

    String IS_GET_BIRTHDAY = "?act=vip_gift_isget&op=is_getvip";

    String GOODS_DETAIL = "?act=goods&op=goods_detail_list&client=android";

    String GOODS_LIST = "?act=goods&op=goods_commonlist&client=android";

    String ORDER_LIST = "?act=member_order&op=order_list&getpayment=true";

    String DISCOUNT_LIST = "?act=goods&op=goods_discount&client=android";

    String CART_ADD = "?act=member_cart&op=cart_add";

    String FOOTPRINT_ADD = "?act=member_footprint&op=add_footprint";

    String FAVORITE_ADD = "?act=favorites&op=member_favorites";

    String ADD_FRIEND = "?act=member_chat_app&op=addfollow";

    String SEARCH_FRIEND = "?act=member_chat_app&op=findlist";

    String IS_THIRD_LOGIN = "?act=login&op=other_login";

    String LOGIN = "?act=login&op=index";

    String BIND_THIRD_LOGIN = "?act=third_account&op=setting_third";

    String FETCH_MEMBER_POINTS = "?act=member_points&op=points_login";

    String TRACK_UNREAD_MSG = "?act=member_message&op=receivedSystemNewNum";

    String IS_VIP = "?act=member_vip&op=is_vip";

    String BACK_GOLD = "?act=member_order&op=order_pay_interrupt";

    String ORDER_SIGN = "?act=payment_app&op=alipay_sign";

    String CONFIRM_DLYP = "?act=dlyp_order&op=dlyp&client=android";

    String ORDER_CANCEL = "?act=member_order&op=order_cancel";

    String ORDER_DELETE = "?act=member_order&op=change_state";

    String ORDER_REFUND = "?act=member_refund&op=add_refund_all";

    String ORDER_RECEIVE = "?act=member_order&op=order_receive";

    String ORDER_PREPAY = "?act=member_order&op=orderlist_prepay";

    String ORDER_PAY_AGAIN = "?act=member_buy&op=order_pay_again";

    String ORDER_SIGN_WX = "?act=payment_app&op=wx_sign_v3";
}

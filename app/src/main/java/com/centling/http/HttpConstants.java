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

    String NORMAL_CART_UPDATE = "?act=member_cart&op=cart_edit_quantity";

    String CART_CLEAR = "?act=member_cart&op=cart_clear";

    String CART_LIST = "?act=member_cart&op=cart_list";

    String ORDER_CONFIRM_STEP_1 = "?act=member_buy&op=buy_step1";

    String ORDER_CONFIRM_OFFPAY = "?act=member_buy&op=change_address";

    String ORDER_CONFIRM_GENERAL = "?act=member_buy&op=buy_step2";

    String ORDER_CONFIRM_BIRTHDAY = "?act=vip_gift_isget&op=apply_birthgift";

    String ORDER_CONFIRM_CUSTOME = "?act=member_personal&op=apply_self_make";

    String ORDER_CONFIRM_NEWTRY = "?act=new_product_try&op=apply_record";

    String ADD_ADDRESS = "?act=member_address&op=address_add_v3";

    String DEL_ADDRESS = "?act=member_address&op=address_del";

    String CLEAR_FOOTPRINT = "?act=member_footprint&op=del_allfootprint";

    String COLLECTION_DEL = "?act=favorites&op=del_favorites";

    String CUSTOM_DELETE = "?act=member_custom&op=del_custom";

    String COLLECTION_LIST = "?act=favorites&op=favorites_list&client=android";

    String CUSTOM_LIST = "?act=member_custom&op=custom&client=android";

    String FOOTPRINT_LIST = "?act=member_footprint&op=footprint&client=android";

    String CHANGE_USER_INFO = "?act=personal_data&op=change_personal_data";

    String CHANGE_USER_AVATAR = "?act=upload&op=uploadAvatar";
}

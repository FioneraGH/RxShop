package com.centling.http;

/**
 * HttpConstants
 */

interface HttpConstants {
    String IP = "http://www.honnyshop.com/mobile";

    String BASE_URL = IP + "/index.php/";

    String HOME_PAGE = "?act=index&op=special&special_id=1&type=app&client=android";

    String CATALOG_LIST = "?act=goods_class&client=android";

    String CATALOG_GOODS_LIST = "?act=goods&op=goods_commonlist&client=android";

    String IS_GET_BIRTHDAY = "?act=vip_gift_isget&op=is_getvip";

    String GOODS_DETAIL = "?act=goods&op=goods_detail_list&client=android";

    String GOODS_LIST = "?act=goods&op=goods_commonlist&client=android";

    String DISCOUNT_LIST = "?act=goods&op=goods_discount&client=android";

    String CART_ADD = "?act=member_cart&op=cart_add";

    String FOOTPRINT_ADD = "?act=member_footprint&op=add_footprint";

    String FAVORITE_ADD = "?act=favorites&op=member_favorites";

    String ADD_FRIEND = "?act=member_chat_app&op=addfollow";

    String SEARCH_FRIEND = "?act=member_chat_app&op=findlist";

    String IS_THIRD_LOGIN = "?act=login&op=other_login";

    String LOGIN = "?act=login&op=index";

    String BIND_THIRD_LOGIN = "?act=third_account&op=setting_third";
}

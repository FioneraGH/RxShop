package com.centling.constant;

/**
 * RouterConstant
 * Created by fionera on 17-4-21 in RxShop.
 */

public interface RouterConstant {
    // group main
    String GROUP_MAIN = "/main/";

    interface Main {
        String MAIN = GROUP_MAIN + "main";
        String CART = GROUP_MAIN + "cart";
    }

    // group user
    String GROUP_USER = "/user/";
    interface User {
        String LOGIN = GROUP_USER + "login";
        String SETTING = GROUP_USER + "setting";
        String INFO = GROUP_USER + "info";
    }

    // group order
    String GROUP_ORDER = "/order/";
    interface Order {
        String MAIN = GROUP_ORDER + "main";
    }

    // group order
    String GROUP_COLLECTION = "/collection/";
    interface Collection {
        String MAIN = GROUP_COLLECTION + "main";
    }
}

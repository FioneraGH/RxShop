package com.centling.event;

/**
 * OrderRelationEvent
 * Created by fionera on 17-4-19 in RxShop.
 */

public interface OrderRelationEvent {
    class UpdateCart {

    }

    class UpdateOrder {
        public int order_type;

        public UpdateOrder(int order_type) {
            this.order_type = order_type;
        }
    }

    class WxPayResult {
        public int order_type;
        public boolean success;

        public WxPayResult(int order_type, boolean success) {
            this.order_type = order_type;
            this.success = success;
        }
    }
}

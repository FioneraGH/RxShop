package com.centling.event;

/**
 * CommonEvent
 * Created by fionera on 17-4-19 in RxShop.
 */

public interface CommonEvent {
    class ChangeTabEvent{
        public int tabPosition;

        public ChangeTabEvent(int tabPosition) {
            this.tabPosition = tabPosition;
        }
    }
}

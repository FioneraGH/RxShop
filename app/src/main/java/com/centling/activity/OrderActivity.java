package com.centling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.adapter.OrderFragmentAdapter;

@Route(path = "/order/main")
public class OrderActivity
        extends TitleBarActivity {

    @Autowired
    int order_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setTitleBarText("我的订单");

        TabLayout tlOrderTabs = (TabLayout) findViewById(R.id.tl_order_tabs);
        ViewPager vpOrderContainer = (ViewPager) findViewById(R.id.vp_order_container);

        OrderFragmentAdapter orderFragmentAdapter = new OrderFragmentAdapter(
                getSupportFragmentManager(), mContext);
        vpOrderContainer.setAdapter((orderFragmentAdapter));
        tlOrderTabs.setupWithViewPager(vpOrderContainer);

        /*
          切换自定义View
         */
        for (int i = 0; i < tlOrderTabs.getTabCount(); i++) {
            TabLayout.Tab tab = tlOrderTabs.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(orderFragmentAdapter.getTabView(i));
                View currentView = tab.getCustomView();
                if (0 == i && currentView != null) {
                    currentView.setSelected(true);
                }
            }
        }

        vpOrderContainer.setOffscreenPageLimit(1);
        vpOrderContainer.setCurrentItem(order_type);
    }
}

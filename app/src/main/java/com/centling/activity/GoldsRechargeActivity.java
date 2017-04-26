package com.centling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.adapter.GoldsRechargeFragmentAdapter;
import com.centling.constant.RouterConstant;
import com.centling.event.OrderRelationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


@Route(path = RouterConstant.User.GOLDS_RECHARGE)
public class GoldsRechargeActivity
        extends TitleBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golds_recharge);

        EventBus.getDefault().register(this);

        setTitleBarText("金币充值");

        TabLayout tlGoldsRecharge = (TabLayout) findViewById(R.id.tl_golds_recharge);
        ViewPager vpGoldsRecharge = (ViewPager) findViewById(R.id.vp_golds_recharge);

        vpGoldsRecharge.setAdapter(new GoldsRechargeFragmentAdapter(getSupportFragmentManager()));
        tlGoldsRecharge.setupWithViewPager(vpGoldsRecharge);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventWxPayResult(OrderRelationEvent.WxPayResult wxPayResult) {
        finish();
    }
}

package com.centling.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.R;
import com.centling.constant.RouterConstant;
import com.centling.fragment.GoodsDetailFragment;
import com.centling.util.ActivityContainer;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;

public class GoodsDetailActivity
        extends TitleBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        setTitleBarText("商品详情");
        mTitleBar.getToolbar().inflateMenu(R.menu.menu_goods_detail);
        mTitleBar.getToolbar().setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_goods_detail_cart:
                    ARouter.getInstance().build("/main/container").withString("fragment_type",
                            RouterConstant.Main.CART).navigation();
                    break;
                case R.id.menu_goods_detail_kf:
                    if (!UserInfoUtil.isLogin()) {
                        ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
                        return false;
                    }
                    if (UserInfoUtil.isKFOnline()) {
                        ShowToast.show("kf online");
                    } else {
                        ShowToast.show("kf offline");
                    }
                    break;
            }
            return false;
        });

        ActivityContainer.add(this);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityContainer.remove(this);
    }

    private void init() {
        GoodsDetailFragment goodsDetailFragment = new GoodsDetailFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.rl_goods_detail, goodsDetailFragment);
        ft.commit();
    }
}

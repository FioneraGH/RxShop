package com.centling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.centling.R;
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
                    ShowToast.show("cart");
                    break;
                case R.id.menu_goods_detail_kf:
                    if (TextUtils.isEmpty(UserInfoUtil.getName())) {
                        startActivity(new Intent(mContext, LoginActivity.class));
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

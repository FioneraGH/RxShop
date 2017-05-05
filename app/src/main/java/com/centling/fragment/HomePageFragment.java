package com.centling.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.R;
import com.centling.activity.GoodsDetailActivity;
import com.centling.activity.GoodsListActivity;
import com.centling.constant.RouterConstant;
import com.centling.databinding.FragmentHomePageBinding;
import com.centling.entity.HomeBean;
import com.centling.http.ApiManager;
import com.centling.util.ImageUtil;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment
        extends BaseFragment {
    private HomeBean mHomeBean;

    private FragmentHomePageBinding mFragmentHomePageBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentHomePageBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.fragment_home_page, container, false);
        return mFragmentHomePageBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentHomePageBinding.bannerHome.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mFragmentHomePageBinding.bannerHome.isAutoPlay(true);
        mFragmentHomePageBinding.bannerHome.setDelayTime(3000);
        mFragmentHomePageBinding.bannerHome.setIndicatorGravity(BannerConfig.CENTER);
        mFragmentHomePageBinding.bannerHome.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView) {
                if (o != null) {
                    ImageUtil.loadImage(o.toString(), imageView, R.drawable.iv_place_holder_2);
                }
            }
        });
        mFragmentHomePageBinding.bannerHome.setOnBannerClickListener(position -> {
            String type = mHomeBean.getAdvList().get(position - 1).getType();
            String data = mHomeBean.getAdvList().get(position - 1).getData();
            clickBanner(type, data);
        });

        mFragmentHomePageBinding.ivHomeVip.setOnClickListener(v -> {
            if (TextUtils.isEmpty(UserInfoUtil.getKey())) {
                ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
            } else {
                if (!UserInfoUtil.isLogin()) {
//                    startActivity(new Intent(mContext, VipActivity.class));
                }
            }
        });
        mFragmentHomePageBinding.ivHomeBrand.setOnClickListener(v -> {
//            startActivity(new Intent(mContext, BrandActivity.class));
        });
        mFragmentHomePageBinding.ivHomeCustom.setOnClickListener(v -> {
            if (!UserInfoUtil.isLogin()) {
                ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
            } else {
//                startActivity(new Intent(mContext, CustomizationActivity.class));
            }
        });
        mFragmentHomePageBinding.ivHomeCool.setOnClickListener(v -> {
//            startActivity(new Intent(mContext, CoolListActivity.class));
        });

        loadHomePage();
    }

    private boolean isFailed = true;

    public void loadHomePage() {
        if (!isFailed) {
            return;
        }
        ApiManager.getHomePage().compose(bindLifecycle()).subscribe(homeBean -> {
            this.mHomeBean = homeBean;
            isFailed = false;
            setHomePageData();
        }, throwable -> ShowToast.show("获取首页数据失败，点击底部重试"));
    }

    private void setHomePageData() {
        List<String> bannerUrl = new ArrayList<>();
        for (HomeBean.AdvListEntity entity : mHomeBean.getAdvList()) {
            bannerUrl.add(entity.getImage());
        }
        mFragmentHomePageBinding.bannerHome.update(bannerUrl);

        ImageUtil.loadImage(mHomeBean.getGoodsRecommend().get(0).getImage(),
                mFragmentHomePageBinding.ivHomeRecommended1, R.drawable.iv_place_holder_2);
        ImageUtil.loadImage(mHomeBean.getGoodsRecommend().get(1).getImage(),
                mFragmentHomePageBinding.ivHomeRecommended2, R.drawable.iv_place_holder_2);
        ImageUtil.loadImage(mHomeBean.getGoodsDiscount().get(0).getImage(),
                mFragmentHomePageBinding.ivHomeRecommended3, R.drawable.iv_place_holder_2);

        mFragmentHomePageBinding.ivHomeRecommended1.setOnClickListener(
                v -> clickBanner(mHomeBean.getGoodsRecommend().get(0).getType(),
                        mHomeBean.getGoodsRecommend().get(0).getData()));
        mFragmentHomePageBinding.ivHomeRecommended2.setOnClickListener(v -> clickBanner(
                mHomeBean.getGoodsRecommend().get(1).getType(),
                mHomeBean.getGoodsRecommend().get(1).getData()));
        mFragmentHomePageBinding.ivHomeRecommended3.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsListActivity.class);
            intent.putExtra("title", "折扣区");
            intent.putExtra("discount", "discount");
            startActivity(intent);
        });
    }

    private void clickBanner(String type, String data) {
        if (type.equals("goods")) {
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            intent.putExtra("goodsId", data);
            startActivity(intent);
        }
        if (type.equals("season")) {
            String[] filters = data.split("&amp;");
            String season = "", keyword = "";
            if (filters.length == 2) {
                if (filters[0].split("=").length > 1) {
                    season = filters[0].split("=")[1];
                }
                if (filters[1].split("=").length > 1) {
                    keyword = filters[1].split("=")[1];
                }
            }
            Intent intent = new Intent(mContext, GoodsListActivity.class);
            intent.putExtra("season", season);
            intent.putExtra("keyword", keyword);
            intent.putExtra("title", setTitle(data));
            startActivity(intent);
        }
        if (type.equals("keyword")) {
            Intent intent = new Intent(mContext, GoodsListActivity.class);
            intent.putExtra("keyword", data);
            intent.putExtra("title", data);
            startActivity(intent);
        }
    }

    private String setTitle(String data) {
        String title = "商品列表";
        if (data.contains("spring")) {
            title = "春季";
        } else if (data.contains("summer")) {
            title = "夏季";
        } else if (data.contains("autumn")) {
            title = "秋季";
        } else if (data.contains("winter")) {
            title = "冬季";
        } else if (data.contains("all")) {
            title = "全部";
        }
        return title;
    }
}

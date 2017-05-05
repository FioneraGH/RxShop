package com.centling.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.R;
import com.centling.activity.OrderConfirmActivity;
import com.centling.adapter.GoodsDetailCommendedAdapter;
import com.centling.constant.RouterConstant;
import com.centling.databinding.FragmentGoodsDetailBinding;
import com.centling.entity.GoodsDetailBean;
import com.centling.event.CollectionRelationEvent;
import com.centling.event.OrderRelationEvent;
import com.centling.http.ApiManager;
import com.centling.http.HttpConstants;
import com.centling.popupwindow.SharedPopupWindow;
import com.fionera.base.util.DisplayUtil;
import com.centling.util.ImageUtil;
import com.centling.util.SPUtil;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;
import com.fionera.base.widget.RadioGroupFlowLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class GoodsDetailFragment
        extends BaseFragment {
    private int innerTemp = 0;
    int temp_i = 0;
    int temp_j = 0;
    private boolean select_flag = false;

    private void isGetBirthday() {
        Map<String, String> params = new HashMap<>();
        params.put("goods_id", selectGoodsId);

        ApiManager.getIsBirthDay(params).compose(bindLifecycle()).subscribe(empty -> {
            if (UserInfoUtil.getVip().equals("1")) {
                buy();
            } else {
                ShowToast.show("您还不是VIP,不能领取");
            }
        }, throwable -> ShowToast.show("获取信息失败"));

    }

    private void buy() {
        if (!UserInfoUtil.isLogin()) {
            ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
            return;
        }
        if (buyNum > selectStorage) {
            ShowDialog.showConfirmDialog(mContext, "提示", "抱歉,该规格库存不足", null);
            return;
        }
        Intent jumpIntent = new Intent(mContext, OrderConfirmActivity.class);
        if (intent.hasExtra("judgment_enter")) {
            switch (intent.getStringExtra("judgment_enter")) {
                case "vip":
                    if (UserInfoUtil.getVip().equals("0")) {
                        ShowToast.show("您还不是VIP,不能购买此商品");
                        return;
                    }
                    break;
                case "birthday":
                    jumpIntent.putExtra("judgment_url", "birthday");
                    break;
                case "new_try":
                    if (UserInfoUtil.getVip().equals("1")) {
                        jumpIntent.putExtra("judgment_url", "new_try");
                    } else {
                        ShowToast.show("您还不是VIP,不能申请试穿");
                        return;
                    }
                    break;
                case "recommand":
                    if (UserInfoUtil.getVip().equals("1")) {
                        jumpIntent.putExtra("judgment_url", "new_try");
                    } else {
                        ShowToast.show("您还不是VIP,不能购买此商品");
                        return;
                    }
                    break;
                case "custom":
                    jumpIntent.putExtra("judgment_url", "custom");
                    break;
                case "collection":
                    if (honnyType.equals("3") || honnyType.equals("4") || honnyType.equals(
                            "5") || honnyType.equals("6")) {
                        if (!UserInfoUtil.getVip().equals("1")) {
                            ShowToast.show("您还不是VIP,不能购买此商品");
                            return;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        jumpIntent.putExtra("cart_from", "0");
        jumpIntent.putExtra("cart_info", selectGoodsId + "|" + buyNum);
        jumpIntent.putExtra("data", honnyType);
        mContext.startActivity(jumpIntent);
    }

    // 保存每个详细属性的引用, 如用get(0)得到"颜色"的详细属性列表的TextView引用
    private ArrayList<ArrayList<TextView>> allValuesTvList;
    // 详细属性value的list
    private ArrayList<ArrayList<String>> detailValueList;
    // 详细属性key的list
    private ArrayList<ArrayList<String>> detailKeyList;
    // 缩略图列表w
    private Map<String, String> specImageMap;
    // 属性key组合查找goodsID
    private Map<String, String> goodsIdMap;
    // 属性key组合查找storage
    private Map<String, String> goodsStorageMap;
    // 商品列表
    private ArrayList<GoodsDetailBean.ResultEntity.GoodsListEntity> goodList;
    // 每个大属性下选择的详细属性的pos
    private int selectValue[];
    // 当前点击属性对应的key,用于碰specImageMap判断是否更换VP图片
    private String curClickKey;
    private int buyNum = 1;
    private String selectGoodsId;

    private int selectStorage;

    // 图文详情地址
    private String detailUrl;
    // 商品参数地址
    private String paramUrl;
    private boolean isFavorite;
    private String honnyType = "";
    private boolean isRequestFinish;
    private Intent intent;
    private String share_pic = "";
    private String sharedName = "";

    private ArrayList<String> valueNameList;

    private FragmentGoodsDetailBinding mFragmentGoodsDetailBinding;
    private GoodsDetailBean mGoodsDetailBean;

    /**
     * 分享-弹出框监听内部类
     */
    View.OnClickListener itemsSharedOnClickReview = new View.OnClickListener() {

        public void onClick(View v) {
            UMImage image = new UMImage(mActivity, share_pic);
            String sharedUrl = HttpConstants.GOODS_SHARED_URL + selectGoodsId;
            //恒尼内衣APP是一款为您提供高端商务定制内衣的电商平台。
            String sharedStr = sharedName;
            switch (v.getId()) {
                case R.id.weixin_rl:
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN)
                            .setCallback(umShareListener).withText(sharedStr).withMedia(image)
                            .withTitle("恒尼内衣").withTargetUrl(sharedUrl).share();
                    break;
                case R.id.friend_rl:
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(umShareListener).withText(sharedStr).withTitle(sharedStr)
                            .withTargetUrl(sharedUrl).withMedia(image).share();
                    break;
                case R.id.sina_rl:
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA)
                            .setCallback(umShareListener).withText(sharedStr + sharedUrl)
                            .withMedia(image).share();
                    break;
                case R.id.qzone_rl:
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE)
                            .setCallback(umShareListener).withText(sharedStr)//必填且不为空
                            .withMedia(image)//必填且不为空
                            .withTargetUrl(sharedUrl).withTitle("恒尼内衣").share();
                    break;
                case R.id.qq_rl:
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ)
                            .setCallback(umShareListener).withText(sharedStr).withMedia(image)
                            .withTitle("恒尼内衣").withTargetUrl(sharedUrl).share();
                    break;
            }
        }

    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ShowToast. show(" 分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ShowToast.show(" 分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentGoodsDetailBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.fragment_goods_detail, container, false);
        return mFragmentGoodsDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isBirthDayAndNewTry();
        init();
        sendRequestToGetGoodsInfo();
    }

    private void isBirthDayAndNewTry() {
        intent = mActivity.getIntent();
        //从生日礼包进入时不显示商品推荐
        if (intent.getStringExtra("judgment_enter") != null) {
            if (intent.getStringExtra("judgment_enter").equals("birthday")) {
                mFragmentGoodsDetailBinding.rvGoodsDetailRecommended.setVisibility(View.GONE);
                mFragmentGoodsDetailBinding.fivGoodsDetailRecommended.setVisibility(View.GONE);
            }
        }
        //判断是否是生日礼包\新品试穿页面
        if (intent.hasExtra("birthday") || intent.hasExtra("is_newTry") || intent.hasExtra("")) {
            mFragmentGoodsDetailBinding.ivGoodsDetailCart.setVisibility(View.GONE);
            mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setVisibility(View.GONE);
            mFragmentGoodsDetailBinding.ivBuyPopupAdd.setClickable(false);
            if (intent.hasExtra("birthday")) {
                mFragmentGoodsDetailBinding.ivGoodsDetailBuy.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_goods_detail_gift));
            } else {
                mFragmentGoodsDetailBinding.ivGoodsDetailBuy.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_goods_detail_apply));
            }
        }
    }

    private void init() {
        mFragmentGoodsDetailBinding.ivBuyPopupSub.setEnabled(false);
        allValuesTvList = new ArrayList<>();
        mFragmentGoodsDetailBinding.detailScrollView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    doOnBorderListener();
                }
                return false;
            }
        });
        mFragmentGoodsDetailBinding.returnTopIv.setOnClickListener(v -> {
            mFragmentGoodsDetailBinding.detailScrollView.post(
                    () -> mFragmentGoodsDetailBinding.detailScrollView
                            .fullScroll(ScrollView.FOCUS_UP));
            mFragmentGoodsDetailBinding.returnTopIv.setVisibility(View.GONE);
        });
        mFragmentGoodsDetailBinding.vpBuyFragment.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mFragmentGoodsDetailBinding.vpBuyFragment.isAutoPlay(true);
        mFragmentGoodsDetailBinding.vpBuyFragment.setDelayTime(3000);
        mFragmentGoodsDetailBinding.vpBuyFragment.setIndicatorGravity(BannerConfig.CENTER);
        mFragmentGoodsDetailBinding.vpBuyFragment.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView) {
                if (o != null) {
                    ImageUtil.loadImage(o.toString(), imageView, R.drawable.iv_place_holder_1);
                }
            }
        });

        mFragmentGoodsDetailBinding.ivGoodsDetailBuy.setOnClickListener(v -> {
            if (!isRequestFinish) {
                return;
            }
            if (intent.hasExtra("birthday")) {
                isGetBirthday();
            } else {
                if (UserInfoUtil.getLevel_digit().equals("0")) {
                    ShowToast.show("请点击“我的”“金币”充值后享受更多超值体验~");
                }
                buy();
            }
        });
        mFragmentGoodsDetailBinding.ivGoodsDetailCart.setOnClickListener(v -> {
            if (!isRequestFinish) {
                return;
            }
            sendRequestToAddCart();
        });
        mFragmentGoodsDetailBinding.ivBuyPopupAdd.setOnClickListener(v -> {
            if (!isRequestFinish) {
                return;
            }
            mFragmentGoodsDetailBinding.tvBuyPopupNum.setText(String.valueOf(++buyNum));
            if (buyNum == 2) {
                mFragmentGoodsDetailBinding.ivBuyPopupSub.setEnabled(true);
            }
        });
        mFragmentGoodsDetailBinding.ivBuyPopupSub.setOnClickListener(v -> {
            if (!isRequestFinish) {
                return;
            }
            if (buyNum == 2) {
                mFragmentGoodsDetailBinding.ivBuyPopupSub.setEnabled(false);
            }
            mFragmentGoodsDetailBinding.tvBuyPopupNum.setText(String.valueOf(--buyNum));
        });
        mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setOnClickListener(v -> {
            if (!isRequestFinish) {
                return;
            }
            setGoodsFavorite(!isFavorite);
        });
        mFragmentGoodsDetailBinding.ivGoodsDetailShare.setOnClickListener(v -> {
            if (!isRequestFinish) {
                return;
            }
            new SharedPopupWindow(mContext, itemsSharedOnClickReview).showAtLocation(mFragmentGoodsDetailBinding.popParentRl, Gravity.BOTTOM, 0, 0);
        });
    }

    private void doOnBorderListener() {
        if (mFragmentGoodsDetailBinding.detailScrollView.getChildAt(
                0) != null && mFragmentGoodsDetailBinding.detailScrollView.getChildAt(0)
                .getMeasuredHeight() <= mFragmentGoodsDetailBinding.detailScrollView
                .getScrollY() + mFragmentGoodsDetailBinding.detailScrollView.getHeight()) {
            mFragmentGoodsDetailBinding.returnTopIv.setVisibility(View.VISIBLE);
        } else if (mFragmentGoodsDetailBinding.detailScrollView.getScrollY() > 30) {
            mFragmentGoodsDetailBinding.returnTopIv.setVisibility(View.VISIBLE);
        } else {
            mFragmentGoodsDetailBinding.returnTopIv.setVisibility(View.GONE);
        }
    }

    private void sendRequestToAddCart() {
        if (!UserInfoUtil.isLogin()) {
            ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("goods_id", selectGoodsId);
        params.put("quantity", String.valueOf(buyNum));

        ApiManager.addToCart(params).compose(bindLifecycle()).subscribe(empty -> {
            ShowToast.show("加入购物车成功");
            SPUtil.setBoolean("update_cart", true);
            EventBus.getDefault().post(new OrderRelationEvent.UpdateCart());
        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    private void setGoodsFavorite(boolean isAdd) {
        if (!UserInfoUtil.isLogin()) {
            ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
            return;
        }
        // 0添加  1取消
        sendFavoriteRequest(isAdd ? "0" : "1");
    }

    private void sendFavoriteRequest(String type) {
        Map<String, String> params = new HashMap<>();
        params.put("goods_id", mGoodsDetailBean.getResult().getGoods_common_info().getFirst_goods_id());
        params.put("type", type);
        showLoading("正在" + ("0".equals(type) ? "添加" : "取消") + "收藏");
        ApiManager.addToFavorite(params).compose(bindUntil(
                FragmentEvent.DESTROY_VIEW)).subscribe(string -> {
            dismissLoading();
            isFavorite = !isFavorite;
            mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setSelected(isFavorite);
            ShowToast.show("操作成功");
            EventBus.getDefault().post(new CollectionRelationEvent.UpdateCollection());
        }, throwable -> {
            dismissLoading();
            ShowDialog.showConfirmDialog(mContext, "提示", "操作失败", v -> mActivity.finish());
        });
    }

    private void sendRequestToGetGoodsInfo() {
        if (!intent.hasExtra("goodsCommonId") && !intent.hasExtra("goodsId")) {
            ShowToast.show("缺少goodsCommonId或goodsId");
            mActivity.finish();
            return;
        }
        showLoading();
        String goodsCommonId = intent.getStringExtra("goodsCommonId");
        String first_goods_id = intent.getStringExtra("goodsId");
        String key = UserInfoUtil.getKey();

        ApiManager.getGoodsDetail(goodsCommonId, first_goods_id, key).compose(bindUntil(
                FragmentEvent.DESTROY_VIEW)).subscribe(json -> {
            dismissLoading();
            isRequestFinish = true;
            mGoodsDetailBean = new Gson().fromJson(json, GoodsDetailBean.class);
            if (TextUtils.isEmpty(mGoodsDetailBean.getResult().getGoods_common_info().getGoods_name())) {
                ShowDialog.showConfirmDialog(mContext, "提示", "该商品不存在", v -> mActivity.finish());
                return;
            }
            addFootPrint();
            setCommendedGoods();

            honnyType = mGoodsDetailBean.getResult().getGoods_common_info().getHonny_type();
            isSpecial(honnyType);
            isFavorite = mGoodsDetailBean.getResult().isIs_favorites();
            mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setSelected(isFavorite);
            mFragmentGoodsDetailBinding.tvGoodsDetailName.setText(
                    mGoodsDetailBean.getResult().getGoods_common_info().getGoods_name());
            sharedName = mGoodsDetailBean.getResult().getGoods_common_info().getGoods_name();
            mFragmentGoodsDetailBinding.tvGoodsDetailPrice.setText(
                    String.format(Locale.CHINA, "￥%s",
                            mGoodsDetailBean.getResult().getGoods_common_info().getGoods_price()));
            mFragmentGoodsDetailBinding.tvGoodsDetailFreight.setText(
                    String.format(Locale.CHINA, "快递:%s元",
                            mGoodsDetailBean.getResult().getGoods_common_info().getGoods_freight()));
            mFragmentGoodsDetailBinding.tvGoodsDetailSaleNum.setText(
                    String.format(Locale.CHINA, "%s%s",
                            mGoodsDetailBean.getResult().getGoods_common_info().getGoods_salenum(),
                            mGoodsDetailBean.getResult().getGoods_common_info().getGoods_unit()));
            mFragmentGoodsDetailBinding.tvGoodsDetailComment.setText("1条");
            detailUrl = mGoodsDetailBean.getResult().getGoods_common_info().getGoods_body();
            if (TextUtils.isEmpty(detailUrl)) {
                mFragmentGoodsDetailBinding.detailViewFiv.setVisibility(View.GONE);
            }
            mFragmentGoodsDetailBinding.wvGoodsDetail.loadData(detailUrl);
            paramUrl = mGoodsDetailBean.getResult().getGoods_common_info().getParameter();
            goodList = (ArrayList<GoodsDetailBean.ResultEntity.GoodsListEntity>) mGoodsDetailBean.getResult()
                    .getGoods_list();
            if (mGoodsDetailBean.getResult().getGoods_common_info().getGoods_salenum_open() == 1) {
                mFragmentGoodsDetailBinding.salenumLl.setVisibility(View.VISIBLE);
            } else {
                mFragmentGoodsDetailBinding.salenumLl.setVisibility(View.GONE);
            }
            if (!mGoodsDetailBean.getResult().getGoods_common_info().getGoods_discount().equals("100")) {
                mFragmentGoodsDetailBinding.tvGoodsDetailPreviousPrice.setText(
                        String.format(Locale.CHINA, "¥ %s",
                                mGoodsDetailBean.getResult().getGoods_common_info().getGoods_marketprice()));
                DisplayUtil.setTextViewDeleteLine(
                        mFragmentGoodsDetailBinding.tvGoodsDetailPreviousPrice);
                mFragmentGoodsDetailBinding.tvGoodsDetailDiscount.setText(
                        String.format(Locale.CHINA, "%1.1f折", Float.parseFloat(
                                mGoodsDetailBean.getResult().getGoods_common_info().getGoods_discount()) / 10));
                mFragmentGoodsDetailBinding.tvGoodsDetailPreviousPrice.setVisibility(View.VISIBLE);
                mFragmentGoodsDetailBinding.tvGoodsDetailDiscount.setVisibility(View.VISIBLE);
            }
            mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setSelected(isFavorite);

            ArrayList<String> valueNameList = new ArrayList<>();
            specImageMap = new HashMap<>();
            goodsIdMap = new HashMap<>();
            goodsStorageMap = new HashMap<>();
            detailKeyList = new ArrayList<>();
            detailValueList = new ArrayList<>();
            JSONObject infoObject;
            try {
                infoObject = new JSONObject(json).getJSONObject("result").getJSONObject("goods_common_info");
                //spec_name为null或者""说明是只有一个属性的商品
                if (mGoodsDetailBean.getResult().getGoods_common_info()
                        .getSpec_name_sort() != null && mGoodsDetailBean.getResult().getGoods_common_info()
                        .getSpec_name_sort().size() > 0) {
                    initData(mGoodsDetailBean.getResult().getGoods_common_info().getSpec_name_sort(),
                            valueNameList,
                            mGoodsDetailBean.getResult().getGoods_common_info().getSpec_value_sort(),
                            detailKeyList, detailValueList);
                    specImageMap = new Gson().fromJson(infoObject.get("spec_image").toString(),
                            new TypeToken<Map<String, String>>() {
                            }.getType());
                    goodsIdMap = new Gson().fromJson(infoObject.get("spec_list").toString(),
                            new TypeToken<Map<String, String>>() {
                            }.getType());
                    goodsStorageMap = new Gson().fromJson(infoObject.get("spec_storage").toString(),
                            new TypeToken<Map<String, String>>() {
                            }.getType());
                } else {
                    selectStorage = Integer.parseInt(goodList.get(0).getGoods_info()
                            .getGoods_storage());
                    selectGoodsId = goodList.get(0).getGoods_info().getGoods_id();
                    ArrayList<String> urlsList = new ArrayList<>(Arrays.asList(goodList.get(0)
                            .getGoods_image().split(",")));
                    if (urlsList.size() != 0) {
                        share_pic = urlsList.get(0);
                    }
                    mFragmentGoodsDetailBinding.vpBuyFragment.update(urlsList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setGoodsInfo(valueNameList);
        }, throwable -> {
            dismissLoading();
            ShowToast.show("获取详情失败");
        });
    }

    private void addFootPrint() {
        Map<String, String> params = new HashMap<>();
        params.put("goods_id", mGoodsDetailBean.getResult().getGoods_common_info().getFirst_goods_id());
        ApiManager.addToFootprint(params).compose(bindLifecycle()).subscribe(empty -> {},throwable -> {});
    }

    private void setCommendedGoods() {
        GoodsDetailCommendedAdapter adapter = new GoodsDetailCommendedAdapter(mContext,
                mGoodsDetailBean.getResult().getGoods_commendlist());
        mFragmentGoodsDetailBinding.rvGoodsDetailRecommended.setLayoutManager(
                new GridLayoutManager(mContext, 2));
        mFragmentGoodsDetailBinding.rvGoodsDetailRecommended.setAdapter(adapter);
        if (mGoodsDetailBean.getResult().getGoods_commendlist().size() == 0) {
            mFragmentGoodsDetailBinding.fivGoodsDetailRecommended.setVisibility(View.GONE);
        }
    }

    //判断是否是新品试穿页面 或者生日礼包
    private void isSpecial(String honnyType) {
        //3 商品推荐
        //4 生日礼包/新品试穿
        //5 生日礼包/新品试穿
        if (honnyType.equals("4") || honnyType.equals("5")) {
            mFragmentGoodsDetailBinding.ivGoodsDetailCart.setVisibility(View.GONE);
            mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setVisibility(View.GONE);
            mFragmentGoodsDetailBinding.ivBuyPopupAdd.setClickable(false);
            if (intent.hasExtra("birthday")) {
                mFragmentGoodsDetailBinding.ivGoodsDetailBuy.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_goods_detail_gift));
            } else {
                mFragmentGoodsDetailBinding.ivGoodsDetailBuy.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_goods_detail_apply));
            }
        } else if (honnyType.equals("3")) {
            mFragmentGoodsDetailBinding.ivGoodsDetailCart.setVisibility(View.VISIBLE);
            mFragmentGoodsDetailBinding.ivGoodsDetailFavorite.setVisibility(View.VISIBLE);
            mFragmentGoodsDetailBinding.ivBuyPopupAdd.setClickable(true);
            mFragmentGoodsDetailBinding.ivGoodsDetailBuy.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ic_goods_detail_buy));
        }
    }

    /**
     * @param nameSort     后端返回的数组, 例 ["1,颜色","15,尺码"]
     * @param targetNames  处理后的数组, 例["颜色","尺码"]
     * @param valueSort    后端返回的数组, 例 [["623,灰色","624,白色"],["625,M","626,L","627,XL","628,XXL"]]
     * @param targetKeys   处理后的数组, 例 [["623", "624"], ["625", "626", "627", "628"]]
     * @param targetValues 处理后的数组, 例 [["灰色", "白色"], ["M", "L", "XL", "XXL"]]
     */
    private static void initData(List<String> nameSort, ArrayList<String> targetNames,
                                 List<List<String>> valueSort,
                                 ArrayList<ArrayList<String>> targetKeys,
                                 ArrayList<ArrayList<String>> targetValues) {
        for (String s : nameSort) {
            targetNames.add(s.split(",")[1]);
        }
        for (List<String> item : valueSort) {
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            for (String s : item) {
                keys.add(s.split(",")[0]);
                values.add(s.split(",")[1]);
            }
            targetKeys.add(keys);
            targetValues.add(values);
        }
    }

    private void setGoodsInfo(ArrayList<String> valuesNameList) {
        String specStr = "";
        if (mActivity.getIntent().hasExtra("goodsId")) {
            select_flag = true;
            String[] firstGoodsKey = new String[detailKeyList.size()];
            selectGoodsId = mActivity.getIntent().getStringExtra("goodsId");
            for (String s : goodsIdMap.keySet()) {
                if (goodsIdMap.get(s).equals(selectGoodsId)) {
                    firstGoodsKey = s.split("|");
                    specStr = s;
                    selectStorage = Integer.parseInt(goodsStorageMap.get(s));
                    break;
                }
            }
            for (int i = 0; i < detailKeyList.size(); i++) {
                for (int j = 0; j < detailKeyList.get(i).size(); j++) {
                    for (String s : firstGoodsKey) {
                        if (s.equals(detailKeyList.get(i).get(j))) {
                            allValuesTvList.get(i).get(j).setSelected(true);
                            selectValue[i] = j;
                            if (specImageMap.get(s) != null) {
                                curClickKey = s;
                            }
                            break;
                        }
                    }
                }
            }
        }
        valueNameList = new ArrayList<>();
        valueNameList = valuesNameList;
        selectValue = new int[valuesNameList.size()];
        for (int i = 0; i < selectValue.length; i++) {
            selectValue[i] = 0;
        }
        //第一行
        if (valuesNameList.size() > 0) {
            View firstValueView = View.inflate(mContext, R.layout.buy_window_value_item, null);
            RadioGroupFlowLayout firstFlowLayout = (RadioGroupFlowLayout) firstValueView
                    .findViewById(R.id.fl_buy_popup_item_property);
            TextView tvName = (TextView) firstValueView.findViewById(R.id.tv_buy_popup_item_name);
            tvName.setText(valuesNameList.get(0));

            // 新建一个每个属性的详细属性的列表, 里面包含该属性的所有TextView的引用
            ArrayList<TextView> itemValueList = new ArrayList<>();
            for (int j = 0; j < detailValueList.get(0).size(); j++) {
                TextView tvProperty = new TextView(mContext);
                tvProperty.setText(detailValueList.get(0).get(j));
                tvProperty.setTextColor(
                        ContextCompat.getColorStateList(mContext, R.color.sl_text_buy_rbtn));
                tvProperty.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.sl_bg_buy_rbtn));
                tvProperty.setMinWidth(DisplayUtil.dp2px(70));
                tvProperty.setGravity(Gravity.CENTER);
                int paddingLeft = DisplayUtil.dp2px(10);
                int paddingTop = DisplayUtil.dp2px(5);
                tvProperty.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
                tvProperty.setOnClickListener(new ValueClickListener(0, j));
                itemValueList.add(tvProperty);
                firstFlowLayout.addView(tvProperty);
            }
            String[] spArr = specStr.split("\\|");
            for (int i = 0; i < detailKeyList.get(0).size(); i++) {
                if (spArr[0].equals(detailKeyList.get(0).get(i))) {
                    temp_i = i;
                    break;
                }
            }
            firstFlowLayout.getChildAt(temp_i).setSelected(true);
            allValuesTvList.add(itemValueList);
            mFragmentGoodsDetailBinding.llBuyPopupContainer.addView(firstValueView);
        }
        //剩下的行数
        for (int i = 1; i < valuesNameList.size(); i++) {
            View valueView = LayoutInflater.from(mContext).inflate(R.layout.buy_window_value_item,
                    mFragmentGoodsDetailBinding.llBuyPopupContainer, false);
            RadioGroupFlowLayout flowLayout = (RadioGroupFlowLayout) valueView.findViewById(
                    R.id.fl_buy_popup_item_property);
            TextView tvName = (TextView) valueView.findViewById(R.id.tv_buy_popup_item_name);
            tvName.setText(valuesNameList.get(i));
            // 新建一个每个属性的详细属性的列表, 里面包含该属性的所有TextView的引用
            ArrayList<TextView> itemValueList = new ArrayList<>();
            for (int j = 0; j < detailValueList.get(i).size(); j++) {
                TextView tvProperty = new TextView(mContext);
                tvProperty.setText(detailValueList.get(i).get(j));
                tvProperty.setTextColor(
                        ContextCompat.getColorStateList(mContext, R.color.sl_text_buy_rbtn));
                tvProperty.setBackground(
                        ContextCompat.getDrawable(mContext, R.drawable.sl_bg_buy_rbtn));
                tvProperty.setMinWidth(DisplayUtil.dp2px(70));
                tvProperty.setGravity(Gravity.CENTER);
                int paddingLeft = DisplayUtil.dp2px(10);
                int paddingTop = DisplayUtil.dp2px(5);
                tvProperty.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
                tvProperty.setOnClickListener(new ValueClickListener(i, j));
                itemValueList.add(tvProperty);
                flowLayout.addView(tvProperty);
            }
            String[] spArr = specStr.split("\\|");
            for (int x = 1; x < spArr.length; x++) {
                for (int m = 1; m < detailKeyList.size(); m++) {
                    for (int n = 0; n < detailKeyList.get(m).size(); n++) {
                        if (spArr[x].equals(detailKeyList.get(m).get(n))) {
                            temp_j = n;
                            break;
                        }
                    }
                }
            }
            flowLayout.getChildAt(temp_j).setSelected(true);
            allValuesTvList.add(itemValueList);
            mFragmentGoodsDetailBinding.llBuyPopupContainer.addView(valueView);
        }

        if (mActivity.getIntent().hasExtra("goodsCommonId")) {
            if (mGoodsDetailBean.getResult().getGoods_list().size() == 1) {
                selectGoodsId = mGoodsDetailBean.getResult().getGoods_list().get(0).getGoods_info()
                        .getGoods_id();
                selectStorage = Integer.parseInt(mGoodsDetailBean.getResult().getGoods_list().get(0)
                        .getGoods_info().getGoods_storage());
            } else {
                curClickKey = detailKeyList.get(0).get(0);
                StringBuilder sb = new StringBuilder();
                sb.append(detailKeyList.get(0).get(0));
                for (int i = 1; i < detailKeyList.size(); i++) {
                    sb.append("|").append(detailKeyList.get(i).get(0));
                }
                selectGoodsId = goodsIdMap.get(sb.toString());
                selectStorage = Integer.parseInt(goodsStorageMap.get(sb.toString()));
                if (selectGoodsId == null) {
                    sb.delete(0, sb.length());
                    sb.append(detailKeyList.get(detailKeyList.size() - 1).get(0));
                    for (int i = detailKeyList.size() - 2; i >= 0; i--) {
                        sb.append("|").append(detailKeyList.get(i).get(0));
                    }
                    selectGoodsId = goodsIdMap.get(sb.toString());
                    selectStorage = Integer.parseInt(goodsStorageMap.get(sb.toString()));
                }
            }
        }
        updateGoodsInfoWithId();
    }

    private class ValueClickListener
            implements View.OnClickListener {
        private int outer;
        private int inner;

        ValueClickListener(int outer, int inner) {
            this.outer = outer;
            this.inner = inner;
        }

        @Override
        public void onClick(View v) {
            if (v.isSelected()) {
                return;
            }
            for (int i = 0; i < allValuesTvList.get(outer).size(); i++) {
                allValuesTvList.get(outer).get(i).setSelected(i == inner);
            }
            //记录第二行规格
            if (outer == 1) {
                innerTemp = inner;
            }
            if (outer == 0) {
                temp_i = inner;//记录第一行规格
            }
            curClickKey = detailKeyList.get(outer).get(inner);
            selectValue[outer] = inner;
            StringBuilder sbProperty = new StringBuilder();
            StringBuilder sbKey = new StringBuilder();
            sbProperty.append("选择了:  ");
            if (selectGoodsId != null && select_flag) {
                sbKey.append(detailKeyList.get(0).get(temp_i));
                Log.d("loren",
                        "detailKeyList.get(0).get(temp_i):" + detailKeyList.get(0).get(temp_i));
            } else {
                sbKey.append(detailKeyList.get(0).get(selectValue[0]));//0,0
                Log.d("loren", "detailKeyList.get(0).get(selectValue[0]):" + detailKeyList.get(0)
                        .get(selectValue[0]));
            }
            for (int i = 0; i < selectValue.length; i++) {
                sbProperty.append("\"").append(detailValueList.get(i).get(selectValue[i])).append(
                        "\"  ");
                if (i > 0) {
                    sbKey.append("|").append(detailKeyList.get(i).get(selectValue[i]));
                }
            }
            selectGoodsId = goodsIdMap.get(sbKey.toString());
            Log.d("loren+",
                    "sbKey.toString():" + sbKey.toString() + " selectGoodsId:" + selectGoodsId);
            selectStorage = Integer.parseInt(goodsStorageMap.get(sbKey.toString()));
            if (selectGoodsId == null) {
                sbKey.delete(0, sbKey.length());
                sbKey.append(detailKeyList.get(detailKeyList.size() - 1)
                        .get(selectValue[selectValue.length - 1]));
                for (int i = detailKeyList.size() - 2; i >= 0; i--) {
                    sbKey.append("|").append(detailKeyList.get(i).get(selectValue[i]));
                }
                selectGoodsId = goodsIdMap.get(sbKey.toString());
                selectStorage = Integer.parseInt(goodsStorageMap.get(sbKey.toString()));

            }
            updateGoodsInfoWithId();
            //若点击第一行，则刷新第二行
            if (outer == 0) {
                refreshStorage(outer);
            }
        }
    }

    private void updateGoodsInfoWithId() {
        System.out.println("选择的id=" + selectGoodsId + "   数量=" + selectStorage);
        for (GoodsDetailBean.ResultEntity.GoodsListEntity goodsListEntity : goodList) {
            if (goodsListEntity.getGoods_info().getGoods_id().equals(selectGoodsId)) {
                mFragmentGoodsDetailBinding.tvGoodsDetailPrice.setText(
                        String.format(Locale.CHINA, "￥%s",
                                goodsListEntity.getGoods_info().getGoods_price()));
            }
        }

        int pos = 0;
        for (int i = 0; i < this.goodList.size(); i++) {
            if (this.goodList.get(i).getGoods_info().getGoods_id().equals(this.selectGoodsId)) {
                pos = i;
                break;
            }
        }
        ArrayList<String> urlsList = new ArrayList<>(Arrays.asList(goodList.get(pos)
                .getGoods_image().split(",")));
        if (urlsList.size() != 0) {
            share_pic = urlsList.get(0);
        }
        mFragmentGoodsDetailBinding.vpBuyFragment.update(urlsList);
    }

    private void refreshStorage(int outer) {
        int outerTemp = outer + 1;
        if (outerTemp < allValuesTvList.size()) {
            //移除其余行
            int count = mFragmentGoodsDetailBinding.llBuyPopupContainer.getChildCount();
            for (int m = count - 1; m >= 1; m--) {
                mFragmentGoodsDetailBinding.llBuyPopupContainer.removeViewAt(m);
                allValuesTvList.remove(m);
            }
            //添加其余行
            addViewLine(outer);
        }
    }

    private void addViewLine(int outer) {
        for (int i = 1; i < valueNameList.size(); i++) {
            View valueView = LayoutInflater.from(mContext).inflate(R.layout.buy_window_value_item,
                    mFragmentGoodsDetailBinding.llBuyPopupContainer, false);
            RadioGroupFlowLayout flowLayout = (RadioGroupFlowLayout) valueView.findViewById(
                    R.id.fl_buy_popup_item_property);
            TextView tvName = (TextView) valueView.findViewById(R.id.tv_buy_popup_item_name);
            tvName.setText(valueNameList.get(i));
            // 新建一个每个属性的详细属性的列表, 里面包含该属性的所有TextView的引用
            ArrayList<TextView> itemValueList = new ArrayList<>();
            for (int j = 0; j < detailValueList.get(i).size(); j++) {
                TextView tvProperty = new TextView(mContext);
                tvProperty.setText(detailValueList.get(i).get(j));
                //判断库存
                if (!hasStorage(outer, j)) {
                    //无库存
                    tvProperty.setTextColor(getResources().getColor(R.color.white));
                    tvProperty.setBackgroundColor(getResources().getColor(R.color.grey2));
                } else {
                    //有库存
                    tvProperty.setTextColor(
                            ContextCompat.getColorStateList(mContext, R.color.sl_text_buy_rbtn));
                    tvProperty.setBackground(
                            ContextCompat.getDrawable(mContext, R.drawable.sl_bg_buy_rbtn));
                }
                tvProperty.setMinWidth(DisplayUtil.dp2px(70));
                tvProperty.setGravity(Gravity.CENTER);
                int paddingLeft = DisplayUtil.dp2px(10);
                int paddingTop = DisplayUtil.dp2px(5);
                tvProperty.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
                tvProperty.setOnClickListener(new ValueClickListener(i, j));
                itemValueList.add(tvProperty);
                flowLayout.addView(tvProperty);
            }
            flowLayout.getChildAt(innerTemp).setSelected(true);
            allValuesTvList.add(itemValueList);
            mFragmentGoodsDetailBinding.llBuyPopupContainer.addView(valueView);
        }
    }

    private boolean hasStorage(int outer, int i) {
        int outerTemp = outer + 1;
        int storageTemp = 0;
        Set<String> key = goodsStorageMap.keySet();
        for (String s : key) {
            if (s.contains(curClickKey + "|" + detailKeyList.get(outerTemp).get(i))) {
                storageTemp += Integer.parseInt(goodsStorageMap.get(s));
            }
        }
        return storageTemp != 0;
    }
}

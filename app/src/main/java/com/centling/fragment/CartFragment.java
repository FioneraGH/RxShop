package com.centling.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.activity.FragmentContainerActivity;
import com.centling.activity.GoodsDetailActivity;
import com.centling.adapter.CartListAdapter;
import com.centling.constant.RouterConstant;
import com.centling.databinding.FragmentCartBinding;
import com.centling.entity.CartBean;
import com.centling.event.OrderRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.SPUtil;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * CartFragment
 * Created by fionera on 15-12-2.
 */

@Route(path = RouterConstant.Main.CART)
public class CartFragment
        extends BaseFragment implements View.OnClickListener {

    private static final int TOTAL_PRICE = 1000;
    private static final int CHECK_ALL = 1001;

    private static float totalPriceInCart = 0f;
    private volatile static int totalCountInCart = 0;
    private static boolean allChecked = false;

    private Handler updatePriceHandler = new WeakHandler(this);

    private List<CartBean.CartListEntity> cartList;
    private List<CartBean.GoodsCommonlistEntity> cartRecommendList;
    private CartListAdapter cartListAdapter;

    private boolean isEdit;
    private static Intent commonIntent = new Intent();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cart_title_right:
                changeEditMode(isEdit);
                break;
            case R.id.cb_cart_check_all:
                selectedAll();
                mFragmentCartBinding.cbCartCheckAll.setEnabled(false);
                break;
            case R.id.tv_cart_buy_or_del:
                if (0 == cartListAdapter.getSelectedCount() && !mFragmentCartBinding.cbCartCheckAll.isChecked()) {
                    if (!isEdit) {
                        ShowToast.show("请选择商品");
                        return;
                    }
                }
                if (isEdit) {
                    ShowDialog.showSelectDialog(mContext, "确认删除", "是否删除选中商品？", "(删除后将需要重新添加)",
                            v1 -> deleteGoodsFromCart());

                } else {
                    if (UserInfoUtil.getLevel_digit().equals("0")) {
                        ShowToast.show("请点击“我的”“金币”充值后享受更多超值体验~");
                    }
                    StringBuilder cartInfo = new StringBuilder();
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).isChecked()) {
                            if (!"0".equals(cartList.get(i)
                                    .getGoods_num())) {
                                cartInfo.append(cartList.get(i).getCart_id()).append("|").append(
                                        cartList.get(i).getGoods_num());
                            }
                            if (i != cartList.size() - 1) {
                                cartInfo.append(",");
                            }
                        }
                    }
//                    commonIntent.setClass(mContext, OrderConfirmActivity.class);
                    commonIntent.putExtra("cart_info", cartInfo.toString());
                    commonIntent.putExtra("cart_from", "1");
                    startActivity(commonIntent);
                }
                break;
        }
    }

    private void deleteGoodsFromCart() {
        mProcessDialog.setTitle("正在发送请求").showDialog(false);
        /*
          计算清空id
         */
        StringBuilder ids = new StringBuilder();
        for (int i = cartList.size() - 1; i >= 0; i--) {
            if (cartList.get(i).isChecked()) {
                ids.append(cartList.get(i).getCart_id()).append(",");
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("cart_id", ids.toString());

        ApiManager.cartClear(params).compose(bindUntil(FragmentEvent.DESTROY_VIEW)).subscribe(
                empty -> {
                    mProcessDialog.dismiss();
                    for (int i = cartList.size() - 1; i >= 0; i--) {
                        if (cartList.get(i).isChecked()) {
                            cartList.remove(i);
                            mFragmentCartBinding.rvCartList.getAdapter().notifyItemRemoved(i);
                        }
                    }
                    /*
                      通知删除之后的位置变化
                     */
                    mFragmentCartBinding.rvCartList.getAdapter().notifyItemRangeChanged(0,
                            cartList.size() + 1 + cartRecommendList.size());
                    /*
                      更新购物车价格
                     */
                    updatePriceHandler.obtainMessage(TOTAL_PRICE,
                            cartListAdapter.getSelectedCount(), 0, cartListAdapter.getTotalPrice())
                            .sendToTarget();
                    updatePriceHandler.obtainMessage(CHECK_ALL, cartListAdapter.isAllSelected())
                            .sendToTarget();
                    checkEmpty();
                }, throwable -> {
                    mProcessDialog.dismiss();
                    checkEmpty();
                    ShowDialog.showSelectDialog(mContext, "是否重试", "删除购物车失败", "", v -> {
                        deleteGoodsFromCart();
                    });
                });
    }

    /**
     * 为空时不显示结算栏
     */
    private void checkEmpty() {
        if (0 == cartList.size()) {
            mFragmentCartBinding.llCartBottom.setVisibility(View.GONE);
            mFragmentCartBinding.tvCartTitleRight.setVisibility(View.INVISIBLE);
            mFragmentCartBinding.tvCartTitleRight.setClickable(false);
        } else {
            mFragmentCartBinding.llCartBottom.setVisibility(View.VISIBLE);
            mFragmentCartBinding.tvCartTitleRight.setVisibility(View.VISIBLE);
            mFragmentCartBinding.tvCartTitleRight.setClickable(true);
        }
    }

    /**
     * 切换编辑模式
     */
    private void changeEditMode(boolean isEdit) {
        if (isEdit) {
            mFragmentCartBinding.tvCartTitleRight.setText("编辑");
            mFragmentCartBinding.tvCartBuyOrDel.setText("去结算");
            mFragmentCartBinding.tvCartBuyOrDel.setBackground(
                    ContextCompat.getDrawable(mContext, R.drawable.bg_dark_red_corner5));
        } else {
            mFragmentCartBinding.tvCartTitleRight.setText("完成");
            mFragmentCartBinding.tvCartBuyOrDel.setText("删除");
            mFragmentCartBinding.tvCartBuyOrDel
                    .setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_dark_red_corner5));
        }
        cartListAdapter.setEditMode(!isEdit);
        mFragmentCartBinding.rvCartList.getAdapter().notifyDataSetChanged();
        this.isEdit = !isEdit;
    }

    FragmentCartBinding mFragmentCartBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mFragmentCartBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.fragment_cart, container, false);
        return mFragmentCartBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartList = new ArrayList<>();
        cartRecommendList = new ArrayList<>();

        mFragmentCartBinding.tvCartTitleRight.setOnClickListener(this);
        mFragmentCartBinding.tvCartBuyOrDel.setOnClickListener(this);
        mFragmentCartBinding.cbCartCheckAll.setOnClickListener(this);

        initPage();

        if (mActivity instanceof FragmentContainerActivity) {
            mFragmentCartBinding.ivCartTitleBack.setVisibility(View.VISIBLE);
            mFragmentCartBinding.ivCartTitleBack.setOnClickListener(v -> mActivity.finish());
            getCartData(true);
        }
        // 第一次进来刷新购物车
        SPUtil.setBoolean("update_cart", true);
    }

    private void initPage() {

        cartListAdapter = new CartListAdapter(mContext, cartList, cartRecommendList, mFragmentCartBinding.rvCartList,
                updatePriceHandler, mProcessDialog, mActivity instanceof FragmentContainerActivity);

        mFragmentCartBinding.srCartList.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getCartData(false);
            }
        });

        final GridLayoutManager manager = new GridLayoutManager(mContext, 2,
                GridLayoutManager.VERTICAL, false);
        /*
          设置列表分2列
         */
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position <= cartList.size()) ? manager.getSpanCount() : 1;
            }
        });
        mFragmentCartBinding.rvCartList.setLayoutManager(manager);
        mFragmentCartBinding.rvCartList.setAdapter(cartListAdapter);
        cartListAdapter.setOnItemClickListener((View view, int pos) -> {

            if (pos < cartList.size()) {
                /*
                  点击跳转对应商品
                 */
                if ("0".equals(cartList.get(pos).getHonny_type())) {
                    startActivity(new Intent(mContext, GoodsDetailActivity.class)
                            .putExtra("goodsId", cartList.get(pos).getGoods_id()));
                } else if ("2".equals(cartList.get(pos).getHonny_type())) {
                    startActivity(new Intent(mContext, GoodsDetailActivity.class)
                            .putExtra("goodsId", cartList.get(pos).getGoods_id()));
                }
            } else if (pos > cartList.size()) {
                startActivity(new Intent(mContext, GoodsDetailActivity.class)
                        .putExtra("goodsCommonId",
                                cartRecommendList.get(pos - cartList.size() - 1)
                                        .getGoods_commonid()));
            }
        });

        mFragmentCartBinding.cbCartCheckAll.setChecked(allChecked);

        totalPriceInCart = cartListAdapter.getTotalPrice();
        mFragmentCartBinding.tvCartAllPrice.setText("￥" + String.format(Locale.CHINA, "%.2f", totalPriceInCart));
        totalCountInCart = cartListAdapter.getSelectedCount();
        mFragmentCartBinding.tvCartAllGoodsCount.setText("(共" + totalCountInCart + "件，不含运费)");

        checkEmpty();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (UserInfoUtil.isLogin()) {
                if (SPUtil.getBoolean("update_cart")) {
                    updatePriceHandler.post(() -> getCartData(true));
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void getCartData(boolean isShow) {
        if (isShow) {
            mProcessDialog.setTitle("正在获取购物车").showDialog(false);
        }
        Map<String, String> params = new HashMap<>();
        ApiManager.cartList(params).compose(bindUntil(FragmentEvent.DESTROY_VIEW)).subscribe(
                cartBean -> {
                    mProcessDialog.dismiss();
                    mFragmentCartBinding.srCartList.refreshComplete();
                    cartList.clear();
                    for (CartBean.CartListEntity cart : cartBean.getCart_list()) {
                        /*
                          0 s和下架
                         */
                        if ("0".equals(cart.getGoods_storage()) || "0".equals(
                                cart.getGoods_state())) {
                            cart.setGoods_num("0");
                        }
                        cartList.add(cart);
                    }
                    cartRecommendList.clear();
                    cartRecommendList.addAll(cartBean.getGoods_commonlist());
                    mFragmentCartBinding.rvCartList.getAdapter().notifyDataSetChanged();
                    selectedAll();
                    checkEmpty();
                    SPUtil.setBoolean("update_cart", mActivity instanceof FragmentContainerActivity);
                }, throwable -> {
                    mProcessDialog.dismiss();
                    mFragmentCartBinding.srCartList.refreshComplete();
                    ShowToast.show("获取购物车失败！");
                    checkEmpty();
                    ShowDialog.showSelectDialog(mContext, "是否重试", "购物车列表获取失败", "", v -> {
                        getCartData(true);
                    });
                });
    }

    /**
     * 选中按钮切换，自己根据购物车状态判定全选还是反选
     */
    private void selectedAll() {
        allChecked = !cartListAdapter.isAllSelected();
        mFragmentCartBinding.cbCartCheckAll.setChecked(allChecked);
        cartListAdapter.selectAll(allChecked);
        mFragmentCartBinding.rvCartList.getAdapter().notifyDataSetChanged();
    }

    private static class WeakHandler
            extends Handler {

        WeakReference<CartFragment> fragmentWeakReference;

        WeakHandler(CartFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            CartFragment theFragment = fragmentWeakReference.get();
            if (msg.what == TOTAL_PRICE) {
                totalPriceInCart = (float) msg.obj;
                totalCountInCart = msg.arg1;
                theFragment.mFragmentCartBinding.tvCartAllPrice
                        .setText("￥" + String.format(Locale.CHINA, "%.2f", totalPriceInCart));
                theFragment.mFragmentCartBinding.tvCartAllGoodsCount.setText("(共" + totalCountInCart + "件，不含运费)");
            }
            if (msg.what == CHECK_ALL) {
                allChecked = (boolean) msg.obj;
                theFragment.mFragmentCartBinding.cbCartCheckAll.setChecked(allChecked);
            }
            /*
              设置延时基于弱引用关联时间
             */
            this.postDelayed(() -> theFragment.mFragmentCartBinding.cbCartCheckAll.setEnabled(true), 500);
        }
    }

    @Subscribe
    public void onEventUpdateCart(OrderRelationEvent.UpdateCart updateCart){
        getCartData(true);
    }
}

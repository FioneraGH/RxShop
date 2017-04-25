package com.centling.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.constant.PaymentConstant;
import com.centling.databinding.ActivityOrderConfirmBinding;
import com.centling.entity.AddressBean;
import com.centling.entity.OrderConfirmBean;
import com.centling.event.OrderRelationEvent;
import com.centling.http.ApiManager;
import com.centling.payment.ali.AlipayUtils;
import com.centling.payment.wx.WXPayUtil;
import com.centling.popupwindow.ChooseDeliveryPopup;
import com.centling.popupwindow.ChoosePayMethodPopup;
import com.centling.util.DisplayUtil;
import com.centling.util.ImageUtil;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class OrderConfirmActivity
        extends TitleBarActivity
        implements ChoosePayMethodPopup.OnDialogListener,View.OnClickListener {

    public static final int DIRECT_ADD_ADDRESS = 666;
    public static final int CHOOSE_SELF_PICKUP_RESULT = 444;
    private static final int ASK_FOR_ADDRESS = 1000;
    private static final int CHOOSE_SELF_PICKUP = 555;

    private String deliveryMsg = "0";
    private ChooseDeliveryPopup chooseDeliveryPopupWindow;
    private ChoosePayMethodPopup choosePayMethodPopup;

    private String cart_info = "";
    private String cart_from = "";
    private String member_info = "";

    private OrderConfirmBean orderConfirmBean;
    private OrderConfirmBean.ResultEntity orderConfirmResultEntity;
    private List<OrderConfirmBean.ResultEntity.BuyListEntity.StoreCartListEntity>
            orderConfirmStoreCartList;

    private AlipayUtils alipayUtils;
    private float goldsCount = 0f;
    private float useGolds = 0f;

    private String mPersonId = "";
    private String selfPickupId = "";

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private String freightTemp = "";
    private String freight_hash = "";
    private String area_id = "";
    private String city_id = "";
    private String personal_info = "";
    private String offpay_hash;
    private String offpay_hash_batch;
    private String addressId;
    private String orderName;
    private String pay_sn;
    private double totalPrice;
    private double totalPriceOrigin;
    private List<EditText> ets = new ArrayList<>();
    TextWatcher textWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;
        private boolean sign = false;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = ets.get(0).getSelectionStart();
            editEnd = ets.get(0).getSelectionEnd();
            if (temp.length() > 200) {
                ShowToast.show("您输入的字数不能超过200字");
                s.delete(editStart - 1, editEnd);
            }
        }
    };
    private TextView tvFreightTemp;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_order_confirm:
                String honnyType = "";
                Intent m = getIntent();
                if (m.getStringExtra("data") != null) {
                    honnyType = m.getStringExtra("data");
                }
                if (addressId == null) {
                    ShowToast.show("请选择收货地址");
                    return;
                } else {
                    if (totalPrice != 0.0 && !honnyType.equals("4")) {
                        if (selfPickupId.contentEquals("")) {
                            choosePayMethodPopup.setData((float) totalPrice, goldsCount);
                        } else {
                            choosePayMethodPopup.setData(Float.parseFloat(mActivityOrderConfirmBinding.tvOrderConfirmAllPrice.getText().toString()
                                    .substring(1, mActivityOrderConfirmBinding.tvOrderConfirmAllPrice.getText().toString().length())), goldsCount);
                        }
                        showChangeInfoPop(choosePayMethodPopup);
                    } else if (totalPrice == 0.0 || honnyType.equals("4")) {
                        postOrder(0);
                    }
                }
                break;
            case R.id.ll_address_detail:
                if (orderConfirmResultEntity.getBuy_list().getAddress_info()
                        .getAddress_id() != null) {
//                    startActivityForResult(new Intent(mContext, AddressListActivity.class)
//                            .putExtra("is_choose", true), ASK_FOR_ADDRESS);
                } else {
//                    startActivityForResult(new Intent(mContext, AddressAddActivity.class),
//                            DIRECT_ADD_ADDRESS);
                }
                break;
            case R.id.ll_order_confirm_self_pickup:
//                startActivityForResult(new Intent(this, SelfPickupStoreActivity.class), CHOOSE_SELF_PICKUP);
                break;
            case R.id.delivery_time_ll:
                if (chooseDeliveryPopupWindow != null) {
                    chooseDeliveryPopupWindow.show();
                }
                break;
        }
    }

    private ActivityOrderConfirmBinding mActivityOrderConfirmBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mActivityOrderConfirmBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_order_confirm, null, false);
        setContentView(mActivityOrderConfirmBinding.getRoot());

        mActivityOrderConfirmBinding.btnOrderConfirm.setOnClickListener(this);
        mActivityOrderConfirmBinding.llAddressDetail.setOnClickListener(this);
        mActivityOrderConfirmBinding.llOrderConfirmSelfPickup.setOnClickListener(this);
        mActivityOrderConfirmBinding.deliveryTimeLl.setOnClickListener(this);

        String honnyType = getIntent().getStringExtra("data");
        if (getIntent().getStringExtra("judgment_url") != null) {
            if (honnyType.equals("4") || honnyType.equals("5")) {
                mActivityOrderConfirmBinding.btnOrderConfirm.setText("提交申请");
                setTitleBarText("申请信息");
            } else {
                setTitleBarText("确认订单");
            }
        } else {
            setTitleBarText("确认订单");
        }
        choosePayMethodPopup = new ChoosePayMethodPopup(mContext, this);

        if (getIntent().hasExtra("my_person_id")) {
            mPersonId = getIntent().getStringExtra("my_person_id");
        }
        cart_info = getIntent().getStringExtra("cart_info");
        cart_from = getIntent().getStringExtra("cart_from");
        member_info = getIntent().getStringExtra("member_info");

        orderConfirmStoreCartList = new ArrayList<>();

        alipayUtils = new AlipayUtils(mActivity);
        alipayUtils.setPayCallback(new AlipayUtils.PayCallback() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(mContext, OrderActivity.class));
                finish();
            }

            @Override
            public void onFailure(String reason) {
                startActivity(new Intent(mContext, OrderActivity.class));
                finish();
                requestGoldsBack();
            }
        });
        getOrderData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void requestGoldsBack() {
        Map<String, String> params = new HashMap<>();
        params.put("pay_sn", pay_sn);

        ApiManager.backGold(params).subscribe(empty -> {

        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ASK_FOR_ADDRESS == requestCode && RESULT_OK == resultCode) {
            AddressBean.AddressListEntity result = (AddressBean
                    .AddressListEntity) data
                    .getSerializableExtra("address_info");
            fillAddressWithData(result);
            getOffPay();
        }
        if (DIRECT_ADD_ADDRESS == requestCode && AddressAddActivity.DIRECT_ADD_ADDRESS_RES ==
                resultCode) {
            AddressBean.AddressListEntity result = (AddressBean
                    .AddressListEntity) data
                    .getSerializableExtra("address_info");
            fillAddressWithData(result);
            getOffPay();
        }

        if (CHOOSE_SELF_PICKUP == requestCode && CHOOSE_SELF_PICKUP_RESULT == resultCode) {
            if (data == null) {
                selfPickupId = "";
                mActivityOrderConfirmBinding.tvOrderConfirmSelfPickupName.setText("(可选)");
                tvFreightTemp.setText("快递  运费" + freightTemp + "元");
                mActivityOrderConfirmBinding.tvOrderConfirmFreightPrice.setText("￥" + freightTemp);
                mActivityOrderConfirmBinding.tvOrderConfirmAllPrice.setText("￥" + decimalFormat.format(totalPrice));
            } else {
                if (data.hasExtra("dlyp_id") && data.hasExtra("dlyp_name")) {
                    selfPickupId = data.getStringExtra("dlyp_id");
                    mActivityOrderConfirmBinding.tvOrderConfirmSelfPickupName.setText(data.getStringExtra("dlyp_name"));
                    tvFreightTemp.setText("快递  运费" + 0.00 + "元");
                    mActivityOrderConfirmBinding.tvOrderConfirmFreightPrice.setText("￥0.00");
                    mActivityOrderConfirmBinding.tvOrderConfirmAllPrice.setText("￥" + decimalFormat.format(totalPrice - Double.parseDouble(freightTemp)));
                }
            }
        }

    }

    private void getOrderData() {
        showLoading("正在生成订单");
        Map<String, String> params = new HashMap<>();
        params.put("ifcart", cart_from);
        params.put("cart_id", cart_info);
        params.put("my_personal_id", mPersonId);//私人定制-save return id

        if (member_info != null) {
            String[] infos = member_info.split(":");
            if (infos.length > 4) {
                params.put("height", infos[0]);
                params.put("weight", infos[1]);
                params.put("age", infos[2]);
                params.put("sex", infos[3]);
                params.put("personal_like", infos[4]);
            } else {
                ShowToast.show("所需参数不全");
                finish();
                return;
            }
        }

        ApiManager.orderConfirmStep1(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(s -> {
            dismissLoading();
            Map<String, Double> freights = new HashMap<>();
            Map<String, String[]> distribution = new HashMap<String, String[]>();
            try {
                JSONObject rawObj = new JSONObject(s).getJSONObject("result")
                        .getJSONObject("buy_list").getJSONObject("freight_result");
                freights = new Gson().fromJson(rawObj.get("content").toString(),
                        new TypeToken<Map<String, Double>>() {
                        }.getType());
                JSONObject deliveryJson = new JSONObject(s).getJSONObject("result")
                        .getJSONObject("buy_list");
                distribution = new Gson().fromJson(deliveryJson.getString("distribution"),
                        new TypeToken<Map<String, String[]>>() {
                        }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
                s = s.replace("\"default_address\":[]", "\"default_address\":{}")
                        .replace("\"freight_result\":[]", "\"freight_result\":{}");
            }
            chooseDeliveryPopupWindow = new ChooseDeliveryPopup(mActivity, distribution);
            chooseDeliveryPopupWindow.setGetValueCallback((province, city) -> {
                deliveryMsg = province + city;
                mActivityOrderConfirmBinding.deliveryTimeTv.setText(province + city);
            });
            orderConfirmBean = new Gson().fromJson(s, OrderConfirmBean.class);
            orderConfirmResultEntity = orderConfirmBean.getResult();
            goldsCount = orderConfirmResultEntity.getBuy_list().getTotal_gold_coins();
            fillAddressWithData(orderConfirmResultEntity.getBuy_list().getAddress_info());

            fillContainerWithData(orderConfirmResultEntity, freights);

            freight_hash = orderConfirmResultEntity.getBuy_list().getFreight_hash();
            offpay_hash = orderConfirmResultEntity.getBuy_list().getFreight_result()
                    .getOffpay_hash();
            offpay_hash_batch = orderConfirmResultEntity.getBuy_list().getFreight_result()
                    .getOffpay_hash_batch();
            personal_info = TextUtils.isDigitsOnly(
                    orderConfirmResultEntity.getPersonal_info()) ? orderConfirmResultEntity
                    .getPersonal_info() : "";
        }, throwable -> {
            dismissLoading();
            ShowDialog.showConfirmDialog(mContext, "生成失败", "生成订单失败！", v -> finish());
            EventBus.getDefault().post(new OrderRelationEvent.UpdateCart());
        });
    }

    private void getOffPay() {
        if (TextUtils.isEmpty(freight_hash) || TextUtils.isEmpty(city_id) || TextUtils
                .isEmpty(area_id)) {
            ShowToast.show("请选择一个地址");
            return;
        }
        showLoading("正在生成订单");
        Map<String, String> params = new HashMap<>();
        params.put("freight_hash", freight_hash);
        params.put("city_id", city_id);
        params.put("area_id", area_id);

        ApiManager.orderConfirmOffPay(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(s -> {
            dismissLoading();
            JSONObject rawObj = new JSONObject(s);
            offpay_hash = rawObj.getJSONObject("result").getString("offpay_hash");
            offpay_hash_batch = rawObj.getJSONObject("result").getString("offpay_hash_batch");

            JSONObject freightObj = rawObj.getJSONObject("result");
            Map<String, Double> freights = new Gson().fromJson(freightObj.get("content").toString(),
                    new TypeToken<Map<String, Double>>() {
                    }.getType());

            fillContainerWithData(orderConfirmResultEntity, freights);
        },throwable -> {
            dismissLoading();
            ShowDialog.showSelectDialog(mContext, "是否重试", "生成订单失败！", "", v -> getOrderData());
        });
    }

    private void fillAddressWithData(
            OrderConfirmBean.ResultEntity.BuyListEntity.AddressInfoEntity addressInfoEntity) {

        if (addressInfoEntity.getAddress_id() != null) {
            addressId = addressInfoEntity.getAddress_id();
            mActivityOrderConfirmBinding.tvOrderConfirmAddressName.setText(addressInfoEntity.getTrue_name());
            mActivityOrderConfirmBinding.tvOrderConfirmAddressPhone.setText(addressInfoEntity.getMob_phone());
            if (addressInfoEntity.getIs_visible().equals("1")) {
                mActivityOrderConfirmBinding.tvOrderConfirmAddressDetail.setText("地址保密");
            } else {
                mActivityOrderConfirmBinding.tvOrderConfirmAddressDetail.setText(
                        addressInfoEntity.getArea_info() + "\n" + addressInfoEntity.getAddress());
            }

            city_id = addressInfoEntity.getCity_id();
            area_id = addressInfoEntity.getArea_id();
        }
        mActivityOrderConfirmBinding.llAddressDetail.setVisibility(View.VISIBLE);
    }

    private void fillAddressWithData(AddressBean.AddressListEntity addressInfoEntity) {

        if (addressInfoEntity != null) {
            addressId = addressInfoEntity.getAddress_id();
            mActivityOrderConfirmBinding.tvOrderConfirmAddressName.setText(addressInfoEntity.getTrue_name());
            mActivityOrderConfirmBinding.tvOrderConfirmAddressPhone.setText(addressInfoEntity.getMob_phone());
            if (addressInfoEntity.getIs_visible().equals("1")) {
                mActivityOrderConfirmBinding.tvOrderConfirmAddressDetail.setText("地址保密");
            } else {
                mActivityOrderConfirmBinding.tvOrderConfirmAddressDetail.setText(
                        addressInfoEntity.getArea_info() + "\n" + addressInfoEntity.getAddress());
            }
            city_id = addressInfoEntity.getCity_id();
            area_id = addressInfoEntity.getArea_id();
        }
        mActivityOrderConfirmBinding.llAddressDetail.setVisibility(View.VISIBLE);
    }

    private void fillContainerWithData(OrderConfirmBean.ResultEntity orderConfirmResultEntity,
                                       Map<String, Double> freights) {

        double totalGoodsPrice = 0f;
        double totalFreightPrice = 0f;
        ets.clear();
        mActivityOrderConfirmBinding.llOrderListContainer.removeAllViews();
        orderConfirmStoreCartList = orderConfirmResultEntity.getBuy_list().getStore_cart_list();
        for (int i = 0; i < orderConfirmStoreCartList.size(); i++) {

            totalGoodsPrice += Double
                    .parseDouble(orderConfirmStoreCartList.get(i).getStore_goods_total());
            if (freights.get(orderConfirmStoreCartList.get(i).getStore_id()) == null) {
                freights.put(orderConfirmStoreCartList.get(i).getStore_id(), 0.0);
            }
            totalFreightPrice += freights.get(orderConfirmStoreCartList.get(i).getStore_id());

            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.order_confirm_store_item, null);
            LinearLayout llOrderConfirmGoodsContainer = (LinearLayout) item
                    .findViewById(R.id.ll_order_confirm_goods_container);
            TextView tvOrderConfirmStoreName = (TextView) item
                    .findViewById(R.id.tv_order_confirm_store_name);
            TextView tvOrderConfirmStoreFreight = (TextView) item
                    .findViewById(R.id.tv_order_confirm_store_freight);
            tvFreightTemp = tvOrderConfirmStoreFreight;
            ets.add((EditText) item.findViewById(R.id.et_order_confirm_order_message));
            ets.get(0).addTextChangedListener(textWatcher);
            tvOrderConfirmStoreName.setText(orderConfirmStoreCartList.get(i).getStore_name());
            if (freights.size() > 0) {
                tvOrderConfirmStoreFreight.setText("快递  运费" + decimalFormat.format(freights
                        .get(orderConfirmStoreCartList.get(i).getStore_id())) + "元");
                freightTemp = decimalFormat.format(freights.get(orderConfirmStoreCartList.get(i).getStore_id()));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int px = DisplayUtil.dp2px(2);
            params.setMargins(px, px, px, px);

            mActivityOrderConfirmBinding.llOrderListContainer.addView(item, params);

            List<OrderConfirmBean.ResultEntity.BuyListEntity.StoreCartListEntity.GoodsListEntity>
                    list = orderConfirmStoreCartList
                    .get(i).getGoods_list();

            for (int x = 0; x < list.size(); x++) {
                if (Integer.parseInt(list.get(x).getGoods_storage()) < list.get(x).getGoods_num()) {
                    throw new RuntimeException("商品库存不足");
                }
            }

            orderName = "";
            for (int j = 0; j < list.size(); j++) {
                orderName += list.get(j).getGoods_name();
                View citem = View.inflate(mContext, R.layout.order_confirm_goods_item, null);
                ImageView ivOrderConfirmGoodsPreview = (ImageView) citem
                        .findViewById(R.id.iv_order_confirm_goods_preview);
                TextView tvOrderConfirmGoodsName = (TextView) citem
                        .findViewById(R.id.tv_order_confirm_goods_name);
                TextView tvOrderConfirmGoodsPrice = (TextView) citem
                        .findViewById(R.id.tv_order_confirm_goods_price);
                TextView tvOrderConfirmGoodsNum = (TextView) citem
                        .findViewById(R.id.tv_order_confirm_goods_num);

                ImageUtil.loadImage(list.get(j).getGoods_image_url(), ivOrderConfirmGoodsPreview,
                        R.drawable.iv_place_holder_1);
                tvOrderConfirmGoodsName.setText(list.get(j).getGoods_name());
                tvOrderConfirmGoodsPrice.setText("￥" + list.get(j).getGoods_price());
                tvOrderConfirmGoodsNum.setText("×" + list.get(j).getGoods_num());

                llOrderConfirmGoodsContainer.addView(citem);
            }
        }
        totalPrice = totalPriceOrigin = totalGoodsPrice + totalFreightPrice;
        choosePayMethodPopup.setData((float) totalPrice, goldsCount);
        mActivityOrderConfirmBinding.tvOrderConfirmGoldsPrice.setText("￥" + decimalFormat.format(totalGoodsPrice));
        mActivityOrderConfirmBinding.tvOrderConfirmFreightPrice.setText("￥" + decimalFormat.format(totalFreightPrice));
        mActivityOrderConfirmBinding.tvOrderConfirmAllPrice.setText("￥" + decimalFormat.format(totalPrice));
    }

    private void postOrder(int type) {
        showLoading("正在提交订单");
        Map<String, String> params = new HashMap<>();
        params.put("distribution",
                mActivityOrderConfirmBinding.deliveryTimeTv.getText().toString());
        params.put("fcode", "");
        params.put("ifcart", cart_from);
        params.put("cart_id", cart_info);
        params.put("address_id", addressId);
        params.put("vat_hash", orderConfirmResultEntity.getBuy_list().getVat_hash());
        params.put("offpay_hash", offpay_hash);
        params.put("offpay_hash_batch", offpay_hash_batch);
        params.put("pay_name", "online");
        params.put("invoice_id", "undefined");
        params.put("voucher", "");
        params.put("personal_info", personal_info);
        params.put("dlyp_id", selfPickupId);
        if (getIntent().hasExtra("goods_description")) {
            params.put("order_message", ets.get(0).getText().toString() + ";" + getIntent()
                    .getStringExtra("goods_description"));
        } else {
            params.put("order_message", ets.get(0).getText().toString());
        }
        params.put("coins_pay", useGolds + "");

        String honnyType = "";
        if (getIntent().getStringExtra("data") != null) {
            honnyType = getIntent().getStringExtra("data");
        }
        switch (honnyType) {
            case "5":
                confirmRequest(ApiManager.orderConfirmBirthDay(params)
                        .compose(bindUntil(ActivityEvent.DESTROY)), type);
                break;
            case "4":
                newTryConfirmRequest(ApiManager.orderConfirmNewTry(params)
                        .compose(bindUntil(ActivityEvent.DESTROY)), type);
                break;
            case "1":
                confirmRequest(ApiManager.orderConfirmCustom(params)
                        .compose(bindUntil(ActivityEvent.DESTROY)), type);
                break;
            case "0":
            case "2":
            case "3":
            case "6":
                confirmRequest(ApiManager.orderConfirmGeneral(params)
                        .compose(bindUntil(ActivityEvent.DESTROY)), type);
                break;
        }
    }

    private void confirmRequest(Observable<String> api, int type) {
        api.subscribe(s -> {
            dismissLoading();
            EventBus.getDefault().post(new OrderRelationEvent.UpdateCart());
            JSONObject rawObj = new JSONObject(s);
            JSONObject resultObj = rawObj.getJSONObject("result");
            pay_sn = resultObj.getString("pay_sn");
            double pay_amount = resultObj.getJSONObject("payment_info").getDouble("api_pay_amount");
            if (0 != pay_amount) {
                if (0 == type) {
                    String orderInfo = alipayUtils.generateOrderInfo(pay_sn,
                            "HONNY恒尼-订单编号" + pay_sn, pay_amount,
                            PaymentConstant.ALI_PAY_CALLBACK_1);
                    getRsaSign(orderInfo);
                } else if (1 == type) {
                    new WXPayUtil(mContext).pay(pay_sn, Math.round((float) pay_amount * 100), 0,
                            "HONNY恒尼-订单编号" + pay_sn);
                }
            } else {

                startActivity(new Intent(mContext, OrderActivity.class));
                finish();
            }
        }, throwable -> {
            dismissLoading();
            ShowDialog.showSelectDialog(mContext, "是否重试", "提交订单失败！", "", v -> postOrder(type));
        });
    }

    private void newTryConfirmRequest(Observable<String> api, int type) {
        api.subscribe(s -> {
            dismissLoading();
            JSONObject rawObj = new JSONObject(s);
            ShowToast.show(rawObj.getString("result"));
            finish();
        }, throwable -> {
            dismissLoading();
            ShowDialog.showSelectDialog(mContext, "是否重试", "提交订单失败！", "", v -> postOrder(type));
        });
    }

    private void getRsaSign(String orderInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("orderspec", orderInfo);

        ApiManager.orderSign(params).subscribe(s -> {
            JSONObject rawObj = new JSONObject(s);
            String sign = rawObj.getJSONObject("result").getString("signedstr");
            alipayUtils.pay(orderInfo, sign);
        }, throwable -> {
            ShowDialog.showSelectDialog(mContext, "是否重试", "获取订单签名失败！", "",
                    v -> getRsaSign(orderInfo));
        });
    }

    private void showChangeInfoPop(PopupWindow pop) {
        pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onTakePhoto(float golds) {
        useGolds = golds;
        postOrder(0);
    }

    @Override
    public void onChoosePhoto(float golds) {
        useGolds = golds;
        postOrder(1);
    }

    @Subscribe
    public void onEventWxPayResult(OrderRelationEvent.WxPayResult wxPayResult) {
        if (wxPayResult.success) {
            startActivity(new Intent(mContext, OrderActivity.class));
            finish();
        } else {
            startActivity(new Intent(mContext, OrderActivity.class));
            finish();
            requestGoldsBack();
        }
    }
}

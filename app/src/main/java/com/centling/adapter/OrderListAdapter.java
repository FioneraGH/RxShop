package com.centling.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.constant.PaymentConstant;
import com.centling.entity.OrderBean;
import com.centling.event.OrderRelationEvent;
import com.centling.http.ApiManager;
import com.centling.payment.ali.AlipayUtils;
import com.centling.payment.wx.WXPayUtil;
import com.centling.popupwindow.ChoosePayMethodPopup;
import com.centling.util.ImageUtil;
import com.centling.util.ItemClickListener;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.centling.widget.ProcessDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OrderListAdapter
 * Created by fionera on 15-11-30.
 */

public class OrderListAdapter
        extends RecyclerView.Adapter<OrderListAdapter.OrderHolder>
        implements ChoosePayMethodPopup.OnDialogListener {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<OrderBean.OrderGroupListEntity> data;
    private RecyclerView recyclerView;
    private ProcessDialog processDialog;
    private int order_type;
    private AlipayUtils alipayUtils;

    private ChoosePayMethodPopup choosePayMethodPopup;
    private int currentPayPosition;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderListAdapter(RecyclerView recyclerView, Context context,
                            List<OrderBean.OrderGroupListEntity> data,
                            ProcessDialog processDialog, int order_type) {

        this.context = context;
        this.data = data;
        this.order_type = order_type;
        this.recyclerView = recyclerView;
        this.processDialog = processDialog;
        this.alipayUtils = new AlipayUtils((Activity) context);
        this.alipayUtils.setPayCallback(new AlipayUtils.PayCallback() {
            @Override
            public void onSuccess() {
                opDone(currentPayPosition);
            }

            @Override
            public void onFailure(String reason) {
                requestGoldsBack();
            }
        });

        choosePayMethodPopup = new ChoosePayMethodPopup(context, this);

        layoutInflater = LayoutInflater.from(context);
    }

    public void requestGoldsBack() {
        Map<String, String> params = new HashMap<>();
        params.put("pay_sn", pay_sn);

        ApiManager.backGold(params).subscribe(empty -> {

        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getOrder_list().get(0).getExtend_order_goods().size();
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.rv_order_list_item, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {

        holder.btnConnectService.setVisibility(View.VISIBLE);
//        holder.btnConnectService.setOnClickListener(v ->
//                Honny.getInstance().startECChat(context,
//                        data.get(position).getOrder_list().get(0).getExtend_order_goods().get(0).getGoods_image_url(),
//                        data.get(position).getOrder_list().get(0).getExtend_order_goods().get(0).getGoods_name(),
//                        "¥" + data.get(position).getOrder_list().get(0).getExtend_order_goods().get(0).getGoods_price(),
//                        data.get(position).getOrder_list().get(0).getExtend_order_goods().get(0).getGoods_url()));

        holder.btnDeleteOrder.setVisibility(View.GONE);
        holder.btnCancelOrder.setVisibility(View.GONE);
        holder.btnConfirmReceive.setVisibility(View.GONE);
        holder.btnCheckLogistics.setVisibility(View.GONE);
        holder.btnImmediatePay.setVisibility(View.GONE);
        holder.btnRefund.setVisibility(View.GONE);
        holder.btnCheckRefund.setVisibility(View.GONE);
        holder.btnConfirmDlyp.setVisibility(View.GONE);

        holder.btnConfirmDlyp.setOnClickListener(v -> {
            Log.d("loren", "order_id=" + data.get(position).getOrder_list().get(0).getOrder_id());
            ShowDialog.showSelectDialog(context, "已自提", "您确定已经自提了吗？", "",
                    v1 -> confirmDlyp(data.get(position).getOrder_list().get(0).getOrder_id(), position));
        });
        switch (data.get(position).getOrder_list().get(0).getOrder_state()) {
            case "0":
                holder.btnDeleteOrder.setVisibility(View.VISIBLE);
                holder.btnDeleteOrder.setOnClickListener(v -> ShowDialog
                        .showSelectDialog(context, "删除订单", "您确定要删除订单吗？", "",
                                v1 -> deleteOrder(position)));
                holder.tvOrderStatus
                        .setText(data.get(position).getOrder_list().get(0).getState_desc());
                if (0 != data.get(position).getOrder_list().get(0).getRefund_sure_state()) {
                    holder.btnConfirmDlyp.setVisibility(View.GONE);
                    holder.tvOrderStatus.setText(
                            data.get(position).getOrder_list().get(0).getRefund_sure_msg());
                }
                break;
            case "10":
                holder.btnCancelOrder.setVisibility(View.VISIBLE);
                holder.btnCancelOrder.setOnClickListener(v -> ShowDialog
                        .showSelectDialog(context, "取消订单", "您确定要取消订单吗？", "",
                                v1 -> cancelOrder(position)));
                holder.btnImmediatePay.setVisibility(View.VISIBLE);
                holder.btnImmediatePay.setOnClickListener(v2 -> {
                    currentPayPosition = position;
                    showPayPop(choosePayMethodPopup);
                });
                holder.tvOrderStatus
                        .setText(data.get(position).getOrder_list().get(0).getState_desc());
                break;
            case "20":
                if (Integer.parseInt(data.get(position).getOrder_list().get(0).getDelivery_id()) > 0) {
                    holder.btnConfirmDlyp.setVisibility(View.VISIBLE);
                } else {
                    holder.btnConfirmDlyp.setVisibility(View.GONE);
                }
                holder.btnRefund.setVisibility(View.VISIBLE);
                holder.btnRefund.setOnClickListener(v -> ShowDialog
                        .showEditDialog(context, "订单退款", "您确定要进行退款吗？",
                                v1 -> refundOrder(position, v1.getTag().toString())));
                holder.tvOrderStatus
                        .setText(data.get(position).getOrder_list().get(0).getState_desc());
                if (0 != data.get(position).getOrder_list().get(0).getRefund_sure_state()) {
                    holder.btnRefund.setVisibility(View.GONE);
                    holder.btnConfirmDlyp.setVisibility(View.GONE);
                    holder.tvOrderStatus.setText(
                            data.get(position).getOrder_list().get(0).getRefund_sure_msg());
                }
                break;
            case "30":
                holder.btnCheckRefund.setVisibility(View.VISIBLE);
                holder.btnCheckRefund.setOnClickListener(
                        v -> checkLogistics(data.get(position).getOrder_list().get(0).getOrder_id(),
                                data.get(position).getOrder_list().get(0)
                                        .getExtend_order_goods().get(0)
                                        .getGoods_image_url()));
                holder.btnConfirmReceive.setOnClickListener(v -> ShowDialog
                        .showSelectDialog(context, "确认收货", "您确定要确认收货吗？", "（请确保您已接收到货物）",
                                v1 -> receiveOrder(position)));
                Log.d("loren", "Lock_state=" + data.get(position).getOrder_list().get(0).getLock_state());
                if (Integer.parseInt(data.get(position).getOrder_list().get(0).getLock_state()) > 0) {
                    holder.tvOrderStatus.setText(data.get(position).getOrder_list().get(0).getRefund_sure_msg());
                    holder.btnConfirmReceive.setVisibility(View.GONE);
                } else {
                    holder.tvOrderStatus.setText(data.get(position).getOrder_list().get(0).getState_desc());
                    holder.btnConfirmReceive.setVisibility(View.VISIBLE);
                }
                break;
            case "40":
                holder.btnCheckRefund.setVisibility(View.VISIBLE);
                holder.btnCheckRefund.setOnClickListener(v -> checkLogistics(data.get(position)
                        .getOrder_list().get(0).getOrder_id(), data.get(position).getOrder_list()
                        .get(0).getExtend_order_goods().get(0).getGoods_image_url()));
        }

        holder.tvStoreName.setText("单号：" + data.get(position).getOrder_list().get(0).getOrder_sn());
        int sumCount = 0;
        for (OrderBean.OrderGroupListEntity.OrderListEntity.ExtendOrderGoodsEntity
                good : data
                .get(position).getOrder_list().get(0).getExtend_order_goods()) {
            sumCount += Integer.parseInt(good.getGoods_num());
        }
        holder.tvGoodsSum.setText("共" + sumCount + "件商品");
        holder.tvPriceSum
                .setText("合计" + data.get(position).getOrder_list().get(0).getOrder_amount() + "元");
        holder.tvDeliver.setText(
                "（含运费" + data.get(position).getOrder_list().get(0).getShipping_fee() + "元） ");

        holder.tvOrderHonnyType.setText(data.get(position).getOrder_list().get(0).getHonny_type());

        if (holder.llMyGoods.getChildCount() == data.get(position).getOrder_list().get(0)
                .getExtend_order_goods().size()) {
            for (int i = 0; i < data.get(position).getOrder_list().get(0).getExtend_order_goods()
                    .size(); i++) {
                View goodsItemView = holder.llMyGoods.getChildAt(i);
                ImageView ivGoodsPreview = (ImageView) goodsItemView
                        .findViewById(R.id.iv_goods_preview);
                ImageUtil.loadImage(
                        data.get(position).getOrder_list().get(0).getExtend_order_goods()
                                .get(i).getGoods_image_url(),ivGoodsPreview, R.drawable.iv_place_holder_1);
                TextView tvGoodsDescription = (TextView) goodsItemView
                        .findViewById(R.id.tv_goods_description);
                tvGoodsDescription.setText(
                        data.get(position).getOrder_list().get(0).getExtend_order_goods().get(i)
                                .getGoods_name());
                TextView tvGoodsSpec = (TextView) goodsItemView.findViewById(R.id.tv_goods_spec);
                tvGoodsSpec.setText(
                        "￥" + data.get(position).getOrder_list().get(0).getExtend_order_goods()
                                .get(i).getGoods_price());
                TextView tvGoodsCount = (TextView) goodsItemView.findViewById(R.id.tv_goods_count);
                tvGoodsCount.setText(
                        "×" + data.get(position).getOrder_list().get(0).getExtend_order_goods()
                                .get(i).getGoods_num());
            }
        } else {
            holder.llMyGoods.removeAllViews();
            for (int i = 0; i < data.get(position).getOrder_list().get(0).getExtend_order_goods()
                    .size(); i++) {
                View goodsItemView = layoutInflater
                        .inflate(R.layout.rv_order_goods_item, (ViewGroup) holder.itemView, false);
                ImageView ivGoodsPreview = (ImageView) goodsItemView
                        .findViewById(R.id.iv_goods_preview);
                ImageUtil.loadImage(
                        data.get(position).getOrder_list().get(0).getExtend_order_goods()
                                .get(i).getGoods_image_url(),ivGoodsPreview, R.drawable.iv_place_holder_1);
                TextView tvGoodsDescription = (TextView) goodsItemView
                        .findViewById(R.id.tv_goods_description);
                tvGoodsDescription.setText(
                        data.get(position).getOrder_list().get(0).getExtend_order_goods().get(i)
                                .getGoods_name());
                TextView tvGoodsSpec = (TextView) goodsItemView.findViewById(R.id.tv_goods_spec);
                tvGoodsSpec.setText(
                        "￥" + data.get(position).getOrder_list().get(0).getExtend_order_goods()
                                .get(i).getGoods_price());
                TextView tvGoodsCount = (TextView) goodsItemView.findViewById(R.id.tv_goods_count);
                tvGoodsCount.setText(
                        "×" + data.get(position).getOrder_list().get(0).getExtend_order_goods()
                                .get(i).getGoods_num());
                holder.llMyGoods.addView(goodsItemView);
            }
        }

        holder.itemView.setOnClickListener(
                v -> itemClickListener.onClick(v, position));
    }

    private void confirmDlyp(String order_id, int position) {
        processDialog.setTitle("加载中").showDialog(false);
        Map<String, String> params = new HashMap<>();
        params.put("order_id", order_id);

        ApiManager.confirmDlyp(params).subscribe(empty -> {
            processDialog.dismiss();
            ShowToast.show("自提成功");
            opDone(position);
        },throwable -> {
            processDialog.dismiss();
            ShowToast.show(throwable.getMessage());
        });
    }

    private void cancelOrder(int position) {
        processDialog.setTitle("正在取消订单").showDialog(false);
        Map<String, String> params = new HashMap<>();
        params.put("order_id", data.get(position).getOrder_list().get(0).getOrder_id());

        ApiManager.orderCancel(params).subscribe(empty -> {
            processDialog.dismiss();
            opDone(position);
        }, throwable -> {
            processDialog.dismiss();
            ShowToast.show(throwable.getMessage());
        });
    }

    public void opDone() {
        opDone(currentPayPosition);
    }

    private void opDone(int position) {
        if (order_type != 0) {
            data.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            recyclerView.getAdapter().notifyItemRangeChanged(0, data.size());
        }
        EventBus.getDefault().post(new OrderRelationEvent.UpdateOrder(order_type));
    }

    private void deleteOrder(int position) {
        processDialog.setTitle("正在删除订单").showDialog(false);
        Map<String, String> params = new HashMap<>();
        params.put("order_id", data.get(position).getOrder_list().get(0).getOrder_id());
        params.put("state_type", "order_delete");

        ApiManager.orderDelete(params).subscribe(empty -> {
            processDialog.dismiss();
            opDone(position);
        }, throwable -> {
            processDialog.dismiss();
            ShowToast.show(throwable.getMessage());
        });
    }

    private void refundOrder(int position, String reason) {
        processDialog.setTitle("正在发起退款").showDialog(false);
        Map<String, String> params = new HashMap<>();
        params.put("order_id", data.get(position).getOrder_list().get(0).getOrder_id());
        params.put("buyer_message", TextUtils.isEmpty(reason) ? "我不想要了" : reason);
        ApiManager.orderRefund(params).subscribe(empty -> {
            processDialog.dismiss();
            opDone(position);
        }, throwable -> {
            processDialog.dismiss();
            ShowToast.show(reason);
        });
    }

    private void checkLogistics(String order_id, String img_url) {
//        context.startActivity(
//                new Intent(context, LogisticsDetailActivity.class).putExtra("order_id", order_id)
//                        .putExtra("img_url", img_url).putExtra("check_type", 0));
    }

    private void receiveOrder(int position) {
        processDialog.setTitle("正在确认收货").showDialog(false);
        Map<String, String> params = new HashMap<>();
        params.put("order_id", data.get(position).getOrder_list().get(0).getOrder_id());
        ApiManager.orderReceive(params).subscribe(empty -> {
            processDialog.dismiss();
            opDone(position);
        }, throwable -> {
            processDialog.dismiss();
            ShowToast.show(throwable.getMessage());
        });
    }

    private String pay_sn;

    private void showPayPop(PopupWindow pop) {
        pay_sn = data.get(currentPayPosition).getOrder_list().get(0).getPay_sn();
        Map<String, String> params = new HashMap<>();
        params.put("pay_sn", pay_sn);

        ApiManager.orderPrepay(params).subscribe(s -> {
            ((ChoosePayMethodPopup) pop).setData(Float.parseFloat(data.get(currentPayPosition)
                    .getOrder_list().get(0).getOrder_amount()), (float) new JSONObject(s)
                    .getJSONObject("result").getDouble("total_coins"));
            pop.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0,
                    0);
        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onTakePhoto(float golds) {
        Map<String, String> params = new HashMap<>();
        params.put("pay_sn", pay_sn);
        params.put("coins_pay", golds + "");

        ApiManager.orderPayAgain(params).subscribe(s -> {
            JSONObject resultObj = new JSONObject(s).getJSONObject("result")
                    .getJSONObject("payment_info");
            String pay_sn = resultObj.getString("pay_sn");
            double pay_amount = resultObj.getDouble("api_pay_amount");
            if (pay_amount != 0) {
                String orderInfo = alipayUtils
                        .generateOrderInfo(pay_sn, "HONNY恒尼-订单编号" + pay_sn, pay_amount,
                                PaymentConstant.ALI_PAY_CALLBACK_1);
                getRsaSign(orderInfo);
            } else {
                opDone(currentPayPosition);
            }
        },throwable -> ShowToast.show(throwable.getMessage()));
    }

    @Override
    public void onChoosePhoto(float golds) {
        Map<String, String> params = new HashMap<>();
        params.put("pay_sn", pay_sn);
        params.put("coins_pay", golds + "");

        ApiManager.orderPayAgain(params).subscribe(s -> {
            JSONObject resultObj = new JSONObject(s).getJSONObject("result")
                    .getJSONObject("payment_info");
            String pay_sn = resultObj.getString("pay_sn");
            double pay_amount = resultObj.getDouble("api_pay_amount");
            if (pay_amount != 0) {
                new WXPayUtil(context).pay(pay_sn + "h" + System.currentTimeMillis(),
                        Math.round(pay_amount * 100), 0, "恒尼订单-" + pay_sn);
            } else {
                opDone(currentPayPosition);
            }
        },throwable -> ShowToast.show(throwable.getMessage()));
    }

    private void getRsaSign(String orderInfo) {
        Map<String, String> params = new HashMap<>();
            params.put("orderspec", orderInfo);
        ApiManager.orderSign(params).subscribe(s -> {
            JSONObject rawObj = new JSONObject(s);
            String sign = rawObj.getJSONObject("result").getString("signedstr");
            alipayUtils.pay(orderInfo, sign);
        },throwable -> {
            ShowDialog.showSelectDialog(context, "是否重试", "获取订单签名失败！", "",
                    v -> getRsaSign(orderInfo));
        });
    }

    class OrderHolder
            extends RecyclerView.ViewHolder {
        LinearLayout llMyGoods;
        //各种状态的按钮们
        TextView btnConnectService;
        TextView btnDeleteOrder;
        TextView btnCheckLogistics;
        TextView btnCancelOrder;
        TextView btnConfirmReceive;
        TextView btnImmediatePay;
        TextView btnRefund;
        TextView btnCheckRefund;
        TextView btnConfirmDlyp;
        //列表中的文本内容
        TextView tvStoreName;
        TextView tvOrderStatus;
        TextView tvGoodsSum;
        TextView tvPriceSum;
        TextView tvDeliver;
        TextView tvOrderHonnyType;

        OrderHolder(View itemView) {
            super(itemView);
            llMyGoods = (LinearLayout) itemView.findViewById(R.id.ll_my_goods);

            btnConfirmDlyp = (TextView) itemView.findViewById(R.id.tv_confirm_dlyp);
            btnDeleteOrder = (TextView) itemView.findViewById(R.id.tv_del_order);
            btnConnectService = (TextView) itemView.findViewById(R.id.tv_connect_service);
            btnCheckLogistics = (TextView) itemView.findViewById(R.id.tv_check_logistics);
            btnCancelOrder = (TextView) itemView.findViewById(R.id.tv_cancel_order);
            btnConfirmReceive = (TextView) itemView.findViewById(R.id.tv_confirm_receive);
            btnImmediatePay = (TextView) itemView.findViewById(R.id.tv_immediate_pay);
            btnRefund = (TextView) itemView.findViewById(R.id.tv_refund_order);
            btnCheckRefund = (TextView) itemView.findViewById(R.id.tv_check_refund);

            tvStoreName = (TextView) itemView.findViewById(R.id.tv_order_item_shop);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.tv_order_item_status);
            tvGoodsSum = (TextView) itemView.findViewById(R.id.tv_order_item_goods_count);
            tvPriceSum = (TextView) itemView.findViewById(R.id.tv_order_item_sum_price);
            tvDeliver = (TextView) itemView.findViewById(R.id.tv_order_item_deliver_price);
            tvOrderHonnyType = (TextView) itemView.findViewById(R.id.tv_order_honny_type);
        }
    }
}

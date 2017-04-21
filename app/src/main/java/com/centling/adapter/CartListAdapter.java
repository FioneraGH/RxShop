package com.centling.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.R;
import com.centling.activity.GoodsDetailActivity;
import com.centling.activity.MainActivity;
import com.centling.constant.RouterConstant;
import com.centling.entity.CartBean;
import com.centling.event.CommonEvent;
import com.centling.http.ApiManager;
import com.centling.util.DisplayUtil;
import com.centling.util.ImageUtil;
import com.centling.util.ShowToast;
import com.centling.util.TouchHelper;
import com.centling.util.UserInfoUtil;
import com.centling.widget.FlexibleImageView;
import com.centling.widget.ProcessDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CartListAdapter
 * Created by fionera on 15-11-30.
 */
public class CartListAdapter
        extends RecyclerView.Adapter<CartListAdapter.OrderHolder> {

    public static final int TOTAL_PRICE = 1000;
    public static final int CHECK_ALL = 1001;
    public static final int MIDDLE_FIELD = 2002;
    public static final int RECOMMEND_LIST = 2001;
    public static final int CART_LIST = 2000;

    private Context context;
    private Handler handler;
    private LayoutInflater layoutInflater;
    private List<CartBean.CartListEntity> data;
    private List<CartBean.GoodsCommonlistEntity> dataRec;
    private RecyclerView recyclerView;
    private ProcessDialog processDialog;
    private boolean isInGoodDetail;

    private boolean isEditMode;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    /**
     * 点击接口
     */
    public interface OnItemClickListener {

        void onItemClick(View view, int pos);
    }

    protected OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < data.size()) ? CART_LIST : (position == data
                .size()) ? MIDDLE_FIELD : RECOMMEND_LIST;
    }

    private boolean isCart(int position) {
        return position < data.size();
    }

    private boolean isHeader(int position) {
        return position == data.size();
    }

    public CartListAdapter(Context context, List<CartBean.CartListEntity> data,
                           List<CartBean.GoodsCommonlistEntity> dataRec,
                           RecyclerView recyclerView, Handler handler, ProcessDialog processDialog,
                           boolean isInGoodDetail) {

        this.context = context;
        this.data = data;
        this.dataRec = dataRec;
        this.recyclerView = recyclerView;
        this.handler = handler;
        this.processDialog = processDialog;
        this.isInGoodDetail = isInGoodDetail;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /*
          按类型创建Holder
         */
        if (viewType == RECOMMEND_LIST) {
            View view = layoutInflater.inflate(R.layout.rv_catalog_list_item, parent, false);
            return new OrderHolder(view);
        } else if (viewType == CART_LIST) {
            View view = layoutInflater.inflate(R.layout.rv_cart_list_item, parent, false);
            return new OrderHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.rv_cart_recommand_header, parent, false);
            return new OrderHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, final int position) {

        /*
          按类型处理数据
         */
        if (isCart(position)) {

            ImageUtil.loadImage(data.get(position).getGoods_image_url(),holder.ivPreview,  R.drawable.iv_place_holder_1);
            holder.tvGoodsName.setText(data.get(position).getGoods_name());
            holder.tvGoodsNum.setText(data.get(position).getGoods_num());
            holder.tvGoodsPrice.setText("￥" + data.get(position).getGoods_price());

            /*
              选中逻辑
             */
            holder.cbGoodsCheck.setChecked(data.get(position).isChecked());
            holder.cbGoodsCheck.setOnClickListener(v -> {
                data.get(position).setChecked(((CheckBox) v).isChecked());
                recyclerView.getAdapter().notifyItemChanged(position);
                handler.obtainMessage(TOTAL_PRICE, getSelectedCount(), 0, getTotalPrice())
                        .sendToTarget();
                handler.obtainMessage(CHECK_ALL, isAllSelected()).sendToTarget();
            });
            TouchHelper.expandViewTouchDelegate(holder.cbGoodsCheck, DisplayUtil.dp2px(15.0f),
                    DisplayUtil.dp2px(15.0f), DisplayUtil.dp2px(15.0f),
                    DisplayUtil.dp2px(15.0f));

            /*
              加减购物车
             */
            holder.ivAdd.setOnClickListener(v -> addToCart(position, false));
            holder.ivSub.setOnClickListener(v -> subFromCart(position, false));

            if (isEditMode()) {
                holder.tvGoodsPrice.setVisibility(View.INVISIBLE);
            } else {
                holder.tvGoodsPrice.setVisibility(View.VISIBLE);
            }

            if ("0".equals(data.get(position).getGoods_num())) {
                holder.llCartNum.setVisibility(View.GONE);
                holder.tvGoodsSpec.setVisibility(View.VISIBLE);
            } else {
                holder.llCartNum.setVisibility(View.VISIBLE);
                holder.tvGoodsSpec.setVisibility(View.GONE);
            }
        } else if (isHeader(position)) {
            /*
              去逛逛
             */
            ((LinearLayout) holder.llNoCart.getChildAt(2)).getChildAt(0).setOnClickListener(v -> {
                EventBus.getDefault().post(new CommonEvent.ChangeTabEvent(0));
                if (isInGoodDetail) {
                    try {
                        context.startActivity(new Intent(context, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            /*
              我的收藏
             */
            ((TextView) ((LinearLayout) holder.llNoCart.getChildAt(2)).getChildAt(1))
                    .setText("我的收藏");
            ((LinearLayout) holder.llNoCart.getChildAt(2)).getChildAt(1).setOnClickListener(v -> {
                if (UserInfoUtil.isLogin()) {
                    ARouter.getInstance().build(RouterConstant.Collection.MAIN).withInt("collection_type", 0)
                            .navigation();
                } else {
                    ARouter.getInstance().build(RouterConstant.User.LOGIN).navigation();
                }
            });
            if (0 == data.size()) {
                holder.llNoCart.setVisibility(View.VISIBLE);
            } else {
                holder.llNoCart.setVisibility(View.GONE);
            }
            if (0 != dataRec.size()) {
                holder.llCartTitle.setVisibility(View.VISIBLE);
            } else {
                holder.llCartTitle.setVisibility(View.GONE);
            }
            return;
        } else {
            ImageUtil.loadImage(dataRec.get(position - data.size() - 1).getGoods_image_url(),
                    holder.ivCartRecPreview, R.drawable.iv_place_holder_1);
            holder.tvRecGoodsName.setText(dataRec.get(position - data.size() - 1).getGoods_name());
            holder.tvRecGoodsPrice.setText(
                    "￥" + dataRec.get(position - data.size() - 1).getGoods_price());
            holder.tvRecGoodsOriginPrice.setText(
                    "￥" + dataRec.get(position - data.size() - 1).getGoods_marketprice());
            DisplayUtil.setTextViewDeleteLine(holder.tvRecGoodsOriginPrice);
            if(dataRec.get(position - data.size() - 1).getGoods_discount().equals("100"))  {
                holder.tvRecGoodsOriginPrice.setVisibility(View.GONE);
                holder.tvRecGoodsDiscount.setVisibility(View.GONE);
            } else {
                holder.tvRecGoodsOriginPrice.setVisibility(View.VISIBLE);
                holder.tvRecGoodsDiscount.setVisibility(View.VISIBLE);
            }
            holder.tvRecGoodsDiscount.setText(Float.parseFloat(dataRec.get(position - data.size() - 1).getGoods_discount()) / 10 + "折");
        }

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
    }

    /**
     * 添加购物车数量
     */
    private void addToCart(int pos, boolean isFailed) {
        if (Integer.parseInt(data.get(pos).getGoods_num()) < Integer
                .parseInt(data.get(pos).getGoods_storage())) {
            data.get(pos).setGoods_num(
                    String.valueOf(Integer.parseInt(data.get(pos).getGoods_num()) + 1));
            recyclerView.getAdapter().notifyItemChanged(pos);
        } else {
            ShowToast.show("不能再加了");
            return;
        }
        updateCartNum(0, pos, isFailed);
    }

    /**
     * 减少购物车数量
     */
    private void subFromCart(int pos, boolean isFailed) {
        if (Integer.parseInt(data.get(pos).getGoods_num()) > 1 && Integer
                .parseInt(data.get(pos).getGoods_num()) > Integer
                .parseInt(data.get(pos).getGoods_storage())) {
            data.get(pos).setGoods_num(
                    String.valueOf(Integer.parseInt(data.get(pos).getGoods_storage())));
            recyclerView.getAdapter().notifyItemChanged(pos);
            updateCartNum(2, pos, isFailed);
        } else {
            if (Integer.parseInt(data.get(pos).getGoods_num()) > 1) {
                data.get(pos).setGoods_num(
                        String.valueOf(Integer.parseInt(data.get(pos).getGoods_num()) - 1));
                recyclerView.getAdapter().notifyItemChanged(pos);
            } else {
                ShowToast.show("不能再减了");
                return;
            }
            updateCartNum(1, pos, isFailed);
        }
    }

    private void updateCartNum(int flags, int pos, boolean isFailed) {
        handler.obtainMessage(TOTAL_PRICE, getSelectedCount(), 0, getTotalPrice()).sendToTarget();
        handler.obtainMessage(CHECK_ALL, isAllSelected()).sendToTarget();
        if (isFailed) {
            return;
        } else {
            processDialog.setTitle("正在更新购物车").setCancelable(false).show();
        }
        Map<String, String> params = new HashMap<>();
        params.put("cart_id", data.get(pos).getCart_id());
        params.put("quantity", data.get(pos).getGoods_num());

        ApiManager.normalCartUpdate(params).subscribe(empty -> {
            processDialog.dismiss();
        }, throwable -> {
            processDialog.dismiss();
            ShowToast.show(throwable.getMessage());
            if (0 == flags) {
                subFromCart(pos, true);
            } else if (1 == flags) {
                addToCart(pos, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size() + dataRec.size() + 1/* 中间分割*/;
    }

    class OrderHolder
            extends RecyclerView.ViewHolder {

        /**
         * cart
         */
        CheckBox cbGoodsCheck;
        FlexibleImageView ivPreview;
        LinearLayout llCartNum;
        ImageView ivAdd;
        ImageView ivSub;
        TextView tvGoodsName;
        TextView tvGoodsSpec;
        TextView tvGoodsNum;
        TextView tvGoodsPrice;

        LinearLayout llNoCart;
        LinearLayout llCartTitle;

        /**
         * recommend
         */
        ImageView ivCartRecPreview;
        TextView tvRecGoodsName;
        TextView tvRecGoodsPrice;
        TextView tvRecGoodsOriginPrice;
        TextView tvRecGoodsDiscount;

        OrderHolder(View itemView) {
            super(itemView);

            cbGoodsCheck = (CheckBox) itemView.findViewById(R.id.cb_cart_goods_check);
            ivPreview = (FlexibleImageView) itemView.findViewById(R.id.iv_cart_goods_preview);
            llCartNum = (LinearLayout) itemView.findViewById(R.id.ll_cart_num_op);
            ivAdd = (ImageView) itemView.findViewById(R.id.iv_cart_goods_add);
            ivSub = (ImageView) itemView.findViewById(R.id.iv_cart_goods_sub);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_cart_goods_name);
            tvGoodsSpec = (TextView) itemView.findViewById(R.id.tv_cart_goods_spec);
            tvGoodsNum = (TextView) itemView.findViewById(R.id.tv_cart_goods_num);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_cart_goods_price);

            llNoCart = (LinearLayout) itemView.findViewById(R.id.ll_cart_empty);
            llCartTitle = (LinearLayout) itemView.findViewById(R.id.ll_cart_recommend_title);

            ivCartRecPreview = (FlexibleImageView) itemView.findViewById(R.id.iv_catalog_goods);
            tvRecGoodsName = (TextView) itemView.findViewById(R.id.tv_catalog_goods_name);
            tvRecGoodsPrice = (TextView) itemView.findViewById(R.id.tv_catalog_goods_price);
            tvRecGoodsOriginPrice = (TextView) itemView
                    .findViewById(R.id.tv_catalog_goods_previous_price);
            tvRecGoodsDiscount = (TextView) itemView.findViewById(R.id.tv_catalog_goods_discount);

        }
    }

    /**
     * 获取购物车选中总价，每次选中状态发生更改都进行调用
     *
     * @return 总价
     */
    public float getTotalPrice() {
        float totalPrice = 0;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                totalPrice += Float.parseFloat(data.get(i).getGoods_price()) * Float
                        .parseFloat(data.get(i).getGoods_num());
            }
        }
        return totalPrice;
    }

    /**
     * 返回选中数量
     *
     * @return 总数
     */
    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                count += Integer.parseInt(data.get(i).getGoods_num());
            }
        }
        return count;
    }

    /**
     * 返回是否全选
     *
     * @return 全选
     */
    public boolean isAllSelected() {
        boolean flag = true;
        for (CartBean.CartListEntity result : data) {
            if (!result.isChecked()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 选中所有
     *
     * @param flag 是否
     */
    public void selectAll(Boolean flag) {

        for (CartBean.CartListEntity result : data) {
            result.setChecked(flag);
        }
        recyclerView.getAdapter().notifyItemRangeChanged(0, data.size());
        handler.obtainMessage(TOTAL_PRICE, getSelectedCount(), 0, getTotalPrice()).sendToTarget();
        handler.obtainMessage(CHECK_ALL, isAllSelected()).sendToTarget();
    }
}

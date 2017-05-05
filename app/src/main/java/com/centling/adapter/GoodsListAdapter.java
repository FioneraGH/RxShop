package com.centling.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.GoodsDetailActivity;
import com.centling.entity.CatalogGoodsBean;
import com.fionera.base.util.DisplayUtil;
import com.centling.util.ImageUtil;

import java.util.List;
import java.util.Locale;

public class GoodsListAdapter
        extends RecyclerView.Adapter<GoodsListAdapter.Holder> {
    private Context mContext;
    private List<CatalogGoodsBean.GoodsCommonlistEntity> goodsCommonList;


    public GoodsListAdapter(Context context,
                            List<CatalogGoodsBean.GoodsCommonlistEntity> goodsCommonList) {
        this.mContext = context;
        this.goodsCommonList = goodsCommonList;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext)
                .inflate(R.layout.rv_goods_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.tvGoodsName.setText(goodsCommonList.get(position).getGoods_name());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            intent.putExtra("goodsCommonId", goodsCommonList.get(position).getGoods_commonid());
            mContext.startActivity(intent);
        });
        if (goodsCommonList.get(position).getGoods_salenum_open() == 0) {
            holder.tvGoodsBuyNum.setVisibility(View.INVISIBLE);
        } else {
            holder.tvGoodsBuyNum.setVisibility(View.VISIBLE);
        }
        holder.tvGoodsBuyNum.setText(String.format(Locale.CHINA, "%s人已经购买",
                goodsCommonList.get(position).getGoods_salenum()));
        holder.tvGoodsPrice.setText(String.format(Locale.CHINA, "￥ %s",
                goodsCommonList.get(position).getGoods_price()));
        DisplayUtil.setTextViewDeleteLine(holder.tvGoodsPreviousPrice);
        holder.tvGoodsPreviousPrice.setText(String.format(Locale.CHINA, "￥ %s",
                goodsCommonList.get(position).getGoods_marketprice()));
        if (goodsCommonList.get(position).getGoods_discount().contentEquals("100")) {
            holder.tvGoodsDiscount.setVisibility(View.GONE);
            holder.tvGoodsPreviousPrice.setVisibility(View.GONE);
        } else {
            holder.tvGoodsDiscount.setVisibility(View.VISIBLE);
            holder.tvGoodsPreviousPrice.setVisibility(View.VISIBLE);
            holder.tvGoodsDiscount.setText(String.format(Locale.CHINA, "%1.1f折",
                    Float.valueOf(goodsCommonList.get(position).getGoods_discount()) / 10));
        }
        ImageUtil.loadImage(goodsCommonList.get(position).getGoods_image_url(), holder.ivGoodsItem,
                R.drawable.iv_place_holder_1);
    }

    @Override
    public int getItemCount() {
        return goodsCommonList.size();
    }


    class Holder
            extends RecyclerView.ViewHolder {

        LinearLayout llGoodsItem;
        ImageView ivGoodsItem;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvGoodsPreviousPrice;
        TextView tvGoodsDiscount;
        TextView tvGoodsBuyNum;

        Holder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            llGoodsItem = (LinearLayout) itemView.findViewById(R.id.ll_goods_item);
            ivGoodsItem = (ImageView) itemView.findViewById(R.id.iv_goods_item);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tvGoodsPreviousPrice = (TextView) itemView.findViewById(R.id.tv_goods_previous_price);
            tvGoodsDiscount = (TextView) itemView.findViewById(R.id.tv_goods_discount);
            tvGoodsBuyNum = (TextView) itemView.findViewById(R.id.tv_goods_buy_num);
        }
    }
}

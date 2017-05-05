package com.centling.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.GoodsDetailActivity;
import com.centling.entity.CatalogGoodsBean;
import com.fionera.base.util.DisplayUtil;
import com.centling.util.ImageUtil;
import com.fionera.base.widget.FlexibleImageView;

import java.util.List;
import java.util.Locale;

/**
 * CatalogGoodsListAdapter
 * Created by Victor on 15/12/1.
 */

public class CatalogGoodsListAdapter
        extends RecyclerView.Adapter<CatalogGoodsListAdapter.MyHolder> {
    private Context mContext;
    private String gcId;
    private List<CatalogGoodsBean.GoodsCommonlistEntity> goodsCommonList;

    public CatalogGoodsListAdapter(Context mContext,
                                   List<CatalogGoodsBean.GoodsCommonlistEntity> goodsCommonList,
                                   String gcId) {
        this.mContext = mContext;
        this.goodsCommonList = goodsCommonList;
        this.gcId = gcId;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.rv_catalog_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tvName.setText(goodsCommonList.get(position).getGoods_name());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            intent.putExtra("goodsCommonId", goodsCommonList.get(position).getGoods_commonid());
            intent.putExtra("gcId", gcId);
            mContext.startActivity(intent);
        });

        holder.tvPrice.setText(
                String.format(Locale.CHINA, "￥%s", goodsCommonList.get(position).getGoods_price()));
        holder.tvPreviousPrice.setText(String.format(Locale.CHINA, "￥%s",
                goodsCommonList.get(position).getGoods_marketprice()));
        DisplayUtil.setTextViewDeleteLine(holder.tvPreviousPrice);
        if (goodsCommonList.get(position).getGoods_discount().equals("100")) {
            holder.tvPreviousPrice.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
        } else {
            holder.tvPreviousPrice.setVisibility(View.VISIBLE);
            holder.tvDiscount.setVisibility(View.VISIBLE);
        }
        holder.tvDiscount.setText(String.format(Locale.CHINA, "%1.1f折",
                Float.parseFloat(goodsCommonList.get(position).getGoods_discount()) / 10));
        ImageUtil.loadImage(goodsCommonList.get(position).getGoods_image_url(), holder.ivPic,
                R.drawable.iv_place_holder_1);
    }


    @Override
    public int getItemCount() {
        return goodsCommonList.size();
    }

    class MyHolder
            extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPrice;
        TextView tvPreviousPrice;
        FlexibleImageView ivPic;
        TextView tvDiscount;

        MyHolder(final View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_catalog_goods_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_catalog_goods_price);
            tvPreviousPrice = (TextView) itemView.findViewById(
                    R.id.tv_catalog_goods_previous_price);
            ivPic = (FlexibleImageView) itemView.findViewById(R.id.iv_catalog_goods);
            tvDiscount = (TextView) itemView.findViewById(R.id.tv_catalog_goods_discount);
        }
    }
}

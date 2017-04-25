package com.centling.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.GoodsDetailActivity;
import com.centling.entity.FootPrintBean;
import com.centling.util.ImageUtil;
import com.centling.widget.FlexibleImageView;

import java.util.List;
import java.util.Locale;

public class FootPrintListAdapter
        extends RecyclerView.Adapter<FootPrintListAdapter.MyHolder> {
    private Context mContext;
    private List<FootPrintBean.BrowseListEntity> data;

    public FootPrintListAdapter(Context mContext,
                                List<FootPrintBean.BrowseListEntity> data ) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.rv_collection_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        ImageUtil.loadImage(data.get(position).getGoods_image_url(), holder.ivPic,
                R.drawable.iv_place_holder_1);
        holder.tvName.setText(data.get(position).getGoods_name());
        holder.tvPrice.setText(
                String.format(Locale.CHINA, "价格：￥%s", data.get(position).getGoods_price()));
        holder.itemView.setOnClickListener(v -> mContext.startActivity(
                new Intent(mContext, GoodsDetailActivity.class)
                        .putExtra("goodsCommonId", data.get(position).getGoods_commonid())));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder
            extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvName;
        TextView tvPrice;

        MyHolder(final View itemView) {
            super(itemView);
            ivPic = (FlexibleImageView) itemView.findViewById(R.id.iv_collection_goods_preview);
            tvName = (TextView) itemView.findViewById(R.id.tv_collection_goods_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_collection_goods_price);
        }
    }
}

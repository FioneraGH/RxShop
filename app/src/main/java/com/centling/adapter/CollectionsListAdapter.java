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
import com.centling.entity.CollectionBean;
import com.centling.event.CollectionRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.ImageUtil;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.fionera.base.widget.FlexibleImageView;
import com.centling.widget.ProcessDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CollectionsListAdapter
        extends RecyclerView.Adapter<CollectionsListAdapter.MyHolder> {
    private Context mContext;
    private List<CollectionBean.FavoritesListEntity> data;
    private ProcessDialog processDialog;
    private RecyclerView recyclerView;

    public CollectionsListAdapter(Context mContext,
                                  List<CollectionBean.FavoritesListEntity> data,
                                  RecyclerView recyclerView) {
        this.mContext = mContext;
        this.data = data;
        this.recyclerView = recyclerView;
        this.processDialog = new ProcessDialog(mContext);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.rv_collection_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        ImageUtil.loadImage(data.get(position).getGoods_commonlist().get(0).getGoods_image_url(),
                holder.ivPic, R.drawable.iv_place_holder_1);
        holder.tvName.setText(data.get(position).getGoods_commonlist().get(0).getGoods_name());
        holder.tvPrice.setText(String.format(Locale.CHINA, "价格：￥%s", data.get(position)
                .getGoods_commonlist().get(0).getGoods_price()));
        holder.ivDel.setOnClickListener(v1 -> ShowDialog
                .showSelectDialog(mContext, "删除收藏", "您确定要删除收藏商品吗？", "",
                        v2 -> deleteCollection(holder.getAdapterPosition())));
        holder.ivDel.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext,
                GoodsDetailActivity.class).putExtra("goodsCommonId", data.get(position)
                .getGoods_commonlist().get(0).getGoods_commonid())
                .putExtra("judgment_enter", "collection")));
    }

    private void deleteCollection(int position) {
        processDialog.setTitle("正在删除收藏").showDialog(false);
        Map<String,String> params = new HashMap<>();
        params.put("fav_id", data.get(position).getFav_id());

        ApiManager.collectionDel(params).subscribe(o -> {
            processDialog.dismiss();
            data.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            recyclerView.getAdapter().notifyItemRangeChanged(0, data.size());
            if (data.size() == 0) {
                EventBus.getDefault().post(new CollectionRelationEvent.UpdateCollection());
            }
        },throwable -> {
            processDialog.dismiss();
            ShowToast.show(throwable.getMessage());
        });
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
        ImageView ivDel;

        MyHolder(final View itemView) {
            super(itemView);
            ivPic = (FlexibleImageView) itemView.findViewById(R.id.iv_collection_goods_preview);
            tvName = (TextView) itemView.findViewById(R.id.tv_collection_goods_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_collection_goods_price);
            ivDel = (ImageView) itemView.findViewById(R.id.iv_collection_goods_del);
        }
    }
}

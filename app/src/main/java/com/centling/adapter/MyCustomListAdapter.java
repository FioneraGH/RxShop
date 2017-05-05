package com.centling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.centling.R;
import com.centling.entity.MyCustomBean;
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

public class MyCustomListAdapter
        extends RecyclerView.Adapter<MyCustomListAdapter.MyHolder> {
    private Context mContext;
    private List<MyCustomBean.PersonalListEntity> data;
    private ProcessDialog processDialog;
    private RecyclerView recyclerView;

    public MyCustomListAdapter(Context mContext,
                               List<MyCustomBean.PersonalListEntity> data,
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
        ImageUtil.loadImage(data.get(position).getGoods_image().split(",")[0], holder.ivPic,
                R.drawable.iv_place_holder_1);
        holder.tvName.setText(data.get(position).getGoods_name());
        holder.tvPrice.setText(
                String.format(Locale.CHINA, "价格：￥%s", data.get(position).getGoods_price()));
        holder.ivDel.setOnClickListener(v1 -> ShowDialog
                .showSelectDialog(mContext, "删除定制", "您确定要删除定制商品吗？", "",
                        v2 -> deleteCollection(holder.getAdapterPosition())));
        holder.ivDel.setVisibility(View.VISIBLE);
    }

    private void deleteCollection(int position) {
        processDialog.setTitle("正在删除定制").showDialog(false);
        Map<String,String> params = new HashMap<>();
        params.put("id", data.get(position).getMy_personal_id());

        ApiManager.customDelete(params).subscribe(o -> {
            processDialog.dismiss();
            data.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            recyclerView.getAdapter().notifyItemRangeChanged(0, data.size());
            if (data.size() == 0) {
                EventBus.getDefault().post(new CollectionRelationEvent.UpdateCustom());
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

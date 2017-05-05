package com.centling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centling.R;
import com.centling.entity.GoldsRecordBean;
import com.fionera.base.widget.CircleTextImageView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoldsRecordAdapter
        extends RecyclerView.Adapter<GoldsRecordAdapter.Holder> {
    private Context context;
    private List<GoldsRecordBean.GoldDetailEntity> data;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private SparseIntArray timeShow = new SparseIntArray();
    private SparseArray<String> timeWhat = new SparseArray<>();
    private List<GoldsRecordBean.GoldDetailEntity.LgContentEntity> dataUnit = new ArrayList<>();

    public GoldsRecordAdapter(Context context, List<GoldsRecordBean.GoldDetailEntity> data) {
        this.context = context;
        this.data = data;
        String currentTime = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(
                new Date(System.currentTimeMillis()));
        String[] currentTimes = currentTime.split("-");
        if (3 == currentTimes.length) {
            currentYear = Integer.parseInt(currentTimes[0]);
            currentMonth = Integer.parseInt(currentTimes[1]);
            currentDay = Integer.parseInt(currentTimes[2]);
        }
        computeShow(data);
    }

    public void computeShow(List<GoldsRecordBean.GoldDetailEntity> data) {
        int currentShow = 0;
        timeShow.clear();
        timeWhat.clear();
        dataUnit.clear();
        for (int i = 0; i < data.size(); i++) {
            timeShow.put(i, currentShow);
            currentShow += data.get(i).getLg_content().size();
            timeWhat.put(i, data.get(i).getLg_title());
            dataUnit.addAll(data.get(i).getLg_content());
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(
                LayoutInflater.from(context).inflate(R.layout.rv_gold_record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        ((LinearLayout) holder.itemView).getChildAt(0).setVisibility(View.GONE);
        for (int i = 0; i < data.size(); i++) {
            if (position == timeShow.get(i)) {
                ((LinearLayout) holder.itemView).getChildAt(0).setVisibility(View.VISIBLE);
                holder.tvTime0.setText(timeWhat.get(i));
            }
        }
        if (dataUnit.get(position).getLg_av_amount().contains("-") || dataUnit.get(position)
                .getLg_av_amount().equals("0.00")) {
            holder.imageView.setImageResource(R.drawable.iv_item_recharge);
        } else {
            holder.imageView.setImageResource(R.drawable.iv_item_consume);
        }
        String time[] = dataUnit.get(position).getLg_add_time().split(" ");
        if (2 == time.length) {
            String[] recordTimes = time[0].split("-");
            int recordYear = Integer.parseInt(recordTimes[0]);
            int recordMonth = Integer.parseInt(recordTimes[1]);
            int recordDay = Integer.parseInt(recordTimes[2]);

            /*
              time1
             */
            if (recordYear == currentYear && recordMonth == currentMonth && recordDay ==
                    currentDay) {
                holder.tvTime1.setText("今天");
            } else if (recordYear == currentYear && recordMonth == currentMonth && recordDay ==
                    currentDay - 1) {
                holder.tvTime1.setText("昨天");
            } else {
                Calendar c = Calendar.getInstance();
                c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                        .parse(dataUnit.get(position).getLg_add_time(), new ParsePosition(0)));
                holder.tvTime1.setText(new SimpleDateFormat("EEEE", Locale.CHINA)
                        .format(c.getTime()).replace("星期", "周"));
            }

            /*
              time2
             */
            holder.tvTime2.setText(String.format(Locale.CHINA, "%s-%s", recordMonth, recordDay));
        }
        holder.tvMoney.setText(dataUnit.get(position).getLg_av_amount());
        holder.tvComments.setText(dataUnit.get(position).getLg_desc());
    }

    @Override
    public int getItemCount() {
        return dataUnit.size();
    }

    class Holder
            extends RecyclerView.ViewHolder {
        CircleTextImageView imageView;
        TextView tvTime0;
        TextView tvTime1;
        TextView tvTime2;
        TextView tvMoney;
        TextView tvComments;

        Holder(View itemView) {
            super(itemView);
            imageView = (CircleTextImageView) itemView.findViewById(R.id.iv_gold_record);
            tvMoney = (TextView) itemView.findViewById(R.id.iv_gold_record_money);
            tvComments = (TextView) itemView.findViewById(R.id.iv_gold_record_comments);
            tvTime0 = (TextView) itemView.findViewById(R.id.iv_gold_record_time0);
            tvTime1 = (TextView) itemView.findViewById(R.id.iv_gold_record_time1);
            tvTime2 = (TextView) itemView.findViewById(R.id.iv_gold_record_time2);
        }
    }
}

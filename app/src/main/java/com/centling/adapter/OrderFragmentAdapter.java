package com.centling.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.centling.R;
import com.centling.fragment.OrderListFragment;

/**
 * OrderFragmentAdapter
 * Created by fionera on 15-11-30.
 */

public class OrderFragmentAdapter
        extends FragmentPagerAdapter {
    private final static int PAGE_COUNT = 5;
    private final static String[] titles = new String[]{"全部", "待付款", "待发货", "待收货", "退换货"};

    private Context mContext;
    private SparseArray<String> orderStateMaps = new SparseArray<>();
    private SparseArray<String> refundStateMaps = new SparseArray<>();

    public OrderFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        orderStateMaps.put(0, "");
        orderStateMaps.put(1, "10");
        orderStateMaps.put(2, "20");
        orderStateMaps.put(3, "30");
        orderStateMaps.put(4, "");
        refundStateMaps.put(0, "");
        refundStateMaps.put(1, "");
        refundStateMaps.put(2, "");
        refundStateMaps.put(3, "");
        refundStateMaps.put(4, "1");
    }

    @Override
    public Fragment getItem(int position) {
        OrderListFragment orderListFragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("order_type", position);
        bundle.putString("order_state", orderStateMaps.get(position));
        bundle.putString("refund_state", refundStateMaps.get(position));
        orderListFragment.setArguments(bundle);
        return orderListFragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public String getPageTitle(int position) {
        return titles[position % PAGE_COUNT];
    }

    public View getTabView(int position) {
        View view = View.inflate(mContext, R.layout.tl_catalog_title_item, null);
        ((TextView) view.findViewById(R.id.tv_title_name)).setText(titles[position]);
        return view;
    }
}

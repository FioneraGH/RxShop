package com.centling.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centling.R;
import com.centling.adapter.OrderListAdapter;
import com.centling.entity.OrderBean;
import com.centling.event.OrderRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;
import com.fionera.base.widget.AutoRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OrderListFragment
 * Created by fionera on 15-11-30.
 */

public class OrderListFragment
        extends BaseFragment {

    public static final int PAGE_SIZE = 10;

    private AutoRecyclerView rvOrderList;

    private OrderListAdapter orderListAdapter;
    private List<OrderBean.OrderGroupListEntity> orderListEntities = new ArrayList<>();

    private int order_type = 0;
    private String order_state = "";
    private String refund_state = "";
    private int currentPage = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        rvOrderList = (AutoRecyclerView) view.findViewById(R.id.rv_order_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            order_type = bundle.getInt("order_type");
            refund_state = bundle.getString("refund_state");
            order_state = bundle.getString("order_state");
        }

        orderListAdapter = new OrderListAdapter(rvOrderList, mContext, orderListEntities,
                                                mProcessDialog, order_type);
        rvOrderList.setLayoutManager(
                new GridLayoutManager(mContext, 1, LinearLayoutManager.VERTICAL, false));
        rvOrderList.setAdapter(orderListAdapter);
        orderListAdapter.setItemClickListener((view, pos) -> {
//            startActivity(new Intent(mContext,
//                OrderDetailActivity.class).putExtra("order_id", orderListEntities.get(pos)
//                .getOrder_list().get(0).getOrder_id())
//                .putExtra("order_state", orderListEntities.get(pos).getOrder_list().get(0)
//                        .getRefund_sure_msg()));
        });
        rvOrderList.setLoadDataListener(() -> getOrderList(true));

        getOrderList(false);
    }

    private void getOrderList(boolean loadMore) {
        if (!loadMore) {
            currentPage = 1;
            orderListEntities.clear();
        }
        Map<String, String> params = new HashMap<>();
        params.put("refund_state", refund_state);
        params.put("order_state", order_state);

        ApiManager.getOrderList(currentPage, PAGE_SIZE, params).subscribe(orderBean -> {
            currentPage++;
            if (order_type == 2) {
                for (OrderBean.OrderGroupListEntity entity : orderBean.getOrder_group_list()) {
                    if (0 == entity.getOrder_list().get(0).getRefund_sure_state()) {
                        orderListEntities.add(entity);
                    }
                }
            } else {
                orderListEntities.addAll(orderBean.getOrder_group_list());
            }
            rvOrderList.getAdapter().notifyDataSetChanged();
            rvOrderList.loadMoreComplete(!orderBean.isHasmore());
        }, throwable -> ShowToast.show(throwable.getMessage()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventUpdateOrder(OrderRelationEvent.UpdateOrder updateOrder) {
        if (0 == updateOrder.order_type || updateOrder.order_type != order_type) {
            getOrderList(false);
        }
    }

    @Subscribe
    public void onEventWxPayResult(OrderRelationEvent.WxPayResult wxPayResult) {
        if (wxPayResult.success) {
            if (order_type == wxPayResult.order_type) {
                orderListAdapter.opDone();
            }
        } else {
            if (order_type == wxPayResult.order_type) {
                orderListAdapter.requestGoldsBack();
            }
        }
    }
}

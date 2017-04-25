package com.centling.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centling.R;
import com.centling.adapter.FootPrintListAdapter;
import com.centling.entity.FootPrintBean;
import com.centling.event.CollectionRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;
import com.centling.widget.AutoRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * CollectionListFragment
 * Created by fionera on 15-11-30.
 */
public class FootPrintListFragment
        extends BaseFragment {

    public static final int PAGE_SIZE = 10;

    private AutoRecyclerView rvCollectionList;
    private PtrClassicFrameLayout srCollectionList;

    private List<FootPrintBean.BrowseListEntity> collectionListEntities = new ArrayList<>();

    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_collection_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srCollectionList = (PtrClassicFrameLayout) view.findViewById(R.id.sr_collection_list);
        rvCollectionList = (AutoRecyclerView) view.findViewById(R.id.rv_collection_list);
        init();
    }

    private void init() {
        srCollectionList.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getCollectionList(false);
            }
        });

        rvCollectionList.setLayoutManager(
                new GridLayoutManager(mContext, 1));
        rvCollectionList.setAdapter(new FootPrintListAdapter(mContext, collectionListEntities));
    }

    private void getCollectionList(boolean loadMore) {
        if (!loadMore) {
            currentPage = 1;
            collectionListEntities.clear();
        }
        Map<String,String> params = new HashMap<>();
        params.put("page",String.valueOf(currentPage));
        params.put("pagsizee",String.valueOf(PAGE_SIZE));

        ApiManager.getFootprintList(params).compose(bindLifecycle()).subscribe(
                footPrintBean -> {
                    srCollectionList.refreshComplete();
                    currentPage++;
                    collectionListEntities.addAll(footPrintBean.getBrowse_list());

                    rvCollectionList.getAdapter().notifyDataSetChanged();

                    rvCollectionList.setLoadDataListener(() -> getCollectionList(true));

                    rvCollectionList.loadMoreComplete(
                            footPrintBean.getBrowse_list().size() < PAGE_SIZE);
                }, throwable -> {
                    rvCollectionList.loadMoreComplete(false);
                    srCollectionList.refreshComplete();
                    ShowToast.show(throwable.getMessage());
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getCollectionList(false);
        }
    }

    @Subscribe
    public void onEventClearFootprint(CollectionRelationEvent.ClearFootprint clearFootprint) {
        getCollectionList(false);
    }
}

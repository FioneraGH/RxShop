package com.centling.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centling.R;
import com.centling.adapter.CollectionsListAdapter;
import com.centling.entity.CollectionBean;
import com.centling.event.CollectionRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;
import com.fionera.base.widget.AutoRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * CollectionListFragment
 * Created by fionera on 15-11-30.
 */
public class CollectionListFragment
        extends BaseFragment {

    public static final int PAGE_SIZE = 10;

    private AutoRecyclerView rvCollectionList;
    private PtrClassicFrameLayout srCollectionList;

    private List<CollectionBean.FavoritesListEntity> collectionListEntities = new ArrayList<>();

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

        rvCollectionList.setLayoutManager(new GridLayoutManager(mContext, 1));
        rvCollectionList.setAdapter(
                new CollectionsListAdapter(mContext, collectionListEntities, rvCollectionList));

        getCollectionList(false);
    }

    private void getCollectionList(boolean loadMore) {
        if (!loadMore) {
            currentPage = 1;
            collectionListEntities.clear();
        }

        ApiManager.getCollectionList(currentPage, PAGE_SIZE, new HashMap<>()).compose(
                bindLifecycle()).subscribe(collectionBean -> {
            srCollectionList.refreshComplete();
            currentPage++;
            collectionListEntities.addAll(collectionBean.getFavorites_list());

            rvCollectionList.getAdapter().notifyDataSetChanged();

            rvCollectionList.setLoadDataListener(() -> getCollectionList(true));

            rvCollectionList.loadMoreComplete(!collectionBean.isHasmore());
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

    @Subscribe
    public void onEventUpdateCollection(CollectionRelationEvent.UpdateCollection updateCollection) {
        getCollectionList(false);
    }
}

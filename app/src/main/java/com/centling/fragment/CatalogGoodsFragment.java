package com.centling.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centling.R;
import com.centling.adapter.CatalogGoodsListAdapter;
import com.centling.databinding.FragmentCatalogGoodsBinding;
import com.centling.entity.CatalogGoodsBean;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * CatalogGoodsFragment
 * Created by Victor on 15/12/18.
 */

public class CatalogGoodsFragment
        extends BaseFragment {
    private String gc_id;
    private List<CatalogGoodsBean.GoodsCommonlistEntity> goodsData = new ArrayList<>();

    private FragmentCatalogGoodsBinding mFragmentCatalogGoodsBinding;

    private int curPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentCatalogGoodsBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.fragment_catalog_goods, container, false);
        return mFragmentCatalogGoodsBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gc_id = getArguments().getString("gc_id");
        if (gc_id == null) {
            ShowToast.show("没有gc_id");
            return;
        }
        mFragmentCatalogGoodsBinding.rvCatalogRightList.setLayoutManager(
                new GridLayoutManager(mContext, 2));
        mFragmentCatalogGoodsBinding.rvCatalogRightList.setAdapter(
                new CatalogGoodsListAdapter(mContext, goodsData, gc_id));
        mFragmentCatalogGoodsBinding.rvCatalogRightList.setLoadDataListener(
                () -> getGoodsListByGcId(false));

        mFragmentCatalogGoodsBinding.ptrCatalogGoods.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                curPage = 1;
                getGoodsListByGcId(true);
            }
        });
        mFragmentCatalogGoodsBinding.ptrCatalogGoods.postDelayed(() -> getGoodsListByGcId(true),
                100);
    }

    private void getGoodsListByGcId(boolean isRefresh) {
        ApiManager.getCatalogGoodsList(gc_id, curPage, 6).compose(bindLifecycle()).subscribe(catalogGoodsBean -> {
            mFragmentCatalogGoodsBinding.ptrCatalogGoods.refreshComplete();
            if (isRefresh) {
                goodsData.clear();
                goodsData.addAll(catalogGoodsBean.getGoods_commonlist());
                mFragmentCatalogGoodsBinding.rvCatalogRightList.setLoadDataListener(
                        () -> getGoodsListByGcId(false));
                mFragmentCatalogGoodsBinding.rvCatalogRightList.getAdapter().notifyDataSetChanged();
            } else {
                int size = goodsData.size();
                goodsData.addAll(catalogGoodsBean.getGoods_commonlist());
                mFragmentCatalogGoodsBinding.rvCatalogRightList.getAdapter()
                        .notifyItemRangeInserted(size,
                                catalogGoodsBean.getGoods_commonlist().size());
            }
            curPage++;
            mFragmentCatalogGoodsBinding.rvCatalogRightList.loadMoreComplete(
                    !catalogGoodsBean.isHasmore());
        }, throwable -> {
            mFragmentCatalogGoodsBinding.ptrCatalogGoods.refreshComplete();
            mFragmentCatalogGoodsBinding.rvCatalogRightList.loadMoreComplete(false);
            if (getUserVisibleHint()) {
                ShowToast.show("获取商品失败");
            }
        });
    }
}

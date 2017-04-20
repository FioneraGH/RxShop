package com.centling.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.centling.R;
import com.centling.activity.SearchActivity;
import com.centling.adapter.CatalogFragmentAdapter;
import com.centling.databinding.FragmentCatalogSlideBinding;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;

/**
 * CatalogSlideFragment
 * Created by Victor on 15/12/18.
 */

public class CatalogSlideFragment
        extends BaseFragment {

    private FragmentCatalogSlideBinding fragmentCatalogSlideBinding;

    private CatalogFragmentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCatalogSlideBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.fragment_catalog_slide, container, false);
        return fragmentCatalogSlideBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentCatalogSlideBinding.ivCatalogSlideSearch.setOnClickListener(v -> {
            startActivity(new Intent(mContext, SearchActivity.class));
        });
        fragmentCatalogSlideBinding.tvCatalogGoodsRefresh.setOnClickListener(v -> getCatalogList());
        getCatalogList();
    }

    private void getCatalogList() {
        ApiManager.getCatalogList().compose(bindLifecycle()).subscribe(catalogBean -> {
            fragmentCatalogSlideBinding.tlCatalogList.setVisibility(View.VISIBLE);
            fragmentCatalogSlideBinding.vpCatalogGoods.setVisibility(View.VISIBLE);
            fragmentCatalogSlideBinding.tvCatalogGoodsRefresh.setVisibility(View.GONE);

            mAdapter = new CatalogFragmentAdapter(getChildFragmentManager(), mContext,
                    catalogBean.getClass_list());
            fragmentCatalogSlideBinding.vpCatalogGoods.setAdapter(mAdapter);
            fragmentCatalogSlideBinding.tlCatalogList.setupWithViewPager(
                    fragmentCatalogSlideBinding.vpCatalogGoods);

            for (int i = 0; i < fragmentCatalogSlideBinding.tlCatalogList.getTabCount(); i++) {
                TabLayout.Tab tab = fragmentCatalogSlideBinding.tlCatalogList.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(mAdapter.getTabView(i));
                    View currentView = tab.getCustomView();
                    if (0 == i && currentView != null) {
                        currentView.setSelected(true);
                    }
                }
            }
        }, throwable -> {
            ShowToast.show("加载分类失败");

            fragmentCatalogSlideBinding.tlCatalogList.setVisibility(View.GONE);
            fragmentCatalogSlideBinding.vpCatalogGoods.setVisibility(View.GONE);
            fragmentCatalogSlideBinding.tvCatalogGoodsRefresh.setVisibility(View.VISIBLE);
        });
    }
}

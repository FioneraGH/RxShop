package com.centling.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.centling.R;
import com.centling.entity.CatalogBean;
import com.centling.fragment.CatalogGoodsFragment;

import java.util.List;


/**
 * CatalogFragmentAdapter
 * Created by Victor on 15/12/18.
 */

public class CatalogFragmentAdapter
        extends FragmentStatePagerAdapter {

    private List<CatalogBean.ClassListEntity> catalogList;
    private Context context;

    public CatalogFragmentAdapter(FragmentManager fm, Context context,
                                  List<CatalogBean.ClassListEntity> catalogList) {
        super(fm);
        this.context = context;
        this.catalogList = catalogList;
    }

    @Override
    public Fragment getItem(int position) {
        CatalogGoodsFragment fragment = new CatalogGoodsFragment();
        Bundle idBundle = new Bundle();
        idBundle.putString("gc_id", catalogList.get(position).getGc_id());
        fragment.setArguments(idBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return catalogList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return catalogList.get(position).getGc_name();
    }

    public View getTabView(int position) {
        View view = View.inflate(context, R.layout.tl_catalog_title_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_title_name);
        tv.setText(catalogList.get(position).getGc_name());
        ImageView img = (ImageView) view.findViewById(R.id.iv_title_indicator);

        return view;
    }
}

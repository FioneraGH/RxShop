package com.centling.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.centling.fragment.CollectionListFragment;
import com.centling.fragment.FootPrintListFragment;
import com.centling.fragment.MyCustomFragment;

/**
 * CollectionFragmentAdapter
 * Created by fionera on 15-11-30.
 */
public class CollectionFragmentAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private static final String[] titles = new String[]{"收藏的商品", "我的定制", "浏览足迹"};

    public CollectionFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return 0 == position ? new CollectionListFragment() : 1 == position ? new
                MyCustomFragment() : new FootPrintListFragment();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public  CharSequence getPageTitle(int position) {
        return titles[position % PAGE_COUNT];
    }
}

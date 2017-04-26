package com.centling.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.centling.fragment.GoldsRechargeFragment;

/**
 * GoldsRechargeFragmentAdapter
 * Created by fionera on 15-11-30.
 */
public class GoldsRechargeFragmentAdapter extends FragmentPagerAdapter {

    public GoldsRechargeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position){
        return new GoldsRechargeFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "现金充值";
    }
}

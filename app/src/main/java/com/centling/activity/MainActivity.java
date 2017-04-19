package com.centling.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.centling.R;
import com.centling.event.CommonEvent;
import com.centling.fragment.CatalogSlideFragment;
import com.centling.fragment.HomePageFragment;
import com.centling.util.ShowToast;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity
        extends BaseActivity {
    private final static int TIME_TO_EXIT = 2000;

    private List<Fragment> fragmentList = new ArrayList<>();
    private HomePageFragment homeFragment;
    private CatalogSlideFragment catalogFragment;
    private HomePageFragment cartFragment;
    private HomePageFragment userFragment;

    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        bottomNavigationBar = (BottomNavigationBar) findViewById(
                R.id.bottom_navigation_bar);

        homeFragment = new HomePageFragment();
        catalogFragment = new CatalogSlideFragment();
        cartFragment = new HomePageFragment();
        userFragment = new HomePageFragment();

        addFragment(homeFragment);
        addFragment(catalogFragment);
        addFragment(cartFragment);
        addFragment(userFragment);
        showSelectedFragment(0);

        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.iv_guide_home, "首页")).addItem(
                new BottomNavigationItem(R.drawable.iv_guide_catagory, "分类")).addItem(
                new BottomNavigationItem(R.drawable.iv_guide_cart, "购物")).addItem(
                new BottomNavigationItem(R.drawable.iv_guide_personal, "我的")).initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int i) {
                showSelectedFragment(i);
            }

            @Override
            public void onTabUnselected(int i) {

            }

            @Override
            public void onTabReselected(int i) {

            }
        });

        lifecycle.throttleFirst(TIME_TO_EXIT, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(this.bindLifecycle()).subscribe(
                integer -> ShowToast.show("再按一次退出"), Throwable::printStackTrace);

        lifecycle.timeInterval(AndroidSchedulers.mainThread()).skip(1).filter(
                timed -> timed.time() < TIME_TO_EXIT).compose(this.bindLifecycle())
                .subscribe(timed -> finish(), Throwable::printStackTrace);
    }

    private void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.show_layout, fragment)
                .commitAllowingStateLoss();
    }

    private int prePos = -1;

    private void showSelectedFragment(int clickPos) {
        Fragment fragment = fragmentList.get(clickPos);
        if(-1 != clickPos && clickPos == prePos){
                return;
        }
        hideAndShowFragment(fragment,-1 == clickPos);
        prePos = clickPos;
    }

    private void hideAndShowFragment(Fragment fragment, boolean create) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!create && homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (catalogFragment != null) {
            fragmentTransaction.hide(catalogFragment);
        }
        if (cartFragment != null) {
            fragmentTransaction.hide(cartFragment);
        }
        if (userFragment != null) {
            fragmentTransaction.hide(userFragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (lifecycle != null) {
            lifecycle.onNext(0);
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,null);
    }

    @Subscribe
    public void OnEventChangeTab(CommonEvent.ChangeTabEvent changeTabEvent) {
        bottomNavigationBar.selectTab(changeTabEvent.tabPosition);
    }
}

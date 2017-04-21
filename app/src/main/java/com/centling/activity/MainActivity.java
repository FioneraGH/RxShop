package com.centling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.centling.R;
import com.centling.event.CommonEvent;
import com.centling.fragment.CatalogSlideFragment;
import com.centling.fragment.HomePageFragment;
import com.centling.fragment.UserFragment;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity
        extends BaseActivity {
    private final static int TIME_TO_EXIT = 2000;

    private List<Fragment> fragmentList = new ArrayList<>();
    private HomePageFragment homeFragment;
    private CatalogSlideFragment catalogFragment;
    private HomePageFragment cartFragment;
    private UserFragment userFragment;

    private BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        homeFragment = new HomePageFragment();
        catalogFragment = new CatalogSlideFragment();
        cartFragment = new HomePageFragment();
        userFragment = new UserFragment();

        addFragment(homeFragment);
        addFragment(catalogFragment);
        addFragment(cartFragment);
        addFragment(userFragment);
        showSelectedFragment(0);

        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.iv_guide_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.iv_guide_catagory, "分类")).addItem(
                new BottomNavigationItem(R.drawable.iv_guide_cart, "购物")).addItem(
                new BottomNavigationItem(R.drawable.iv_guide_personal, "我的")).initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int i) {
                if ((2 == i || 3 == i) && !UserInfoUtil.isLogin()) {
                    bottomNavigationBar.selectTab(prePos);
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                showSelectedFragment(i);
            }

            @Override
            public void onTabUnselected(int i) {

            }

            @Override
            public void onTabReselected(int i) {
                if (0 == i) {
                    homeFragment.loadHomePage();
                }
            }
        });

        lifecycle.throttleFirst(TIME_TO_EXIT, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .compose(bindLifecycle()).subscribe(integer -> ShowToast.show("再按一次退出"),
                Throwable::printStackTrace);

        lifecycle.timeInterval(AndroidSchedulers.mainThread()).skip(1).filter(
                timed -> timed.time() < TIME_TO_EXIT).compose(bindLifecycle()).subscribe(
                timed -> finish(), Throwable::printStackTrace);
    }

    private void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.show_layout, fragment)
                .commitAllowingStateLoss();
    }

    private int prePos = -1;

    private void showSelectedFragment(int clickPos) {
        Fragment fragment = fragmentList.get(clickPos);
        if (-1 != clickPos && clickPos == prePos) {
            return;
        }
        hideAndShowFragment(fragment, -1 == clickPos);
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
    protected void onResume() {
        super.onResume();
        tryToFetchScore();
    }

    private void tryToFetchScore() {
        if (!UserInfoUtil.isLogin()){
            return;
        }
        Map<String,String> params = new HashMap<>();
        ApiManager.fetchMemberPoint(params).subscribe(s -> {
            JSONObject object = new JSONObject(s);
            JSONObject resultObj = object.getJSONObject("result");
            long addPoint = Long.parseLong(resultObj.getString("points"));
            ShowToast.show("签到成功,积分+" + addPoint);
        }, throwable -> {});
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
        StatusBarUtil.setTranslucentForImageViewInFragment(this, null);
    }

    @Subscribe
    public void OnEventChangeTab(CommonEvent.ChangeTabEvent changeTabEvent) {
        bottomNavigationBar.selectTab(changeTabEvent.tabPosition);
    }
}

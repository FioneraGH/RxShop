package com.centling.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.adapter.CollectionFragmentAdapter;
import com.centling.event.CollectionRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

@Route(path = "/collection/main")
public class CollectionActivity
        extends TitleBarActivity {

    @Autowired
    int collection_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collection);
        setTitleBarText("我的收藏");

        TabLayout tlCollectionTabs = (TabLayout) findViewById(R.id.tl_collection_tabs);
        ViewPager vpCollectionContainer = (ViewPager) findViewById(R.id.vp_collection_container);

        vpCollectionContainer.setAdapter( new CollectionFragmentAdapter(
                getSupportFragmentManager()));
        vpCollectionContainer.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (2 == position) {
                    mTitleBar.getToolbar().inflateMenu(R.menu.menu_collection_list);
                    mTitleBar.getToolbar().setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.menu_collection_list_clear) {
                            clearFootprint();
                            return true;
                        }
                        return false;
                    });
                } else {
                    mTitleBar.getToolbar().getMenu().clear();
                }
            }
        });
        tlCollectionTabs.setupWithViewPager(vpCollectionContainer);

        vpCollectionContainer.setCurrentItem(collection_type);
    }

    private void clearFootprint() {
        ShowDialog.showSelectDialog(mContext, "提示", "确定清空足迹吗?", "", v -> {
            showLoading("正在清空足迹");
            ApiManager.clearFootprint(new HashMap<>()).compose(bindUntil(ActivityEvent.DESTROY))
                    .subscribe(o -> {
                        dismissLoading();
                        ShowToast.show("清空成功");
                        EventBus.getDefault().post(new CollectionRelationEvent.ClearFootprint());
                    }, throwable -> {
                        dismissLoading();
                        ShowToast.show(throwable.getMessage());
                    });
        });
    }
}

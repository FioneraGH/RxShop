package com.centling.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.adapter.GoldsRecordAdapter;
import com.centling.constant.RouterConstant;
import com.centling.entity.GoldsRecordBean;
import com.centling.http.ApiManager;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = RouterConstant.User.GOLDS_RECORD)
public class GoldsRecordActivity
        extends TitleBarActivity {

    private RecyclerView recyclerView;
    private GoldsRecordAdapter mGoldsRecordAdapter;

    private List<GoldsRecordBean.GoldDetailEntity> goldDetailEntityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_record);
        setTitleBarText("金币明细");

        recyclerView = (RecyclerView) findViewById(R.id.rv_gold_record);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mGoldsRecordAdapter = new GoldsRecordAdapter(mContext, goldDetailEntityList);
        recyclerView.setAdapter(mGoldsRecordAdapter);

        getRecordList();
    }

    private void getRecordList() {
        showLoading("正在请求金币明细");
        ApiManager.goldRecordList(new HashMap<>()).compose(bindUntil(ActivityEvent.DESTROY))
                .subscribe(goldsRecordBean -> {
                    dismissLoading();
                    goldDetailEntityList.clear();
                    goldDetailEntityList.addAll(goldsRecordBean.getGold_detail());
                    mGoldsRecordAdapter.computeShow(goldDetailEntityList);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }, throwable -> {
                    dismissLoading();
                    ShowToast.show(TextUtils.isEmpty(throwable.getMessage()) ? "服务器未知错误" : throwable
                            .getMessage());
                });
    }
}

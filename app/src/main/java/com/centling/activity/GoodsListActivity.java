package com.centling.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.centling.BaseApplication;
import com.centling.R;
import com.centling.adapter.GoodsListAdapter;
import com.centling.databinding.ActivityGoodsListBinding;
import com.centling.entity.CatalogGoodsBean;
import com.centling.http.ApiManager;
import com.centling.popupwindow.GoodsTitlePopup;
import com.centling.util.DisplayUtil;
import com.centling.widget.DrawableTextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;

public class GoodsListActivity extends BaseActivity implements View.OnClickListener {
    private List<CatalogGoodsBean.GoodsCommonlistEntity> goodsData = new ArrayList<>();
    private DrawableTextView preClickView;
    private GoodsTitlePopup goodsTitlePopup;

    private int curPage = 1;

    private String orderKey = "1"; //分类字段
    private String order = "0";
    private String season = "";
    private String keyword = "";
    private String[] orders = new String[]{"0", "0", "0", "0"}; //0:降序 1:升序

    private ActivityGoodsListBinding mActivityGoodsListBinding;

    private void orderSelected(DrawableTextView view, int pos) {
        orderKey = String.valueOf(pos + 1);
        curPage = 1;
        if (preClickView == view) {
            orders[pos] = (TextUtils.equals(orders[pos],"0"))? "1" : "0";
            order = orders[pos];
            view.setSelected(!view.isSelected());
        } else {
            order = orders[pos];
            preClickView.setTextColor(ContextCompat.getColor(mContext, R.color.text_default));
            view.setTextColor(ContextCompat.getColor(mContext, R.color.tomato));
            preClickView = view;
        }
        //暂定新品默认order=1
        if (pos == 3) {
            order = "1";
        }
        mActivityGoodsListBinding.srGoodsList.autoRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityGoodsListBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_goods_list, null, false);
        setContentView(mActivityGoodsListBinding.getRoot());
        try {
            init();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void init() throws UnsupportedEncodingException {
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            mActivityGoodsListBinding.titleBarTitle.setText(intent.getStringExtra("title"));
        }
        mActivityGoodsListBinding.titleBarRightIcon.setOnClickListener(this);
        mActivityGoodsListBinding.titleBarLeftIcon.setOnClickListener(this);
        mActivityGoodsListBinding.tvGoodsListLatest.setOnClickListener(this);
        mActivityGoodsListBinding.tvGoodsListSales.setOnClickListener(this);
        mActivityGoodsListBinding.tvGoodsListPopularity.setOnClickListener(this);
        mActivityGoodsListBinding.tvGoodsListPrice.setOnClickListener(this);

        if (intent.hasExtra("season")) {
            season = intent.getStringExtra("season");
            mActivityGoodsListBinding.titleBarTitle.setOnClickListener(this);
        } else {
            mActivityGoodsListBinding.titleBarTitle.setCompoundDrawables(null, null, null, null);
        }
        if (intent.hasExtra("keyword")) {
            keyword = URLEncoder.encode(intent.getStringExtra("keyword"), "utf-8");
        }
        if (intent.hasExtra("discount")) {
            mActivityGoodsListBinding.llGoodsListOptions.setVisibility(View.GONE);
        }
        if (intent.hasExtra("search")) {
            keyword = URLEncoder.encode(intent.getStringExtra("keyword"), "utf-8");
            mActivityGoodsListBinding.titleBarTitle.setText("搜索结果");
        }

        mActivityGoodsListBinding.tvGoodsListSales.setSelected(true);
        mActivityGoodsListBinding.tvGoodsListPrice.setSelected(true);
        mActivityGoodsListBinding.tvGoodsListPopularity.setSelected(true);
        mActivityGoodsListBinding.tvGoodsListLatest.setSelected(true);

        preClickView = mActivityGoodsListBinding.tvGoodsListSales;
        mActivityGoodsListBinding.tvGoodsListSales.setTextColor(ContextCompat.getColor(mContext, R.color.tomato));

        mActivityGoodsListBinding.rvGoodsList.setLayoutManager(new GridLayoutManager(mContext, 1));
        mActivityGoodsListBinding.rvGoodsList.setAdapter(new GoodsListAdapter(mContext, goodsData));
        mActivityGoodsListBinding.rvGoodsList.setLoadDataListener(() -> getGoodsList(false));
        mActivityGoodsListBinding.srGoodsList.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                curPage = 1;
                getGoodsList(true);
            }
        });

        goodsTitlePopup = new GoodsTitlePopup(mContext, pos -> {
            String text = "商品列表";
            switch (pos) {
                case 0:
                    season = "spring";
                    text = "春季";
                    break;
                case 1:
                    season = "summer";
                    text = "夏季";
                    break;
                case 2:
                    season = "autumn";
                    text = "秋季";
                    break;
                case 3:
                    season = "winter";
                    text = "冬季";
                    break;
                case 4:
                    season = "all";
                    text = "全部";
                    break;
            }
            mActivityGoodsListBinding.titleBarTitle.setText(text);
            curPage = 1;
            mActivityGoodsListBinding.srGoodsList.autoRefresh(true);
        });

        mActivityGoodsListBinding.srGoodsList.postDelayed(() -> mActivityGoodsListBinding.srGoodsList.autoRefresh(true), 150);
    }

    private void getGoodsList(boolean isRefresh) {
        Observable<CatalogGoodsBean> api = ApiManager.getGoodsList(keyword, curPage, 4,
                season, orderKey, order).compose(bindLifecycle());
        if (getIntent().hasExtra("discount")) {
            api = ApiManager.getDiscountList(curPage, 4).compose(bindLifecycle());
        }
        api.subscribe(catalogGoodsBean -> {
            mActivityGoodsListBinding.srGoodsList.refreshComplete();
            if (isRefresh) {
                mActivityGoodsListBinding.rvGoodsList.scrollToPosition(0);
                goodsData.clear();
                goodsData.addAll(catalogGoodsBean.getGoods_commonlist());
                mActivityGoodsListBinding.rvGoodsList.setLoadDataListener(
                        () -> getGoodsList(false));
                mActivityGoodsListBinding.rvGoodsList.getAdapter().notifyDataSetChanged();
            } else {
                int size = goodsData.size();
                goodsData.addAll(catalogGoodsBean.getGoods_commonlist());
                mActivityGoodsListBinding.rvGoodsList.getAdapter().notifyItemRangeInserted(size,
                        catalogGoodsBean.getGoods_commonlist().size());
            }
            curPage++;
            mActivityGoodsListBinding.rvGoodsList.loadMoreComplete(!catalogGoodsBean.isHasmore());
        },throwable -> {
            mActivityGoodsListBinding.srGoodsList.refreshComplete();
            mActivityGoodsListBinding.rvGoodsList.loadMoreComplete(false);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_right_icon : {
                startActivity(new Intent(mContext, SearchActivity.class));
                finish();
                break;
            }
            case R.id.title_bar_left_icon :
                finish();
                break;
            case R.id.title_bar_title:
                if (getIntent().hasExtra("season")) {
                    goodsTitlePopup.showAsDropDown(mActivityGoodsListBinding.titleBar,
                            (BaseApplication.screenWidth - DisplayUtil.dp2px(150f)) / 2, 0);
                }
                break;
            case R.id.tv_goods_list_sales :
                orderSelected(mActivityGoodsListBinding.tvGoodsListSales, 0);
                break;
            case R.id.tv_goods_list_popularity :
                orderSelected(mActivityGoodsListBinding.tvGoodsListPopularity, 1);
                break;
            case R.id.tv_goods_list_price :
                orderSelected(mActivityGoodsListBinding.tvGoodsListPrice, 2);
                break;
            case R.id.tv_goods_list_latest :
                orderSelected(mActivityGoodsListBinding.tvGoodsListLatest, 3);
                break;
        }
    }
}

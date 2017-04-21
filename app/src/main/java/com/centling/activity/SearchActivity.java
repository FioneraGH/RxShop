package com.centling.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.entity.FriendBean;
import com.centling.http.ApiManager;
import com.centling.util.ImageUtil;
import com.centling.util.SPUtil;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = "/main/search")
public class SearchActivity
        extends TitleBarActivity
        implements View.OnClickListener {
    private EditText etSearch;
    private TextView tvAdd;
    private TextView tvSearch;
    private TextView tvClear;
    private LinearLayout llHistoryArea;
    private LinearLayout llHistory;
    private LinearLayout llHot;
    private LinearLayout llFriend;
    private ArrayList<String> hotList;
    private ArrayList<String> historyList;
    private Bundle bundle;
    private InputMethodManager inputManager;
    private String hisSearchString;
    private FriendBean mFriendBean;
    private boolean isSearchFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitleBarText("搜索", true);
        initView();
        initListener();
    }

    private void initView() {
        tvAdd = (TextView) findViewById(R.id.tv_search_friend_add);
        etSearch = (EditText) findViewById(R.id.et_search_input);
        tvSearch = (TextView) findViewById(R.id.tv_search_search);
        tvClear = (TextView) findViewById(R.id.tv_search_clear);
        llHistoryArea = (LinearLayout) findViewById(R.id.ll_search_history);
        llHistory = (LinearLayout) findViewById(R.id.ll_item_history);
        llFriend = (LinearLayout) findViewById(R.id.ll_search_friend);
        llHot = (LinearLayout) findViewById(R.id.ll_hot);
        isSearchFriend = getIntent().hasExtra("friend");
        if (isSearchFriend) {
            etSearch.setHint("请输入要搜索的手机号码");
            etSearch.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            etSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
            setTitleBarText("搜索好友");
        }
        hotList = new ArrayList<>();
        historyList = new ArrayList<>();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            etSearch.setText(bundle.getString("key"));
        }
//      重写dispatch的回车键会触发两次事件，所以用这种方法
        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) etSearch.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                if (isSearchFriend) {
                    searchFriend(etSearch.getText().toString());
                } else {
                    searchGoods(etSearch.getText().toString());
                }
                return true;
            }
            return false;
        });

        // 启动500ms后弹出软键盘
        etSearch.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                inputManager = (InputMethodManager) etSearch.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etSearch, 0);
            }
        }, 500);
    }

    private void initListener() {
        tvSearch.setOnClickListener(this);
        tvClear.setOnClickListener(this);
    }

    private void getSearchData() {
        hisSearchString = SPUtil.getString("HISTORY_SEARCH");
        historyList.clear();
        if (hisSearchString.contains("®")) {
            String values[] = hisSearchString.split("®");
            Collections.addAll(historyList, values);
        }
        setHistoryItem(historyList);
    }

    private void clearHistorySearch() {
        llHistoryArea.setVisibility(View.GONE);
        ShowToast.show("已清空搜索历史");
        SPUtil.setString("HISTORY_SEARCH", "");
        historyList.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_search:
                if (isSearchFriend) {
                    searchFriend(etSearch.getText().toString());
                } else {
                    searchGoods(etSearch.getText().toString());
                }
                break;
            case R.id.tv_search_clear:
                clearHistorySearch();
                break;
            default:
                break;
        }
    }

    private void setHistoryItem(final ArrayList<String> data) {
        llHistory.removeAllViews();
        for (int i = data.size() - 1; i >= 0; i--) {
            final int pos = i;
            View view = View.inflate(mContext, R.layout.search_history_item, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_search_item);
            textView.setText(data.get(i));
            view.setOnClickListener(v -> searchGoods(data.get(pos)));
            llHistory.addView(view);
        }

        if (data.size() == 0) {
            tvClear.setVisibility(View.GONE);
        } else {
            tvClear.setVisibility(View.VISIBLE);
        }
    }

    private void searchGoods(String key) {
        if (TextUtils.isEmpty(key.trim())) {
            ShowToast.show("请输入搜索关键字");
            return;
        }

        Intent intent = new Intent(mContext, GoodsListActivity.class);
        intent.putExtra("search", "yes");
        intent.putExtra("keyword", key);
        startActivity(intent);
    }

    private void searchFriend(String phone) {
        if (TextUtils.isEmpty(phone.trim())) {
            ShowToast.show("请输入搜索关键字");
            return;
        }
        if (phone.trim().length() < 11) {
            ShowToast.show("请输入完整的手机号");
            return;
        }
        showLoading("正在搜索");
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        ApiManager.searchFriend(params).compose(bindUntil(
                ActivityEvent.DESTROY)).subscribe(friendBean -> {
            dismissLoading();
            if (friendBean.getMemberlist().size() > 0) {
                llFriend.setVisibility(View.VISIBLE);
                ImageUtil.loadImage(friendBean.getMemberlist().get(0).getMember_avatar(),
                        (ImageView) findViewById(R.id.iv_search_friend),
                        R.drawable.iv_place_holder_1);
                ((TextView) findViewById(R.id.tv_search_friend_name)).setText(
                        friendBean.getMemberlist().get(0).getMember_nickname());
                ((TextView) findViewById(R.id.tv_search_friend_phone)).setText(phone);
                if (friendBean.getMemberlist().get(0).getFollowstate().equals("0")) {
                    tvAdd.setText("+好友");
                    tvAdd.setOnClickListener(
                            v -> addFriend(friendBean.getMemberlist().get(0).getMember_id()));
                } else if (friendBean.getMemberlist().get(0).getFollowstate().equals("1")) {
                    tvAdd.setText("已发送申请");
                    tvAdd.setOnClickListener(null);
                } else {
                    tvAdd.setOnClickListener(null);
                    tvAdd.setText("已添加");
                }
            } else {
                llFriend.setVisibility(View.INVISIBLE);
                ShowToast.show("没有搜索到" + phone);
            }
        }, throwable -> {
            dismissLoading();
            ShowToast.show("搜索失败");
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        bundle = intent.getExtras();
        if (bundle != null) {
            etSearch.setText(bundle.getString("key"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSearchData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getWindow().peekDecorView() != null) {
            InputMethodManager inputManger = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(getWindow().peekDecorView().getWindowToken(), 0);
        }
    }

    private void addFriend(String mid) {
        showLoading("正在添加好友");
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        ApiManager.addFriend(params).compose(bindUntil(
                ActivityEvent.DESTROY)).subscribe(json -> {
            dismissLoading();
            String followState;
            try {
                JSONObject jsonObject = new JSONObject(json);
                followState = jsonObject.getJSONObject("result").getString("followstate");
                if (followState.equals("1")) {
                    ShowToast.show("好友请求发送成功, 等待对方回应");
                    tvAdd.setText("已发送申请");
                    tvAdd.setOnClickListener(null);
                }
                if (followState.equals("2")) {
                    ShowToast.show("添加好友成功");
                    tvAdd.setText("已添加");
                    tvAdd.setOnClickListener(null);
                    SPUtil.setBoolean("refresh_friend", true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, throwable -> {
            dismissLoading();
            ShowToast.show("添加失败");
        });
    }
}

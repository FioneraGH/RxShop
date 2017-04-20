package com.centling.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.centling.R;
import com.centling.activity.LoginActivity;
import com.centling.activity.OrderActivity;
import com.centling.databinding.FragmentUserinfoBinding;
import com.centling.event.UserRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.SPUtil;
import com.centling.util.UserInfoUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;

public class UserFragment
        extends BaseFragment implements View.OnClickListener {

    private static Intent commonIntent = new Intent();

    private FragmentUserinfoBinding mFragmentUserinfoBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mFragmentUserinfoBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.fragment_userinfo, container, false);
        return mFragmentUserinfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateAvatar();
        updateUserName();

        mFragmentUserinfoBinding.ivUserInfoSetting.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoMyInfo.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoCardAccount.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoGetScore.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoScore.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoGoldAccount.setOnClickListener(this);

        mFragmentUserinfoBinding.tvUserInfoAllOrder.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoUnpayOrder.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoUnpostOrder.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoUnreceiveOrder.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoAfterService.setOnClickListener(this);

        mFragmentUserinfoBinding.tvUserInfoAllCollection.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoGoodsCollection.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoCustomization.setOnClickListener(this);
        mFragmentUserinfoBinding.tvUserInfoFootprint.setOnClickListener(this);

        mFragmentUserinfoBinding.llUserInfoMyMsg.setOnClickListener(this);
        mFragmentUserinfoBinding.llUserInfoManageAddress.setOnClickListener(this);
        mFragmentUserinfoBinding.llUserInfoCustomizationData.setOnClickListener(this);
        mFragmentUserinfoBinding.llUserInfoMyFriends.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUnreadMsg();
            acquireVip();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            getUnreadMsg();
            acquireVip();
        }
    }

    private void getUnreadMsg() {
        if (!UserInfoUtil.isLogin()) {
            return;
        }
        Map<String,String> params = new HashMap<>();
        ApiManager.trackUnreadMsg(params).compose(bindUntil(FragmentEvent.DESTROY_VIEW)).subscribe(s -> {
            JSONObject rawObject = new JSONObject(s);
            String count = rawObject.getJSONObject("result").getString("countnum");
            if (0 != Integer.parseInt(count)) {
                mFragmentUserinfoBinding.bllUserInfoMsg.showCirclePointBadge();
                mFragmentUserinfoBinding.tvUserInfoMyMsg.setText(String.format(Locale.CHINA, "有%s条未读消息", count));
            } else {
                mFragmentUserinfoBinding.bllUserInfoMsg.hiddenBadge();
                mFragmentUserinfoBinding.tvUserInfoMyMsg.setText("无未读消息");
            }
        },throwable -> {});
    }

    private void acquireVip() {
        if (!UserInfoUtil.isLogin()) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        ApiManager.isVip(params).compose(bindUntil(FragmentEvent.DESTROY_VIEW)).subscribe(
                s -> {
                    JSONObject rawObj = new JSONObject(s);
                    String isVip = rawObj.getJSONObject("result").getString("is_vip");
                    String level = rawObj.getJSONObject("result").getString("member_level");
                    String digit = rawObj.getJSONObject("result").getString("member_level_digit");
                    SPUtil.setString("member_level_digit", digit);
                    SPUtil.setString("isVip", isVip);
                    SPUtil.setString("member_level", level);

                    Log.d("loren", "user is_Vip:" + isVip + "  level:");
                    if (isVip.equals("0")) {
                        Log.d("loren", "level:" + UserInfoUtil.getLevel());
                        mFragmentUserinfoBinding.tvUserInfoMemberNormal.setText(
                                UserInfoUtil.getLevel());
                        mFragmentUserinfoBinding.tvUserInfoMemberNormal.setVisibility(View.VISIBLE);
                        mFragmentUserinfoBinding.tvUserInfoMemberLevel.setVisibility(
                                View.INVISIBLE);
                    } else {
                        mFragmentUserinfoBinding.tvUserInfoMemberLevel.setVisibility(View.VISIBLE);
                        mFragmentUserinfoBinding.tvUserInfoMemberNormal.setVisibility(
                                View.INVISIBLE);
                    }
                }, throwable -> {
                });
    }

    private void updateAvatar() {
        Glide.with(this).load(UserInfoUtil.getAvatar())
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mFragmentUserinfoBinding.ivUserInfoUserAvator.setImageDrawable(resource);
                    }
                });
        Glide.with(this).load(UserInfoUtil.getAvatar())
                .bitmapTransform(new BlurTransformation(mContext, 25),
                        new CropTransformation(mContext, 150, 75))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mFragmentUserinfoBinding.backgroundIv.setImageDrawable(resource);
                    }
                });
    }

    private void updateUserName() {
        mFragmentUserinfoBinding.tvUserInfoUsername.setText(TextUtils.isEmpty(UserInfoUtil.getKey()) ? "请登录" : TextUtils
                .isEmpty(UserInfoUtil.getNickName()) ? UserInfoUtil.getName() : UserInfoUtil
                .getNickName());
        if (UserInfoUtil.getVip().equals("0")) {
            Log.d("loren", "level:" + UserInfoUtil.getLevel());
            mFragmentUserinfoBinding.tvUserInfoMemberNormal.setText(UserInfoUtil.getLevel());
            mFragmentUserinfoBinding.tvUserInfoMemberNormal.setVisibility(View.VISIBLE);
            mFragmentUserinfoBinding.tvUserInfoMemberLevel.setVisibility(View.INVISIBLE);
        } else {
            mFragmentUserinfoBinding.tvUserInfoMemberLevel.setVisibility(View.VISIBLE);
            mFragmentUserinfoBinding.tvUserInfoMemberNormal.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(UserInfoUtil.getKey())) {
            startActivity(new Intent(mContext, LoginActivity.class));
            return;
        }
        onUserClick(v);
        onOrderClick(v);
        onCollectionClick(v);
        onMiscClick(v);
    }

    /**
     * 用户信息
     */
    void onUserClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_info_setting:
//                commonIntent.setClass(mContext, SettingsActivity.class);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_my_info:
//                commonIntent.setClass(mContext, MyInfoActivity.class);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_card_account:
//                commonIntent.setClass(mContext, PresentCardActivity.class);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_get_score:
//                commonIntent.setClass(mContext, ScoreExchangeActivity.class).putExtra(
//                        "is_add_score", true);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_score:
//                commonIntent.setClass(mContext, ScoreExchangeActivity.class).putExtra(
//                        "is_add_score", false);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_gold_account:
//                commonIntent.setClass(mContext, GoldsAccountActivity.class);
                startActivity(commonIntent);
                break;
        }
    }

    /**
     * 订单跳转
     */
    void onOrderClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_info_all_order:
                commonIntent.setClass(mContext, OrderActivity.class);
                commonIntent.putExtra("order_type", 0);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_unpay_order:
                commonIntent.setClass(mContext, OrderActivity.class);
                commonIntent.putExtra("order_type", 1);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_unpost_order:
                commonIntent.setClass(mContext, OrderActivity.class);
                commonIntent.putExtra("order_type", 2);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_unreceive_order:
                commonIntent.setClass(mContext, OrderActivity.class);
                commonIntent.putExtra("order_type", 3);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_after_service:
                commonIntent.setClass(mContext, OrderActivity.class);
                commonIntent.putExtra("order_type", 4);
                startActivity(commonIntent);
                break;
        }
    }

    /**
     * 收藏跳转
     */
    void onCollectionClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_info_all_collection:
//                commonIntent.setClass(mContext, CollectionActivity.class);
                commonIntent.putExtra("collection_type", 0);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_goods_collection:
//                commonIntent.setClass(mContext, CollectionActivity.class);
                commonIntent.putExtra("collection_type", 0);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_customization:
//                commonIntent.setClass(mContext, CollectionActivity.class);
                commonIntent.putExtra("collection_type", 1);
                startActivity(commonIntent);
                break;
            case R.id.tv_user_info_footprint:
//                commonIntent.setClass(mContext, CollectionActivity.class);
                commonIntent.putExtra("collection_type", 2);
                startActivity(commonIntent);
                break;
        }
    }

    /**
     * 量体数据/管理收获地址/体验包/我的兑换
     */
    void onMiscClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_info_my_msg:
//                commonIntent.setClass(mContext, MyMessageActivity.class);
                startActivity(commonIntent);
                break;
            case R.id.ll_user_info_manage_address:
//                commonIntent.setClass(mContext, AddressListActivity.class);
                startActivity(commonIntent);
                break;
            case R.id.ll_user_info_customization_data:
//                commonIntent.setClass(mContext, MeasureDataActivity.class);
                startActivity(commonIntent);
                break;
            case R.id.ll_user_info_my_friends:
//                commonIntent.setClass(mContext, MyFriendsActivity.class);
                startActivity(commonIntent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventUpdateAvatar(UserRelationEvent.UpdateAvatarEvent updateAvatarEvent){
        updateAvatar();
    }

    @Subscribe
    public void onEventUpdateName(UserRelationEvent.UpdateNameEvent updateNameEvent){
        updateUserName();
    }
}

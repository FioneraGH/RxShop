package com.centling.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.constant.RouterConstant;
import com.centling.event.CommonEvent;
import com.centling.event.UserRelationEvent;
import com.centling.util.ImageUtil;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;

import org.greenrobot.eventbus.EventBus;

@Route(path = RouterConstant.User.SETTING)
public class SettingsActivity
        extends TitleBarActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitleBarText("设置");

        findViewById(R.id.tv_setting_account_settings).setOnClickListener(this);
        findViewById(R.id.tv_setting_clear_image_cache).setOnClickListener(this);
        findViewById(R.id.tv_setting_user_proposal).setOnClickListener(this);
        findViewById(R.id.tv_setting_feed_back).setOnClickListener(this);
        findViewById(R.id.tv_setting_about_us).setOnClickListener(this);
        findViewById(R.id.tv_setting_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting_account_settings:
//                    startActivity(new Intent(mContext, AccountSettingsActivity.class));
                break;
            case R.id.tv_setting_clear_image_cache:
                ShowDialog.showSelectDialog(mContext, "清除图片缓存", "执行此操作将会清除您设备上的图片缓存，确定要清除吗？", "",
                        v1 -> ImageUtil.clearCache());
                break;
            case R.id.tv_setting_feed_back:
//                    startActivity(new Intent(mContext, FeedbackActivity.class));
                break;
            case R.id.tv_setting_user_proposal:
//                    startActivity(new Intent(mContext, UserProposalActivity.class));
                break;
            case R.id.tv_setting_about_us:
//                    startActivity(new Intent(mContext, AboutUsActivity.class));
                break;
            case R.id.tv_setting_logout:
                ShowDialog.showSelectDialog(mContext, "退出登录", "您确定要退出登录吗？", "", v1 -> {
                    ShowToast.show("已退出登录");
                    unBindAlias();
                    UserInfoUtil.clearAll();
                    EventBus.getDefault().post(new UserRelationEvent.UpdateAvatarEvent());
                    EventBus.getDefault().post(new UserRelationEvent.UpdateNameEvent());
                    EventBus.getDefault().post(new CommonEvent.ChangeTabEvent(0));
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                });
                break;
        }
    }

    /*解绑个推别名*/
    private void unBindAlias() {
//        PushManager.getInstance().unBindAlias(mContext, UserInfoUtil.getId(), true);
    }
}

package com.centling.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.centling.R;
import com.centling.databinding.ActivityLoginBinding;
import com.centling.entity.LoginBean;
import com.centling.event.UserRelationEvent;
import com.centling.http.ApiManager;
import com.centling.util.L;
import com.centling.util.MD5;
import com.centling.util.SPUtil;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity
        extends TitleBarActivity
        implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login :
                if (!isThird) {
                    login();
                } else {
                    loginWithThird();
                }
                break;
            case R.id.tv_login_register :
                if (!isThird) {
//                    startActivityForResult(Intent(mContext, RegisterActivity.java), 100)
                } else {
//                    Intent intent = new Intent(mContext, RegisterActivity.java);
//                    intent.putExtra("openid", openId);
//                    intent.putExtra("type", type);
//                    intent.putExtra("nick_name", nickName);
//                    startActivityForResult(intent, 100);
                }
                break;
            case R.id.tv_login_forget :
//                startActivity(Intent(mContext, ResetPasswordActivity.java))；
                break;
        }
    }

    private LoginBean mLoginBean;

    private UMShareAPI mShareAPI;
    private SHARE_MEDIA platform;
    private String openId;
    private String nickName;
    private String type;
    private String thirdTitleStr;
    private Boolean isThird = false;

    private ActivityLoginBinding mActivityLoginBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(),
                R.layout.activity_login, null, false);
        setContentView(mActivityLoginBinding.getRoot());
        setTitleBarText("登录");

        mActivityLoginBinding.tvLoginProposal.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mActivityLoginBinding.tvLoginProposal.getPaint().setAntiAlias(true);
        mActivityLoginBinding.tvLogin.setOnClickListener(this);
        mActivityLoginBinding.tvLoginRegister.setOnClickListener(this);
        mActivityLoginBinding.tvLoginForget.setOnClickListener(this);
        mActivityLoginBinding.etLoginPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
            }
            return true;
        });

        mShareAPI = UMShareAPI.get(this);
        initUmengListener();

        mTitleBar.getToolbar().setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        if (isThird) {
            backFromThird();
        } else {
            super.onBackPressed();
        }
    }

    private void backFromThird() {
        finish();
        startActivity(new Intent(mContext, LoginActivity.class));
    }

    private void initUmengListener() {
        mActivityLoginBinding.wxImage.setOnClickListener( v -> {
            platform = SHARE_MEDIA.WEIXIN;
            type = "weixin";
            thirdTitleStr = "微信账号绑定";
            Log.d("loren+++", "  login  " + platform);
            mShareAPI.doOauthVerify(this, platform, umAuthListener);
        });
        mActivityLoginBinding.qqImage.setOnClickListener( v -> {
            platform = SHARE_MEDIA.QQ;
            type = "qq";
            thirdTitleStr = "QQ账号绑定";
            Log.d("loren+++", "  login  " + platform);
            mShareAPI.doOauthVerify(this, platform, umAuthListener);
        });
        mActivityLoginBinding.sinaImage.setOnClickListener (v -> {
            platform = SHARE_MEDIA.SINA;
            type = "sina";
            thirdTitleStr = "新浪账号绑定";
            Log.d("loren+++", "  login  " + platform);
            mShareAPI.doOauthVerify(this, platform, umAuthListener);
        });
    }

    /*授权回调*/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.d("loren++", "授权成功");
            showLoading();
            if (data.get("openid") != null) {
                openId = data.get("openid"); // QQ && WX
            } else if (data.get("uid") != null) {
                openId = data.get("uid"); // Sina
            }
            Log.d("loren+++", "openid++++++++++" + openId);
            mShareAPI.getPlatformInfo(mActivity, platform, umAuthMsgListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ShowToast.show("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.d("loren++", "授权取消");
        }
    };

    /*获取用户信息回调*/
    private UMAuthListener umAuthMsgListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.d("loren++", "获取成功");
            if (data.get("screen_name") != null) {
                // QQ && Sina
                nickName = data.get("screen_name");
                Log.d("loren++", "QQ && Sina用户信息：：：" + nickName);
            } else if (data.get("nickname") != null) {
                // weixin
                nickName = data.get("nickname");
                Log.d("loren++", "微信用户信息：：：" + nickName);
            }
            Log.d("loren++", "三方登录用户信息" + nickName);
            //判断openid是否存在
            isExistOpenIdRequest();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissLoading();
            ShowToast.show("登录失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissLoading();
            Log.d("loren++", "获取取消");
        }
    };

    //判断openid是否存在
    private void isExistOpenIdRequest() {
        Map<String,String> params = new HashMap<>();
        params.put("user_id", openId);
        params.put("type", type);
        params.put("client", "android");
        ApiManager.isThirdLogin(params).compose(bindUntil(
                ActivityEvent.DESTROY)).subscribe(thirdLoginBean -> {
            mLoginBean = thirdLoginBean;
            Log.d("loren", "登录成功");
            saveLocalData();
            setResult(101);
            finish();
        }, throwable -> {
            dismissLoading();
            ShowToast.show(throwable.getMessage());
            if (throwable.getMessage() != null && throwable.getMessage().contains("未注册")) {
                //未绑定
                setTitleBarText(thirdTitleStr);
                isThird = true;
            }
        });
    }

    private void login() {
        if (TextUtils.isEmpty(mActivityLoginBinding.etLoginUsername.getText().toString())) {
            ShowToast.show("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(mActivityLoginBinding.etLoginPassword.getText().toString())) {
            ShowToast.show("请输入密码");
            return;
        }

        showLoading();
        Map<String,String> params = new HashMap<>();
        params.put("uname_mobile", mActivityLoginBinding.etLoginUsername.getText().toString());
        params.put("password", mActivityLoginBinding.etLoginPassword.getText().toString());
        ApiManager.login(params).compose(bindUntil(
                ActivityEvent.DESTROY)).subscribe(loginBean -> {
            dismissLoading();
            mLoginBean = loginBean;
            Log.d("loren", "登录成功");
            saveLocalData();
            setResult(100);
            bindPushAlias();
            finish();
        },throwable -> {
            dismissLoading();
            if (TextUtils.isEmpty(throwable.getMessage())) {
                ShowToast.show("登录失败");
            } else {
                ShowToast.show(throwable.getMessage());
            }
        });
    }

    private void loginWithThird() {
        if (TextUtils.isEmpty(mActivityLoginBinding.etLoginUsername.getText().toString())) {
            ShowToast.show("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(mActivityLoginBinding.etLoginPassword.getText().toString())) {
            ShowToast.show("请输入密码");
            return;
        }

        showLoading();
        Map<String,String> params = new HashMap<>();
        params.put("uname_mobile", mActivityLoginBinding.etLoginUsername.getText().toString());
        params.put("password", mActivityLoginBinding.etLoginPassword.getText().toString());
        ApiManager.login(params).subscribe(loginBean -> {
            dismissLoading();
            mLoginBean = loginBean;
            Log.d("loren", "登录成功");
            saveLocalData();
            if (!TextUtils.isEmpty(openId)) {
                bundleRequest();
            }
            setResult(100);
            bindPushAlias();
            finish();
        },throwable -> {
            dismissLoading();
            if (TextUtils.isEmpty(throwable.getMessage())) {
                ShowToast.show("登录失败");
            } else {
                ShowToast.show(throwable.getMessage());
            }
        });
    }

    /*绑定请求*/
    private void bundleRequest() {
        Map<String,String> params = new HashMap<>();
        params.put("type", type);
        params.put("nick_name", nickName);
        params.put("user_id", openId);
        ApiManager.bindThirdLogin(params).subscribe(empty -> ShowToast.show("绑定成功"),
                throwable -> L.d("绑定失败"));
    }

    private void bindPushAlias() {
        if (!TextUtils.isEmpty(UserInfoUtil.getId())) {
//            PushManager.getInstance().bindAlias(mContext, UserInfoUtil.getId());
            Log.d("loren", "member_id=" + UserInfoUtil.getId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            finish();
        }
        if (requestCode == 100 && resultCode == 101) {
            Log.d("Loren++++", "注册页返回101！！！！！！！");
            String userName = data.getStringExtra("username");
            String passWord = data.getStringExtra("password");
            //注册后直接登录
            afterRegisterLogin(userName, passWord);
        }
        if (requestCode == 100 && resultCode == 102) {
            Log.d("Loren++++", "注册页返回102！！！！！！！");
        }
    }

    private void afterRegisterLogin(String userName, String passWord) {
        mActivityLoginBinding.etLoginUsername.setText(userName);
        mActivityLoginBinding.etLoginPassword.setText(passWord);
        login();
    }

    private void saveLocalData() {
        SPUtil.setString("member_level", mLoginBean.getMember_level());
        SPUtil.setString("realName", mLoginBean.getMember_truename());
        SPUtil.setString("userName", mLoginBean.getUsername());
        SPUtil.setString("userNickName", mLoginBean.getMember_nickname());
        SPUtil.setString("password", MD5.getMessageDigest(
                mActivityLoginBinding.etLoginPassword.getText().toString().getBytes()));
        SPUtil.setString("userKey", mLoginBean.getKey());
        SPUtil.setString("userMobile", mLoginBean.getMobile());
        SPUtil.setString("userId", mLoginBean.getMember_id());
        SPUtil.setString("userSex", mLoginBean.getMember_sex());
        SPUtil.setString("userHeight", mLoginBean.getHeight().contains(".") ? mLoginBean.getHeight()
                .substring(0, mLoginBean.getHeight().indexOf(".")) : mLoginBean.getHeight());
        SPUtil.setString("userWeight", (mLoginBean.getWeight().contains(".")) ? mLoginBean.getWeight()
                .substring(0, mLoginBean.getWeight().indexOf(".")) : mLoginBean.getWeight());
        SPUtil.setString("userShoulder",
                (mLoginBean.getWeight().contains(".")) ? mLoginBean.getWeight()
                        .substring(0, mLoginBean.getWeight().indexOf(".")) : mLoginBean.getWeight());
        SPUtil.setString("userBirth", mLoginBean.getMember_birthday());
        SPUtil.setString("isVip", mLoginBean.getIs_vip());
        SPUtil.setString("inviteCode", mLoginBean.getInvite_code());
        SPUtil.setString("userAvatar", mLoginBean.getMember_avatar());
        SPUtil.setString("member_qq", mLoginBean.getMember_qq());
        SPUtil.setString("member_level_digit", mLoginBean.getMember_level_digit());
        SPUtil.setBoolean("update_cart", true);
        EventBus.getDefault().post(new UserRelationEvent.UpdateAvatarEvent());
        EventBus.getDefault().post(new UserRelationEvent.UpdateNameEvent());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

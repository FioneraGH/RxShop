package com.centling.activity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.centling.R;
import com.centling.constant.RouterConstant;
import com.centling.databinding.ActivityMyInfoBinding;
import com.centling.event.UserRelationEvent;
import com.centling.http.ApiManager;
import com.centling.mvp.contract.AvatarDetailContract;
import com.centling.mvp.presenter.AvatarDetailPresenterImpl;
import com.centling.popupwindow.ChangeAvatarPopup;
import com.centling.popupwindow.ChangeGenderPopup;
import com.centling.popupwindow.ChangeNickNamePopup;
import com.centling.popupwindow.ChangeQqPopupWindow;
import com.centling.popupwindow.ChangeRealNamePopup;
import com.centling.util.ImageUtil;
import com.centling.util.SPUtil;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.centling.util.UserInfoUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

@Route(path = RouterConstant.User.INFO)
public class MyInfoActivity
        extends TitleBarActivity
        implements View.OnClickListener, ChangeAvatarPopup.OnDialogListener, ChangeNickNamePopup
        .OnDialogListener, ChangeGenderPopup.OnDialogListener, ChangeRealNamePopup
        .OnDialogListener, ChangeQqPopupWindow.OnDialogListener, AvatarDetailContract.View {

    public static final int RESULT_ALTER = 100;

    private static final int ASK_FOR_HEIGHT = 2000;
    private static final int ASK_FOR_WEIGHT = 2001;
    private static final int ASK_FOR_BIRTH = 2002;

    private boolean isEdited;
    private String nickname = "";
    private String realname = "";
    private String gender = "";
    private String birthday = "";
    private String height = "";
    private String weight = "";
    private String QQnum = "";
    private ChangeRealNamePopup changeRealNamePopup;
    private ChangeAvatarPopup changeAvatarPopup;
    private ChangeNickNamePopup changeNickNamePopup;
    private ChangeGenderPopup changeGenderPopup;
    private ChangeQqPopupWindow changeQqPopupWindow;

    public ActivityMyInfoBinding mActivityMyInfoBinding;
    private AvatarDetailContract.Presenter mPresenter;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMyInfoBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_my_info, null, false);
        setContentView(mActivityMyInfoBinding.getRoot());
        setTitleBarText("个人资料");

        rxPermissions = new RxPermissions(this);

        mPresenter = new AvatarDetailPresenterImpl(this);
        mPresenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unInit();
    }

    @Override
    public void setPresenter(AvatarDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttachPresenter() {
        mTitleBar.getToolbar().setNavigationOnClickListener(v -> checkEdited());
        mActivityMyInfoBinding.tvMyInfoSave.setOnClickListener(this);
        mActivityMyInfoBinding.llMyInfoAvator.setOnClickListener(this);
        mActivityMyInfoBinding.tvMyInfoUsername.setOnClickListener(this);
        mActivityMyInfoBinding.tvMyInfoRealname.setOnClickListener(this);
        mActivityMyInfoBinding.tvMyInfoGender.setOnClickListener(this);
        mActivityMyInfoBinding.bllMyInfoIfQq.setOnClickListener(this);
        mActivityMyInfoBinding.tvMyInfoTall.setOnClickListener(this);
        mActivityMyInfoBinding.tvMyInfoWeight.setOnClickListener(this);
        mActivityMyInfoBinding.tvMyInfoBirth.setOnClickListener(this);

        changeRealNamePopup = new ChangeRealNamePopup(mContext, this);
        changeAvatarPopup = new ChangeAvatarPopup(mContext, this);
        changeNickNamePopup = new ChangeNickNamePopup(mContext, this);
        changeGenderPopup = new ChangeGenderPopup(mContext, this);
        changeQqPopupWindow = new ChangeQqPopupWindow(mContext, this);

        nickname = UserInfoUtil.getNickName();
        realname = UserInfoUtil.getRealName();
        gender = UserInfoUtil.getSex();
        height = UserInfoUtil.getHeight();
        weight = UserInfoUtil.getWeight();
        birthday = UserInfoUtil.getBirth();
        QQnum = UserInfoUtil.getQQ();

        isEmpty();

        if (TextUtils.isEmpty(birthday)) {
            mActivityMyInfoBinding.tvMyInfoBirth.setTextColor(
                    ContextCompat.getColor(mContext, R.color.black));
        } else {
            mActivityMyInfoBinding.tvMyInfoBirth.setTextColor(
                    ContextCompat.getColor(mContext, R.color.text_default));
        }

        updateAvatar();

        checkFillup();
    }

    @Override
    public void onDetachPresenter() {

    }

    @Override
    public void updateAvatar(Bitmap avatar) {
        mActivityMyInfoBinding.ivUserInfoUserAvatar.setImageBitmap(avatar);
    }

    private void isEmpty() {
        mActivityMyInfoBinding.tvMyInfoUsername.setText(
                TextUtils.isEmpty(nickname) ? "请填写" : nickname);
        mActivityMyInfoBinding.tvMyInfoRealname.setText(
                TextUtils.isEmpty(realname) ? "可不填" : realname);
        mActivityMyInfoBinding.tvMyInfoInvite.setText(UserInfoUtil.getInviteCode());
        mActivityMyInfoBinding.tvMyInfoGender.setText(
                TextUtils.isEmpty(gender) ? "请填写" : "0".equals(gender) ? "男" : "女");
        mActivityMyInfoBinding.tvMyInfoTall.setText(
                TextUtils.isEmpty(height) || "0".equals(height) ? "请填写" : height + "cm");
        mActivityMyInfoBinding.tvMyInfoWeight.setText(
                TextUtils.isEmpty(weight) || "0".equals(weight) ? "请填写" : weight + "kg");
        mActivityMyInfoBinding.tvMyInfoBirth.setText(
                TextUtils.isEmpty(birthday) ? "请填写" : birthday);
        mActivityMyInfoBinding.tvMyInfoQq.setText(TextUtils.isEmpty(QQnum) ? "可不填" : QQnum);
    }

    private void updateAvatar() {
        ImageUtil.loadAvatarImage(UserInfoUtil.getAvatar(),
                mActivityMyInfoBinding.ivUserInfoUserAvatar, R.drawable.iv_avatar_default);
    }

    private void checkFillup() {
        if (TextUtils.isEmpty(nickname)) {
            mActivityMyInfoBinding.bllMyInfoIfNickname.showTextBadge("");
        } else {
            mActivityMyInfoBinding.bllMyInfoIfNickname.hiddenBadge();
        }
        if (TextUtils.isEmpty(gender)) {
            mActivityMyInfoBinding.bllMyInfoIfGender.showTextBadge("");
        } else {
            mActivityMyInfoBinding.bllMyInfoIfGender.hiddenBadge();
        }
        if (TextUtils.isEmpty(height) || "0".equals(height)) {
            mActivityMyInfoBinding.bllMyInfoIfTall.showTextBadge("");
        } else {
            mActivityMyInfoBinding.bllMyInfoIfTall.hiddenBadge();
        }
        if (TextUtils.isEmpty(weight) || "0".equals(weight)) {
            mActivityMyInfoBinding.bllMyInfoIfWeight.showTextBadge("");
        } else {
            mActivityMyInfoBinding.bllMyInfoIfWeight.hiddenBadge();
        }
        if (TextUtils.isEmpty(birthday)) {
            mActivityMyInfoBinding.bllMyInfoIfBirth.showTextBadge("");
        } else {
            mActivityMyInfoBinding.bllMyInfoIfBirth.hiddenBadge();
        }
    }

    @Override
    public void onTakePhoto() {
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
            if (granted) {
                mPresenter.tryToTakePhoto();
            } else {
                ShowToast.show("请授予摄像头权限");
            }
        });
    }

    @Override
    public void onChoosePhoto() {
        mPresenter.tryToSelectPhoto();
    }

    @Override
    public void onSelectMan() {
        gender = "0";
        isEdited = true;
        mActivityMyInfoBinding.tvMyInfoGender.setText("男");
        checkFillup();
    }

    @Override
    public void onSelectWoman() {
        gender = "1";
        isEdited = true;
        mActivityMyInfoBinding.tvMyInfoGender.setText("女");
        checkFillup();
    }

    @Override
    public void onConfirmOp(String s) {
        nickname = s;
        isEdited = true;
        if (!TextUtils.isEmpty(nickname)) {
            mActivityMyInfoBinding.tvMyInfoUsername.setText(nickname);
        }
        checkFillup();
        isEmpty();
    }

    @Override
    public void onConfirmRealOp(String s) {
        realname = s;
        isEdited = true;
        if (!TextUtils.isEmpty(realname)) {
            mActivityMyInfoBinding.tvMyInfoRealname.setText(realname);
        }
        isEmpty();
    }

    @Override
    public void onConfirmQQ(String s) {
        QQnum = s;
        isEdited = true;
        if (!TextUtils.isEmpty(QQnum)) {
            mActivityMyInfoBinding.tvMyInfoQq.setText(QQnum);
        }
        isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_ALTER == resultCode) {
            switch (requestCode) {
                case ASK_FOR_HEIGHT:
                    height = data.getStringExtra("data");
                    isEdited = true;
                    mActivityMyInfoBinding.tvMyInfoTall.setText(
                            String.format(Locale.CHINA, "%scm", height));
                    checkFillup();
                    break;
                case ASK_FOR_WEIGHT:
                    weight = data.getStringExtra("data");
                    isEdited = true;
                    mActivityMyInfoBinding.tvMyInfoWeight.setText(
                            String.format(Locale.CHINA, "%skg", weight));
                    checkFillup();
                    break;
                case ASK_FOR_BIRTH:
                    birthday = data.getStringExtra("data");
                    isEdited = true;
                    mActivityMyInfoBinding.tvMyInfoBirth.setText(birthday);
                    checkFillup();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_my_info_save:
                if (isEdited) {
                    saveInfos();
                } else {
                    ShowToast.show("未进行更改");
                }
                break;
            case R.id.ll_my_info_avator:
                showChangeInfoPop(changeAvatarPopup);
                break;
            case R.id.tv_my_info_username:
                showChangeInfoPop(changeNickNamePopup);
                break;
            case R.id.tv_my_info_realname:
                showChangeInfoPop(changeRealNamePopup);
                break;
            case R.id.tv_my_info_gender:
                showChangeInfoPop(changeGenderPopup);
                break;
            case R.id.bll_my_info_if_qq:
                showChangeInfoPop(changeQqPopupWindow);
                break;
            case R.id.tv_my_info_tall:
                if (height.contains(".")) {
                    height = height.substring(0, height.indexOf("."));
                }
//                    startActivityForResult(new Intent(mContext, UserAlterActivity.class)
//                                    .putExtra(MeasureType.TYPE_KEY,
//                                            MeasureType.HEIGHT)
//                                    .putExtra(MeasureType.INIT_KEY, height),
//                            ASK_FOR_HEIGHT);
//                    overridePendingTransition(R.anim.anim_activity_fade_in,
//                            R.anim.anim_activity_exit);
                break;
            case R.id.tv_my_info_weight:
                if (weight.contains(".")) {
                    weight = weight.substring(0, weight.indexOf("."));
                }
//                    startActivityForResult(new Intent(mContext, UserAlterActivity.class)
//                                    .putExtra(MeasureType.TYPE_KEY,
//                                            MeasureType.WEIGHT)
//                                    .putExtra(MeasureType.INIT_KEY, weight),
//                            ASK_FOR_WEIGHT);
//                    overridePendingTransition(R.anim.anim_activity_fade_in,
//                            R.anim.anim_activity_exit);
                break;
            case R.id.tv_my_info_birth:
                if (!TextUtils.isEmpty(UserInfoUtil.getBirth())) {
                    ShowToast.show("生日已经设定不可更改");
                } else {
//                        startActivityForResult(new Intent(mContext, UserAlterActivity.class)
//                                        .putExtra(MeasureType.TYPE_KEY,
//                                                MeasureType.BIRTHDAY)
//                                        .putExtra(MeasureType.INIT_KEY, birthday),
//                                ASK_FOR_BIRTH);
//                        overridePendingTransition(R.anim.anim_activity_fade_in,
//                                R.anim.anim_activity_exit);
                }
                break;
        }
    }

    private void checkEdited() {
        if (isEdited) {
            ShowDialog.showSelectDialog(mContext, "忘记保存资料？", "您还没有保存修改，是否放弃？", "", v -> finish());
        } else {
            finish();
        }
    }

    /**
     * 保存个人信息
     */
    private void saveInfos() {
        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(
                height) || TextUtils.isEmpty(weight)/* || TextUtils
                .isEmpty(shoulder)*/ || TextUtils.isEmpty(gender)) {
            ShowToast.show("您的个人信息不完整,请填写");
            Timber.d(nickname + " " + birthday + " " + height + " " + weight + gender);
            return;
        }
        showLoading("正在同步资料");
        Map<String, String> params = new HashMap<>();
        params.put("is_personal_create", "1");
        if (!TextUtils.isEmpty(realname) && !TextUtils.isEmpty(QQnum)) {
            params.put("complete_flag", "1");
        } else {
            params.put("complete_flag", "0");
        }
        params.put("member_nickname", nickname);
        params.put("member_truename", realname);
        params.put("member_birthday", birthday);
        params.put("member_height", height);
        params.put("member_weight", weight);
        params.put("shoulder_breadth", "0");
        params.put("member_sex", gender);
        params.put("member_qq", QQnum);

        ApiManager.changeUserInfo(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(o -> {
            dismissLoading();
            ShowToast.show("保存成功");
            isEdited = false;
            updateLocalData();
            finish();
        }, throwable -> {
            dismissLoading();
            ShowToast.show(throwable.getMessage());
        });
    }

    private void updateLocalData() {
        SPUtil.setString("realName", realname);
        SPUtil.setString("userNickName", nickname);
        SPUtil.setString("userSex", gender);
        SPUtil.setString("userHeight", height);
        SPUtil.setString("userWeight", weight);
        SPUtil.setString("userBirth", birthday);
        SPUtil.setString("member_qq", QQnum);
        EventBus.getDefault().post(new UserRelationEvent.UpdateNameEvent());
    }

    /**
     * 更换头像
     */
    private void showChangeInfoPop(PopupWindow pop) {
        if (pop instanceof ChangeNickNamePopup) {
            ((ChangeNickNamePopup) pop).setNickname(nickname);
        }
        if (pop instanceof ChangeRealNamePopup) {
            ((ChangeRealNamePopup) pop).setRealname(realname);
        }
        if (pop instanceof ChangeQqPopupWindow) {
            ((ChangeQqPopupWindow) pop).setQQ(QQnum);
        }
        pop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onBackPressed() {
        checkEdited();
    }
}

package com.centling.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.BaseApplication;
import com.centling.R;
import com.centling.constant.RouterConstant;
import com.centling.databinding.ActivityGoldsAccountBinding;
import com.centling.event.CommonEvent;
import com.centling.http.ApiManager;
import com.centling.util.DisplayUtil;
import com.centling.util.SPUtil;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Route(path = RouterConstant.User.GOLDS)
public class GoldsAccountActivity
        extends TitleBarActivity
        implements View.OnClickListener {

    private boolean isExplanation;
    private ObjectAnimator animator1;
    private ObjectAnimator animator2;
    private boolean isExpand = false;
    private boolean isAnimRunning = false;
    private ObjectAnimator animIv;
    private ValueAnimator animHeight;

    private ActivityGoldsAccountBinding mActivityGoldsAccountBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityGoldsAccountBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_golds_account, null, false);
        setContentView(mActivityGoldsAccountBinding.getRoot());
        setTitleBarText("金币账户");

        mTitleBar.getToolbar().setNavigationOnClickListener(v -> leftClick());
        mTitleBar.getToolbar().inflateMenu(R.menu.menu_golds_account);
        mTitleBar.getToolbar().setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_golds_help) {
                rightClick();
                return true;
            }
            return false;
        });

        mActivityGoldsAccountBinding.tvGoldsCardSpend.setOnClickListener(this);
        mActivityGoldsAccountBinding.tvGoldsCardRecharge.setOnClickListener(this);
        mActivityGoldsAccountBinding.tvGoldsCardSheet.setOnClickListener(this);
        mActivityGoldsAccountBinding.rlGoldTypeTitle.setOnClickListener(this);

        init();
    }

    private void init() {
        animator1 = new ObjectAnimator();
        animator2 = new ObjectAnimator();
        animator1.setDuration(300);
        animator2.setDuration(300);
        animator1.setTarget(mActivityGoldsAccountBinding.llGoldsAccountRoot);
        animator2.setTarget(mActivityGoldsAccountBinding.llGoldsAccountExplanation);
        animator1.addUpdateListener(animation -> {
            assert animator1.getTarget() != null;
            ((View) animator1.getTarget()).setX((float) animation.getAnimatedValue());
        });
        animator2.addUpdateListener(animation -> {
            assert animator2.getTarget() != null;
            ((View) animator2.getTarget()).setX((float) animation.getAnimatedValue());
        });

        animIv = new ObjectAnimator();
        animIv.setDuration(300);
        animIv.setPropertyName("rotation");
        animIv.setTarget(mActivityGoldsAccountBinding.ivGoldExpand);

        animHeight = new ValueAnimator();
        animHeight.setDuration(300);
        ViewGroup.LayoutParams lp = mActivityGoldsAccountBinding.llGoldDetail.getLayoutParams();
        animHeight.addUpdateListener(animation -> {
            lp.height = (int) animation.getAnimatedValue();
            mActivityGoldsAccountBinding.llGoldDetail.setLayoutParams(lp);
        });
        lp.height = 0;
        mActivityGoldsAccountBinding.llGoldDetail.setLayoutParams(lp);
    }

    @Override
    public void onStart() {
        super.onStart();
        getGoldBalance();
    }

    private void getGoldBalance() {
        showLoading("正在请求数据");
        Map<String, String> params = new HashMap<>();
        ApiManager.goldBalance(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(s -> {
            dismissLoading();
            JSONObject rawObj = new JSONObject(s);
            JSONObject resultObj = rawObj.getJSONObject("result");
            BigDecimal db = new BigDecimal(resultObj.getString("total_available"));
            mActivityGoldsAccountBinding.tvGoldsBalance.setText(
                    String.format(Locale.CHINA, "￥%s", db.toPlainString()));
            mActivityGoldsAccountBinding.tvGoldDescription.setText(resultObj.getString("rules_desc")
                    .replace("\\n", "\n").replace("\\t", "\t"));
            SPUtil.setString("userGolds", resultObj.getString("total_available"));
            mActivityGoldsAccountBinding.tvCoinsBuy.setText(
                    new DecimalFormat("0.00").format(resultObj.getDouble("coins_buy")));
            mActivityGoldsAccountBinding.tvCoinsDonate.setText(
                    new DecimalFormat("0.00").format(resultObj.getDouble("coins_donate")));
            mActivityGoldsAccountBinding.tvCoinsEarn.setText(resultObj.getString("coins_earn"));
            mActivityGoldsAccountBinding.tvCoinsEarnTotal.setText(
                    resultObj.getString("coins_earn_total"));
        }, throwable -> {
            dismissLoading();
            ShowToast.show("获取金币失败！");
        });
    }

    private void leftClick() {
        if (isExplanation) {
            setTitleBarText("金币账户");
            isExplanation = false;
            mTitleBar.getToolbar().inflateMenu(R.menu.menu_golds_account);
            mTitleBar.getToolbar().setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_golds_help) {
                    rightClick();
                    return true;
                }
                return false;
            });
            animator1.setFloatValues(-BaseApplication.screenWidth, 0);
            animator2.setFloatValues(0, BaseApplication.screenWidth);
            animator1.start();
            animator2.start();
        } else {
            finish();
        }
    }

    private void rightClick() {
        if (!isExplanation) {
            setTitleBarText("金币说明");
            isExplanation = true;
            mTitleBar.getToolbar().getMenu().clear();
            animator1.setFloatValues(0, -BaseApplication.screenWidth);
            animator2.setFloatValues(BaseApplication.screenWidth, 0);
            animator1.start();
            animator2.start();
        }
    }

    @Override
    public void onBackPressed() {
        leftClick();
    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.tv_golds_card_spend:
                EventBus.getDefault().post(new CommonEvent.ChangeTabEvent(0));
                startActivity(new Intent(mContext, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                break;
            case R.id.tv_golds_card_recharge:
                ARouter.getInstance().build(RouterConstant.User.GOLDS_RECHARGE).navigation();
                break;
            case R.id.tv_golds_card_sheet:
                ARouter.getInstance().build(RouterConstant.User.GOLDS_RECORD).navigation();
                break;
            case R.id.rl_gold_type_title:
                startGoldDetailAnim();
                break;
        }
        v.setEnabled(true);
    }

    private void startGoldDetailAnim() {
        if (isAnimRunning) {
            return;
        }
        mActivityGoldsAccountBinding.llGoldDetail.setPivotY(0);
        if (isExpand) {
            animIv.setFloatValues(-180f, -360f);
            animIv.start();
            animHeight.setIntValues(DisplayUtil.dp2px(165), 0);
            animHeight.start();
            mActivityGoldsAccountBinding.llGoldDetail.setScaleY(1);
            mActivityGoldsAccountBinding.llGoldDetail.animate().scaleY(0).setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnimRunning = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isExpand = false;
                            isAnimRunning = false;
                        }
                    }).start();
        } else {
            animIv.setFloatValues(0f, 180f);
            animIv.start();
            animHeight.setIntValues(0, DisplayUtil.dp2px(165));
            animHeight.start();
            mActivityGoldsAccountBinding.llGoldDetail.setScaleY(0);
            mActivityGoldsAccountBinding.llGoldDetail.animate().scaleY(1).setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnimRunning = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isExpand = true;
                            isAnimRunning = false;
                        }
                    }).start();
        }
    }
}

package com.centling.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centling.R;
import com.centling.mvp.BindHelper;
import com.centling.widget.ProcessDialog;
import com.jaeger.library.StatusBarUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * BaseActivity
 * Created by fionera on 17-1-9 in AndroidDemo.
 */

public abstract class BaseActivity
        extends RxAppCompatActivity
        implements BindHelper, BGASwipeBackHelper.Delegate {

    public Subject<Integer> lifecycle = PublishSubject.<Integer>create().toSerialized();

    protected BGASwipeBackHelper mSwipeBackHelper;

    protected ProcessDialog mProcessDialog;
    protected Activity mActivity;
    protected Context mContext;
    protected boolean isDestroy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        isDestroy = false;
        mProcessDialog = new ProcessDialog(this);

        ARouter.getInstance().inject(this);
    }

    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        mSwipeBackHelper.setSwipeBackEnable(true);
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        mSwipeBackHelper.setIsWeChatStyle(true);
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        mSwipeBackHelper.setIsNeedShowShadow(true);
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        if (lifecycle != null) {
            lifecycle.onComplete();
            lifecycle = null;
        }
    }

    public void showLoading() {
        showLoading(null, false);
    }

    public void showLoading(String tips) {
        showLoading(tips, false);
    }

    public void showCancelableLoading() {
        showLoading(null, true);
    }

    public void showCancelableLoading(String tips) {
        showLoading(tips, true);
    }

    private void showLoading(String tips, boolean cancelable) {
        if (TextUtils.isEmpty(tips)) {
            mProcessDialog.showDialog(cancelable);
        } else {
            mProcessDialog.setTitle(tips).showDialog(cancelable);
        }
    }

    public void dismissLoading() {
        mProcessDialog.dismiss();
    }

    @Override
    public <T> LifecycleTransformer<T> bindLifecycle() {
        return super.bindToLifecycle();
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntil(@NonNull ActivityEvent event) {
        return super.bindUntilEvent(event);
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntil(@NonNull FragmentEvent event) {
        return null;
    }

    @Override
    public void onSwipeBackLayoutSlide(float v) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    public void closeActivity(View view) {
        ActivityCompat.finishAfterTransition(this);
    }
}

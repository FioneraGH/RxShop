package com.centling.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.centling.mvp.BindHelper;
import com.centling.widget.ProcessDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * BaseFragment
 * Created by fionera on 17-2-8 in MVPPractice.
 */

public class BaseFragment
        extends RxFragment
        implements BindHelper {

    protected ProcessDialog mProcessDialog;
    protected Context mContext;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        mContext = context;
        mActivity = (Activity) context;
        mProcessDialog = new ProcessDialog(mContext);
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
        showLoading(null, true);
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
        return null;
    }

    @Override
    public <T> LifecycleTransformer<T> bindUntil(@NonNull FragmentEvent event) {
        return super.bindUntilEvent(event);
    }
}

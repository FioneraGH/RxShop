package com.centling.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.centling.R;
import com.centling.util.ShowToast;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * SplashActivity
 * Created by wayne on 17-2-9.
 */

public class SplashActivity
        extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE).subscribe(granted -> {
            if (granted) {
                Observable.timer(1200, TimeUnit.MILLISECONDS).observeOn(
                        AndroidSchedulers.mainThread()).subscribe(aLong -> tryToAuth());
            } else {
                ShowToast.show("请授予必要的权限");
                finish();
            }
        });
    }

    private void tryToAuth() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }
}

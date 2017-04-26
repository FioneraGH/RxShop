package com.centling.mvp.model;

import android.graphics.Bitmap;
import android.os.Environment;

import com.centling.entity.BaseEntity;
import com.centling.http.ApiManager;
import com.centling.mvp.contract.AvatarDetailContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MultipartBody;

/**
 * AvatarDetailModelImpl
 * Created by fionera on 2017/02/21
 */

public class AvatarDetailModelImpl
        implements AvatarDetailContract.Model {

    @Override
    public Observable<String> updateAvatar(String base64) {
        Map<String, String> params = new HashMap<>();
        params.put("base64_string", base64);
        return ApiManager.changeUserAvatar(params);
    }
}
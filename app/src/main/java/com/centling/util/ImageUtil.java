package com.centling.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.centling.BaseApplication;
import com.centling.R;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ImageUtil
 * Created by fionera on 16-6-15.
 */
@SuppressWarnings("unused")
public class ImageUtil {

    public static void loadImage(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).placeholder(R.drawable.iv_default).error(
                R.drawable.iv_default).into(imageView);
    }

    public static void loadImageWithoutCache(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).skipMemoryCache(true).into(imageView);
    }

    public static void loadImage(String url, ImageView imageView, int holderResId) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).placeholder(holderResId).into(imageView);
    }

    public static void loadImageWithBitmap(String url, ImageView imageView, int holderResId) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).asBitmap().placeholder(holderResId).into(
                imageView);
    }

    public static void loadImageWithoutCache(String url, ImageView imageView, int holderResId) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).skipMemoryCache(true).placeholder(holderResId)
                .into(imageView);
    }

    public static void loadThumbnail(String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).dontAnimate().thumbnail(0.3f).into(imageView);
    }

    public static void loadThumbnail(String url, ImageView imageView, int placeHolder) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_default);
            return;

        }
        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(placeHolder).into(
                imageView);
    }

    public static void loadAvatarImage(String url, ImageView imageView, int holderResId) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.iv_avatar_default);
            return;
        }
        Glide.with(imageView.getContext()).load(url).dontAnimate().placeholder(holderResId).into(
                imageView);
    }

    public static void clearCache() {
        Single.create((SingleOnSubscribe<Integer>) e -> {
            Glide.get(BaseApplication.getInstance()).clearDiskCache();
            e.onSuccess(0);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) throws Exception {
                if (throwable != null && !TextUtils.isEmpty(throwable.getMessage())) {
                    ShowToast.show("清理缓存失败");
                } else if (0 == integer) {
                    ShowToast.show("缓存已清除");
                }
            }
        });
    }
}

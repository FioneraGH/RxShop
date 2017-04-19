package com.centling.mvp;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * BindHelper
 * Created by fionera on 17-2-24 in sweeping_robot.
 */

public interface BindHelper {
    <T> LifecycleTransformer<T> bindLifecycle();

    <T> LifecycleTransformer<T> bindUntil(@NonNull ActivityEvent event);

    <T> LifecycleTransformer<T> bindUntil(@NonNull FragmentEvent event);
}

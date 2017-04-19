package com.centling.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityContainer
 * Created by Victor on 16/3/3.
 */

public class ActivityContainer {
    private static final int MAX_COUNT = 3;
    private static List<Activity> container;

    public static void add(Activity activity) {
        if (container == null) {
            container = new ArrayList<>();
        }
        container.add(activity);
        if (container.size() > MAX_COUNT) {
            container.get(0).finish();
            container.remove(0);
        }
    }

    public static void remove(Activity activity) {
        if (container == null) {
            return;
        }
        container.remove(activity);
    }
}

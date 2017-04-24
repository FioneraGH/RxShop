package com.centling.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.activity.BaseActivity;
import com.centling.widget.wheelview.ArrayWheelAdapter;
import com.centling.widget.wheelview.OnWheelChangedListener;
import com.centling.widget.wheelview.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 配送时间选择器
 */

public class ChooseDeliveryPopup
        extends PopupWindow
        implements OnWheelChangedListener {
    private View view;
    private Activity context;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private ChooseDeliveryPopup.GetValueCallback getValueCallback;

    private Window window;
    private ValueAnimator valueAnimator;

    public interface GetValueCallback {
        void getValue(String province, String city);
    }

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    private Map<String, String[]> distribution = new HashMap<>();
    private List<String> provinceList;
    private String[] str = {"0:00-24:00"};

    public ChooseDeliveryPopup(final Activity context, Map<String, String[]> distribution) {
        this.distribution = distribution;
        this.context = context;

        view = LayoutInflater.from(context).inflate(R.layout.popup_choose_delivery, null);
        setContentView(view);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_delivery_cancel);
        TextView tv_save = (TextView) view.findViewById(R.id.tv_delivery_confirm);
        mViewCity = (WheelView) view.findViewById(R.id.wv_delivery_left);
        mViewProvince = (WheelView) view.findViewById(R.id.wv_delivery_right);
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        setUpData();

        tv_cancel.setOnClickListener(v -> dismiss());

        tv_save.setOnClickListener(v -> {
            if (getValueCallback != null) {
                getValueCallback.getValue(mCurrentProviceName, mCurrentCityName);
            }
            dismiss();
        });

        setOutsideTouchable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(android.R.style.Animation_InputMethod);
        setBackgroundDrawable(new ColorDrawable(0));

        window = ((BaseActivity) context).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(animation -> {
            params.alpha = (float) animation.getAnimatedValue();
            window.setAttributes(params);
        });

        update();
    }

    public void setValue(String address) {
        String addresses[] = address.split(":");
        if (addresses.length < 0) return;
        if (TextUtils.isEmpty(addresses[0])) return;
        int i = 0, length = mProvinceDatas.length;
        for (; i < length; i++) {
            if (addresses[0].equals(mProvinceDatas[i])) break;
        }
        if (i >= length) return;
        mViewProvince.setCurrentItem(i);
        mCurrentProviceName = addresses[0];
        updateCities();

        i = 0;
        String areas[] = mCitisDatasMap.get(addresses[0]);
        length = areas.length;
        for (; i < length; i++) {
            if (addresses[1].equals(areas[i])) break;
        }
        if (i >= length) return;
        mViewCity.setCurrentItem(i);
        mCurrentCityName = addresses[1];

        i = 0;
        length = areas.length;
        for (; i < length; i++) {
            if (addresses[2].equals(areas[i])) break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
            updateAreas();
        } else if (wheel == mViewCity) {
            updateAreas();
        }
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(context, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        updateCities();
    }

    /**
     * 初始化控件地区信息
     */
    private void initProvinceDatas() {
        provinceList = new ArrayList<>();
        provinceList.add("不限日期 ");
        List<String[]> cityList = new ArrayList<>();
        cityList.add(str);
        Set<String> key = distribution.keySet();
        for (String s : key) {
            provinceList.add(s);
            cityList.add(distribution.get(s));
        }
        // 初始化默认选中
        if (provinceList != null && !provinceList.isEmpty()) {
            mCurrentProviceName = provinceList.get(0);
            if (!cityList.isEmpty()) {
                mCurrentCityName = cityList.get(0)[0];
            }
        }
        int provinceListCount = 0;
        if (provinceList != null) {
            provinceListCount = provinceList.size();
        }
        mProvinceDatas = new String[provinceListCount];
        for (int i = 0; i < provinceListCount; i++) {
            mProvinceDatas[i] = provinceList.get(i);
            String[] cityNames = cityList.get(i);
            mCitisDatasMap.put(mProvinceDatas[i], cityNames);
        }
    }

    private void updateAreas() {
        if (mCitisDatasMap.get(mCurrentProviceName).length != 0) {
            int pCurrent = mViewCity.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        if (mProvinceDatas.length != 0) {
            int pCurrent = mViewProvince.getCurrentItem();
            mCurrentProviceName = mProvinceDatas[pCurrent];
        }
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(context, cities));
        mViewCity.setCurrentItem(0);
    }

    public String getmCurrentCityName() {
        return mCurrentCityName;
    }

    public void setmCurrentCityName(String mCurrentCityName) {
        this.mCurrentCityName = mCurrentCityName;
    }

    public String getmCurrentProviceName() {
        return mCurrentProviceName;
    }

    public void setmCurrentProviceName(String mCurrentProviceName) {
        this.mCurrentProviceName = mCurrentProviceName;
    }

    public void setGetValueCallback(ChooseDeliveryPopup.GetValueCallback getValueCallback) {
        this.getValueCallback = getValueCallback;
    }

    public void show() {
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 1.0f;
            window.setAttributes(params);
        }
        valueAnimator.setFloatValues(1f, 0.8f);
        valueAnimator.start();
        showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void dismiss() {
        valueAnimator.setFloatValues(0.8f, 1f);
        valueAnimator.removeAllListeners();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.dimAmount = 0.0f;
                    window.setAttributes(params);
                }
            }
        });
        valueAnimator.start();
        super.dismiss();
    }
}

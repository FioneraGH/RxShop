package com.centling.popupwindow;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.centling.R;
import com.centling.widget.wheelview.ArrayWheelAdapter;
import com.centling.widget.wheelview.CityModel;
import com.centling.widget.wheelview.DistrictModel;
import com.centling.widget.wheelview.OnWheelChangedListener;
import com.centling.widget.wheelview.ProvinceModel;
import com.centling.widget.wheelview.WheelView;
import com.centling.widget.wheelview.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ProvincePopup
        extends PopupWindow
        implements OnWheelChangedListener {
    private View view;
    private Activity context;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    private GetValueCallback getValueCallback;

    public interface GetValueCallback {
        void getValue(String province, String city, String district, String zipcode);
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
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    /**
     * 解析省市区的XML数据
     */

    public ProvincePopup(Activity context) {

        this.context = context;

        view = View.inflate(context, R.layout.popup_change_province, null);
        setContentView(view);
        TextView tv_cancel = (TextView) view.findViewById(R.id.provice_cancel_tv);
        TextView tv_save = (TextView) view.findViewById(R.id.provice_pop_tv);
        mViewCity = (WheelView) view.findViewById(R.id.id_city);
        mViewProvince = (WheelView) view.findViewById(R.id.id_province);
        mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        setUpData();

        tv_cancel.setOnClickListener(view1 -> dismiss());

        tv_save.setOnClickListener(v -> {
            if (getValueCallback != null) {
                getValueCallback.getValue(mCurrentProviceName, mCurrentCityName,
                        mCurrentDistrictName, mCurrentZipCode);
            }
            dismiss();
        });

        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.AnimPopupWindow);

        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(new ColorDrawable(0));
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
        updateAreas();

        i = 0;
        areas = mDistrictDatasMap.get(addresses[1]);
        length = areas.length;
        for (; i < length; i++) {
            if (addresses[2].equals(areas[i])) break;
        }
        if (i >= length) return;
        mViewDistrict.setCurrentItem(i);
        mCurrentDistrictName = addresses[2];
        mCurrentZipCode = mZipcodeDatasMap.get(addresses[2]);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }


    private void setUpData() {

        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(context, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(5);
        mViewCity.setVisibleItems(5);
        mViewDistrict.setVisibleItems(5);
        updateCities();
    }

    /**
     * 初始化控件地区信息
     */
    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            int provinceListCount = 0;
            if (provinceList != null) {
                provinceListCount = provinceList.size();
            }
            mProvinceDatas = new String[provinceListCount];
            for (int i = 0; i < provinceListCount; i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(
                                districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(context, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(context, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }


    public String getSelectedResult() {
        return mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentDistrictName;
    }


    public String getmCurrentZipCode() {
        return mCurrentZipCode;
    }

    public void setmCurrentZipCode(String mCurrentZipCode) {
        this.mCurrentZipCode = mCurrentZipCode;
    }

    public String getmCurrentDistrictName() {
        return mCurrentDistrictName;
    }

    public void setmCurrentDistrictName(String mCurrentDistrictName) {
        this.mCurrentDistrictName = mCurrentDistrictName;
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

    public void setGetValueCallback(GetValueCallback getValueCallback) {
        this.getValueCallback = getValueCallback;
    }
}

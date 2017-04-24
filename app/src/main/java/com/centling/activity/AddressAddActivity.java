package com.centling.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.centling.R;
import com.centling.databinding.ActivityAddAddressBinding;
import com.centling.entity.AddressBean;
import com.centling.http.ApiManager;
import com.centling.popupwindow.ProvincePopup;
import com.centling.util.ShowDialog;
import com.centling.util.ShowToast;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.HashMap;
import java.util.Map;

public class AddressAddActivity
        extends TitleBarActivity
        implements View.OnClickListener {

    private ProvincePopup provincePopup;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String zipCode;
    private boolean isEdited;

    public static final int DIRECT_ADD_ADDRESS_RES = 777;

    private void checkEdited() {
        if (isEdited) {
            ShowDialog.showSelectDialog(mContext, "忘记保存地址？", "您还没有保存修改，是否放弃？", "", v -> finish());
        } else {
            finish();
        }
    }

    private void addAddress() {
        if (TextUtils.isEmpty(
                mActivityAddAddressBinding.etAddressName.getText().toString()) || TextUtils.isEmpty(
                mActivityAddAddressBinding.etAddressPhone.getText().toString()) || TextUtils
                .isEmpty(mActivityAddAddressBinding.etAddressDetail.getText()
                        .toString()) || TextUtils.isEmpty(
                mActivityAddAddressBinding.tvAddressProvince.getText().toString())) {
            ShowToast.show("信息不完整，请填写");
            return;
        }
        showLoading("正在添加地址");
        Map<String, String> params = new HashMap<>();
        if (addressId != null) {
            params.put("address_id", addressId);
        }
        params.put("is_visible", !isOtherAdd ? "0" : "1");
        params.put("true_name", mActivityAddAddressBinding.etAddressName.getText().toString());
        params.put("mob_phone", mActivityAddAddressBinding.etAddressPhone.getText().toString());
        params.put("tel_phone", mActivityAddAddressBinding.etAddressPhone.getText().toString());
        params.put("city_name", !isOtherAdd ? cityName : cityStr);
        params.put("province_name", !isOtherAdd ? provinceName : provinceStr);
        params.put("area_name", !isOtherAdd ? districtName : districtStr);
        params.put("address", !isOtherAdd ? mActivityAddAddressBinding.etAddressDetail.getText()
                .toString() : detailStr);
        params.put("area_info", !isOtherAdd ? mActivityAddAddressBinding.tvAddressProvince.getText()
                .toString() : provinceStr + cityStr + districtStr);
        params.put("is_default", mActivityAddAddressBinding.cbAddressDefaultCheck
                .isChecked() && !isOtherAdd ? "1" : "0");
        params.put("zip_code", zipCode);

        ApiManager.addAddress(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(
                addressOneBean -> {
                    isEdited = false;
                    dismissLoading();
                    if (addressId == null) {
                        ShowToast.show("添加地址成功！");
                        AddressBean.AddressListEntity addressListEntity = addressOneBean
                                .getAddress_info();
                        Intent intent = new Intent();
                        intent.putExtra("address_info", addressListEntity);
                        setResult(DIRECT_ADD_ADDRESS_RES, intent);
                    } else {
                        ShowToast.show("修改地址成功！");
                    }
                    finish();
                }, throwable -> {
                    dismissLoading();
                    ShowToast.show("添加地址失败！");
                });
    }

    private boolean isOtherAdd = false;
    private String provinceStr = "";
    private String cityStr = "";
    private String districtStr = "";
    private String detailStr = "";

    private void banViewClick() {
        mActivityAddAddressBinding.etAddressDetail.setText("详细地址保密");
        mActivityAddAddressBinding.tvAddressProvince.setText("省市信息保密");
        mActivityAddAddressBinding.rlAddressDefault.setVisibility(View.GONE);
        mActivityAddAddressBinding.etAddressDetail.setFocusable(false);
        mActivityAddAddressBinding.etAddressName.setFocusable(false);
        mActivityAddAddressBinding.etAddressPhone.setFocusable(false);
        mActivityAddAddressBinding.rlAddressProvince.setClickable(false);
    }

    private void delAddress() {
        showLoading("正在删除地址");
        Map<String, String> params = new HashMap<>();
        params.put("address_id", addressId);
        ApiManager.delAddress(params).compose(bindUntil(ActivityEvent.DESTROY)).subscribe(o -> {
            dismissLoading();
            ShowToast.show("删除地址成功！");
            finish();
        },throwable -> {
            dismissLoading();
            ShowToast.show("删除地址失败！");
        });
    }

    private String addressId;

    private AddressBean.AddressListEntity addressInfo;

    private ActivityAddAddressBinding mActivityAddAddressBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddAddressBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_add_address, null, false);
        setContentView(mActivityAddAddressBinding.getRoot());

        mTitleBar.getToolbar().setNavigationOnClickListener(v -> checkEdited());

        addressInfo = (AddressBean.AddressListEntity) getIntent().getSerializableExtra(
                "address_info");

        if (addressInfo == null) {
            mActivityAddAddressBinding.tvAddressDel.setText("取消");
            setTitleBarText("添加收货地址");
        } else {
            setTitleBarText("编辑收货地址");
            mActivityAddAddressBinding.tvAddressDel.setText("删除");
            mActivityAddAddressBinding.etAddressName.setText(addressInfo.getTrue_name());
            mActivityAddAddressBinding.etAddressPhone.setText(addressInfo.getMob_phone());
            mActivityAddAddressBinding.tvAddressProvince.setText(addressInfo.getArea_info());
            mActivityAddAddressBinding.etAddressDetail.setText(addressInfo.getAddress());
            mActivityAddAddressBinding.cbAddressDefaultCheck.setChecked(
                    "1".equals(addressInfo.getIs_default()));
            addressId = addressInfo.getAddress_id();

            if (addressInfo.getIs_visible().equals("1")) {
                banViewClick();
                mActivityAddAddressBinding.tvAddressSave.setVisibility(View.GONE);
            }
        }

        mActivityAddAddressBinding.cbAddressDefaultCheck.setOnCheckedChangeListener(
                (buttonView, isChecked) -> isEdited = true);

        provincePopup = new ProvincePopup(mActivity);
    }

    @Override
    public void onBackPressed() {
        checkEdited();
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_address_save:
                if (isEdited) {
                    addAddress();
                } else {
                    ShowToast.show("未进行修改");
                }
                break;
            case R.id.tv_address_del:
                if (addressInfo != null) {
                    ShowDialog.showSelectDialog(mContext, "删除地址", "你确定要删除吗？", "",
                            v1 -> delAddress());
                } else {
                    finish();
                }
                break;
            case R.id.rl_address_province:
                ((InputMethodManager) mActivity.
                        getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                if (!"请选择省市区".equals(provinceName + ":" + cityName + ":" + districtName)) {
                    provincePopup.setValue(provinceName + ":" + cityName + ":" + districtName);
                }
                provincePopup.setGetValueCallback((province, city, district, zipcode) -> {
                    provinceName = province;
                    cityName = city;
                    districtName = district;
                    zipCode = zipcode;
                    mActivityAddAddressBinding.tvAddressProvince.setText(
                            province + city + district);
                    isEdited = true;
                });
                provincePopup.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.3f;
                getWindow().setAttributes(lp);
                provincePopup.setOnDismissListener(() -> {
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                });
                break;
        }
    }
}

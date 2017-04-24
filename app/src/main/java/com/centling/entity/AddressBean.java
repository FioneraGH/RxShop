package com.centling.entity;

import java.io.Serializable;
import java.util.List;

/**
 * AddressBean
 * Created by fionera on 15-12-15.
 */
public class AddressBean
        implements Serializable {

    /**
     * address_id : 2
     * member_id : 20
     * true_name : 由于
     * area_id : 40
     * city_id : 55
     * area_info : I环保局
     * address : 安徽省 安庆市 枞阳县
     * tel_phone : 13582586877
     * mob_phone : 13582586877
     * is_default : 0
     * dlyp_id : 0
     */

    private List<AddressListEntity> address_list;

    public void setAddress_list(List<AddressListEntity> address_list) {
        this.address_list = address_list;
    }

    public List<AddressListEntity> getAddress_list() {
        return address_list;
    }

    public static class AddressListEntity
            implements Serializable {
        private String address_id;
        private String member_id;
        private String true_name;
        private String area_id;
        private String city_id;
        private String area_info;
        private String address;
        private String tel_phone;
        private String mob_phone;
        private String is_default;
        private String is_visible;
//            private String dlyp_id;


        public String getIs_visible() {
            return is_visible;
        }

        public void setIs_visible(String is_visible) {
            this.is_visible = is_visible;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public void setArea_info(String area_info) {
            this.area_info = area_info;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setTel_phone(String tel_phone) {
            this.tel_phone = tel_phone;
        }

        public void setMob_phone(String mob_phone) {
            this.mob_phone = mob_phone;
        }

        public void setIs_default(String is_default) {
            this.is_default = is_default;
        }

//            public void setDlyp_id(String dlyp_id) {
//                this.dlyp_id = dlyp_id;
//            }

        public String getAddress_id() {
            return address_id;
        }

        public String getMember_id() {
            return member_id;
        }

        public String getTrue_name() {
            return true_name;
        }

        public String getArea_id() {
            return area_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getArea_info() {
            return area_info;
        }

        public String getAddress() {
            return address;
        }

        public String getTel_phone() {
            return tel_phone;
        }

        public String getMob_phone() {
            return mob_phone;
        }

        public String getIs_default() {
            return is_default;
        }
    }
}

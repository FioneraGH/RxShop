package com.centling.entity;

import java.util.List;

/**
 * MyCustomBean
 * Created by fionera on 16-2-22.
 */
public class MyCustomBean {

    private boolean hasmore;
    private int page_total;
    /**
     * my_personal_id : 17
     * goods_id : 879
     * height : 178
     * weight : 57
     * age : 27
     * shoulder : 46.00
     * style : 0
     * desc :
     * goods_image : http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05094721199236144_360.jpg
     * goods_name : 美丽的衣服 红色 极品PIMA 小V领
     * goods_price : 1000.00
     * member_id : 27
     */

    private List<PersonalListEntity> personal_list;

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public void setPersonal_list(List<PersonalListEntity> personal_list) {
        this.personal_list = personal_list;
    }

    public boolean isHasmore() {
        return hasmore;
    }

    public int getPage_total() {
        return page_total;
    }

    public List<PersonalListEntity> getPersonal_list() {
        return personal_list;
    }

    public static class PersonalListEntity {
        private String my_personal_id;
        private String goods_id;
        private String height;
        private String weight;
        private String age;
        private String shoulder;
        private String style;
        private String desc;
        private String goods_image;
        private String goods_name;
        private String goods_price;
        private String member_id;

        public void setMy_personal_id(String my_personal_id) {
            this.my_personal_id = my_personal_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setShoulder(String shoulder) {
            this.shoulder = shoulder;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getMy_personal_id() {
            return my_personal_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public String getHeight() {
            return height;
        }

        public String getWeight() {
            return weight;
        }

        public String getAge() {
            return age;
        }

        public String getShoulder() {
            return shoulder;
        }

        public String getStyle() {
            return style;
        }

        public String getDesc() {
            return desc;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public String getMember_id() {
            return member_id;
        }
    }
}

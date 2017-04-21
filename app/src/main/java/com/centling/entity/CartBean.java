package com.centling.entity;

import java.util.List;

/**
 * CartBean
 * Created by fionera on 15-12-4.
 */
public class CartBean {
    private String sum;
    /**
     * cart_id : 295
     * buyer_id : 20
     * store_id : 3
     * store_name : HONNY恒尼
     * goods_id : 790
     * goods_name : 白色简约舒适内裤
     * goods_price : 189.00
     * goods_num : 1
     * goods_image : 3_05094681570597390.jpg
     * bl_id : 0
     * goods_image_url : http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05094681570597390_240.jpg
     * goods_sum : 189.00
     * goods_storage_alarm : 0
     * goods_storage : 385
     * honny_type : 0
     */

    private List<CartListEntity> cart_list;
    /**
     * goods_commonid : 29
     * goods_name : HONNY恒尼 前途无量系列 超薄莫代尔男士U凸透气舒适小平角商务内裤 P4S1NN084 中国红
     * goods_jingle :
     * gc_id_1 : 3
     * gc_name : 舒适内裤 &gt;兰精莫代尔面料 &gt;平角内裤
     * store_id : 3
     * store_name : HONNY恒尼
     * brand_id : 0
     * brand_name :
     * type_id : 38
     * goods_selltime : 0
     * goods_price : 178.00
     * goods_marketprice : 178.00
     * goods_discount : 100
     * goods_commend : 1
     * goods_freight : 0.00
     * new_goods_sort : 255
     * goods_image_url : http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05070486305089673_360.png
     */

    private List<GoodsCommonlistEntity> goods_commonlist;

    public void setSum(String sum) {
        this.sum = sum;
    }

    public void setCart_list(List<CartListEntity> cart_list) {
        this.cart_list = cart_list;
    }

    public void setGoods_commonlist(List<GoodsCommonlistEntity> goods_commonlist) {
        this.goods_commonlist = goods_commonlist;
    }

    public String getSum() {
        return sum;
    }

    public List<CartListEntity> getCart_list() {
        return cart_list;
    }

    public List<GoodsCommonlistEntity> getGoods_commonlist() {
        return goods_commonlist;
    }

    public static class CartListEntity {
        private String cart_id;
        private String buyer_id;
        private String store_id;
        private String store_name;
        private String goods_id;
        private String goods_name;
        private String goods_price;
        private String goods_num;
        private String goods_image;
        private String bl_id;
        private String goods_image_url;
        private String goods_sum;
        private String goods_storage_alarm;
        private String goods_storage;
        private String honny_type;
        private String goods_state;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public void setBl_id(String bl_id) {
            this.bl_id = bl_id;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public void setGoods_sum(String goods_sum) {
            this.goods_sum = goods_sum;
        }

        public void setGoods_storage_alarm(String goods_storage_alarm) {
            this.goods_storage_alarm = goods_storage_alarm;
        }

        public void setGoods_storage(String goods_storage) {
            this.goods_storage = goods_storage;
        }

        public void setHonny_type(String honny_type) {
            this.honny_type = honny_type;
        }

        public String getCart_id() {
            return cart_id;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public String getBl_id() {
            return bl_id;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }

        public String getGoods_sum() {
            return goods_sum;
        }

        public String getGoods_storage_alarm() {
            return goods_storage_alarm;
        }

        public String getGoods_storage() {
            return goods_storage;
        }

        public String getHonny_type() {
            return honny_type;
        }

        public String getGoods_state() {
            return goods_state;
        }

        public void setGoods_state(String goods_state) {
            this.goods_state = goods_state;
        }
    }

    public static class GoodsCommonlistEntity {
        private String goods_commonid;
        private String goods_name;
        private String goods_jingle;
        private String gc_id_1;
        private String gc_name;
        private String store_id;
        private String store_name;
        private String brand_id;
        private String brand_name;
        private String type_id;
        private String goods_selltime;
        private String goods_price;
        private String goods_marketprice;
        private String goods_discount;
        private String goods_commend;
        private String goods_freight;
        private String new_goods_sort;
        private String goods_image_url;

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public void setGoods_jingle(String goods_jingle) {
            this.goods_jingle = goods_jingle;
        }

        public void setGc_id_1(String gc_id_1) {
            this.gc_id_1 = gc_id_1;
        }

        public void setGc_name(String gc_name) {
            this.gc_name = gc_name;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public void setGoods_selltime(String goods_selltime) {
            this.goods_selltime = goods_selltime;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public void setGoods_marketprice(String goods_marketprice) {
            this.goods_marketprice = goods_marketprice;
        }

        public void setGoods_discount(String goods_discount) {
            this.goods_discount = goods_discount;
        }

        public void setGoods_commend(String goods_commend) {
            this.goods_commend = goods_commend;
        }

        public void setGoods_freight(String goods_freight) {
            this.goods_freight = goods_freight;
        }

        public void setNew_goods_sort(String new_goods_sort) {
            this.new_goods_sort = new_goods_sort;
        }

        public void setGoods_image_url(String goods_image_url) {
            this.goods_image_url = goods_image_url;
        }

        public String getGoods_commonid() {
            return goods_commonid;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public String getGoods_jingle() {
            return goods_jingle;
        }

        public String getGc_id_1() {
            return gc_id_1;
        }

        public String getGc_name() {
            return gc_name;
        }

        public String getStore_id() {
            return store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public String getType_id() {
            return type_id;
        }

        public String getGoods_selltime() {
            return goods_selltime;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public String getGoods_marketprice() {
            return goods_marketprice;
        }

        public String getGoods_discount() {
            return goods_discount;
        }

        public String getGoods_commend() {
            return goods_commend;
        }

        public String getGoods_freight() {
            return goods_freight;
        }

        public String getNew_goods_sort() {
            return new_goods_sort;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }
    }
}

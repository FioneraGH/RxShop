package com.centling.entity;

import java.util.List;

/**
 * FootPrintBean
 * Created by fionera on 16-2-22.
 */
public class FootPrintBean {

    /**
     * goods_commonid : 42
     * goods_name : 兰精莫代尔舒适家居套装
     * goods_price : 0.01
     * goods_marketprice : 1.00
     * store_id : 3
     * gc_id : 36
     * gc_id_1 : 1
     * gc_id_2 : 5
     * gc_id_3 : 36
     * goods_image_url : http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05094707412643832_360.jpg
     */

    private List<BrowseListEntity> browse_list;

    public void setBrowse_list(List<BrowseListEntity> browse_list) {
        this.browse_list = browse_list;
    }

    public List<BrowseListEntity> getBrowse_list() {
        return browse_list;
    }

    public static class BrowseListEntity {
        private String goods_commonid;
        private String goods_name;
        private String goods_price;
        private String goods_marketprice;
        private String store_id;
        private String gc_id;
        private String gc_id_1;
        private String gc_id_2;
        private String gc_id_3;
        private String goods_image_url;

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public void setGoods_marketprice(String goods_marketprice) {
            this.goods_marketprice = goods_marketprice;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
        }

        public void setGc_id_1(String gc_id_1) {
            this.gc_id_1 = gc_id_1;
        }

        public void setGc_id_2(String gc_id_2) {
            this.gc_id_2 = gc_id_2;
        }

        public void setGc_id_3(String gc_id_3) {
            this.gc_id_3 = gc_id_3;
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

        public String getGoods_price() {
            return goods_price;
        }

        public String getGoods_marketprice() {
            return goods_marketprice;
        }

        public String getStore_id() {
            return store_id;
        }

        public String getGc_id() {
            return gc_id;
        }

        public String getGc_id_1() {
            return gc_id_1;
        }

        public String getGc_id_2() {
            return gc_id_2;
        }

        public String getGc_id_3() {
            return gc_id_3;
        }

        public String getGoods_image_url() {
            return goods_image_url;
        }
    }
}

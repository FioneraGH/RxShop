package com.centling.entity;

import java.util.List;

public class CatalogGoodsBean {

    private boolean hasmore;
    private int page_total;
    /**
     * goods_commonid : 1
     * goods_name : test
     * goods_jingle : test
     * gc_id : 13
     * gc_name : 服饰鞋帽 &gt;女装 &gt;衬衫
     * store_id : 3
     * store_name : HONNY恒尼
     * brand_id : 210
     * brand_name : HONNY
     * type_id : 37
     * goods_selltime : 0
     * goods_price : 10.00
     * goods_marketprice : 10.00
     * goods_discount : 100
     * goods_commend : 1
     * goods_freight : 0.00
     * goods_image_url : http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05034927861304760_360.jpg
     */

    private List<GoodsCommonlistEntity> goods_commonlist;

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public void setGoods_commonlist(List<GoodsCommonlistEntity> goods_commonlist) {
        this.goods_commonlist = goods_commonlist;
    }

    public boolean isHasmore() {
        return hasmore;
    }

    public int getPage_total() {
        return page_total;
    }

    public List<GoodsCommonlistEntity> getGoods_commonlist() {
        return goods_commonlist;
    }

    public static class GoodsCommonlistEntity {
        private String goods_commonid;
        private String goods_name;
        private String goods_jingle;
        private String gc_id;
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
        private String goods_image_url;
        private String goods_salenum;
        private int goods_salenum_open;

        public int getGoods_salenum_open() {
            return goods_salenum_open;
        }

        public void setGoods_salenum_open(int goods_salenum_open) {
            this.goods_salenum_open = goods_salenum_open;
        }

        public String getGoods_salenum() {
            return goods_salenum;
        }

        public void setGoods_salenum(String goods_salenum) {
            this.goods_salenum = goods_salenum;
        }

        public void setGoods_commonid(String goods_commonid) {
            this.goods_commonid = goods_commonid;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public void setGoods_jingle(String goods_jingle) {
            this.goods_jingle = goods_jingle;
        }

        public void setGc_id(String gc_id) {
            this.gc_id = gc_id;
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

        public String getGc_id() {
            return gc_id;
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

        public String getGoods_image_url() {
            return goods_image_url;
        }
    }
}

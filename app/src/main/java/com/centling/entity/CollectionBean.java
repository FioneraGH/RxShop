package com.centling.entity;

import java.util.List;

/**
 * CollectionBean
 * Created by fionera on 16-2-22.
 */
public class CollectionBean {

    private boolean hasmore;
    private int page_total;
    /**
     * member_id : 21
     * fav_id : 52
     * fav_type : goods
     * fav_time : 1456468898
     * goods_commonlist : [{"goods_commonid":"52","goods_name":"内衣套装基础秋冬内衣兰精莫代尔面料测试",
     * "gc_id":"58","gc_id_1":"1","gc_id_2":"6","gc_id_3":"58","gc_name":"内衣套装 &gt;基础秋冬内衣
     * &gt;兰精莫代尔面料","store_id":"3","spec_name":"N;","spec_value":"N;","goods_price":"999.00",
     * "goods_state":"1","goods_image_url":"http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05097133960492741_360.jpg"}]
     */

    private List<FavoritesListEntity> favorites_list;

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public void setFavorites_list(List<FavoritesListEntity> favorites_list) {
        this.favorites_list = favorites_list;
    }

    public boolean isHasmore() {
        return hasmore;
    }

    public int getPage_total() {
        return page_total;
    }

    public List<FavoritesListEntity> getFavorites_list() {
        return favorites_list;
    }

    public static class FavoritesListEntity {
        private String member_id;
        private String fav_id;
        private String fav_type;
        private String fav_time;
        /**
         * goods_commonid : 52
         * goods_name : 内衣套装基础秋冬内衣兰精莫代尔面料测试
         * gc_id : 58
         * gc_id_1 : 1
         * gc_id_2 : 6
         * gc_id_3 : 58
         * gc_name : 内衣套装 &gt;基础秋冬内衣 &gt;兰精莫代尔面料
         * store_id : 3
         * spec_name : N;
         * spec_value : N;
         * goods_price : 999.00
         * goods_state : 1
         * goods_image_url : http://7xp9th.com1.z0.glb.clouddn
         * .com/shop/store/goods/3/3_05097133960492741_360.jpg
         */

        private List<GoodsCommonlistEntity> goods_commonlist;

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public void setFav_id(String fav_id) {
            this.fav_id = fav_id;
        }

        public void setFav_type(String fav_type) {
            this.fav_type = fav_type;
        }

        public void setFav_time(String fav_time) {
            this.fav_time = fav_time;
        }

        public void setGoods_commonlist(List<GoodsCommonlistEntity> goods_commonlist) {
            this.goods_commonlist = goods_commonlist;
        }

        public String getMember_id() {
            return member_id;
        }

        public String getFav_id() {
            return fav_id;
        }

        public String getFav_type() {
            return fav_type;
        }

        public String getFav_time() {
            return fav_time;
        }

        public List<GoodsCommonlistEntity> getGoods_commonlist() {
            return goods_commonlist;
        }

        public static class GoodsCommonlistEntity {
            private String goods_commonid;
            private String goods_name;
            private String gc_id;
            private String gc_id_1;
            private String gc_id_2;
            private String gc_id_3;
            private String gc_name;
            private String store_id;
            private String spec_name;
            private String spec_value;
            private String goods_price;
            private String goods_state;
            private String goods_image_url;

            public void setGoods_commonid(String goods_commonid) {
                this.goods_commonid = goods_commonid;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
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

            public void setGc_name(String gc_name) {
                this.gc_name = gc_name;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public void setSpec_name(String spec_name) {
                this.spec_name = spec_name;
            }

            public void setSpec_value(String spec_value) {
                this.spec_value = spec_value;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public void setGoods_state(String goods_state) {
                this.goods_state = goods_state;
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

            public String getGc_name() {
                return gc_name;
            }

            public String getStore_id() {
                return store_id;
            }

            public String getSpec_name() {
                return spec_name;
            }

            public String getSpec_value() {
                return spec_value;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public String getGoods_state() {
                return goods_state;
            }

            public String getGoods_image_url() {
                return goods_image_url;
            }
        }
    }
}

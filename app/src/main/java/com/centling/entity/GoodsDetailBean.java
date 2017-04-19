package com.centling.entity;


import java.util.List;

/**
 * GoodsDetailBean
 * Created by Victor on 15/11/25.
 */

public class GoodsDetailBean {

    private ResultEntity result;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public static class ResultEntity {
        private boolean is_favorites;

        private GoodsCommonInfoEntity goods_common_info;


        private StoreInfoEntity store_info;


        private List<GoodsListEntity> goods_list;


        private List<GoodsCommendlistEntity> goods_commendlist;

        public boolean isIs_favorites() {
            return is_favorites;
        }

        public void setIs_favorites(boolean is_favorites) {
            this.is_favorites = is_favorites;
        }

        public GoodsCommonInfoEntity getGoods_common_info() {
            return goods_common_info;
        }

        public void setGoods_common_info(GoodsCommonInfoEntity goods_common_info) {
            this.goods_common_info = goods_common_info;
        }

        public StoreInfoEntity getStore_info() {
            return store_info;
        }

        public void setStore_info(StoreInfoEntity store_info) {
            this.store_info = store_info;
        }

        public List<GoodsListEntity> getGoods_list() {
            return goods_list;
        }

        public void setGoods_list(List<GoodsListEntity> goods_list) {
            this.goods_list = goods_list;
        }

        public List<GoodsCommendlistEntity> getGoods_commendlist() {
            return goods_commendlist;
        }

        public void setGoods_commendlist(List<GoodsCommendlistEntity> goods_commendlist) {
            this.goods_commendlist = goods_commendlist;
        }

        public static class GoodsCommonInfoEntity {
            private String goods_name;
            private String goods_jingle;
            private String gc_id_1;
            private String gc_id_2;
            private String gc_id_3;
            private String store_id;
            private String goods_body;
            private String goods_specname;
            private String goods_price;
            private String goods_marketprice;
            private String goods_costprice;
            private String goods_discount;
            private String goods_serial;
            private String goods_storage_alarm;
            private String transport_id;
            private String transport_title;
            private String goods_freight;
            private String goods_vat;
            private String areaid_1;
            private String areaid_2;
            private String goods_stcids;
            private String plateid_top;
            private String plateid_bottom;
            private String is_virtual;
            private String virtual_indate;
            private String virtual_limit;
            private String virtual_invalid_refund;
            private String is_fcode;
            private String is_appoint;
            private String appoint_satedate;
            private String is_presell;
            private String presell_deliverdate;
            private String is_own_shop;
            private String is_spring;
            private String is_summer;
            private String is_autumn;
            private String is_winter;
            private String spring_sort;
            private String summer_sort;
            private String autumn_sort;
            private String winter_sort;
            private String new_goods_sort;
            private String parameter;
            private String sales_service;
            private String goods_salenum;
            private String honny_type;
            private String goods_image_rectangle;
            private GoodsListEntity goods_listInfo;
            private String goods_unit;
            private int goods_salenum_open;


            public String getGoods_unit() {
                return goods_unit;
            }

            public void setGoods_unit(String goods_unit) {
                this.goods_unit = goods_unit;
            }

            public int getGoods_salenum_open() {
                return goods_salenum_open;
            }

            public void setGoods_salenum_open(int goods_salenum_open) {
                this.goods_salenum_open = goods_salenum_open;
            }

            private String first_goods_id;
            private List<String> spec_name_sort;
            private List<List<String>> spec_value_sort;

            public String getGoods_name() {
                return goods_name;
            }

            public GoodsListEntity getGoods_listInfo() {
                return goods_listInfo;
            }

            public void setGoods_common_info(GoodsListEntity goods_listInfo) {
                this.goods_listInfo = goods_listInfo;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_jingle() {
                return goods_jingle;
            }

            public void setGoods_jingle(String goods_jingle) {
                this.goods_jingle = goods_jingle;
            }

            public String getGc_id_1() {
                return gc_id_1;
            }

            public void setGc_id_1(String gc_id_1) {
                this.gc_id_1 = gc_id_1;
            }

            public String getGc_id_2() {
                return gc_id_2;
            }

            public void setGc_id_2(String gc_id_2) {
                this.gc_id_2 = gc_id_2;
            }

            public String getGc_id_3() {
                return gc_id_3;
            }

            public void setGc_id_3(String gc_id_3) {
                this.gc_id_3 = gc_id_3;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }


            public String getGoods_body() {
                return goods_body;
            }

            public void setGoods_body(String goods_body) {
                this.goods_body = goods_body;
            }

            public String getGoods_specname() {
                return goods_specname;
            }

            public void setGoods_specname(String goods_specname) {
                this.goods_specname = goods_specname;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getGoods_marketprice() {
                return goods_marketprice;
            }

            public void setGoods_marketprice(String goods_marketprice) {
                this.goods_marketprice = goods_marketprice;
            }

            public String getGoods_costprice() {
                return goods_costprice;
            }

            public void setGoods_costprice(String goods_costprice) {
                this.goods_costprice = goods_costprice;
            }

            public String getGoods_discount() {
                return goods_discount;
            }

            public void setGoods_discount(String goods_discount) {
                this.goods_discount = goods_discount;
            }

            public String getGoods_serial() {
                return goods_serial;
            }

            public void setGoods_serial(String goods_serial) {
                this.goods_serial = goods_serial;
            }

            public String getGoods_storage_alarm() {
                return goods_storage_alarm;
            }

            public void setGoods_storage_alarm(String goods_storage_alarm) {
                this.goods_storage_alarm = goods_storage_alarm;
            }

            public String getTransport_id() {
                return transport_id;
            }

            public void setTransport_id(String transport_id) {
                this.transport_id = transport_id;
            }

            public String getTransport_title() {
                return transport_title;
            }

            public void setTransport_title(String transport_title) {
                this.transport_title = transport_title;
            }

            public String getGoods_freight() {
                return goods_freight;
            }

            public void setGoods_freight(String goods_freight) {
                this.goods_freight = goods_freight;
            }

            public String getGoods_vat() {
                return goods_vat;
            }

            public void setGoods_vat(String goods_vat) {
                this.goods_vat = goods_vat;
            }

            public String getAreaid_1() {
                return areaid_1;
            }

            public void setAreaid_1(String areaid_1) {
                this.areaid_1 = areaid_1;
            }

            public String getAreaid_2() {
                return areaid_2;
            }

            public void setAreaid_2(String areaid_2) {
                this.areaid_2 = areaid_2;
            }

            public String getGoods_stcids() {
                return goods_stcids;
            }

            public void setGoods_stcids(String goods_stcids) {
                this.goods_stcids = goods_stcids;
            }

            public String getPlateid_top() {
                return plateid_top;
            }

            public void setPlateid_top(String plateid_top) {
                this.plateid_top = plateid_top;
            }

            public String getPlateid_bottom() {
                return plateid_bottom;
            }

            public void setPlateid_bottom(String plateid_bottom) {
                this.plateid_bottom = plateid_bottom;
            }

            public String getIs_virtual() {
                return is_virtual;
            }

            public void setIs_virtual(String is_virtual) {
                this.is_virtual = is_virtual;
            }

            public String getVirtual_indate() {
                return virtual_indate;
            }

            public void setVirtual_indate(String virtual_indate) {
                this.virtual_indate = virtual_indate;
            }

            public String getVirtual_limit() {
                return virtual_limit;
            }

            public void setVirtual_limit(String virtual_limit) {
                this.virtual_limit = virtual_limit;
            }

            public String getVirtual_invalid_refund() {
                return virtual_invalid_refund;
            }

            public void setVirtual_invalid_refund(String virtual_invalid_refund) {
                this.virtual_invalid_refund = virtual_invalid_refund;
            }

            public String getIs_fcode() {
                return is_fcode;
            }

            public void setIs_fcode(String is_fcode) {
                this.is_fcode = is_fcode;
            }

            public String getIs_appoint() {
                return is_appoint;
            }

            public void setIs_appoint(String is_appoint) {
                this.is_appoint = is_appoint;
            }

            public String getAppoint_satedate() {
                return appoint_satedate;
            }

            public void setAppoint_satedate(String appoint_satedate) {
                this.appoint_satedate = appoint_satedate;
            }

            public String getIs_presell() {
                return is_presell;
            }

            public void setIs_presell(String is_presell) {
                this.is_presell = is_presell;
            }

            public String getPresell_deliverdate() {
                return presell_deliverdate;
            }

            public void setPresell_deliverdate(String presell_deliverdate) {
                this.presell_deliverdate = presell_deliverdate;
            }

            public String getIs_own_shop() {
                return is_own_shop;
            }

            public void setIs_own_shop(String is_own_shop) {
                this.is_own_shop = is_own_shop;
            }

            public String getIs_spring() {
                return is_spring;
            }

            public void setIs_spring(String is_spring) {
                this.is_spring = is_spring;
            }

            public String getIs_summer() {
                return is_summer;
            }

            public void setIs_summer(String is_summer) {
                this.is_summer = is_summer;
            }

            public String getIs_autumn() {
                return is_autumn;
            }

            public void setIs_autumn(String is_autumn) {
                this.is_autumn = is_autumn;
            }

            public String getIs_winter() {
                return is_winter;
            }

            public void setIs_winter(String is_winter) {
                this.is_winter = is_winter;
            }

            public String getSpring_sort() {
                return spring_sort;
            }

            public void setSpring_sort(String spring_sort) {
                this.spring_sort = spring_sort;
            }

            public String getSummer_sort() {
                return summer_sort;
            }

            public void setSummer_sort(String summer_sort) {
                this.summer_sort = summer_sort;
            }

            public String getAutumn_sort() {
                return autumn_sort;
            }

            public void setAutumn_sort(String autumn_sort) {
                this.autumn_sort = autumn_sort;
            }

            public String getWinter_sort() {
                return winter_sort;
            }

            public void setWinter_sort(String winter_sort) {
                this.winter_sort = winter_sort;
            }

            public String getNew_goods_sort() {
                return new_goods_sort;
            }

            public void setNew_goods_sort(String new_goods_sort) {
                this.new_goods_sort = new_goods_sort;
            }

            public String getParameter() {
                return parameter;
            }

            public void setParameter(String parameter) {
                this.parameter = parameter;
            }

            public String getSales_service() {
                return sales_service;
            }

            public void setSales_service(String sales_service) {
                this.sales_service = sales_service;
            }

            public String getGoods_salenum() {
                return goods_salenum;
            }

            public void setGoods_salenum(String goods_salenum) {
                this.goods_salenum = goods_salenum;
            }

            public String getHonny_type() {
                return honny_type;
            }

            public void setHonny_type(String honny_type) {
                this.honny_type = honny_type;
            }

            public String getGoods_image_rectangle() {
                return goods_image_rectangle;
            }

            public void setGoods_image_rectangle(String goods_image_rectangle) {
                this.goods_image_rectangle = goods_image_rectangle;
            }

            public String getFirst_goods_id() {
                return first_goods_id;
            }

            public void setFirst_goods_id(String first_goods_id) {
                this.first_goods_id = first_goods_id;
            }

            public List<String> getSpec_name_sort() {
                return spec_name_sort;
            }

            public void setSpec_name_sort(List<String> spec_name_sort) {
                this.spec_name_sort = spec_name_sort;
            }

            public List<List<String>> getSpec_value_sort() {
                return spec_value_sort;
            }

            public void setSpec_value_sort(List<List<String>> spec_value_sort) {
                this.spec_value_sort = spec_value_sort;
            }

        }

        public static class StoreInfoEntity {
            private String store_id;
            private String store_name;
            private String member_id;
            private String store_qq;
            private String store_ww;
            private String store_phone;
            private String member_name;
            /**
             * goods_id : 834
             */

            private AvatarEntity avatar;

            public String getStore_id() {
                return store_id;
            }


            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getStore_qq() {
                return store_qq;
            }

            public void setStore_qq(String store_qq) {
                this.store_qq = store_qq;
            }

            public String getStore_ww() {
                return store_ww;
            }

            public void setStore_ww(String store_ww) {
                this.store_ww = store_ww;
            }

            public String getStore_phone() {
                return store_phone;
            }

            public void setStore_phone(String store_phone) {
                this.store_phone = store_phone;
            }

            public String getMember_name() {
                return member_name;
            }

            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }

            public AvatarEntity getAvatar() {
                return avatar;
            }

            public void setAvatar(AvatarEntity avatar) {
                this.avatar = avatar;
            }

            public static class AvatarEntity {
                private String goods_id;

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }
            }
        }

        public static class GoodsListEntity {

            private GoodsInfoEntity goods_info;
            private String goods_image;

            public GoodsInfoEntity getGoods_info() {
                return goods_info;
            }

            public void setGoods_info(GoodsInfoEntity goods_info) {
                this.goods_info = goods_info;
            }

            public String getGoods_image() {
                return goods_image;
            }

            public void setGoods_image(String goods_image) {
                this.goods_image = goods_image;
            }

            public static class GoodsInfoEntity {
                private String goods_id;
                private String goods_name;
                private String goods_jingle;
                private String store_id;
                private String gc_id_1;
                private String gc_id_2;
                private String gc_id_3;
                private String goods_price;
                private String goods_promotion_price;
                private String goods_promotion_type;
                private String goods_marketprice;
                private String goods_serial;
                private String goods_storage_alarm;
                private int goods_click;
                private String goods_salenum;
                private String goods_collect;
                /**
                 * 623 : 灰色
                 * 625 : M
                 */

                private String goods_storage;
                private String areaid_1;
                private String areaid_2;
                private String color_id;
                private String transport_id;
                private String goods_freight;
                private String goods_vat;
                private String goods_stcids;
                private String evaluation_good_star;
                private String evaluation_count;
                private String is_virtual;
                private String virtual_indate;
                private String virtual_limit;
                private String virtual_invalid_refund;
                private String is_fcode;
                private String is_appoint;
                private String is_presell;
                private String have_gift;
                private String is_own_shop;
                private String honny_type;
                private String size_id;
                private String personal_image;
                private String goods_image_rectangle;
                private String goods_url;


                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getGoods_name() {
                    return goods_name;
                }

                public void setGoods_name(String goods_name) {
                    this.goods_name = goods_name;
                }

                public String getGoods_jingle() {
                    return goods_jingle;
                }

                public void setGoods_jingle(String goods_jingle) {
                    this.goods_jingle = goods_jingle;
                }

                public String getStore_id() {
                    return store_id;
                }

                public void setStore_id(String store_id) {
                    this.store_id = store_id;
                }

                public String getGc_id_1() {
                    return gc_id_1;
                }

                public void setGc_id_1(String gc_id_1) {
                    this.gc_id_1 = gc_id_1;
                }

                public String getGc_id_2() {
                    return gc_id_2;
                }

                public void setGc_id_2(String gc_id_2) {
                    this.gc_id_2 = gc_id_2;
                }

                public String getGc_id_3() {
                    return gc_id_3;
                }

                public void setGc_id_3(String gc_id_3) {
                    this.gc_id_3 = gc_id_3;
                }

                public String getGoods_price() {
                    return goods_price;
                }

                public void setGoods_price(String goods_price) {
                    this.goods_price = goods_price;
                }

                public String getGoods_promotion_price() {
                    return goods_promotion_price;
                }

                public void setGoods_promotion_price(String goods_promotion_price) {
                    this.goods_promotion_price = goods_promotion_price;
                }

                public String getGoods_promotion_type() {
                    return goods_promotion_type;
                }

                public void setGoods_promotion_type(String goods_promotion_type) {
                    this.goods_promotion_type = goods_promotion_type;
                }

                public String getGoods_marketprice() {
                    return goods_marketprice;
                }

                public void setGoods_marketprice(String goods_marketprice) {
                    this.goods_marketprice = goods_marketprice;
                }

                public String getGoods_serial() {
                    return goods_serial;
                }

                public void setGoods_serial(String goods_serial) {
                    this.goods_serial = goods_serial;
                }

                public String getGoods_storage_alarm() {
                    return goods_storage_alarm;
                }

                public void setGoods_storage_alarm(String goods_storage_alarm) {
                    this.goods_storage_alarm = goods_storage_alarm;
                }

                public int getGoods_click() {
                    return goods_click;
                }

                public void setGoods_click(int goods_click) {
                    this.goods_click = goods_click;
                }

                public String getGoods_salenum() {
                    return goods_salenum;
                }

                public void setGoods_salenum(String goods_salenum) {
                    this.goods_salenum = goods_salenum;
                }

                public String getGoods_collect() {
                    return goods_collect;
                }

                public void setGoods_collect(String goods_collect) {
                    this.goods_collect = goods_collect;
                }

                public String getGoods_storage() {
                    return goods_storage;
                }

                public void setGoods_storage(String goods_storage) {
                    this.goods_storage = goods_storage;
                }

                public String getAreaid_1() {
                    return areaid_1;
                }

                public void setAreaid_1(String areaid_1) {
                    this.areaid_1 = areaid_1;
                }

                public String getAreaid_2() {
                    return areaid_2;
                }

                public void setAreaid_2(String areaid_2) {
                    this.areaid_2 = areaid_2;
                }

                public String getColor_id() {
                    return color_id;
                }

                public void setColor_id(String color_id) {
                    this.color_id = color_id;
                }

                public String getTransport_id() {
                    return transport_id;
                }

                public void setTransport_id(String transport_id) {
                    this.transport_id = transport_id;
                }

                public String getGoods_freight() {
                    return goods_freight;
                }

                public void setGoods_freight(String goods_freight) {
                    this.goods_freight = goods_freight;
                }

                public String getGoods_vat() {
                    return goods_vat;
                }

                public void setGoods_vat(String goods_vat) {
                    this.goods_vat = goods_vat;
                }

                public String getGoods_stcids() {
                    return goods_stcids;
                }

                public void setGoods_stcids(String goods_stcids) {
                    this.goods_stcids = goods_stcids;
                }

                public String getEvaluation_good_star() {
                    return evaluation_good_star;
                }

                public void setEvaluation_good_star(String evaluation_good_star) {
                    this.evaluation_good_star = evaluation_good_star;
                }

                public String getEvaluation_count() {
                    return evaluation_count;
                }

                public void setEvaluation_count(String evaluation_count) {
                    this.evaluation_count = evaluation_count;
                }

                public String getIs_virtual() {
                    return is_virtual;
                }

                public void setIs_virtual(String is_virtual) {
                    this.is_virtual = is_virtual;
                }

                public String getVirtual_indate() {
                    return virtual_indate;
                }

                public void setVirtual_indate(String virtual_indate) {
                    this.virtual_indate = virtual_indate;
                }

                public String getVirtual_limit() {
                    return virtual_limit;
                }

                public void setVirtual_limit(String virtual_limit) {
                    this.virtual_limit = virtual_limit;
                }

                public String getVirtual_invalid_refund() {
                    return virtual_invalid_refund;
                }

                public void setVirtual_invalid_refund(String virtual_invalid_refund) {
                    this.virtual_invalid_refund = virtual_invalid_refund;
                }

                public String getIs_fcode() {
                    return is_fcode;
                }

                public void setIs_fcode(String is_fcode) {
                    this.is_fcode = is_fcode;
                }

                public String getIs_appoint() {
                    return is_appoint;
                }

                public void setIs_appoint(String is_appoint) {
                    this.is_appoint = is_appoint;
                }

                public String getIs_presell() {
                    return is_presell;
                }

                public void setIs_presell(String is_presell) {
                    this.is_presell = is_presell;
                }

                public String getHave_gift() {
                    return have_gift;
                }

                public void setHave_gift(String have_gift) {
                    this.have_gift = have_gift;
                }

                public String getIs_own_shop() {
                    return is_own_shop;
                }

                public void setIs_own_shop(String is_own_shop) {
                    this.is_own_shop = is_own_shop;
                }

                public String getHonny_type() {
                    return honny_type;
                }

                public void setHonny_type(String honny_type) {
                    this.honny_type = honny_type;
                }

                public String getSize_id() {
                    return size_id;
                }

                public void setSize_id(String size_id) {
                    this.size_id = size_id;
                }

                public String getPersonal_image() {
                    return personal_image;
                }

                public void setPersonal_image(String personal_image) {
                    this.personal_image = personal_image;
                }

                public String getGoods_image_rectangle() {
                    return goods_image_rectangle;
                }

                public void setGoods_image_rectangle(String goods_image_rectangle) {
                    this.goods_image_rectangle = goods_image_rectangle;
                }

                public String getGoods_url() {
                    return goods_url;
                }

                public void setGoods_url(String goods_url) {
                    this.goods_url = goods_url;
                }

            }
        }

        public static class GoodsCommendlistEntity {
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
            private String goods_image_rectangle_url;

            public String getGoods_commonid() {
                return goods_commonid;
            }

            public void setGoods_commonid(String goods_commonid) {
                this.goods_commonid = goods_commonid;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_jingle() {
                return goods_jingle;
            }

            public void setGoods_jingle(String goods_jingle) {
                this.goods_jingle = goods_jingle;
            }

            public String getGc_id_1() {
                return gc_id_1;
            }

            public void setGc_id_1(String gc_id_1) {
                this.gc_id_1 = gc_id_1;
            }

            public String getGc_name() {
                return gc_name;
            }

            public void setGc_name(String gc_name) {
                this.gc_name = gc_name;
            }

            public String getStore_id() {
                return store_id;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getGoods_selltime() {
                return goods_selltime;
            }

            public void setGoods_selltime(String goods_selltime) {
                this.goods_selltime = goods_selltime;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getGoods_marketprice() {
                return goods_marketprice;
            }

            public void setGoods_marketprice(String goods_marketprice) {
                this.goods_marketprice = goods_marketprice;
            }

            public String getGoods_discount() {
                return goods_discount;
            }

            public void setGoods_discount(String goods_discount) {
                this.goods_discount = goods_discount;
            }

            public String getGoods_commend() {
                return goods_commend;
            }

            public void setGoods_commend(String goods_commend) {
                this.goods_commend = goods_commend;
            }

            public String getGoods_freight() {
                return goods_freight;
            }

            public void setGoods_freight(String goods_freight) {
                this.goods_freight = goods_freight;
            }

            public String getNew_goods_sort() {
                return new_goods_sort;
            }

            public void setNew_goods_sort(String new_goods_sort) {
                this.new_goods_sort = new_goods_sort;
            }

            public String getGoods_image_url() {
                return goods_image_url;
            }

            public void setGoods_image_url(String goods_image_url) {
                this.goods_image_url = goods_image_url;
            }

            public String getGoods_image_rectangle_url() {
                return goods_image_rectangle_url;
            }

            public void setGoods_image_rectangle_url(String goods_image_rectangle_url) {
                this.goods_image_rectangle_url = goods_image_rectangle_url;
            }
        }
    }
}

package com.centling.entity;

import java.util.List;

/**
 * OrderConfirmBean
 * Created by fionera on 15-12-23.
 */
public class OrderConfirmBean {


    /**
     * buy_list : {"store_cart_list":[{"store_id":"3","store_name":"HONNY恒尼",
     * "goods_list":[{"goods_num":1,"goods_id":835,"goods_commonid":"42","gc_id":"36",
     * "store_id":"3","goods_name":"兰精莫代尔舒适家居套装 白色 M","goods_price":"0.01",
     * "store_name":"HONNY恒尼","goods_image":"3_05094707412643832.jpg","transport_id":"0",
     * "goods_freight":"0.00","goods_vat":"0","goods_storage":"252","goods_storage_alarm":"0",
     * "is_fcode":"0","have_gift":"0","state":true,"storage_state":true,"groupbuy_info":null,
     * "xianshi_info":null,"cart_id":835,"bl_id":0,"goods_total":"0.01",
     * "goods_image_url":"http://7xp9th.com1.z0.glb.clouddn
     * .com/shop/store/goods/3/3_05094707412643832_240.jpg"}],"store_goods_total":"0.01",
     * "store_mansong_rule_list":null,"store_voucher_list":[],"freight":"1"}],
     * "freight_hash":"qzAitB6a7BuY7jrMu3zbXxUZkLEbY8hHn9DPoJ12k6TCSFT_NnIK_WxYWj7Z2f4R3r-X
     * -LxkMv7U7FxBMx-kbH2Gkm6DT6M0H8CTzc1gnjrCV0Jj7U5xQub","address_info":{"address_id":"69",
     * "member_id":"20","true_name":"哈哈","area_id":"2324","city_id":"202",
     * "area_info":"安徽省宣城市广德县","address":"浩浩荡荡","tel_phone":"13156588525",
     * "mob_phone":"13156588525","is_default":"1","dlyp_id":"0","zip_code":"366000"},
     * "ifshow_offpay":null,"vat_hash":"5Dr9R-V9U4jv-kqkaEsui1mJCImoWXJx-Qu",
     * "inv_info":{"content":"不需要发票"},"available_predeposit":null,"available_rc_balance":null,
     * "total_gold_coins":0,"default_address":{"address_id":"69","member_id":"20",
     * "true_name":"哈哈","area_id":"2324","city_id":"202","area_info":"安徽省宣城市广德县",
     * "address":"浩浩荡荡","tel_phone":"13156588525","mob_phone":"13156588525","is_default":"1",
     * "dlyp_id":"0","zip_code":"366000","area_name":"广德县","city_name":"宣城市"},
     * "freight_result":{"state":"success","content":{"3":0},"allow_offpay":"0",
     * "allow_offpay_batch":{"3":false},"offpay_hash":"ecUmGMB5a4ihk4dI9ri2ZCpnZPuk_7.X8rtYVv9",
     * "offpay_hash_batch":"fWhJhke8OsnMRaTtqJTNWJ8FwYn-8YUa2fwwpSNnIPGkTTD"}}
     */

    private ResultEntity result;

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public ResultEntity getResult() {
        return result;
    }

    public static class ResultEntity {
        /**
         * store_cart_list : [{"store_id":"3","store_name":"HONNY恒尼",
         * "goods_list":[{"goods_num":1,"goods_id":835,"goods_commonid":"42","gc_id":"36",
         * "store_id":"3","goods_name":"兰精莫代尔舒适家居套装 白色 M","goods_price":"0.01",
         * "store_name":"HONNY恒尼","goods_image":"3_05094707412643832.jpg","transport_id":"0",
         * "goods_freight":"0.00","goods_vat":"0","goods_storage":"252",
         * "goods_storage_alarm":"0","is_fcode":"0","have_gift":"0","state":true,
         * "storage_state":true,"groupbuy_info":null,"xianshi_info":null,"cart_id":835,"bl_id":0,
         * "goods_total":"0.01","goods_image_url":"http://7xp9th.com1.z0.glb.clouddn
         * .com/shop/store/goods/3/3_05094707412643832_240.jpg"}],"store_goods_total":"0.01",
         * "store_mansong_rule_list":null,"store_voucher_list":[],"freight":"1"}]
         * freight_hash : qzAitB6a7BuY7jrMu3zbXxUZkLEbY8hHn9DPoJ12k6TCSFT_NnIK_WxYWj7Z2f4R3r-X
         * -LxkMv7U7FxBMx-kbH2Gkm6DT6M0H8CTzc1gnjrCV0Jj7U5xQub
         * address_info : {"address_id":"69","member_id":"20","true_name":"哈哈","area_id":"2324",
         * "city_id":"202","area_info":"安徽省宣城市广德县","address":"浩浩荡荡","tel_phone":"13156588525",
         * "mob_phone":"13156588525","is_default":"1","dlyp_id":"0","zip_code":"366000"}
         * ifshow_offpay : null
         * vat_hash : 5Dr9R-V9U4jv-kqkaEsui1mJCImoWXJx-Qu
         * inv_info : {"content":"不需要发票"}
         * available_predeposit : null
         * available_rc_balance : null
         * total_gold_coins : 0
         * default_address : {"address_id":"69","member_id":"20","true_name":"哈哈",
         * "area_id":"2324","city_id":"202","area_info":"安徽省宣城市广德县","address":"浩浩荡荡",
         * "tel_phone":"13156588525","mob_phone":"13156588525","is_default":"1","dlyp_id":"0",
         * "zip_code":"366000","area_name":"广德县","city_name":"宣城市"}
         * freight_result : {"state":"success","content":{"3":0},"allow_offpay":"0",
         * "allow_offpay_batch":{"3":false},"offpay_hash":"ecUmGMB5a4ihk4dI9ri2ZCpnZPuk_7
         * .X8rtYVv9","offpay_hash_batch":"fWhJhke8OsnMRaTtqJTNWJ8FwYn-8YUa2fwwpSNnIPGkTTD"}
         */

        private BuyListEntity buy_list;

        private String personal_info;

        public String getPersonal_info() {
            return personal_info;
        }

        public void setPersonal_info(String personal_info) {
            this.personal_info = personal_info;
        }

        public void setBuy_list(BuyListEntity buy_list) {
            this.buy_list = buy_list;
        }

        public BuyListEntity getBuy_list() {
            return buy_list;
        }

        public static class BuyListEntity {
            private String freight_hash;
            /**
             * address_id : 69
             * member_id : 20
             * true_name : 哈哈
             * area_id : 2324
             * city_id : 202
             * area_info : 安徽省宣城市广德县
             * address : 浩浩荡荡
             * tel_phone : 13156588525
             * mob_phone : 13156588525
             * is_default : 1
             * dlyp_id : 0
             * zip_code : 366000
             */

            private AddressInfoEntity address_info;
            private Object ifshow_offpay;
            private String vat_hash;
            /**
             * content : 不需要发票
             */

            private InvInfoEntity inv_info;
            private Object available_predeposit;
            private Object available_rc_balance;
            private float total_gold_coins;
            /**
             * address_id : 69
             * member_id : 20
             * true_name : 哈哈
             * area_id : 2324
             * city_id : 202
             * area_info : 安徽省宣城市广德县
             * address : 浩浩荡荡
             * tel_phone : 13156588525
             * mob_phone : 13156588525
             * is_default : 1
             * dlyp_id : 0
             * zip_code : 366000
             * area_name : 广德县
             * city_name : 宣城市
             */

            private DefaultAddressEntity default_address;
            /**
             * state : success
             * content : {"3":0}
             * allow_offpay : 0
             * allow_offpay_batch : {"3":false}
             * offpay_hash : ecUmGMB5a4ihk4dI9ri2ZCpnZPuk_7.X8rtYVv9
             * offpay_hash_batch : fWhJhke8OsnMRaTtqJTNWJ8FwYn-8YUa2fwwpSNnIPGkTTD
             */

            private FreightResultEntity freight_result;
            /**
             * store_id : 3
             * store_name : HONNY恒尼
             * goods_list : [{"goods_num":1,"goods_id":835,"goods_commonid":"42","gc_id":"36",
             * "store_id":"3","goods_name":"兰精莫代尔舒适家居套装 白色 M","goods_price":"0.01",
             * "store_name":"HONNY恒尼","goods_image":"3_05094707412643832.jpg","transport_id":"0",
             * "goods_freight":"0.00","goods_vat":"0","goods_storage":"252",
             * "goods_storage_alarm":"0","is_fcode":"0","have_gift":"0","state":true,
             * "storage_state":true,"groupbuy_info":null,"xianshi_info":null,"cart_id":835,
             * "bl_id":0,"goods_total":"0.01","goods_image_url":"http://7xp9th.com1.z0.glb
             * .clouddn.com/shop/store/goods/3/3_05094707412643832_240.jpg"}]
             * store_goods_total : 0.01
             * store_mansong_rule_list : null
             * store_voucher_list : []
             * freight : 1
             */

            private List<StoreCartListEntity> store_cart_list;

            public void setFreight_hash(String freight_hash) {
                this.freight_hash = freight_hash;
            }

            public void setAddress_info(AddressInfoEntity address_info) {
                this.address_info = address_info;
            }

            public void setIfshow_offpay(Object ifshow_offpay) {
                this.ifshow_offpay = ifshow_offpay;
            }

            public void setVat_hash(String vat_hash) {
                this.vat_hash = vat_hash;
            }

            public void setInv_info(InvInfoEntity inv_info) {
                this.inv_info = inv_info;
            }

            public void setAvailable_predeposit(Object available_predeposit) {
                this.available_predeposit = available_predeposit;
            }

            public void setAvailable_rc_balance(Object available_rc_balance) {
                this.available_rc_balance = available_rc_balance;
            }

            public void setTotal_gold_coins(float total_gold_coins) {
                this.total_gold_coins = total_gold_coins;
            }

            public void setDefault_address(DefaultAddressEntity default_address) {
                this.default_address = default_address;
            }

            public void setFreight_result(FreightResultEntity freight_result) {
                this.freight_result = freight_result;
            }

            public void setStore_cart_list(List<StoreCartListEntity> store_cart_list) {
                this.store_cart_list = store_cart_list;
            }

            public String getFreight_hash() {
                return freight_hash;
            }

            public AddressInfoEntity getAddress_info() {
                return address_info;
            }

            public Object getIfshow_offpay() {
                return ifshow_offpay;
            }

            public String getVat_hash() {
                return vat_hash;
            }

            public InvInfoEntity getInv_info() {
                return inv_info;
            }

            public Object getAvailable_predeposit() {
                return available_predeposit;
            }

            public Object getAvailable_rc_balance() {
                return available_rc_balance;
            }

            public float getTotal_gold_coins() {
                return total_gold_coins;
            }

            public DefaultAddressEntity getDefault_address() {
                return default_address;
            }

            public FreightResultEntity getFreight_result() {
                return freight_result;
            }

            public List<StoreCartListEntity> getStore_cart_list() {
                return store_cart_list;
            }

            public static class AddressInfoEntity {
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
                //                private String dlyp_id;
                private String zip_code;
                private String is_visible;

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

//                public void setDlyp_id(String dlyp_id) {
//                    this.dlyp_id = dlyp_id;
//                }

                public void setZip_code(String zip_code) {
                    this.zip_code = zip_code;
                }

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

//                public String getDlyp_id() {
//                    return dlyp_id;
//                }

                public String getZip_code() {
                    return zip_code;
                }
            }

            public static class InvInfoEntity {
                private String content;

                public void setContent(String content) {
                    this.content = content;
                }

                public String getContent() {
                    return content;
                }
            }

            public static class DefaultAddressEntity {
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
                //                private String dlyp_id;
                private String zip_code;
                private String area_name;
                private String city_name;

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

//                public void setDlyp_id(String dlyp_id) {
//                    this.dlyp_id = dlyp_id;
//                }

                public void setZip_code(String zip_code) {
                    this.zip_code = zip_code;
                }

                public void setArea_name(String area_name) {
                    this.area_name = area_name;
                }

                public void setCity_name(String city_name) {
                    this.city_name = city_name;
                }

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

//                public String getDlyp_id() {
//                    return dlyp_id;
//                }

                public String getZip_code() {
                    return zip_code;
                }

                public String getArea_name() {
                    return area_name;
                }

                public String getCity_name() {
                    return city_name;
                }
            }

            public static class FreightResultEntity {
                private String state;
                private String allow_offpay;
                private String offpay_hash;
                private String offpay_hash_batch;

                public void setState(String state) {
                    this.state = state;
                }

                public void setAllow_offpay(String allow_offpay) {
                    this.allow_offpay = allow_offpay;
                }

                public void setOffpay_hash(String offpay_hash) {
                    this.offpay_hash = offpay_hash;
                }

                public void setOffpay_hash_batch(String offpay_hash_batch) {
                    this.offpay_hash_batch = offpay_hash_batch;
                }

                public String getState() {
                    return state;
                }

                public String getAllow_offpay() {
                    return allow_offpay;
                }

                public String getOffpay_hash() {
                    return offpay_hash;
                }

                public String getOffpay_hash_batch() {
                    return offpay_hash_batch;
                }
            }

            public static class StoreCartListEntity {
                private String store_id;
                private String store_name;
                private String store_goods_total;
                private Object store_mansong_rule_list;
                private String freight;
                /**
                 * goods_num : 1
                 * goods_id : 835
                 * goods_commonid : 42
                 * gc_id : 36
                 * store_id : 3
                 * goods_name : 兰精莫代尔舒适家居套装 白色 M
                 * goods_price : 0.01
                 * store_name : HONNY恒尼
                 * goods_image : 3_05094707412643832.jpg
                 * transport_id : 0
                 * goods_freight : 0.00
                 * goods_vat : 0
                 * goods_storage : 252
                 * goods_storage_alarm : 0
                 * is_fcode : 0
                 * have_gift : 0
                 * state : true
                 * storage_state : true
                 * groupbuy_info : null
                 * xianshi_info : null
                 * cart_id : 835
                 * bl_id : 0
                 * goods_total : 0.01
                 * goods_image_url : http://7xp9th.com1.z0.glb.clouddn.com/shop/store/goods/3/3_05094707412643832_240.jpg
                 */

                private List<GoodsListEntity> goods_list;
                private List<?> store_voucher_list;

                public void setStore_id(String store_id) {
                    this.store_id = store_id;
                }

                public void setStore_name(String store_name) {
                    this.store_name = store_name;
                }

                public void setStore_goods_total(String store_goods_total) {
                    this.store_goods_total = store_goods_total;
                }

                public void setStore_mansong_rule_list(Object store_mansong_rule_list) {
                    this.store_mansong_rule_list = store_mansong_rule_list;
                }

                public void setFreight(String freight) {
                    this.freight = freight;
                }

                public void setGoods_list(List<GoodsListEntity> goods_list) {
                    this.goods_list = goods_list;
                }

                public void setStore_voucher_list(List<?> store_voucher_list) {
                    this.store_voucher_list = store_voucher_list;
                }

                public String getStore_id() {
                    return store_id;
                }

                public String getStore_name() {
                    return store_name;
                }

                public String getStore_goods_total() {
                    return store_goods_total;
                }

                public Object getStore_mansong_rule_list() {
                    return store_mansong_rule_list;
                }

                public String getFreight() {
                    return freight;
                }

                public List<GoodsListEntity> getGoods_list() {
                    return goods_list;
                }

                public List<?> getStore_voucher_list() {
                    return store_voucher_list;
                }

                public static class GoodsListEntity {
                    private int goods_num;
                    private int goods_id;
                    private String goods_commonid;
                    private String gc_id;
                    private String store_id;
                    private String goods_name;
                    private String goods_price;
                    private String store_name;
                    private String goods_image;
                    private String transport_id;
                    private String goods_freight;
                    private String goods_vat;
                    private String goods_storage;
                    private String goods_storage_alarm;
                    private String is_fcode;
                    private String have_gift;
                    private boolean state;
                    private boolean storage_state;
                    private Object groupbuy_info;
                    private Object xianshi_info;
                    private int cart_id;
                    private int bl_id;
                    private String goods_total;
                    private String goods_image_url;

                    public void setGoods_num(int goods_num) {
                        this.goods_num = goods_num;
                    }

                    public void setGoods_id(int goods_id) {
                        this.goods_id = goods_id;
                    }

                    public void setGoods_commonid(String goods_commonid) {
                        this.goods_commonid = goods_commonid;
                    }

                    public void setGc_id(String gc_id) {
                        this.gc_id = gc_id;
                    }

                    public void setStore_id(String store_id) {
                        this.store_id = store_id;
                    }

                    public void setGoods_name(String goods_name) {
                        this.goods_name = goods_name;
                    }

                    public void setGoods_price(String goods_price) {
                        this.goods_price = goods_price;
                    }

                    public void setStore_name(String store_name) {
                        this.store_name = store_name;
                    }

                    public void setGoods_image(String goods_image) {
                        this.goods_image = goods_image;
                    }

                    public void setTransport_id(String transport_id) {
                        this.transport_id = transport_id;
                    }

                    public void setGoods_freight(String goods_freight) {
                        this.goods_freight = goods_freight;
                    }

                    public void setGoods_vat(String goods_vat) {
                        this.goods_vat = goods_vat;
                    }

                    public void setGoods_storage(String goods_storage) {
                        this.goods_storage = goods_storage;
                    }

                    public void setGoods_storage_alarm(String goods_storage_alarm) {
                        this.goods_storage_alarm = goods_storage_alarm;
                    }

                    public void setIs_fcode(String is_fcode) {
                        this.is_fcode = is_fcode;
                    }

                    public void setHave_gift(String have_gift) {
                        this.have_gift = have_gift;
                    }

                    public void setState(boolean state) {
                        this.state = state;
                    }

                    public void setStorage_state(boolean storage_state) {
                        this.storage_state = storage_state;
                    }

                    public void setGroupbuy_info(Object groupbuy_info) {
                        this.groupbuy_info = groupbuy_info;
                    }

                    public void setXianshi_info(Object xianshi_info) {
                        this.xianshi_info = xianshi_info;
                    }

                    public void setCart_id(int cart_id) {
                        this.cart_id = cart_id;
                    }

                    public void setBl_id(int bl_id) {
                        this.bl_id = bl_id;
                    }

                    public void setGoods_total(String goods_total) {
                        this.goods_total = goods_total;
                    }

                    public void setGoods_image_url(String goods_image_url) {
                        this.goods_image_url = goods_image_url;
                    }

                    public int getGoods_num() {
                        return goods_num;
                    }

                    public int getGoods_id() {
                        return goods_id;
                    }

                    public String getGoods_commonid() {
                        return goods_commonid;
                    }

                    public String getGc_id() {
                        return gc_id;
                    }

                    public String getStore_id() {
                        return store_id;
                    }

                    public String getGoods_name() {
                        return goods_name;
                    }

                    public String getGoods_price() {
                        return goods_price;
                    }

                    public String getStore_name() {
                        return store_name;
                    }

                    public String getGoods_image() {
                        return goods_image;
                    }

                    public String getTransport_id() {
                        return transport_id;
                    }

                    public String getGoods_freight() {
                        return goods_freight;
                    }

                    public String getGoods_vat() {
                        return goods_vat;
                    }

                    public String getGoods_storage() {
                        return goods_storage;
                    }

                    public String getGoods_storage_alarm() {
                        return goods_storage_alarm;
                    }

                    public String getIs_fcode() {
                        return is_fcode;
                    }

                    public String getHave_gift() {
                        return have_gift;
                    }

                    public boolean isState() {
                        return state;
                    }

                    public boolean isStorage_state() {
                        return storage_state;
                    }

                    public Object getGroupbuy_info() {
                        return groupbuy_info;
                    }

                    public Object getXianshi_info() {
                        return xianshi_info;
                    }

                    public int getCart_id() {
                        return cart_id;
                    }

                    public int getBl_id() {
                        return bl_id;
                    }

                    public String getGoods_total() {
                        return goods_total;
                    }

                    public String getGoods_image_url() {
                        return goods_image_url;
                    }
                }
            }
        }
    }
}

package com.centling.entity;

import java.util.List;

public class HomeBean {

    /**
     * image : http://7xrahh.com2.z0.glb.qiniucdn.com/mobile/special/s1/s1_05105033016575214.jpg
     * type : season
     * data : season=spring&amp;keyword=
     */

    private List<AdvListEntity> advList;
    /**
     * image : http://7xrahh.com2.z0.glb.qiniucdn.com/mobile/special/s1/s1_05105055139715413.jpg
     * type : season
     * data : season=spring&amp;keyword=
     */

    private List<EveryQuarterEntity> everyQuarter;
    /**
     * image : http://7xrahh.com2.z0.glb.qiniucdn.com/mobile/special/s1/s1_05037764367691448.png
     * type : keyword
     * data : 内裤
     */

    private List<GoodsRecommendEntity> goodsRecommend;
    /**
     * image : http://7xrahh.com2.z0.glb.qiniucdn.com/mobile/special/s1/s1_05115468874320962.jpg
     * type : url
     * data : /mobile/index.php?act=goods&amp;op=goods_discount
     */

    private List<GoodsDiscountEntity> goodsDiscount;

    public List<AdvListEntity> getAdvList() {
        return advList;
    }

    public void setAdvList(List<AdvListEntity> advList) {
        this.advList = advList;
    }

    public List<EveryQuarterEntity> getEveryQuarter() {
        return everyQuarter;
    }

    public void setEveryQuarter(List<EveryQuarterEntity> everyQuarter) {
        this.everyQuarter = everyQuarter;
    }

    public List<GoodsRecommendEntity> getGoodsRecommend() {
        return goodsRecommend;
    }

    public void setGoodsRecommend(List<GoodsRecommendEntity> goodsRecommend) {
        this.goodsRecommend = goodsRecommend;
    }

    public List<GoodsDiscountEntity> getGoodsDiscount() {
        return goodsDiscount;
    }

    public void setGoodsDiscount(List<GoodsDiscountEntity> goodsDiscount) {
        this.goodsDiscount = goodsDiscount;
    }

    public static class AdvListEntity {
        private String image;
        private String type;
        private String data;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class EveryQuarterEntity {
        private String image;
        private String type;
        private String data;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class GoodsRecommendEntity {
        private String image;
        private String type;
        private String data;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class GoodsDiscountEntity {
        private String image;
        private String type;
        private String data;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}

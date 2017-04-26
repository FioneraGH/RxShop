package com.centling.entity;

import java.util.List;

/**
 * GoldsRecordBean
 * Created by fionera on 16-2-22.
 */
public class GoldsRecordBean {

    private List<GoldDetailEntity> gold_detail;

    public void setGold_detail(List<GoldDetailEntity> gold_detail) {
        this.gold_detail = gold_detail;
    }

    public List<GoldDetailEntity> getGold_detail() {
        return gold_detail;
    }

    public static class GoldDetailEntity {
        private String lg_title;
        /**
         * lg_id : 129
         * lg_member_id : 41
         * lg_member_name : 17854204157
         * lg_type : refund
         * lg_av_amount : 0.01
         * lg_freeze_amount : 0.00
         * lg_add_time : 2016-02-28 16:26:11
         * lg_desc : 确认退款
         * lg_time : 1456647971
         */

        private List<LgContentEntity> lg_content;

        public void setLg_title(String lg_title) {
            this.lg_title = lg_title;
        }

        public void setLg_content(List<LgContentEntity> lg_content) {
            this.lg_content = lg_content;
        }

        public String getLg_title() {
            return lg_title;
        }

        public List<LgContentEntity> getLg_content() {
            return lg_content;
        }

        public static class LgContentEntity {
            private String lg_id;
            private String lg_member_id;
            private String lg_member_name;
            private String lg_type;
            private String lg_av_amount;
            private String lg_freeze_amount;
            private String lg_add_time;
            private String lg_desc;
            private String lg_time;

            public void setLg_id(String lg_id) {
                this.lg_id = lg_id;
            }

            public void setLg_member_id(String lg_member_id) {
                this.lg_member_id = lg_member_id;
            }

            public void setLg_member_name(String lg_member_name) {
                this.lg_member_name = lg_member_name;
            }

            public void setLg_type(String lg_type) {
                this.lg_type = lg_type;
            }

            public void setLg_av_amount(String lg_av_amount) {
                this.lg_av_amount = lg_av_amount;
            }

            public void setLg_freeze_amount(String lg_freeze_amount) {
                this.lg_freeze_amount = lg_freeze_amount;
            }

            public void setLg_add_time(String lg_add_time) {
                this.lg_add_time = lg_add_time;
            }

            public void setLg_desc(String lg_desc) {
                this.lg_desc = lg_desc;
            }

            public void setLg_time(String lg_time) {
                this.lg_time = lg_time;
            }

            public String getLg_id() {
                return lg_id;
            }

            public String getLg_member_id() {
                return lg_member_id;
            }

            public String getLg_member_name() {
                return lg_member_name;
            }

            public String getLg_type() {
                return lg_type;
            }

            public String getLg_av_amount() {
                return lg_av_amount;
            }

            public String getLg_freeze_amount() {
                return lg_freeze_amount;
            }

            public String getLg_add_time() {
                return lg_add_time;
            }

            public String getLg_desc() {
                return lg_desc;
            }

            public String getLg_time() {
                return lg_time;
            }
        }
    }
}

package com.centling.entity;

import java.util.List;

/**
 * GoldsRuleBean
 * Created by fionera on 16-2-23.
 */
public class GoldsRuleBean {

    private List<AmountRelusEntity> amount_relus;

    public void setAmount_relus(List<AmountRelusEntity> amount_relus) {
        this.amount_relus = amount_relus;
    }

    public List<AmountRelusEntity> getAmount_relus() {
        return amount_relus;
    }

    public static class AmountRelusEntity {
        private String r_id;
        private String recharge_amount;
        private String given_amount;

        public void setR_id(String r_id) {
            this.r_id = r_id;
        }

        public void setRecharge_amount(String recharge_amount) {
            this.recharge_amount = recharge_amount;
        }

        public void setGiven_amount(String given_amount) {
            this.given_amount = given_amount;
        }

        public String getR_id() {
            return r_id;
        }

        public String getRecharge_amount() {
            return recharge_amount;
        }

        public String getGiven_amount() {
            return given_amount;
        }
    }
}

package com.mobitant.bestfood.item;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int seq;
    private String seller_nickname;
    private String buyer_nickname;
    private String created_at;
    private String updated_at;
    private String realName;
    private String account_number;
    private String exp_date;
    private String card_holder;
    private String card_number;
    private String buyer_phone;

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    private String pay_method;
    private String buy_method;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getSeller_nickname() {
        return seller_nickname;
    }

    public void setSeller_nickname(String seller_nickname) {
        this.seller_nickname = seller_nickname;
    }

    public String getBuyer_nickname() {
        return buyer_nickname;
    }

    public void setBuyer_nickname(String buyer_nickname) {
        this.buyer_nickname = buyer_nickname;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getCard_holder() {
        return card_holder;
    }

    public void setCard_holder(String card_holder) {
        this.card_holder = card_holder;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getBuy_method() {
        return buy_method;
    }

    public void setBuy_method(String buy_method) {
        this.buy_method = buy_method;
    }
}

package com.example.quanlysieuthi;

import java.io.Serializable;

public class ORDER implements Serializable {
    String ord_product;
    String ord_address;
    String ord_quality;
    String ord_reiceve;
    String ord_total;
    String ord_time;
    String ord_date;
    String ord_phone;
    String ord_delivery;
    String ord_discount;
    String ord_payment;
    String ord_img;
    String ord_state;

    public  ORDER()
    {}
    public ORDER(String ord_product, String ord_address, String ord_quality, String ord_reiceve, String ord_total, String ord_time, String ord_date, String ord_phone, String ord_delivery, String ord_discount, String ord_payment, String ord_img, String ord_state) {
        this.ord_product = ord_product;
        this.ord_address = ord_address;
        this.ord_quality = ord_quality;
        this.ord_reiceve = ord_reiceve;
        this.ord_total = ord_total;
        this.ord_time = ord_time;
        this.ord_date = ord_date;
        this.ord_phone = ord_phone;
        this.ord_delivery = ord_delivery;
        this.ord_discount = ord_discount;
        this.ord_payment = ord_payment;
        this.ord_img = ord_img;
        this.ord_state = ord_state;
    }

    public String getOrd_state() {
        return ord_state;
    }

    public void setOrd_state(String ord_state) {
        this.ord_state = ord_state;
    }

    public String getOrd_img() {
        return ord_img;
    }

    public void setOrd_img(String ord_img) {
        this.ord_img = ord_img;
    }

    public String getOrd_payment() {
        return ord_payment;
    }

    public void setOrd_payment(String ord_payment) {
        this.ord_payment = ord_payment;
    }

    public String getOrd_delivery() {
        return ord_delivery;
    }

    public void setOrd_delivery(String ord_delivery) {
        this.ord_delivery = ord_delivery;
    }

    public String getOrd_discount() {
        return ord_discount;
    }

    public void setOrd_discount(String ord_discount) {
        this.ord_discount = ord_discount;
    }

    public String getOrd_date() {
        return ord_date;
    }

    public void setOrd_date(String ord_date) {
        this.ord_date = ord_date;
    }

    public String getOrd_phone() {
        return ord_phone;
    }

    public void setOrd_phone(String ord_phone) {
        this.ord_phone = ord_phone;
    }

    public String getOrd_time() {
        return ord_time;
    }

    public void setOrd_time(String ord_time) {
        this.ord_time = ord_time;
    }
    public String getOrd_product() {
        return ord_product;
    }

    public void setOrd_product(String ord_product) {
        this.ord_product = ord_product;
    }

    public String getOrd_address() {
        return ord_address;
    }

    public void setOrd_address(String ord_address) {
        this.ord_address = ord_address;
    }

    public String getOrd_quality() {
        return ord_quality;
    }

    public void setOrd_quality(String ord_quality) {
        this.ord_quality = ord_quality;
    }

    public String getOrd_reiceve() {
        return ord_reiceve;
    }

    public void setOrd_reiceve(String ord_reiceve) {
        this.ord_reiceve = ord_reiceve;
    }

    public String getOrd_total() {
        return ord_total;
    }

    public void setOrd_total(String ord_total) {
        this.ord_total = ord_total;
    }
}

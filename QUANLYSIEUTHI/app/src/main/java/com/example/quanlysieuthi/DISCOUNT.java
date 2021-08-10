package com.example.quanlysieuthi;

public class DISCOUNT {
    String dis_code,dis_describe,dis_exp,dis_img,dis_required,dis_start,dis_value;
    int dis_type;

    public  DISCOUNT()
    {}
    public DISCOUNT(String dis_code, String dis_describe, String dis_exp, String dis_img, String dis_required, String dis_start, String dis_value, int dis_type) {
        this.dis_code = dis_code;
        this.dis_describe = dis_describe;
        this.dis_exp = dis_exp;
        this.dis_img = dis_img;
        this.dis_required = dis_required;
        this.dis_start = dis_start;
        this.dis_value = dis_value;
        this.dis_type = dis_type;
    }

    public int getDis_type() {
        return dis_type;
    }

    public void setDis_type(int dis_type) {
        this.dis_type = dis_type;
    }

    public String getDis_code() {
        return dis_code;
    }

    public void setDis_code(String dis_code) {
        this.dis_code = dis_code;
    }

    public String getDis_describe() {
        return dis_describe;
    }

    public void setDis_describe(String dis_describe) {
        this.dis_describe = dis_describe;
    }

    public String getDis_exp() {
        return dis_exp;
    }

    public void setDis_exp(String dis_exp) {
        this.dis_exp = dis_exp;
    }

    public String getDis_img() {
        return dis_img;
    }

    public void setDis_img(String dis_img) {
        this.dis_img = dis_img;
    }

    public String getDis_required() {
        return dis_required;
    }

    public void setDis_required(String dis_required) {
        this.dis_required = dis_required;
    }

    public String getDis_start() {
        return dis_start;
    }

    public void setDis_start(String dis_start) {
        this.dis_start = dis_start;
    }

    public String getDis_value() {
        return dis_value;
    }

    public void setDis_value(String dis_value) {
        this.dis_value = dis_value;
    }
}

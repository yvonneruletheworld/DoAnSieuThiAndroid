package com.example.quanlysieuthi;

public class PAYMENT {
    String pm_des, pm_img, pm_limit, pm_name, pm_value;

    public PAYMENT()
    {}
    public PAYMENT(String pm_des, String pm_img, String pm_limit, String pm_name, String pm_value) {
        this.pm_des = pm_des;
        this.pm_img = pm_img;
        this.pm_limit = pm_limit;
        this.pm_name = pm_name;
        this.pm_value = pm_value;
    }

    public String getPm_des() {
        return pm_des;
    }

    public void setPm_des(String pm_des) {
        this.pm_des = pm_des;
    }

    public String getPm_img() {
        return pm_img;
    }

    public void setPm_img(String pm_img) {
        this.pm_img = pm_img;
    }

    public String getPm_limit() {
        return pm_limit;
    }

    public void setPm_limit(String pm_limit) {
        this.pm_limit = pm_limit;
    }

    public String getPm_name() {
        return pm_name;
    }

    public void setPm_name(String pm_name) {
        this.pm_name = pm_name;
    }

    public String getPm_value() {
        return pm_value;
    }

    public void setPm_value(String pm_value) {
        this.pm_value = pm_value;
    }
}

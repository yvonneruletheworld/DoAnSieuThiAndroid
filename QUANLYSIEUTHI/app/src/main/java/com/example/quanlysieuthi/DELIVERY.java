package com.example.quanlysieuthi;

public class DELIVERY {
    String gh_des, gh_phi, gh_name, gh_temp;

    public DELIVERY()
    {

    }

    public DELIVERY(String gh_des, String gh_phi, String gh_name, String gh_temp) {
        this.gh_des = gh_des;
        this.gh_phi = gh_phi;
        this.gh_name = gh_name;
        this.gh_temp = gh_temp;
    }

    public String getGh_temp() {
        return gh_temp;
    }

    public void setGh_temp(String gh_temp) {
        this.gh_temp = gh_temp;
    }

    public String getGh_des() {
        return gh_des;
    }

    public void setGh_des(String gh_des) {
        this.gh_des = gh_des;
    }

    public String getGh_phi() {
        return gh_phi;
    }

    public void setGh_phi(String gh_phi) {
        this.gh_phi = gh_phi;
    }

    public String getGh_name() {
        return gh_name;
    }

    public void setGh_name(String gh_name) {
        this.gh_name = gh_name;
    }
}

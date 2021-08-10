package com.example.quanlysieuthi;

public class RATING {
    String phone;
    String cmt;
    String rat;

    public RATING()
    {

    }
    public RATING(String phone, String cmt, String rat) {
        this.phone = phone;
        this.cmt = cmt;
        this.rat = rat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public String getRat() {
        return rat;
    }

    public void setRat(String rat) {
        this.rat = rat;
    }
}

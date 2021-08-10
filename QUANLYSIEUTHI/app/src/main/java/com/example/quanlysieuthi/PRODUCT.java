package com.example.quanlysieuthi;

import java.io.Serializable;

public class PRODUCT implements Serializable {
    String prd_hinh;
    String prd_ten;
    String prd_gia;
    String prd_dongia;
    String prd_tinhtrang;
    String prd_soton;
    String prd_dungtic;
    String prd_congdung;
    String prd_nhanhang;
    String prd_xuatxu;
    String prd_loai;

    public  PRODUCT()
    {

    }

    public String getPrd_loai() {
        return prd_loai;
    }

    public void setPrd_loai(String prd_loai) {
        this.prd_loai = prd_loai;
    }

    public String getPrd_dongia() {
        return prd_dongia;
    }

    public void setPrd_dongia(String prd_dongia) {
        this.prd_dongia = prd_dongia;
    }

    public String getPrd_tinhtrang() {
        return prd_tinhtrang;
    }

    public void setPrd_tinhtrang(String prd_tinhtrang) {
        this.prd_tinhtrang = prd_tinhtrang;
    }

    public String getPrd_soton() {
        return prd_soton;
    }

    public void setPrd_soton(String prd_soton) {
        this.prd_soton = prd_soton;
    }

    public String getPrd_dungtic() {
        return prd_dungtic;
    }

    public void setPrd_dungtic(String prd_dungtic) {
        this.prd_dungtic = prd_dungtic;
    }

    public String getPrd_congdung() {
        return prd_congdung;
    }

    public void setPrd_congdung(String prd_congdung) {
        this.prd_congdung = prd_congdung;
    }

    public String getPrd_nhanhang() {
        return prd_nhanhang;
    }

    public void setPrd_nhanhang(String prd_nhanhang) {
        this.prd_nhanhang = prd_nhanhang;
    }

    public String getPrd_xuatxu() {
        return prd_xuatxu;
    }

    public void setPrd_xuatxu(String prd_xuatxu) {
        this.prd_xuatxu = prd_xuatxu;
    }

    public PRODUCT(String prd_hinh, String prd_ten, String prd_gia, String prd_dongia, String prd_tinhtrang, String prd_soton, String prd_dungtic, String prd_congdung, String prd_nhanhang, String prd_xuatxu) {
        this.prd_hinh = prd_hinh;
        this.prd_ten = prd_ten;
        this.prd_gia = prd_gia;
        this.prd_dongia = prd_dongia;
        this.prd_tinhtrang = prd_tinhtrang;
        this.prd_soton = prd_soton;
        this.prd_dungtic = prd_dungtic;
        this.prd_congdung = prd_congdung;
        this.prd_nhanhang = prd_nhanhang;
        this.prd_xuatxu = prd_xuatxu;
    }

    public String getPrd_hinh() {
        return prd_hinh;
    }

    public void setPrd_hinh(String prd_hinh) {
        this.prd_hinh = prd_hinh;
    }

    public String getPrd_ten() {
        return prd_ten;
    }

    public void setPrd_ten(String prd_ten) {
        this.prd_ten = prd_ten;
    }

    public String getPrd_gia() {
        return prd_gia;
    }

    public void setPrd_gia(String prd_gia) {
        this.prd_gia = prd_gia;
    }
}

package com.example.quanlysieuthi;

import android.os.Parcelable;

import java.io.Serializable;

public class CART implements Serializable {
    private String name,img, price,date,time,cost;
    private  int quantity;
    private  float totalPrice;

    public CART ()
    {

    }

    public CART(String name, String img, String price, String date, String time, String cost, int quantity, float totalPrice) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}

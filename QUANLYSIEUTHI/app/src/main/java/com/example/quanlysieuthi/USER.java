package com.example.quanlysieuthi;

import java.io.Serializable;

public class USER implements Serializable {
    String UserID;
    String UserName;
//    String Phone;
    String DoB;
    String Address;
    String Pass;
    String Hinh;
    String ndphone;
    int Gender;

    public USER()
    {}

    public USER(String userID, String userName, String DOB, String address, String pass, String hinh, int gender, String ndphone) {
        UserID = userID;
        UserName = userName;
//        Phone = phone;
        DoB = DOB;
        Address = address;
        Pass = pass;
        Hinh = hinh;
        Gender = gender;
        this.ndphone = ndphone;
    }

    public String getNdphone() {
        return ndphone;
    }

    public void setNdphone(String ndphone) {
        this.ndphone = ndphone;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getHinh() {
        return Hinh;
    }

    public void setHinh(String hinh) {
        Hinh = hinh;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

//    public String getPhone() {
//        return Phone;
//    }
//
//    public void setPhone(String phone) {
//        Phone = phone;
//    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }
}

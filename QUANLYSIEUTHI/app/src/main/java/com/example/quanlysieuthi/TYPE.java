package com.example.quanlysieuthi;

import java.io.Serializable;

public class TYPE implements Serializable {
    String Name;
    String Hinh;

    public TYPE ()
    {

    }

    public TYPE(String name, String hinh) {
        Name = name;
        Hinh = hinh;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHinh() {
        return Hinh;
    }

    public void setHinh(String hinh) {
        Hinh = hinh;
    }
}

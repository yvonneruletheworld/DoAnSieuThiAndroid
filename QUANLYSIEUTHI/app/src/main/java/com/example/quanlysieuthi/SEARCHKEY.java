package com.example.quanlysieuthi;

public class SEARCHKEY {
    String srch_key;
    double srch_rat;

    public SEARCHKEY ()
    {}
    public SEARCHKEY(String srch_key, int srch_rat) {
        this.srch_key = srch_key;
        this.srch_rat = srch_rat;
    }

    public String getSrch_key() {
        return srch_key;
    }

    public void setSrch_key(String srch_key) {
        this.srch_key = srch_key;
    }

    public double getSrch_rat() {
        return srch_rat;
    }

    public void setSrch_rat(double srch_rat) {
        this.srch_rat = srch_rat;
    }
}

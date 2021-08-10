package com.example.quanlysieuthi;

import android.view.View;

public interface ItemClickListener {
    void  onClick (View v , int position, boolean isLong);
    void discountClickItem (DISCOUNT dis, int type);
    void deliveryClickItem (DELIVERY del);
    void paymentClickItem (PAYMENT pay);
}

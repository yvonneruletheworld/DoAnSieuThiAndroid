package com.example.quanlysieuthi;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess (List<CART> cartList);
    void onCartLoadFailed (String message);
}

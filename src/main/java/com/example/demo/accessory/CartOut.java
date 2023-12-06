package com.example.demo.accessory;

import com.example.demo.entity.Cart;

public class CartOut {
    public Cart cartInfo;
    public ProductWithPhoto productInfo;

    public CartOut(Cart cartInfo, ProductWithPhoto productInfo) {
        this.cartInfo = cartInfo;
        this.productInfo = productInfo;
    }

    public Cart getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(Cart cartInfo) {
        this.cartInfo = cartInfo;
    }

    public ProductWithPhoto getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductWithPhoto productInfo) {
        this.productInfo = productInfo;
    }
}

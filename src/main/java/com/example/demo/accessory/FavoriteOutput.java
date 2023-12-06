package com.example.demo.accessory;

import com.example.demo.entity.Favorites;
import com.example.demo.entity.Reservation;

public class FavoriteOutput {
    private Favorites favorite;
    private ProductWithPhoto productInfo;

    public FavoriteOutput(Favorites favorite, ProductWithPhoto productInfo) {
        this.favorite = favorite;
        this.productInfo = productInfo;
    }

    public Favorites getFavorite() {
        return favorite;
    }

    public void setFavoriteInfo(Favorites reservationInfo) {
        this.favorite = reservationInfo;
    }

    public ProductWithPhoto getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductWithPhoto productInfo) {
        this.productInfo = productInfo;
    }
}

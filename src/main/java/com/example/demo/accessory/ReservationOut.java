package com.example.demo.accessory;

import com.example.demo.entity.Reservation;

public class ReservationOut {
    private Reservation reservationInfo;
    private ProductWithPhoto productInfo;

    public ReservationOut(Reservation reservationInfo, ProductWithPhoto productInfo) {
        this.reservationInfo = reservationInfo;
        this.productInfo = productInfo;
    }

    public Reservation getReservationInfo() {
        return reservationInfo;
    }

    public void setReservationInfo(Reservation reservationInfo) {
        this.reservationInfo = reservationInfo;
    }

    public ProductWithPhoto getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductWithPhoto productInfo) {
        this.productInfo = productInfo;
    }
}

package com.example.demo.accessory;

import com.example.demo.entity.Productaction;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;

import java.util.Date;

public class BuyInfo {
    private String productName;
    private int pid;
    private int uid;
    private String username;
    private Date date;
    private double rate;
    public BuyInfo(Products product, Productaction productaction, Users user, double _rate) {
        productName=product.getProduct_name();
        pid=product.getPid();
        uid=user.getUid();
        username=user.getUsername();
        date=productaction.getAction_time();
        rate=_rate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}

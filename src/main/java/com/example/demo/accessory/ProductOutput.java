package com.example.demo.accessory;

import com.example.demo.entity.Products;
import com.example.demo.entity.Users;

import java.util.Date;

public class ProductOutput {
    static class Seller {
        private int uid;
        private String username;
        private String avator;
        private int uploaded;
        private int soldout;

        public Seller(int uid, String username, String avator, int uploaded, int soldout) {
            this.uid = uid;
            this.username = username;
            this.avator = avator;
            this.uploaded = uploaded;
            this.soldout = soldout;
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

        public String getAvator() {
            return avator;
        }

        public void setAvator(String avator) {
            this.avator = avator;
        }

        public int getUploaded() {
            return uploaded;
        }

        public void setUploaded(int uploaded) {
            this.uploaded = uploaded;
        }

        public int getSoldout() {
            return soldout;
        }

        public void setSoldout(int soldout) {
            this.soldout = soldout;
        }
    }

    private int pid;
    private String image;
    private String title;
    private double price;
    private Date addTime;
    private int status;
    private Seller seller;

    public ProductOutput(Products product, Users user, String _image, int uploaded, int soldout) {
        pid = product.getPid();
        image = _image;
        title = product.getProduct_name();
        price = product.getPrice();
        addTime = product.getCreate_time();
        status = product.getProduct_status();
        seller = new Seller(user.getUid(), user.getUsername(), user.getUser_img_url(), uploaded, soldout);
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}

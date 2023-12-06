package com.example.demo.accessory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class ProductWithPhotoAndUser {
    static class Seller {
        private int uid;
        private String username;
        private String avator;

        public Seller(int uid, String username, String avator) {
            this.uid = uid;
            this.username = username;
            this.avator = avator;
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
    }

    @TableId(type = IdType.AUTO)
    private int pid;
    private String product_name;
    private double price;
    private int product_number;
    private String isbn;
    private int category_id;
    private int product_status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    private int created_by_uid;
    private String text;
    private List<String> image;
    private Seller seller;

    public ProductWithPhotoAndUser(Products product, Users user) {
        pid = product.getPid();
        product_name = product.getProduct_name();
        price = product.getPrice();
        product_number = product.getProduct_number();
        isbn = product.getIsbn();
        category_id = product.getCategory_id();
        product_status = product.getProduct_status();
        create_time = product.getCreate_time();
        update_time = product.getUpdate_time();
        created_by_uid = product.getCreated_by_uid();
        text = product.getText();
        seller = new Seller(user.getUid(), user.getUsername(), user.getUser_img_url());
    }

    public ProductWithPhotoAndUser(int pid, String product_name, double price, int product_number, String isbn, int category_id, int product_status, Date create_time, Date update_time, int created_by_uid, String text, List<String> image, Users user) {
        this.pid = pid;
        this.product_name = product_name;
        this.price = price;
        this.product_number = product_number;
        this.isbn = isbn;
        this.category_id = category_id;
        this.product_status = product_status;
        this.create_time = create_time;
        this.update_time = update_time;
        this.created_by_uid = created_by_uid;
        this.text = text;
        this.image = image;
        this.seller = new Seller(user.getUid(), user.getUsername(), user.getUser_img_url());
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProduct_number() {
        return product_number;
    }

    public void setProduct_number(int product_number) {
        this.product_number = product_number;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getProduct_status() {
        return product_status;
    }

    public void setProduct_status(int product_status) {
        this.product_status = product_status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public int getCreated_by_uid() {
        return created_by_uid;
    }

    public void setCreated_by_uid(int created_by_uid) {
        this.created_by_uid = created_by_uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}

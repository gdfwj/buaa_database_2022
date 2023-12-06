package com.example.demo.accessory;

import com.example.demo.entity.Users;

import java.util.Date;
import java.util.List;

public class UserInfo {
    private int uid;
    private String username;
    private String email;
    private String phone;
    private boolean is_admin;
    private Date createTime;
    private String avatar;
    private int uploaded;
    private int purchased;
    private int soldout;
    private double sellers_rating;
    private double buyers_rating;
    private List<ProductWithPhoto> items;
    private List<CommentWithUser> reviews;

    public UserInfo(Users user, int _uploaded, int _purchased, int _soldout, double _sellers_rating, double _buyers_rating, List<ProductWithPhoto> _items, List<CommentWithUser> _reviews) {
        uid = user.getUid();
        username = user.getUsername();
        email = user.getEmail();
        phone = user.getPhone();
        is_admin = user.isIs_admin();
        avatar = user.getUser_img_url();
        createTime = user.getRegister_time();
        uploaded = _uploaded;
        purchased = _purchased;
        soldout = _soldout;
        sellers_rating = _sellers_rating;
        buyers_rating = _buyers_rating;
        items = _items;
        reviews = _reviews;
    }

    public UserInfo(int uid, String username, String email, String phone, boolean is_admin, Date createTime, String avatar, int uploaded, int purchased, int soldout, double sellers_rating, double buyers_rating, List<ProductWithPhoto> items, List<CommentWithUser> reviews) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.is_admin = is_admin;
        this.createTime = createTime;
        this.avatar = avatar;
        this.uploaded = uploaded;
        this.purchased = purchased;
        this.soldout = soldout;
        this.sellers_rating = sellers_rating;
        this.buyers_rating = buyers_rating;
        this.items = items;
        this.reviews = reviews;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public int getSoldout() {
        return soldout;
    }

    public void setSoldout(int soldout) {
        this.soldout = soldout;
    }

    public double getSellers_rating() {
        return sellers_rating;
    }

    public void setSellers_rating(double sellers_rating) {
        this.sellers_rating = sellers_rating;
    }

    public double getBuyers_rating() {
        return buyers_rating;
    }

    public void setBuyers_rating(double buyers_rating) {
        this.buyers_rating = buyers_rating;
    }

    public List<ProductWithPhoto> getItems() {
        return items;
    }

    public void setItems(List<ProductWithPhoto> items) {
        this.items = items;
    }

    public List<CommentWithUser> getReviews() {
        return reviews;
    }

    public void setReviews(List<CommentWithUser> reviews) {
        this.reviews = reviews;
    }
}

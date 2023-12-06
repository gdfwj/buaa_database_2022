package com.example.demo.entity;

public class Rating {
    private int rid;
    private int uid1;
    private int uid2;
    private double rating;

    public Rating(int rid, int uid1, int uid2, double rating) {
        this.rid = rid;
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.rating = rating;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getUid1() {
        return uid1;
    }

    public void setUid1(int uid1) {
        this.uid1 = uid1;
    }

    public int getUid2() {
        return uid2;
    }

    public void setUid2(int uid2) {
        this.uid2 = uid2;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

package com.example.demo.entity;

public class Urlidtoproducts {
    private int urlid;
    private int pid;

    public Urlidtoproducts(int urlid, int pid) {
        this.urlid = urlid;
        this.pid = pid;
    }

    public int getUrlid() {
        return urlid;
    }

    public void setUrlid(int urlid) {
        this.urlid = urlid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}

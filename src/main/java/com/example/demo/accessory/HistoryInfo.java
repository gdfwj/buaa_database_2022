package com.example.demo.accessory;

import java.util.Date;

public class HistoryInfo {
    private int uid;
    private String username;
    private int pid;
    private String product_name;
    private Date time;
    private int action;

    public HistoryInfo(int uid, String username, int pid, String product_name, Date time, int action) {
        this.uid = uid;
        this.username = username;
        this.pid = pid;
        this.product_name = product_name;
        this.action = action;
        this.time = time;
    }
    public HistoryInfo(int uid, String username, int pid, String product_name, Date time) {
        this.uid = uid;
        this.username = username;
        this.pid = pid;
        this.product_name = product_name;
        this.action = 0;
        this.time = time;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

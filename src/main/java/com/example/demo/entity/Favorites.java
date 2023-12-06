package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class Favorites {
    @TableId
    private int pid;
    @TableId
    private int uid;
    private int number;
    private Date created_time;
    private Date updated_time;
    private Date expired_time;
    private boolean is_valid;

    public Favorites(int pid, int uid, int number, Date created_time, Date updated_time, Date expired_time, boolean is_valid) {
        this.pid = pid;
        this.uid = uid;
        this.number = number;
        this.created_time = created_time;
        this.updated_time = updated_time;
        this.expired_time = expired_time;
        this.is_valid = is_valid;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    public Date getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(Date updated_time) {
        this.updated_time = updated_time;
    }

    public Date getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(Date expired_time) {
        this.expired_time = expired_time;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }
}

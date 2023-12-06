package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class Productaction {
    @TableId(type = IdType.AUTO)
    private int paid;
    private int uid;
    private int pid;
    private int action_type;
    private Date action_time;
    private int status;

    public Productaction(int paid, int uid, int pid, int action_type, Date action_time, int status) {
        this.paid = paid;
        this.uid = uid;
        this.pid = pid;
        this.action_type = action_type;
        this.action_time = action_time;
        this.status = status;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public Date getAction_time() {
        return action_time;
    }

    public void setAction_time(Date action_time) {
        this.action_time = action_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

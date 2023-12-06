package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class Useraction {
    @TableId(type = IdType.AUTO)
    private int uaid;
    private int uid1;
    private int uid2;
    private int action_type;
    private Date action_time;
    private int status;

    public Useraction(int uaid, int uid1, int uid2, int action_type, Date action_time, int status) {
        this.uaid = uaid;
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.action_type = action_type;
        this.action_time = action_time;
        this.status = status;
    }

    public int getUaid() {
        return uaid;
    }

    public void setUaid(int uaid) {
        this.uaid = uaid;
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

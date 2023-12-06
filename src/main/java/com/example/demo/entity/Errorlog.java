package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class Errorlog {
    @TableId(type = IdType.AUTO)
    private int eid;
    private String msg;
    private int etype;
    private Date time;

    public Errorlog(int eid, String msg, int etype, Date time) {
        this.eid = eid;
        this.msg = msg;
        this.etype = etype;
        this.time = time;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getEtype() {
        return etype;
    }

    public void setEtype(int etype) {
        this.etype = etype;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

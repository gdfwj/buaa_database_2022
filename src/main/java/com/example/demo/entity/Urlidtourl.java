package com.example.demo.entity;

import org.apache.ibatis.annotations.Mapper;

public class Urlidtourl {
    private int urlid;
    private String url;

    public Urlidtourl(int urlid, String url) {
        this.urlid = urlid;
        this.url = url;
    }

    public int getUrlid() {
        return urlid;
    }

    public void setUrlid(int urlid) {
        this.urlid = urlid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

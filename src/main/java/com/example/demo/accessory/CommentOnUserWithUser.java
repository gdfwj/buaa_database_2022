package com.example.demo.accessory;

import com.example.demo.entity.Commentonuser;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommentOnUserWithUser {
    private String username;
    private String user_img_url;
    private int comment_id;
    private String comment_content;
    private int uid1;
    private int uid2;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    public CommentOnUserWithUser(String username, String user_img_url, Commentonuser commentonuser) {
        this.username = username;
        this.user_img_url = user_img_url;
        comment_id = commentonuser.getComment_id();
        comment_content = commentonuser.getComment_content();
        uid1 = commentonuser.getUid1();
        uid2 = commentonuser.getUid2();
        create_time = commentonuser.getCreate_time();
        update_time = commentonuser.getUpdate_time();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_img_url() {
        return user_img_url;
    }

    public void setUser_img_url(String user_img_url) {
        this.user_img_url = user_img_url;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
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
}

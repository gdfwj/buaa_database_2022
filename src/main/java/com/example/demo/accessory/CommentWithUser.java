package com.example.demo.accessory;

import com.example.demo.entity.Commentonuser;
import com.example.demo.entity.Comments;
import com.example.demo.entity.Users;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommentWithUser {
    private int uid;
    private String username;
    private String user_img_url;
    private String comment_content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date create_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_time;

    public CommentWithUser(Users user, Comments comment) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.user_img_url = user.getUser_img_url();
        this.comment_content = comment.getComment_content();
        this.create_time = comment.getCreate_time();
        this.update_time = comment.getUpdate_time();
    }

    public CommentWithUser(Users user, Commentonuser comment) {
        this.uid = user.getUid();
        this.username = user.getUsername();
        this.user_img_url = user.getUser_img_url();
        this.comment_content = comment.getComment_content();
        this.create_time = comment.getCreate_time();
        this.update_time = comment.getUpdate_time();
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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }
}

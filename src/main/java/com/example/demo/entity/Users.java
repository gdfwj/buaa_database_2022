package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Users {
    @TableId(type = IdType.AUTO)
    private int uid = 0;
    private String username;
    private String password;
    private String email;
    private String phone;
    private boolean is_admin;
    private int user_status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date register_time;
    private String token;
    private String user_img_url;

    public Users(String username, String password, String email, String phone, boolean is_admin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.is_admin = is_admin;
    }

    public Users(int uid, String username, String password, String email, String phone, boolean is_admin, int user_status, Date register_time, String token, String user_img_url) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.is_admin = is_admin;
        this.user_status = user_status;
        this.register_time = register_time;
        this.token = token;
        this.user_img_url = user_img_url;
    }

    public String getUser_img_url() {
        return user_img_url;
    }

    public void setUser_img_url(String user_img_url) {
        this.user_img_url = user_img_url;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", is_admin=" + is_admin +
                ", user_status=" + user_status +
                ", register_time=" + register_time +
                ", token='" + token + '\'' +
                ", user_img_url='" + user_img_url + '\'' +
                '}';
    }
}

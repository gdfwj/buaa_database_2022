package com.example.demo.accessory;

public class UserLessInfo {
    private int uid;
    private String username;
    private int uploaded;
    private boolean is_admin;
    private int status;

    public UserLessInfo(int uid, String username, int uploaded, boolean is_admin, int status) {
        this.uid = uid;
        this.username = username;
        this.uploaded = uploaded;
        this.is_admin = is_admin;
        this.status = status;
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

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

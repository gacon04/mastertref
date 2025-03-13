package com.example.mastertref.models;

import java.io.Serializable;

public class Taikhoan implements Serializable {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private Integer role;

    private String from;
    private String description;
    private Integer isActive;
    private String avatar;
    public Taikhoan() {

    }

    public Taikhoan(String username, String password, String email, Integer role, String fullname, String avatar, String from, String description, Integer isActive) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.fullname = fullname;
        this.avatar = avatar;
        this.from = from;
        this.description = description;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}

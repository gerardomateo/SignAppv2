package com.exp.sign;

import cn.bmob.v3.BmobObject;

public class BmobUser extends BmobObject {

    private String number;
    private String name;
    private String username;
    private String password;
    private String sex;
    private String phone;
    private String email;
    private int notify;

    public int getNotify() {
        return notify <= 0 ? 30 : notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

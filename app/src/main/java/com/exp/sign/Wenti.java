package com.exp.sign;

import cn.bmob.v3.BmobObject;

public class Wenti extends BmobObject {

    private String un;
    private String msg;

    @Override
    public String toString() {
        return msg;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

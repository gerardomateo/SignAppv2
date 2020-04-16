package com.exp.sign;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Car extends BmobObject {

    private long id;
    private int row;
    private int col;
    private String name;
    private String route; // string json
    private String position; // PositionJson
    private String carPer;
    private String chepai;
    private String phone;

    @Override
    public String toString() {
        return
                "\n车次：" + name +
                "\n车牌：" + chepai +
                "\n司机：" + carPer +
                "\n电话：" + phone +
                "\n总座位数：" + (row * col) + "\n";
    }

    public List<String> getLux() {
        if(TextUtils.isEmpty(route)) {
            return new ArrayList<>();
        }
        return JSONArray.parseArray(route, String.class);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCarPer() {
        return carPer;
    }

    public void setCarPer(String carPer) {
        this.carPer = carPer;
    }

    public String getChepai() {
        return chepai;
    }

    public void setChepai(String chepai) {
        this.chepai = chepai;
    }

    public List<PositionBean> getBs() {
        if(TextUtils.isEmpty(position)) {
            return new ArrayList<>();
        }
        return JSONArray.parseArray(position, PositionBean.class);
    }

    public List<PositionBean> getBsS() {
        if(TextUtils.isEmpty(position)) {
            return null;
        }
        List<PositionBean> bs = JSONArray.parseArray(position, PositionBean.class);
        if(bs == null) {
            bs = new ArrayList<>();
        }
        List<PositionBean> bs2 = new ArrayList<>();
        for (PositionBean b : bs) {
            if(b.getUname().equalsIgnoreCase((String) SPUtil.get(App.sApp, "username", ""))) {
                bs2.add(b);
            }
        }
        return bs2;
    }

}

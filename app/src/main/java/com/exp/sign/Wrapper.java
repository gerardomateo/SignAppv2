package com.exp.sign;

public class Wrapper {

    private Car mCar;
    private PositionBean mPositionBean;
    private boolean isYuyue;

    public PositionBean getPositionBean() {
        return mPositionBean;
    }

    public void setPositionBean(PositionBean positionBean) {
        mPositionBean = positionBean;
    }

    public Wrapper(Car car, PositionBean positionBean, boolean isYuyue) {
        mCar = car;
        mPositionBean = positionBean;
        this.isYuyue = isYuyue;
    }

    public Wrapper(Car car, boolean isYuyue) {
        mCar = car;
        this.isYuyue = isYuyue;
    }

    public Car getCar() {
        return mCar;
    }

    public void setCar(Car car) {
        mCar = car;
    }

    public boolean isYuyue() {
        return isYuyue;
    }

    public void setYuyue(boolean yuyue) {
        isYuyue = yuyue;
    }

    @Override
    public String toString() {
        if(!isYuyue) {
            return mCar.toString();
        } else {
            return mCar.toString() + "\n" + mPositionBean.toString();
        }
    }
}

package com.exp.sign;

public class Wrapper2 {

    private Car mCar;
    private PositionBean mPositionBean;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wrapper2 wrapper2 = (Wrapper2) o;

        if (mCar != null ? !mCar.equals(wrapper2.mCar) : wrapper2.mCar != null) return false;
        return mPositionBean != null ? mPositionBean.equals(wrapper2.mPositionBean) : wrapper2.mPositionBean == null;
    }

    @Override
    public int hashCode() {
        int result = mCar != null ? mCar.hashCode() : 0;
        result = 31 * result + (mPositionBean != null ? mPositionBean.hashCode() : 0);
        return result;
    }

    public PositionBean getPositionBean() {
        return mPositionBean;
    }

    public void setPositionBean(PositionBean positionBean) {
        mPositionBean = positionBean;
    }

    public Wrapper2(Car car, PositionBean positionBean) {
        mCar = car;
        mPositionBean = positionBean;
    }

    public Wrapper2(Car car, boolean isYuyue) {
        mCar = car;
    }

    public Car getCar() {
        return mCar;
    }

    public void setCar(Car car) {
        mCar = car;
    }

    @Override
    public String toString() {
        return mCar.toString() + "\n" + mPositionBean.toString();
    }
}

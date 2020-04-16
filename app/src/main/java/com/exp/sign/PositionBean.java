package com.exp.sign;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class PositionBean implements Serializable {

    private String uname;
    private int col;
    private int row;
    private String fromDate; // yyyy-MM-dd
    private String endDate; // yyyy-MM-dd
    private long carId;
    private String fromState;
    private String endState;
    private String date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionBean that = (PositionBean) o;

        if (col != that.col) return false;
        if (row != that.row) return false;
        if (carId != that.carId) return false;
        if (uname != null ? !uname.equals(that.uname) : that.uname != null) return false;
        if (fromDate != null ? !fromDate.equals(that.fromDate) : that.fromDate != null)
            return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (fromState != null ? !fromState.equals(that.fromState) : that.fromState != null)
            return false;
        if (endState != null ? !endState.equals(that.endState) : that.endState != null)
            return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = uname != null ? uname.hashCode() : 0;
        result = 31 * result + col;
        result = 31 * result + row;
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (int) (carId ^ (carId >>> 32));
        result = 31 * result + (fromState != null ? fromState.hashCode() : 0);
        result = 31 * result + (endState != null ? endState.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return
                "从" + fromDate + "到" + endDate +
                        "\n每天" + date + "  座位：" + col + "排" + row + "座\n";

    }

    public String zuowei() {
        return col + "排" + row + "座";
    }

    public PositionBean() {
    }

    public PositionBean(String uname, int col, int row, String fromDate, String endDate, long carId, String fromState, String endState, String date) {
        this.uname = uname;
        this.col = col;
        this.row = row;
        this.fromDate = fromDate;
        this.endDate = endDate;
        this.carId = carId;
        this.fromState = fromState;
        this.endState = endState;
        this.date = date;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getEndState() {
        return endState;
    }

    public void setEndState(String endState) {
        this.endState = endState;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean yes(int notify) {
        try {
            long f = TableActivity.sSimpleDateFormat1.parse(fromDate).getTime();
            long e = TableActivity.sSimpleDateFormat1.parse(endDate).getTime();
            long d = TableActivity.sSimpleDateFormat13.parse(TableActivity.sSimpleDateFormat11.format(new Date()) + " " + date).getTime();
            if(d >= f && d <= e + 1000 * 60 * 60 * 24 && d > System.currentTimeMillis() && Math.abs(d - System.currentTimeMillis()) <= notify * 1000 * 60) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}

package com.lenovo.smarttraffic.bean;

public class AdaBean {
    private String msg;
    private int max;
    private int min;
    private int avg;

    public AdaBean(String msg, int max, int min, int avg) {
        this.msg = msg;
        this.max = max;
        this.min = min;
        this.avg = avg;
    }

    @Override
    public String toString() {
        return "AdaBean{" +
                "msg='" + msg + '\'' +
                ", max=" + max +
                ", min=" + min +
                ", avg=" + avg +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }
}

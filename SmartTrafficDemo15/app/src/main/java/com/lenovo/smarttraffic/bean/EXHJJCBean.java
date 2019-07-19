package com.lenovo.smarttraffic.bean;

public class EXHJJCBean {
    private int count;
    private int pm;

    public EXHJJCBean(int count, int pm) {
        this.count = count;
        this.pm = pm;
    }

    @Override
    public String toString() {
        return "EXHJJCBean{" +
                "count=" + count +
                ", pm=" + pm +
                '}';
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }
}

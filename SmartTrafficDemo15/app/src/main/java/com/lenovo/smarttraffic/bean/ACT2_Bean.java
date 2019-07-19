package com.lenovo.smarttraffic.bean;

import org.litepal.crud.LitePalSupport;

public class ACT2_Bean extends LitePalSupport {
    private String time;
    private String username;
    private String carnumber;
    private int money;
    private String times;

    public ACT2_Bean(String time, String username, String carnumber, int money, String times) {
        this.time = time;
        this.username = username;
        this.carnumber = carnumber;
        this.money = money;
        this.times = times;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "ACT2_Bean{" +
                "time='" + time + '\'' +
                ", username='" + username + '\'' +
                ", carnumber='" + carnumber + '\'' +
                ", money=" + money +
                ", times='" + times + '\'' +
                '}';
    }
}

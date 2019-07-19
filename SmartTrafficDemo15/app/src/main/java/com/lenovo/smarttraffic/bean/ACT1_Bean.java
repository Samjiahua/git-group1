package com.lenovo.smarttraffic.bean;

public class ACT1_Bean {
    private int carid;
    private String carbrand;
    private String carnumber;
    private String pname;
    private int balance;
    private Boolean set;

    public ACT1_Bean(int carid, String carbrand, String carnumber, String pname, int balance, Boolean set) {
        this.carid = carid;
        this.carbrand = carbrand;
        this.carnumber = carnumber;
        this.pname = pname;
        this.balance = balance;
        this.set = set;
    }

    public int getCarid() {
        return carid;
    }

    public void setCarid(int carid) {
        this.carid = carid;
    }

    public String getCarbrand() {
        return carbrand;
    }

    public void setCarbrand(String carbrand) {
        this.carbrand = carbrand;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Boolean getSet() {
        return set;
    }

    public void setSet(Boolean set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return "ACT1_Bean{" +
                "carid=" + carid +
                ", carbrand='" + carbrand + '\'' +
                ", carnumber='" + carnumber + '\'' +
                ", pname='" + pname + '\'' +
                ", balance=" + balance +
                ", set=" + set +
                '}';
    }
}

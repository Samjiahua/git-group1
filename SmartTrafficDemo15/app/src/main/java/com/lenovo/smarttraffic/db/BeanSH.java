package com.lenovo.smarttraffic.db;

import org.litepal.crud.LitePalSupport;

public class BeanSH extends LitePalSupport {
    private int pm;
    private int co2;
    private int temper;
    private int hum;
    private int light;

    public BeanSH(int pm, int co2, int temper, int hum, int light) {
        this.pm = pm;
        this.co2 = co2;
        this.temper = temper;
        this.hum = hum;
        this.light = light;
    }

    @Override
    public String toString() {
        return "BeanBJ{" +
                "pm=" + pm +
                ", co2=" + co2 +
                ", temper=" + temper +
                ", hum=" + hum +
                ", light=" + light +
                '}';
    }
}

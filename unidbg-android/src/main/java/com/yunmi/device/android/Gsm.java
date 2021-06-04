package com.yunmi.device.android;

import java.io.Serializable;

public class Gsm implements Serializable {
    private int cid;
    private int lac;
    private int psc;

    public int getCid() {
        return this.cid;
    }

    public int getLac() {
        return this.lac;
    }

    public int getPsc() {
        return this.psc;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }
}
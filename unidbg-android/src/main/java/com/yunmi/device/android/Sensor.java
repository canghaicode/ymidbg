package com.yunmi.device.android;

import java.io.Serializable;

public class Sensor implements Serializable {
    private double maximumRange;
    private int minDelay;
    private String name;
    private float power;
    private double resolution;
    private int type;
    private String vendor;
    private int version;

    public double getMaximumRange() {
        return this.maximumRange;
    }

    public int getMinDelay() {
        return this.minDelay;
    }

    public String getName() {
        return this.name;
    }

    public float getPower() {
        return this.power;
    }

    public double getResolution() {
        return this.resolution;
    }

    public int getType() {
        return this.type;
    }

    public String getVendor() {
        return this.vendor;
    }

    public int getVersion() {
        return this.version;
    }

    public void setMaximumRange(double maximumRange) {
        this.maximumRange = maximumRange;
    }

    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

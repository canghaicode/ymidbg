package com.yunmi.device.android;

import java.io.Serializable;

public class PictureSize implements Serializable {
    private int height;
    private int width;

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
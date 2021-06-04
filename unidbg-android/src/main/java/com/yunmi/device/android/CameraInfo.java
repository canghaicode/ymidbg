package com.yunmi.device.android;

import java.io.Serializable;

public class CameraInfo implements Serializable {
    private int facing;
    private int orientation;
    private PictureSize[] supportedPictureSizes;

    public int getFacing() {
        return this.facing;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public PictureSize[] getSupportedPictureSizes() {
        return this.supportedPictureSizes;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setSupportedPictureSizes(PictureSize[] supportedPictureSizes) {
        this.supportedPictureSizes = supportedPictureSizes;
    }
}

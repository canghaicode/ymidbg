package com.yunmi.device.android;

import java.io.Serializable;

public class NetworkInfo implements Serializable {
    private boolean available;
    private boolean connected;
    private String extraInfo;
    private boolean failover;
    private boolean roaming;
    private String state;
    private int subtype;
    private String subtypeName;
    private int type;
    private String typeName;

    public String getExtraInfo() {
        return this.extraInfo;
    }

    public String getState() {
        return this.state;
    }

    public int getSubtype() {
        return this.subtype;
    }

    public String getSubtypeName() {
        return this.subtypeName;
    }

    public int getType() {
        return this.type;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean isFailover() {
        return this.failover;
    }

    public boolean isRoaming() {
        return this.roaming;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSubtype(int subtype) {
        this.subtype = subtype;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

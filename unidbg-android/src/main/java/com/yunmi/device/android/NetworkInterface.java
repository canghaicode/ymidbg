package com.yunmi.device.android;

public class NetworkInterface {
    private String[] addresses;
    private String hardwareAddress;
    private int mtu;
    private String name;

    public String[] getAddresses() {
        return this.addresses;
    }

    public String getHardwareAddress() {
        return this.hardwareAddress;
    }

    public int getMtu() {
        return this.mtu;
    }

    public String getName() {
        return this.name;
    }

    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }

    public void setHardwareAddress(String hardwareAddress) {
        this.hardwareAddress = hardwareAddress;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }

    public void setName(String name) {
        this.name = name;
    }
}

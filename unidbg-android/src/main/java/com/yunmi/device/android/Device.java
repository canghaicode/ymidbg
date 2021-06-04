package com.yunmi.device.android;

import com.yunmi.device.android.Enviorment;
import com.yunmi.device.android.Hardware;
import com.yunmi.device.android.NetworkInterface;
import com.yunmi.device.android.OS;

import java.io.Serializable;
import java.util.List;

public interface Device extends Enviorment, Hardware, OS, Serializable {
    String getDeviceJson();

    List<NetworkInterface> getNetworkInterfaces();
}


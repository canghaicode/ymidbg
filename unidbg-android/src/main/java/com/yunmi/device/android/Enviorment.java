package com.yunmi.device.android;

import com.yunmi.device.InstalledPackage;
import com.yunmi.device.MobileOperator;

public interface Enviorment {
    String getActiveNetInfo();

    NetworkInfo getActiveNetworkInfo();

    Audio getAudio();

    String getBluetoothName();

    String getBssid();

    String getDeviceSoftwareVersion();

    String getExternalStorageState();

    Gsm getGsm();

    String getImei();

    String getImsi();

    InstalledPackage[] getInstalled();

    String getIpAddress();

    double getLatitude();

    String getLine1Number();

    double getLongitude();

    NetworkInfo getMobileNetworkInfo();

    String getNetworkCountryIso();

    String getNetworkOperator();

    String getNetworkOperatorName();

    MobileOperator getOperator();

    String getSimCountryIso();

    String getSimOperator();

    String getSimOperatorName();

    String getSimSerialNumber();

    String getSsid();

    String getUsbState();
}
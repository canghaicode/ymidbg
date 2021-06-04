package com.yunmi.device.android;

public interface Hardware {
    String getBluetoothAddress();

    String getBoard();

    String getBootloader();

    String getBrand();

    CameraInfo[] getCameraInfos();

    String getCid();

    CpuCore[] getCpu();

    String getCpu_possible();

    String getCpu_present();

    String getCpuInfo();

    String getCpuInfoMaxFreq();

    String getCpuInfoMinFreq();

    StatFs getDataStatFs();

    int getDensity();

    String getDevice();

    String getDisplay();

    StatFs getExternalStatFs();

    String getGlExtensions();

    String getGlRenderer();

    String getGlVendor();

    String getGlVersion();

    String getHardware();

    int getHeight();

    String getId();

    String getIdProduct();

    String getIdVendor();

    String getMacAddress();

    String getManufacturer();

    String getMemInfo();

    String getModel();

    String getProduct();

    String getRadio();

    StatFs getRootStatFs();

    Sensor[] getSensors();

    String getSerial();

    String[] getSystemAvailableFeatures();

    int getWidth();

    String getISerial();
}

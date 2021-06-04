package com.yunmi.device.android;

public interface OS {
    String getAndroidId();

    String getBinMd5();

    String getBootId();

    String getBuildProp();

    String getCodename();

    String getCpuAbi();

    String getCpuAbi2();

    String getDefaultInputMethod();

    String getDefaultProp();

    String getDescription();

    byte[] getDeviceUniqueId();

    String getFingerprint();

    String getFrameworkArm64Md5();

    String getFrameworkArmMd5();

    String getFrameworkMd5();

    String getFsid();

    String getHost();

    String getIncremental();

    InputMethodInfo[] getInputMethods();

    String getKernel();

    String getProperties();

    byte[] getProvisioningUniqueId();

    String getRelease();

    int getSdk();

    String getSystemProperty(String arg1);

    String[] getSystemSharedLibraryNames();

    String getTags();

    long getTime();

    String getType();

    String getUnknown();

    String getUser();

    String getUserAgent();
}

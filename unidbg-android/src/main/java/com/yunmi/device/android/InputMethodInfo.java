package com.yunmi.device.android;

public class InputMethodInfo {
    private String packageName;
    private String serviceName;
    private String settingsActivity;

    public String getPackageName() {
        return this.packageName;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getSettingsActivity() {
        return this.settingsActivity;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setSettingsActivity(String settingsActivity) {
        this.settingsActivity = settingsActivity;
    }
}

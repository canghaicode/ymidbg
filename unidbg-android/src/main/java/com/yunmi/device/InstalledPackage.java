package com.yunmi.device;

import java.io.Serializable;

public class InstalledPackage implements Serializable {
    private int fileSize;
    private long firstInstallTime;
    private int flags;
    private long lastUpdateTime;
    private String name;
    private String packageName;
    private static final long serialVersionUID = 0x490366D094405FF9L;
    private String signatures;
    private int versionCode;
    private String versionName;

    @Override
    public boolean equals(Object obj) {
        if(this == (obj)) {
            return true;
        }

        if(obj == null) {
            return false;
        }

        if(this.getClass() != obj.getClass()) {
            return false;
        }

        InstalledPackage other = (InstalledPackage)obj;
        return this.packageName == null ? other.packageName == null : this.packageName.equals(other.packageName);
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public long getFirstInstallTime() {
        return this.firstInstallTime;
    }

    public int getFlags() {
        return this.flags;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public String getName() {
        return this.name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getSignatures() {
        return this.signatures;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    @Override
    public int hashCode() {
        return this.packageName == null ? 0x1F : this.packageName.hashCode() + 0x1F;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    @Override
    public String toString() {
        return this.name + '(' + this.packageName + '_' + this.versionName + '.' + this.versionCode + ')';
    }
}

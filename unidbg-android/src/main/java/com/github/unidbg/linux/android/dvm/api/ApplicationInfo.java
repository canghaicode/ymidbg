package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.BaseVM;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.yunmi.device.InstalledPackage;

import java.util.Properties;

public class ApplicationInfo extends DvmObject<InstalledPackage> {
    int sdk;

    public ApplicationInfo(VM vm, InstalledPackage packageInfo, int sdk) {
        super(vm.resolveClass("android/content/pm/ApplicationInfo"), packageInfo);
        this.sdk = sdk;
    }

    public int getFlags() {
        return value.getFlags();
    }


    public String getName() {
        return value.getName();
    }

    public String getDataDir() {
        return "/data/user/0/"+ value.getPackageName();
    }

    public String getSourceDir() {
        return "/data/app/"+ this.value.getPackageName() + "-1/base.apk";
    }

    public Bundle getMetaData(VM vm, int clientVersion) {
        Properties properties = new Properties();
        properties.put("com.tencent.mm.BuildInfo.CLIENT_VERSION",Integer.toHexString(clientVersion));
        return new Bundle(vm,properties);
    }
}

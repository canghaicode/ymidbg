package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.ArrayListObject;
import com.github.unidbg.linux.android.dvm.BaseVM;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.yunmi.device.InstalledPackage;
import com.yunmi.device.android.Device;

import java.util.ArrayList;
import java.util.List;

public class PackageManager extends DvmObject<Device> {
    public PackageManager(VM vm, Device device) {
        super(vm.resolveClass("android/content/pm/PackageManager"),device);
    }

    private InstalledPackage getInstalledPackage(String packageName){
        for (InstalledPackage installedPackage : value.getInstalled()) {
            if(installedPackage.getPackageName().equals(packageName)){
                return installedPackage;
            }
        }
        return null;
    }
    public StringObject getApplicationLabel(VM vm, ApplicationInfo applicationInfo) {
        return new StringObject(vm,applicationInfo.getName());
    }
    public ApplicationInfo getApplicationInfo(VM vm, String packageName, int flags) {
        return new ApplicationInfo(vm,getInstalledPackage(packageName),flags);
    }

    public ArrayListObject getInstalledPackages(VM vm, int flags) {
        InstalledPackage[] installed = value.getInstalled();
        List<PackageInfo> installList = new ArrayList<>();
        for (InstalledPackage pkg : installed) {
            installList.add(new PackageInfo(vm,pkg.getPackageName(),pkg.getFlags()));
        }
        return new ArrayListObject(vm,installList);
    }
}

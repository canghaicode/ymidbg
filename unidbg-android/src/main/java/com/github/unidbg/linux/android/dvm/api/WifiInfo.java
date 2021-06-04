package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.yunmi.device.android.Device;

public class WifiInfo extends DvmObject<Device> {
    public WifiInfo(VM vm, Device value) {
        super(vm.resolveClass("android/net/wifi/WifiInfo"), value);
    }

    public StringObject getBSSID(VM vm) {
        return new StringObject(vm, "getBSSID");
//        return new StringObject(vm, value.getString("getBSSID"));
    }

    public StringObject getSSID(VM vm){
        return new StringObject(vm,"getSSID");
//        return new StringObject(vm,value.getString("getSSID"));
    }
}
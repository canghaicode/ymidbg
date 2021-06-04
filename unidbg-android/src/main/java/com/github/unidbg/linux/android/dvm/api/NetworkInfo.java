package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;

public class NetworkInfo extends DvmObject<com.yunmi.device.android.NetworkInfo> {
    public NetworkInfo(VM vm, com.yunmi.device.android.NetworkInfo value){
        super(vm.resolveClass("android/net/NetworkInfo"),value);
    }

    public StringObject getExtraInfo(VM vm) {
        return new StringObject(vm,value.getExtraInfo());
    }

    public int getType() {
        return value.getType();
    }

    public int getSubtype() {
        return value.getSubtype();
    }
}
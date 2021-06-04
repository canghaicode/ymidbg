package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;

public class NetworkInterface extends DvmObject<com.yunmi.device.android.NetworkInterface> {
    public NetworkInterface(VM vm, com.yunmi.device.android.NetworkInterface networkInterface){
        super(vm.resolveClass("java.net.NetworkInterface"), networkInterface);
    }

    public NetworkInterface(DvmClass dvmClass, com.yunmi.device.android.NetworkInterface networkInterface){
        super(dvmClass, networkInterface);
    }

    public boolean isLoopback() {
        return false;
    }

    public String[] getAddresses() {
        return value.getAddresses();
    }
}

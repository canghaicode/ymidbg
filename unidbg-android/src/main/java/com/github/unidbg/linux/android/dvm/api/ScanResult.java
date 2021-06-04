package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.BaseVM;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.yunmi.device.android.Device;

public class ScanResult extends DvmObject<Device> {
    public ScanResult(VM vm, Device value) {
        super(vm.resolveClass("android/net/wifi/ScanResult"), value);
    }

    public StringObject getBSSID(BaseVM vm) {
        return new StringObject(vm, value.getBssid());
    }

    public int getLevel() {
        return 0;
    }
}

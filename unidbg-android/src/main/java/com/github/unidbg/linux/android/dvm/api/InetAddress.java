package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;

public class InetAddress extends DvmObject<String> {

    public InetAddress(VM vm, String value) {
        super(vm.resolveClass("java.net.InetAddress"), value);
    }
}
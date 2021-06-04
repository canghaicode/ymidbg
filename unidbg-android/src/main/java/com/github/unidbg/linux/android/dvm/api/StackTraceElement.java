package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;

public class StackTraceElement extends DvmObject<java.lang.StackTraceElement> {
    private final VM vm;

    public StackTraceElement(VM vm, java.lang.StackTraceElement element) {
        super(vm.resolveClass("java.lang.StackTraceElement"), element);
        this.vm=vm;
    }

    public StringObject getClassName() {
        return new StringObject(vm,this.value.getClassName());
    }
}
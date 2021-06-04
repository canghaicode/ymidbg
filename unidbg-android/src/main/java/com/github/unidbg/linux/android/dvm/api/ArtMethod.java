package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;

public class ArtMethod extends DvmObject<String> {

    public ArtMethod(VM vm, String value) {
        super(vm.resolveClass("java/lang/reflect/ArtMethod"), value);
    }

    public DvmObject<?> invoke(DvmObject<?> obj, ArrayObject args) {
        return this;
    }
}

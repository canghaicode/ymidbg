package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;

public class ArtField extends DvmObject<String> {
    public ArtField(VM vm, String value){
        super(vm.resolveClass("java/lang/reflect/ArtField"),value);
    }

    public DvmObject<?> getObject(DvmObject<?> object) {
        return object;
    }
}

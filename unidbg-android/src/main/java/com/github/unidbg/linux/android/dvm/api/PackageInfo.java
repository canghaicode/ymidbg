package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.BaseVM;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;

public class PackageInfo extends DvmObject<String> {

    private final int flags;

    public PackageInfo(VM vm, String packageName, int flags) {
        super(vm.resolveClass("android/content/pm/PackageInfo"), packageName);
        this.flags = flags;
    }

    public String getPackageName() {
        return getValue();
    }

    public int getFlags() {
        return flags;
    }

    public ApplicationInfo getApplicationInfo(VM vm, int sdk) {
        // TODO: May not be null
        return null;
    }
}

package com.github.unidbg.linux.android.dvm.array;

import com.github.unidbg.Emulator;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.pointer.UnidbgPointer;
import com.sun.jna.Pointer;

public class ByteArray extends BaseArray<byte[]> implements PrimitiveArray<byte[]> {

    public ByteArray(VM vm, byte[] value) {
        super(vm.resolveClass("[B"), value);
    }

    @Override
    public int length() {
        return value.length;
    }

    public void setValue(byte[] value) {
        super.value = value;
    }

    @Override
    public void setData(int start, byte[] data) {
        System.arraycopy(data, 0, value, start, data.length);
    }

    @Override
    public UnidbgPointer _GetArrayCritical(Emulator<?> emulator, Pointer isCopy) {
        if (isCopy != null) {
            isCopy.setInt(0, VM.JNI_TRUE);
        }
        UnidbgPointer pointer = this.allocateMemoryBlock(emulator, value.length);
        pointer.write(0, value, 0, value.length);
        return pointer;
    }

    @Override
    public void _ReleaseArrayCritical(Pointer elems, int mode) {
        switch (mode) {
            case VM.JNI_COMMIT:
                this.setValue(elems.getByteArray(0, this.value.length));
                break;
            case 0:
                this.setValue(elems.getByteArray(0, this.value.length));
            case VM.JNI_ABORT:
                this.freeMemoryBlock(elems);
                break;
        }
    }
}

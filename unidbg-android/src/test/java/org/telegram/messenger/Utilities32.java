package org.telegram.messenger;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.LibraryResolver;
import com.github.unidbg.Module;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.utils.Inspector;
import com.github.unidbg.virtualmodule.android.AndroidModule;
import com.github.unidbg.virtualmodule.android.JniGraphics;

import java.io.File;
import java.io.IOException;

public class Utilities32 extends AbstractJni {

    private static LibraryResolver createLibraryResolver() {
        return new AndroidResolver(23);
    }

    private static AndroidEmulator createARMEmulator() {
        return new AndroidARMEmulator("org.telegram.messenger");
    }

    private final AndroidEmulator emulator;
    private final VM vm;

    private final DvmClass cUtilities;

    private Utilities32() {
        emulator = createARMEmulator();
        final Memory memory = emulator.getMemory();
        memory.setLibraryResolver(createLibraryResolver());

        vm = emulator.createDalvikVM(null);
        vm.setJni(this);
        Module module = new JniGraphics(emulator, vm).register(memory);
        assert module != null;
        new AndroidModule(emulator, vm).register(memory);

        vm.setVerbose(true);
        DalvikModule dm = vm.loadLibrary(new File("unidbg-android/src/test/resources/example_binaries/armeabi-v7a/libtmessages.29.so"), false);
        dm.callJNI_OnLoad(emulator);

        cUtilities = vm.resolveClass("org/telegram/messenger/Utilities");
    }

    private void destroy() throws IOException {
        emulator.close();
        System.out.println("destroy");
    }

    public static void main(String[] args) throws Exception {
        Utilities32 test = new Utilities32();

        test.aesCbcEncryptionByteArray();
        test.aesCtrDecryptionByteArray();
        test.pbkdf2();

        test.destroy();
    }

    private void aesCbcEncryptionByteArray() {
        long start = System.currentTimeMillis();
        ByteArray data = new ByteArray(vm, new byte[16]);
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        cUtilities.callStaticJniMethod(emulator, "aesCbcEncryptionByteArray([B[B[BIIII)V", vm.addLocalObject(data),
                vm.addLocalObject(new ByteArray(vm, key)),
                vm.addLocalObject(new ByteArray(vm, iv)),
                0, data.length(), 0, 0);
        Inspector.inspect(data.getValue(), "aesCbcEncryptionByteArray offset=" + (System.currentTimeMillis() - start) + "ms");
    }

    private void aesCtrDecryptionByteArray() {
        long start = System.currentTimeMillis();
        ByteArray data = new ByteArray(vm, new byte[16]);
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        cUtilities.callStaticJniMethod(emulator, "aesCtrDecryptionByteArray([B[B[BIII)V", vm.addLocalObject(data),
                vm.addLocalObject(new ByteArray(vm, key)),
                vm.addLocalObject(new ByteArray(vm, iv)),
                0, data.length(), 0);
        Inspector.inspect(data.getValue(), "aesCtrDecryptionByteArray offset=" + (System.currentTimeMillis() - start) + "ms");
    }

    private void pbkdf2() {
        long start = System.currentTimeMillis();
        byte[] password = "123456".getBytes();
        byte[] salt = new byte[8];
        ByteArray dst = new ByteArray(vm, new byte[64]);
        cUtilities.callStaticJniMethod(emulator, "pbkdf2([B[B[BI)V", vm.addLocalObject(new ByteArray(vm, password)),
                vm.addLocalObject(new ByteArray(vm, salt)),
                vm.addLocalObject(dst), 100000);
        Inspector.inspect(dst.getValue(), "pbkdf2 offset=" + (System.currentTimeMillis() - start) + "ms");
    }

}

package com.yunmi.wechat;

import com.github.unidbg.debugger.Debugger;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.SystemPropertyHook;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.linux.android.dvm.wrapper.DvmBoolean;
import com.github.unidbg.linux.android.dvm.wrapper.DvmInteger;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.utils.Inspector;
import com.github.unidbg.virtualmodule.android.AndroidModule;
import com.github.unidbg.virtualmodule.android.JniGraphics;
import com.yunmi.device.AndroidDevice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sign {
    //需要更换
//    private static final File APK_FILE = new File("//home//mark//workspace//weixin//apks//weixin7017android1720.apk");
    private static final File APK_FILE = new File(
//            "unidbg-android/src/test/resources/apks/weixin7017android1720.apk"
//            "unidbg-android/src/test/resources/apks/weixin7019android1760.apk"
//            "unidbg-android/src/test/resources/apks/weixin7018android1740.apk"
//            "unidbg-android/src/test/resources/apks/weixin7021android1800.apk"
//            "unidbg-android/src/test/resources/apks/weixin803android1880.apk"
            "unidbg-android/src/test/resources/apks/etao.apk"
    );
//    private static final File APK_FILE = new File("/home/mark/workspace/wechat/apks/weixin7019android1760.apk");


    public static AndroidDevice getDevice() {
        try {
            return new JsonFileToSerializable(
                    "unidbg-android/src/test/java/com/yunmi/wechat/device.json")
                    .toObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {

//        MongoClientURI uri = new MongoClientURI("mongodb://root:root@localhost:27017/");
//        MongoClientURI uri = new MongoClientURI(
//                "mongodb://root:Yunmi001@dds-wz97d7d5e6b6fbc41729-pub.mongodb.rds.aliyuncs.com:3717,dds-wz97d7d5e6b6fbc42618-pub.mongodb.rds.aliyuncs.com:3717/admin?replicaSet=mgset-36118783"
//        );

//        MongoClient mongoClient = new MongoClient(uri);

//        MongoDatabase database = mongoClient.getDatabase("unidbg");

//        String collectionName = "Time" + String.format("%d", System.currentTimeMillis());
//        MongoCollection<Document> collection = database.getCollection(collectionName);


        AndroidARMEmulator emulator = new AndroidARMEmulator("com.taobao.etao", new File("target/rootfs"));
        WeChatHandler weChatHandler = new WeChatHandler(0x27001111, getDevice());
        Memory memory = emulator.getMemory();

        //需要注意先后顺序,需要先导入系统库
        memory.setLibraryResolver(new AndroidResolver(23));
        emulator.getSyscallHandler().addIOResolver(weChatHandler);

        memory.addHookListener(new SystemPropertyHook(emulator));

        VM vm = emulator.createDalvikVM(APK_FILE);
        vm.setJni(weChatHandler);
//        vm.setVerbose(true);
        new AndroidModule(emulator, vm).register(memory);
        new JniGraphics(emulator, vm).register(memory);
//        CodeListener codeListener = new CodeListener(collection);
//        MemoryWriteListener memoryWriteListener = new MemoryWriteListener(collection);
//        MemoryReadListener memoryReadListener = new MemoryReadListener(collection);
//        emulator.traceWrite();

        DalvikModule wechatnormsg = vm.loadLibrary("sgmainso-6.5.22", false);
//        emulator.traceCode(1,0,new FileTraceCode());
//        emulator.traceWrite(1,0,new FileTraceMemoryWrite());
//        emulator.traceRead(1,0,new FileTraceMemoryRead());
//        emulator.traceCode(1, 0, codeListener);
//        emulator.traceWrite(1, 0, memoryWriteListener);
//        emulator.traceRead(1, 0, memoryReadListener);
//        Module module = wechatnormsg.getModule();
        Debugger debugger = emulator.attach();
        wechatnormsg.callJNI_OnLoad(emulator);
        //init
        wechatnormsg = vm.loadLibrary("sgsecuritybodyso-6.5.28", false);
        wechatnormsg.callJNI_OnLoad(emulator);
        wechatnormsg = vm.loadLibrary("sgmiddletierso-6.5.24", false);
        wechatnormsg.callJNI_OnLoad(emulator);
        DvmClass dvmClass = vm.resolveClass("com/taobao/wireless/security/adapter/JNICLibrary");

        // 90字段
//        debugger.addBreakPoint(0x401b3261);     //r0
//        debugger.addBreakPoint(0x401b3275);     //r1
//        debugger.addBreakPoint(0x401b3343);     //r0 = value

        //91字段
//        debugger.addBreakPoint(0x401aa54f); //r6 获取时间
//        debugger.addBreakPoint(0x401aa5a5); //获取加密参数?
//        debugger.addBreakPoint(0x401aa662); //r1 结果

        DvmObject context = vm.resolveClass("com/taobao/sns/ISApplication").newObject(null);
        DvmObject ret = dvmClass.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;",
                10101,
                new ArrayObject(context, DvmInteger.valueOf(vm, 3), new StringObject(vm, ""), new StringObject(vm, new File("target/app_SGLib").getAbsolutePath()), new StringObject(vm, ""))
        );
        System.out.println("10101 -> " + ret.getValue());

        ret = dvmClass.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;",
                10102,
                new ArrayObject(new StringObject(vm, "main"), new StringObject(vm, "6.5.22"),
                        new StringObject(vm, "/root/Desktop/projects/ymidbg/unidbg-android/src/test/resources/example_binaries/armeabi-v7a/libsgmainso-6.5.22.so"))
        );
        System.out.println("10102 -> " + ret.getValue());

        ret = dvmClass.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;",
                10102,
                new ArrayObject(new StringObject(vm, "securitybody"), new StringObject(vm, "6.5.28"),
                        new StringObject(vm, "/root/Desktop/projects/ymidbg/unidbg-android/src/test/resources/example_binaries/armeabi-v7a/libsgsecuritybodyso-6.5.28.so"))
        );
        System.out.println("10102 -> " + ret.getValue());

        Map<String, String> map = new HashMap<>();
        map.put("INPUT", "YHPAD8SWj+gDALwiyqQdaUNN21783927");
        ret = dvmClass.callStaticJniMethodObject(emulator,
                "doCommandNative(I[Ljava/lang/Object;)Ljava/lang/Object;",
                10401,
                new ArrayObject(vm.resolveClass("java/util/HashMap").newObject(map),
                        new StringObject(vm, "21783927"), DvmInteger.valueOf(vm, 3), null, DvmBoolean.valueOf(vm, true))
        );
        System.out.println("10401 -> " + ret.getValue());

//        ByteArray ret = dvmClass.callStaticJniMethodObject(emulator, "aa(II)[B", 0,1);
//        Inspector.inspect(ret.getValue(), "doCommandNative");
    }
}

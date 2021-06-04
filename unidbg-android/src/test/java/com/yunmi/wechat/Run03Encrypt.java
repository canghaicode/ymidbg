package com.yunmi.wechat;

import com.github.unidbg.debugger.Debugger;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.SystemPropertyHook;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.utils.Inspector;
import com.github.unidbg.virtualmodule.android.AndroidModule;
import com.github.unidbg.virtualmodule.android.JniGraphics;
import com.yunmi.device.AndroidDevice;

import java.io.File;

public class Run03Encrypt {

    //需要更换
//    private static final File APK_FILE = new File("//home//mark//workspace//weixin//apks//weixin7017android1720.apk");
    private static final File APK_FILE = new File(
//            "unidbg-android/src/test/resources/apks/weixin7017android1720.apk"
//            "unidbg-android/src/test/resources/apks/weixin7019android1760.apk"
//            "unidbg-android/src/test/resources/apks/weixin7018android1740.apk"
//            "unidbg-android/src/test/resources/apks/weixin7021android1800.apk"
            "unidbg-android/src/test/resources/apks/weixin803android1880.apk"
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


        AndroidARMEmulator emulator = new AndroidARMEmulator("com.tencent.mm", new File("target/rootfs"));
        WeChatHandler weChatHandler = new WeChatHandler(0x27001111, getDevice());
        Memory memory = emulator.getMemory();

        //需要注意先后顺序,需要先导入系统库
        memory.setLibraryResolver(new AndroidResolver(23));
        emulator.getSyscallHandler().addIOResolver(weChatHandler);

        memory.addHookListener(new SystemPropertyHook(emulator));

        VM vm = emulator.createDalvikVM(APK_FILE);
        vm.setJni(weChatHandler);
        vm.setVerbose(true);
        new AndroidModule(emulator, vm).register(memory);
        new JniGraphics(emulator, vm).register(memory);
//        CodeListener codeListener = new CodeListener(collection);
//        MemoryWriteListener memoryWriteListener = new MemoryWriteListener(collection);
//        MemoryReadListener memoryReadListener = new MemoryReadListener(collection);
//        emulator.traceWrite();

        DalvikModule wechatnormsg = vm.loadLibrary("wechatnormsg", false);
//        emulator.traceCode(1,0,new FileTraceCode());
//        emulator.traceWrite(1,0,new FileTraceMemoryWrite());
//        emulator.traceRead(1,0,new FileTraceMemoryRead());
//        emulator.traceCode(1, 0, codeListener);
//        emulator.traceWrite(1, 0, memoryWriteListener);
//        emulator.traceRead(1, 0, memoryReadListener);
//        Module module = wechatnormsg.getModule();
        Debugger debugger = emulator.attach();
        wechatnormsg.callJNI_OnLoad(emulator);
        DvmClass dvmClass = vm.resolveClass("com/tencent/mm/normsg/c$p");

        // 90字段
//        debugger.addBreakPoint(0x401b3261);     //r0
//        debugger.addBreakPoint(0x401b3275);     //r1
//        debugger.addBreakPoint(0x401b3343);     //r0 = value

        //91字段
//        debugger.addBreakPoint(0x401aa54f); //r6 获取时间
//        debugger.addBreakPoint(0x401aa5a5); //获取加密参数?
//        debugger.addBreakPoint(0x401aa662); //r1 结果



        ByteArray ret = dvmClass.callStaticJniMethodObject(emulator, "aa(II)[B", 0,1);
        Inspector.inspect(ret.getValue(), "03EncryptValue");
    }
}

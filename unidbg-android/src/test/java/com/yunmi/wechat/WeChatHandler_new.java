package com.yunmi.wechat;

import com.github.unidbg.Emulator;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.ArrayListObject;
import com.github.unidbg.linux.android.dvm.BaseVM;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.Enumeration;
import com.github.unidbg.linux.android.dvm.Jni;
import com.github.unidbg.linux.android.dvm.StringObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.VaList;
import com.github.unidbg.linux.android.dvm.api.ApplicationInfo;
import com.github.unidbg.linux.android.dvm.api.ArtField;
import com.github.unidbg.linux.android.dvm.api.ArtMethod;
import com.github.unidbg.linux.android.dvm.api.ClassLoader;
import com.github.unidbg.linux.android.dvm.api.InetAddress;
import com.github.unidbg.linux.android.dvm.api.NetworkInfo;
import com.github.unidbg.linux.android.dvm.api.NetworkInterface;
import com.github.unidbg.linux.android.dvm.api.PackageInfo;
import com.github.unidbg.linux.android.dvm.api.PackageManager;
import com.github.unidbg.linux.android.dvm.api.ScanResult;
import com.github.unidbg.linux.android.dvm.api.WifiInfo;
import com.github.unidbg.linux.android.dvm.api.StackTraceElement;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.linux.file.ByteArrayFileIO;
import com.github.unidbg.linux.file.DirectoryFileIO;
import com.github.unidbg.linux.file.DriverFileIO;
import com.github.unidbg.linux.file.SimpleFileIO;
import com.yunmi.device.AndroidDevice;
import com.yunmi.device.InstalledPackage;
import com.yunmi.wechat.utils.StringUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import unicorn.UnicornException;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WeChatHandler_new extends AbstractJni implements Jni, IOResolver<AndroidFileIO> {
    private static final Log log = LogFactory.getLog(WeChatHandler_new.class);
    private static final String ALLOW_MOCK_LOCATION = "mock_location";
    private static final String ANDROID_ID = "android_id";
    private static final String ADB_ENABLED = "adb_enabled";
    private static final String DEFAULT_INPUT_METHOD = "default_input_method";
    private static final String ENABLED_ACCESSIBILITY_SERVICES = "enabled_accessibility_services";
    private static final int CALL_STATE_IDLE = 0;
    private static final String WIFI_ON = "wifi_on";

    private final int clientVersion;
    private final AndroidDevice device;

    public WeChatHandler_new(int clientVersion, AndroidDevice device) {
        this.clientVersion = clientVersion;
        this.device = device;
        try {
            fpMap.put("soft_config", Hex.decodeHex("445945010600000003385b88cbe9ffff0305096303000aec38040b4603020e4303060c5338070e9203020ec138060e1d03030a3938010a86380709bc38070a4d3806083103050d12380709e038050c0803050dfd380308ac380308d303060c25380409d838000cc603060e7303060e8138030ef703050f7903000ae803040c9a03030f5c03070d9538040c1ce9b8b400".toCharArray()));
            fpMap.put("soft_data", Hex.decodeHex("693f613b673f386b6f3767356230346408555455040659580d0f510a06565805035206045055080631653e306c3033635a560206540655576135346132393433383832376130396163613630643561343737343461663831633564393262653806070006535051513c3a603c323d6b683436653e6764626806040f545d025202".toCharArray()));
            fpMap.put("waid", "waid".getBytes());
            fpMap.put("wqid", "wqid".getBytes());
            fpMap.put("lkid", "lkid".getBytes());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final String PACKAGE_NAME = "com.tencent.mm";

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "android/app/Application->getPackageManager()Landroid/content/pm/PackageManager;":
                return new PackageManager(vm, device);
            case "android/app/Application->getApplicationInfo()Landroid/content/pm/ApplicationInfo;": {
                return new ApplicationInfo(vm, device.getPackageInfo(PACKAGE_NAME), 23);
            }
            case "java/lang/ClassLoader->loadClass(Ljava/lang/String;)Ljava/lang/Class;": {
                StringObject className = vaList.getObject(0);
                if (className != null) {
                    if ("android/os/Debug".equals(className.getValue())) {
                        return vm.resolveClass(className.getValue());
                    }
                }
                return super.callObjectMethodV(vm, dvmObject, signature + className, vaList);
            }
            case "java/lang/reflect/ArtMethod->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;": {
                ArtMethod artMethod = (ArtMethod) dvmObject;
                DvmObject<?> obj = vaList.getObject(0);
                ArrayObject args = vaList.getObject(4);
//                log.debug("invoke:"+obj.getValue()+"->"+artMethod.getValue()+"("+args.toString()+")");
                return artMethod.invoke(obj, args);
            }
            case "java/lang/String->getBytes(Ljava/nio/charset/Charset;)[B": {
                StringObject str = (StringObject) dvmObject;
                Charset charset = (Charset) vaList.getObject(0).getValue();
                System.out.println("java/lang/String->getBytes str=" + str.getValue() + ", charset=" + charset);
                return new ByteArray(vm, str.getValue().getBytes(charset));
            }
            case "android/telephony/TelephonyManager->getDeviceId()Ljava/lang/String;":
                return new StringObject(vm, device.getImei());
            case "android/net/wifi/WifiManager->getConnectionInfo()Landroid/net/wifi/WifiInfo;":
                return new WifiInfo(vm, device);
            case "android/net/wifi/WifiInfo->getSSID()Ljava/lang/String;": {
                WifiInfo wifiInfo = (WifiInfo) dvmObject;
                return wifiInfo.getSSID(vm);
            }
            case "android/net/wifi/WifiInfo->getBSSID()Ljava/lang/String;": {
                WifiInfo wifiInfo = (WifiInfo) dvmObject;
                return wifiInfo.getBSSID(vm);
            }
            case "android/telephony/TelephonyManager->getSimOperatorName()Ljava/lang/String;": {
                String value = "SimOperatorName";
                if (!device.getSimOperatorName().isEmpty()) {
                    value = device.getSimOperatorName();
                }
                return new StringObject(vm, StringUtils.isEmpty(device.getSimOperatorName()) ? "SimOperatorName" : device.getSimOperatorName());
            }
            case "android/net/ConnectivityManager->getActiveNetworkInfo()Landroid/net/NetworkInfo;": {
                return new NetworkInfo(vm, device.getActiveNetworkInfo());
            }
            case "android/net/NetworkInfo->getExtraInfo()Ljava/lang/String;": {
                NetworkInfo networkInfo = (NetworkInfo) dvmObject;
                return networkInfo.getExtraInfo(vm);
            }
            case "android/telephony/TelephonyManager->getLine1Number()Ljava/lang/String;": {
                return device.getLine1Number() == null ? null : new StringObject(vm, device.getLine1Number());
            }
            case "android/content/pm/PackageManager->getApplicationLabel(Landroid/content/pm/ApplicationInfo;)Ljava/lang/CharSequence;": {
                PackageManager pm = (PackageManager) dvmObject;
                ApplicationInfo applicationInfo = vaList.getObject(0);
                return pm.getApplicationLabel(vm, applicationInfo);
            }
            case "android/content/pm/PackageManager->getApplicationInfo(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;": {
                PackageManager pm = (PackageManager) dvmObject;
                StringObject packageName = vaList.getObject(0);
                int flags = vaList.getInt(0);
                return pm.getApplicationInfo(vm, packageName.getValue(), flags);
            }
            case "java/net/NetworkInterface->getInetAddresses()Ljava/util/Enumeration;":
                NetworkInterface ni = (NetworkInterface) dvmObject;
                String[] addresses = ni.getAddresses();
                List<InetAddress> list = new ArrayList<>();
                if (addresses != null) {
                    for (String address : addresses) {
                        list.add(new InetAddress(vm, address));
                    }
                }
                return new Enumeration(vm, list);
            case "android/os/Looper->getThread()Ljava/lang/Thread;":
                return vm.resolveClass("java/lang/Thread").newObject(null);
            case "java/lang/Thread->getStackTrace()[Ljava/lang/StackTraceElement;":
                java.lang.StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                DvmObject<?>[] objects = new DvmObject[elements.length];
                for (int i = 0; i < objects.length; i++) {
                    objects[i] = new StackTraceElement(vm, elements[i]);
                }
                return new ArrayObject(objects);
            case "java.lang.StackTraceElement->getClassName()Ljava/lang/String;":
                StackTraceElement element = (StackTraceElement) dvmObject;
                return element.getClassName();
            case "android/app/ActivityThread->getInstrumentation()Landroid/app/Instrumentation;":
                return vm.resolveClass("com.tencent.mm.splash.l").newObject(null);
            case "android/os/BinderProxy->getClass()Ljava/lang/Class;":
                return vm.resolveClass("android/os/BinderProxy");
            case "android/app/ActivityManagerNative->getClass()Ljava/lang/Class;":
                return vm.resolveClass("android/app/ActivityManagerProxy");
            case "android/net/ConnectivityManager->getAllNetworks()[Landroid/net/Network;":
                // TODO: maybe must return all networks
                return new ArrayObject();
            case "android/telephony/TelephonyManager->getSubscriberId()Ljava/lang/String;":
                String imsi = device.getImsi();
                if (imsi == null) imsi = "";
                return new StringObject(vm, imsi);
            case "java/lang/Class->getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;": {
                DvmClass clazz = (DvmClass) dvmObject;
                StringObject name = vaList.getObject(0);
                String fieldName = clazz.getClassName() + "->" + name.getValue();
                if ("java/lang/reflect/ArtMethod->accessFlags".equals(fieldName)) {
                    return new ArtField(vm, fieldName);
                }
            }
            case "java/lang/reflect/ArtField->get(Ljava/lang/Object;)Ljava/lang/Object;": {
                ArtField artField = (ArtField) dvmObject;
                return artField.getObject(vaList.getObject(0));
            }
            case "android/content/pm/PackageManager->getInstalledPackages(I)Ljava/util/List;": {
                PackageManager pm = (PackageManager) dvmObject;
                int flags = vaList.getInt(0);
                System.out.println("android/content/pm/PackageManager->getInstalledPackages flags=0x" + Integer.toHexString(flags));
                return pm.getInstalledPackages(vm, flags);
            }
            case "android/net/wifi/WifiManager->getScanResults()Ljava/util/List;": {
                List<DvmObject<?>> scanResults = Collections.emptyList();
                if (!StringUtils.isEmpty(device.getBssid())) {
                    scanResults = Collections.<DvmObject<?>>singletonList(new ScanResult(vm, device));
                }
                return new ArrayListObject(vm, scanResults);
            }
            case "java/lang/ClassLoader->findClass(Ljava/lang/String;)Ljava/lang/Class;": {
                StringObject name = vaList.getObject(0);
                System.out.println("findClass className: " + name.getValue());
                if ("de/robv/android/xposed/XposedBridge".equals(name.getValue())) {
                    return vm.resolveClass("de/robv/android/xposed/XposedBridge");
//                    vm.throwException(vm.resolveClass("java/lang/ClassNotFoundException").newObject(new ClassNotFoundException(name.getValue())));
//                    return null;
                } else {
                    throw new UnicornException(signature);
                }
            }
            case "java/util/Map->keySet()Ljava/util/Set;":
                Map<?, ?> map = (Map<?, ?>) dvmObject.getValue();
                return vm.resolveClass("java/util/Set").newObject(map.keySet());
            case "android/app/Application->getResources()Landroid/content/res/Resources;": { // added from 7.0.7
                return vm.resolveClass("android/content/res/Resources").newObject(dvmObject);
            }
            case "android/content/res/Resources->getAssets()Landroid/content/res/AssetManager;": {
                return vm.resolveClass("android/content/res/AssetManager").newObject(null);
            }
            case "android/media/MediaDrm->getPropertyByteArray(Ljava/lang/String;)[B":
                StringObject propertyName = vaList.getObject(0);
                System.err.println("android/media/MediaDrm->getPropertyByteArray propertyName=" + propertyName);
                try {
                    if ("deviceUniqueId".equals(propertyName.getValue())) {
                        return new ByteArray(vm, Hex.decodeHex("3430344533363235314239420000000000000000000000000000000000000000".toCharArray()));
                    }
                    if ("provisioningUniqueId".equals(propertyName.getValue())) {
                        return new ByteArray(vm, Hex.decodeHex("7544ba089e26debe61c53f632d9944dd".toCharArray()));
                    }
                    throw new UnicornException(propertyName.toString());
                } catch (DecoderException e) {
                    throw new IllegalStateException(e);
                }
            case "android/app/Application->getDir(Ljava/lang/String;I)Ljava/io/File;": {
                StringObject obj = vaList.getObject(0);
                if (obj == null) {
                    throw new UnicornException();
                }
                String dir = obj.getValue();
                return vm.resolveClass("java/io/File").newObject(new File((device.getSdk() >= 25 ? "/data/user/0/" : "/data/data/") + PACKAGE_NAME + "/" + dir));
            }
            //TODO:
            case "java.lang.StackTraceElement->getMethodName()Ljava/lang/String;": {
                return new StringObject(vm, "unknow");
            }
            case "android/os/IServiceManager->listServices()[Ljava/lang/String;":{
                return ArrayObject.newStringArray(vm,"1231231321");
            }
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public DvmObject<?> getObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        switch (signature) {
            case "android/content/pm/ApplicationInfo->dataDir:Ljava/lang/String;": {
                ApplicationInfo applicationInfo = (ApplicationInfo) dvmObject;
                return new StringObject(vm, applicationInfo.getDataDir());
            }
            case "android/content/pm/ApplicationInfo->sourceDir:Ljava/lang/String;": {
                ApplicationInfo applicationInfo = (ApplicationInfo) dvmObject;
                return new StringObject(vm, applicationInfo.getSourceDir());
            }
            case "android/content/pm/ApplicationInfo->metaData:Landroid/os/Bundle;": {
                ApplicationInfo applicationInfo = (ApplicationInfo) dvmObject;
                return applicationInfo.getMetaData(vm, clientVersion);
            }
            case "android/content/pm/PackageInfo->applicationInfo:Landroid/content/pm/ApplicationInfo;": {
                PackageInfo packageInfo = (PackageInfo) dvmObject;
                return packageInfo.getApplicationInfo(vm, device.getSdk());
            }
            case "android/net/wifi/ScanResult->BSSID:Ljava/lang/String;":
                ScanResult result = (ScanResult) dvmObject;
                return result.getBSSID(vm);
        }
        return super.getObjectField(vm, dvmObject, signature);
    }

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/ClassLoader->getSystemClassLoader()Ljava/lang/ClassLoader;": {
                return new ClassLoader(vm, signature);
            }
            case "java/nio/charset/Charset->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;": {
                StringObject charsetName = vaList.getObject(0);
                return vm.resolveClass("java/nio/charset/Charset").newObject(Charset.forName(charsetName.getValue()));
            }
            case "android/provider/Settings$Secure->getString(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;": {
                StringObject object = vaList.getObject(4);
                String name = object.getValue();
                if (ANDROID_ID.equals(name)) {
                    return device.getAndroidId() == null ? null : new StringObject(vm, device.getAndroidId());
                }
                if (DEFAULT_INPUT_METHOD.equals(name)) {
//                    return new StringObject(vm, StringUtils.isEmpty(device.getDefaultInputMethod()) ? "" : device.getDefaultInputMethod());
                    return new StringObject(vm, "");
                }
                if (ENABLED_ACCESSIBILITY_SERVICES.equals(name)) {
                    return new StringObject(vm, "enabled_accessibility_services");
                }
                throw new UnicornException("signature=" + signature + ", name=" + name);
            }
            case "java/net/NetworkInterface->getByName(Ljava/lang/String;)Ljava/net/NetworkInterface;": {
                DvmObject<String> object = vaList.getObject(0);
                String name = object.getValue();
                if ("wlan0".equals(name)) {
                    return null;
                }
                List<com.yunmi.device.android.NetworkInterface> interfaces = device.getNetworkInterfaces();
//                NetworkInterface networkInterface = null;
//                if (interfaces != null) {
//                    for (NetworkInterface ni : interfaces) {
//                        if (name.equals(ni.getName())) {
//                            networkInterface = ni;
//                        }
//                    }
//                }
//                System.err.println("java/net/NetworkInterface->getByName(Ljava/lang/String;)Ljava/net/NetworkInterface; name=" + name + ", networkInterface=" + networkInterface);
//                return networkInterface == null ? null : new NetworkInterface(dvmClass, networkInterface);
            }
            case "java/net/NetworkInterface->getNetworkInterfaces()Ljava/util/Enumeration;":
                List<NetworkInterface> list = new ArrayList<>();
                if (device.getNetworkInterfaces() != null) {
                    for (com.yunmi.device.android.NetworkInterface ni : device.getNetworkInterfaces()) {
                        list.add(new NetworkInterface(dvmClass, ni));
                    }
                }
                return new Enumeration(vm, list);
            case "android/os/Looper->getMainLooper()Landroid/os/Looper;":
                return vm.resolveClass("android/os/Looper").newObject(null);
            case "java/lang/reflect/Proxy->getInvocationHandler(Ljava/lang/Object;)Ljava/lang/reflect/InvocationHandler;":
                return vaList.getObject(0);
            case "android/app/ActivityManagerNative->getDefault()Landroid/app/IActivityManager;":
                return vm.resolveClass("android/app/ActivityManagerNative").newObject(null);
            case "com/tencent/mm/normsg/c$q->c7()Ljava/lang/String;":
                return new StringObject(vm, "k62");
            case "java/lang/Runtime->getRuntime()Ljava/lang/Runtime;":
                return vm.resolveClass("java/lang/Runtime").newObject(Runtime.getRuntime());
            case "com/tencent/mm/normsg/c$q->c9()Ljava/lang/String;":
                return new StringObject(vm, "c9");
            case "com/tencent/mm/normsg/c$q->c14()Ljava/lang/String;":
                return new StringObject(vm, "c14");
            case "com/tencent/mm/normsg/c$q->c12(Ljava/lang/String;)[B": // getByteFromMMKV
//                StringObject key = vaList.getObject(0);
//                byte[] data = fpMap.get(key.getValue());
//                if (data != null) {
//                    Inspector.inspect(data, "c12 " + key.getValue());
//                    return new ByteArray(vm, data);
//                }
//                return super.callStaticObjectMethodV(vm, dvmClass, signature + ", key=" + key, vaList);

                StringObject c12arg1 = vaList.getObject(0);
                String c12Data = "";
                if (c12arg1.toString().equals("\"soft_config\"")) {
//                    c12Data = "44594501060000000b4d618fcce5ffff0b070a260b040b844d040ab64d0608c20b0609740b0509ca0b0208864d020c714d040d1e0b030f7b0b0008bf0b070e680b0709864d0109e40b070eea4d020e034d060ace0b020f3f4d050db64d040fd60b04094a0b060e3f0b0209340b030e804d030ecc4d020ac94d0409fc0b020e570b0409520b0008360b010a8b0b070f614d010b9a4d02096f0b060fdd4d010cdb4d010c7d0b010af34d000c890b0609ff4d070a1a0b030ddfe52a1500";
                    c12Data = "688969160001279119-87-66-9-1-1793144793109279312-10779612-997939-1137921193123146212714-4579114-9979114381251267793134312713-1051218-1271208-1479315121261346122980797101212414-4512281679682512011591271357120116879614747908-7379115-1031208-5879613-3812213-8379415-97123137179195112714-5279512-1279313-3379491041231112079513-231269-11979312501269-3312712501208-9279514-1087941033-9-35-380";
                } else if (c12arg1.toString().equals("\"soft_data\"")) {
//                    c12Data = "0702050d00070203396035633633346b6235626161366662373563373632336564336137353730306136613465613366030607585001005152050407570004045505045d055702553b363138363b313031616131663031645403500753500553373763386163616265656633656161340f0405510406015c0f04570703030201";
                    c12Data = "185949001937837911026158710357108511011019610661361178410814854505250991015453574956551021015550104107501011001015083184118408284910858979899595650979951979956491025756101485452488418518185851586959180850429593948284153899041452100559898101514955101";
                }
                byte[] c12bData = hexStringToBytes(c12Data);
                System.out.println("c12arg1:" + c12arg1 + ",retdata len:" + c12bData.length);
                return new ByteArray(vm, c12bData);
            default:
                return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d;
        //转换的时候应该注意的是单双数的情况，网上很多都只处理了双数的情况，也就是默认刚好填满，这样16进制字符串长度是单数的话会丢失掉一个字符 因为length/2 是舍去余数的
        if (hexString.length() % 2 != 0) {// 16进制字符串长度是单数
            length = length + 1;
            d = new byte[length];
            // 这里把byte数组从后往前填，字符串也是翻转着填的，最后会空出byte数组的第一个（用来填充我们单出来的那个字符）
            for (int i = length - 1; i > 0; i--) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) | charToByte(hexChars[pos - 1]) << 4);
            }
            d[0] = charToByte(hexChars[0]);
        } else {// 双数情况
            d = new byte[length];
            for (int i = 0; i < length; i++) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }
        }
        return d;
    }


    @Override
    public boolean callBooleanMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "java/net/NetworkInterface->isLoopback()Z":
                NetworkInterface networkInterface = (NetworkInterface) dvmObject;
                return networkInterface.isLoopback();
            case "android/app/KeyguardManager->isKeyguardSecure()Z":
            case "android/net/wifi/WifiManager->startScan()Z":
                return true;
            case "android/os/BinderProxy->isUserAMonkey()Z":
            case "java/net/NetworkInterface->isUp()Z":
                return false;
        }
        return super.callBooleanMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public boolean callStaticBooleanMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "android/os/Debug->isDebuggerConnected()Z":
                return false;
            case "com/tencent/mm/normsg/c$q->c16(Landroid/content/Context;)Z":
                return false; // getMagiskState
            //TODO:
            case "com/tencent/mm/normsg/c$q->c17()Z":
                return false;

        }
        return super.callStaticBooleanMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public long getLongField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        if ("android/content/pm/PackageInfo->firstInstallTime:J".equals(signature)) {
            return 1230739200000L;
//            InstalledPackage info = (InstalledPackage) dvmObject.getValue();
//            return info.getFirstInstallTime() < 1000 ? System.currentTimeMillis() : info.getFirstInstallTime();
        }
        return super.getLongField(vm, dvmObject, signature);
    }

    @Override
    public DvmObject<?> toReflectedMethod(BaseVM vm, DvmClass dvmClass, String signature) {
        switch (signature) {
            case "java/lang/Class->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;":
            case "android/os/SystemProperties->get(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;":
            case "android/os/SystemProperties->get(Ljava/lang/String;)Ljava/lang/String;":
            case "com/tencent/wcdb/database/SQLiteDatabase->insertWithOnConflict(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;I)J": // 监控防撤回插件
            case "java/lang/Thread->getStackTrace()[Ljava/lang/StackTraceElement;":
                return new ArtMethod(vm, signature);
        }
        return super.toReflectedMethod(vm, dvmClass, signature);
    }

    private static final int GET_SIGNATURES = 0x00000040;
    private static final int GET_META_DATA = 0x00000080;
    private static final int TYPE_WIFI = 1;
    private static final int MODE_PRIVATE = 0x0000;
    private static final int RECENT_IGNORE_UNAVAILABLE = 0x0002;
    private static final int TYPE_VPN = 17;
    private static final int ACTION_UP = 1;

    @Override
    public int getStaticIntField(BaseVM vm, DvmClass dvmClass, String signature) {
        switch (signature) {
            case "android/content/pm/PackageManager->GET_SIGNATURES:I":
                return GET_SIGNATURES;
            case "android/net/ConnectivityManager->TYPE_WIFI:I":
                return TYPE_WIFI;
            case "android/content/pm/PackageManager->GET_META_DATA:I":
                return GET_META_DATA;
            case "android/telephony/TelephonyManager->CALL_STATE_IDLE:I":
                return CALL_STATE_IDLE;
            case "android/net/ConnectivityManager->TYPE_VPN:I":
                return TYPE_VPN;
            case "android/content/Context->MODE_PRIVATE:I":
                return MODE_PRIVATE;
        }
        return super.getStaticIntField(vm, dvmClass, signature);
    }

    @Override
    public DvmObject<?> newObjectV(BaseVM vm, DvmClass clazz, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/RuntimeException-><init>(Ljava/lang/String;)V":
                StringObject message = vaList.getObject(0);
                return clazz.newObject(message.getValue());
            case "java/util/UUID-><init>(JJ)V":
                long mostSigBits = vaList.getLong(0);
                long leastSigBits = vaList.getLong(8);
                System.err.println("java/util/UUID-><init> mostSigBits=" + mostSigBits + ", leastSigBits=" + leastSigBits);
                return clazz.newObject(new UUID(mostSigBits, leastSigBits));
            case "android/media/MediaDrm-><init>(Ljava/util/UUID;)V":
                UUID uuid = (UUID) vaList.getObject(0).getValue();
                return clazz.newObject(uuid);

        }
        return super.newObjectV(vm, clazz, signature, vaList);
    }

    @Override
    public void callVoidMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/reflect/ArtMethod->setAccessible(Z)V":
            case "java/lang/reflect/ArtField->setAccessible(Z)V":
            case "java/lang/Runtime->gc()V":
            case "android/media/MediaDrm->close()V":
                return;
        }
        super.callVoidMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public int callIntMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/reflect/ArtMethod->intValue()I":
                return 0;
            case "android/net/NetworkInfo->getType()I":
                NetworkInfo networkInfo = (NetworkInfo) dvmObject;
                return networkInfo.getType();
            case "android/telephony/TelephonyManager->getCallState()I":
                return CALL_STATE_IDLE;

        }
        return super.callIntMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public int getIntField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        switch (signature) {
            case "android/content/pm/ApplicationInfo->flags:I":
                ApplicationInfo applicationInfo = (ApplicationInfo) dvmObject;
                return applicationInfo.getFlags();
            case "android/net/wifi/ScanResult->level:I":
                ScanResult result = (ScanResult) dvmObject;
                return result.getLevel();
        }
        return super.getIntField(vm, dvmObject, signature);
    }

    @Override
    public long callStaticLongMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "com/tencent/mm/normsg/c$q->c2()J":
                return 0; // AccessibilityClickCount
        }
        return super.callStaticLongMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public DvmObject<?> getStaticObjectField(BaseVM vm, DvmClass dvmClass, String signature) {
        switch (signature) {
            case "android/provider/Settings$Secure->ALLOW_MOCK_LOCATION:Ljava/lang/String;":
                return new StringObject(vm, ALLOW_MOCK_LOCATION);
            case "android/os/Build->RADIO:Ljava/lang/String;":
                return new StringObject(vm, device.getRadio());
            case "android/provider/Settings$Secure->ANDROID_ID:Ljava/lang/String;":
                return new StringObject(vm, ANDROID_ID);
            case "android/os/Build->SERIAL:Ljava/lang/String;":
                return new StringObject(vm, device.getSerial());
            case "android/provider/Settings$Global->WIFI_ON:Ljava/lang/String;":
                return new StringObject(vm, WIFI_ON);
            case "android/provider/Settings$Global->ADB_ENABLED:Ljava/lang/String;":
                return new StringObject(vm, ADB_ENABLED);
            case "android/provider/Settings$Secure->ENABLED_ACCESSIBILITY_SERVICES:Ljava/lang/String;":
                return new StringObject(vm, ENABLED_ACCESSIBILITY_SERVICES);
            case "de/robv/android/xposed/XposedBridge->sHookedMethodCallbacks:Ljava/util/Map;":
                return vm.resolveClass("java/util/Map").newObject(Collections.EMPTY_MAP);
            //TODO:
            case  "android/os/Build->FINGERPRINT:Ljava/lang/String;":
                return new StringObject(vm,"unknow");

        }
        return super.getStaticObjectField(vm, dvmClass, signature);
    }

    @Override
    public int callStaticIntMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "android/provider/Settings$Global->getInt(Landroid/content/ContentResolver;Ljava/lang/String;I)I":
            case "android/provider/Settings$Secure->getInt(Landroid/content/ContentResolver;Ljava/lang/String;I)I": {
                StringObject object = vaList.getObject(4);
                String name = object.getValue();
                switch (name) {
                    case ALLOW_MOCK_LOCATION:
                    case WIFI_ON:
                    case ADB_ENABLED:
                        return VM.JNI_FALSE;
                    default:
                        int def = vaList.getInt(8);
                        System.err.println("signature=" + signature + ", name=" + name + ", return default value: " + def);
                        return def;
                }
            }
            case "android/os/Process->myUid()I":
                return 88;
        }
        return super.callStaticIntMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public void callStaticVoidMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "com/tencent/mm/normsg/c$q->c4(III)V":
                return;
        }
        super.callStaticVoidMethodV(vm, dvmClass, signature, vaList);
    }

    private final Map<String, File> fileMap = new HashMap<>();
    private final Map<String, byte[]> fpMap = new HashMap<>();

    @Override
    public FileResult<AndroidFileIO> resolve(Emulator<AndroidFileIO> emulator, String pathname, int oflags) {
        File file = fileMap.get(pathname);
        if (file != null) {
            return FileResult.<AndroidFileIO>success(new SimpleFileIO(oflags, file, pathname));
        } else if (fileMap.containsKey(pathname)) {
            return FileResult.<AndroidFileIO>success(new DirectoryFileIO(oflags, pathname));
        }
        if (pathname.equals("/dev/alarm")) {
            return FileResult.<AndroidFileIO>success(DriverFileIO.create(emulator, oflags, pathname));
        }

        if ("/proc/stat".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("cpu  36152 2046 53569 3049583 2718 8997 4513 0 0 0\n" +
                    "cpu0 10781 610 16052 755326 617 3603 1439 0 0 0\n" +
                    "cpu1 13120 687 22262 746415 901 3288 2190 0 0 0\n" +
                    "cpu2 8518 602 10775 767574 634 1228 439 0 0 0\n" +
                    "cpu3 3732 144 4478 780266 565 876 443 0 0 0\n" +
                    "intr 3008709 0 0 0 688254 0 76211 10065 3 5 4 233 0 0 476 12619 17 4167 194 0 67005 0 0 0 0 4760 688 0 0 0 0 0 0 31371 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1657 11260 399 76462 0 0 0 0 1 77388 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 0 0 5 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 4 0 0 0 0 0 0 0 5 0 0 0 0 0 0 0 4 0 0 0 0 0 77362 0 0 0 0 0 0 0 0 0 0 586 7 7 7 0 0 0 0 0 0 0 32 12104 0 26 0 0 0 2 2 0 0 0 6 6 0 0 0 2 0 462 5485 626 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 376 0 0 0 0 88 992 0 0 0 5 0 0 0 0 0 0 0 0 0 0 2634 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 34 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 90 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 11788 0 0 0 0 0 11928 1 506 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 15110 8646 22198 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 393051 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "ctxt 4728218\n" +
                    "btime 1577622710\n" +
                    "processes 16073\n" +
                    "procs_running 1\n" +
                    "procs_blocked 0\n" +
                    "softirq 1827392 80848 628289 2172 6204 23209 0 336365 217108 2870 530327\n").getBytes()));
        }
        if ("/proc/cpuinfo".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, device.getCpuInfo().getBytes(StandardCharsets.UTF_8)));
        }
        if ("/proc/self/status".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("Name:\tcom.tencent.mm\n" +
                    "State:\tR (running)\n" +
                    "Tgid:\t2561\n" +
                    "Pid:\t2561\n" +
                    "PPid:\t640\n" +
                    "TracerPid:\t0\n" +
                    "Uid:\t10090\t10090\t10090\t10090\n" +
                    "Gid:\t10090\t10090\t10090\t10090\n" +
                    "Ngid:\t0\n" +
                    "FDSize:\t256\n" +
                    "Groups:\t3001 3002 3003 9997 50090\n" +
                    "VmPeak:\t 2079508 kB\n" +
                    "VmSize:\t 2078708 kB\n" +
                    "VmLck:\t       0 kB\n" +
                    "VmPin:\t       0 kB\n" +
                    "VmHWM:\t  239564 kB\n" +
                    "VmRSS:\t  212808 kB\n" +
                    "VmData:\t  294476 kB\n" +
                    "VmStk:\t    8196 kB\n" +
                    "VmExe:\t      52 kB\n" +
                    "VmLib:\t  167708 kB\n" +
                    "VmPTE:\t    1300 kB\n" +
                    "VmSwap:\t       0 kB\n" +
                    "Threads:\t80\n" +
                    "SigQ:\t0/14024\n" +
                    "SigPnd:\t0000000000000000\n" +
                    "ShdPnd:\t0000000000000000\n" +
                    "SigBlk:\t0000000000001204\n" +
                    "SigIgn:\t0000000000001000\n" +
                    "SigCgt:\t20000002000084f8\n" +
                    "CapInh:\t0000000000000000\n" +
                    "CapPrm:\t0000000000000000\n" +
                    "CapEff:\t0000000000000000\n" +
                    "CapBnd:\t0000000000000000\n" +
                    "Seccomp:\t0\n" +
                    "Cpus_allowed:\tf\n" +
                    "Cpus_allowed_list:\t0-3\n" +
                    "Mems_allowed:\t1\n" +
                    "Mems_allowed_list:\t0\n" +
                    "voluntary_ctxt_switches:\t2024\n" +
                    "nonvoluntary_ctxt_switches:\t3954\n").getBytes()));
        }
        if (("/proc/self/task/" + emulator.getPid() + "/status").equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("Name:\tcom.tencent.mm\n" +
                    "State:\tR (running)\n" +
                    "Tgid:\t2561\n" +
                    "Pid:\t2561\n" +
                    "PPid:\t640\n" +
                    "TracerPid:\t0\n" +
                    "Uid:\t10090\t10090\t10090\t10090\n" +
                    "Gid:\t10090\t10090\t10090\t10090\n" +
                    "Ngid:\t0\n" +
                    "FDSize:\t256\n" +
                    "Groups:\t3001 3002 3003 9997 50090\n" +
                    "VmPeak:\t 2079772 kB\n" +
                    "VmSize:\t 2067032 kB\n" +
                    "VmLck:\t       0 kB\n" +
                    "VmPin:\t       0 kB\n" +
                    "VmHWM:\t  239564 kB\n" +
                    "VmRSS:\t  226452 kB\n" +
                    "VmData:\t  292876 kB\n" +
                    "VmStk:\t    8196 kB\n" +
                    "VmExe:\t      52 kB\n" +
                    "VmLib:\t  167708 kB\n" +
                    "VmPTE:\t    1320 kB\n" +
                    "VmSwap:\t       0 kB\n" +
                    "Threads:\t78\n" +
                    "SigQ:\t0/14024\n" +
                    "SigPnd:\t0000000000000000\n" +
                    "ShdPnd:\t0000000000000000\n" +
                    "SigBlk:\t0000000000001204\n" +
                    "SigIgn:\t0000000000001000\n" +
                    "SigCgt:\t20000002000084f8\n" +
                    "CapInh:\t0000000000000000\n" +
                    "CapPrm:\t0000000000000000\n" +
                    "CapEff:\t0000000000000000\n" +
                    "CapBnd:\t0000000000000000\n" +
                    "Seccomp:\t0\n" +
                    "Cpus_allowed:\tf\n" +
                    "Cpus_allowed_list:\t0-3\n" +
                    "Mems_allowed:\t1\n" +
                    "Mems_allowed_list:\t0\n" +
                    "voluntary_ctxt_switches:\t83823\n" +
                    "nonvoluntary_ctxt_switches:\t211713\n").getBytes()));
        }
        if ("/proc/bus/pci/devices".equals(pathname)) { // check contains "virtio"
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("0000\t17cb0104\t0\t         c300004\t               0\t               0\t               0\t               0\t               0\t               0\t            1000\t               0\t               0\t               0\t               0\t               0\t               0\n" +
                    "0100\t168c003e\t22\t         c400004\t               0\t               0\t               0\t               0\t               0\t               0\t          200000\t               0\t               0\t               0\t               0\t               0\t               0\tcnss_wlan_pci\n").getBytes()));
        }
        if ("/proc/interrupts".equals(pathname)) { // check contains "virtio"
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("           CPU0       CPU1       CPU2       CPU3\n" +
                    "  3:     241275     331030     107302      62839     GICv3  27  arch_timer\n" +
                    "  5:      48391       9679      12424      10460     GICv3  62  arch_mem_timer\n" +
                    "  6:      10186          0          0          0     GICv3 115  MDSS\n" +
                    "  7:          3          0          0          0     GICv3 483  smp2p\n" +
                    "  8:          5          0          0          0     GICv3 190  smp2p\n" +
                    "  9:          4          0          0          0     GICv3 210  smp2p\n" +
                    " 10:        237          0          0          0     GICv3 224  spdm_bw_hyp\n" +
                    " 13:        476          0          0          0     GICv3 138  i2c-msm-v2-irq\n" +
                    " 14:      13105          0          0          0     GICv3 270  sps\n" +
                    " 15:         17          0          0          0     GICv3 271  sps\n" +
                    " 16:        122       4123          0          0     GICv3 133  i2c-msm-v2-irq\n" +
                    " 17:        194          0          0          0     GICv3 134  i2c-msm-v2-irq\n" +
                    " 19:        673      71944          0          0     GICv3 215  408000.qcom,cpu-bwmon\n" +
                    " 24:       4760          0          0          0     GICv3 195  ngd_slim_irq\n" +
                    " 25:        688          0          0          0     GICv3 196  sps\n" +
                    " 30:          0          0          0          0     GICv3 290  630000.ufsice\n" +
                    " 31:          0          0          0          0     GICv3 493  7443000.sdcc1ice\n" +
                    " 32:      31627          0          0          0     GICv3 297  ufshcd\n" +
                    " 33:          0          0          0          0     GICv3 437  int_msi\n" +
                    " 40:          0          0          0          0     GICv3 283  int_pls_err\n" +
                    " 41:          0          0          0          0     GICv3 284  int_aer_legacy\n" +
                    " 43:          0          0          0          0     GICv3 286  int_pls_link_down\n" +
                    "165:        356       1315          0          0     GICv3 481  qcom,smd-modem\n" +
                    "166:      11260          0          0          0     GICv3 188  qcom,smd-adsp\n" +
                    "167:        388         11          0          0     GICv3 208  qcom,smd-dsps\n" +
                    "168:      68098       6909       2596       1606     GICv3 200  qcom,smd-rpm, qcom,glink-smem-native-xprt-rpm\n" +
                    "169:          0          0          0          0     GICv3  60  apps_wdog_bark\n" +
                    "171:          0          0          0          0     GICv3 379  hs_phy_irq\n" +
                    "172:          0          0          0          0     GICv3 275  ss_phy_irq\n" +
                    "173:          1          0          0          0     GICv3 212  msm_dwc3\n" +
                    "174:      81154          0          0          0     GICv3 358  400f000.qcom,spmi\n" +
                    "175:          0          0          0          0  qpnp-int 288  pm8994_tz\n" +
                    "176:          0          0          0          0  qpnp-int  64  qpnp_kpdpwr_status\n" +
                    "177:          0          0          0          0  qpnp-int  65  qpnp_resin_status\n" +
                    "179:          0          0          0          0  qpnp-int  69  qpnp_kpdpwr_resin_bark\n" +
                    "182:          0          0          0          0  qpnp-int 419  qpnp_adc_tm_high_interrupt\n" +
                    "183:          0          0          0          0  qpnp-int 420  qpnp_adc_tm_low_interrupt\n" +
                    "184:          0          0          0          0  qpnp-int 777  qpnp_rtc_alarm\n" +
                    "185:          0          0          0          0  qpnp-int 4624  bcl_ibat_interrupt\n" +
                    "186:          0          0          0          0  qpnp-int 4625  bcl_vbat_interrupt\n" +
                    "188:          0          0          0          0  qpnp-int 4491  qpnp_vadc_high_interrupt\n" +
                    "189:          0          0          0          0  qpnp-int 4492  qpnp_vadc_low_interrupt\n" +
                    "190:          0          0          0          0  qpnp-int 4224  chg-error\n" +
                    "194:          9          0          0          0  qpnp-int 4228  chg-p2f-thr\n" +
                    "195:          0          0          0          0  qpnp-int 4229  chg-rechg-thr\n" +
                    "196:          0          0          0          0  qpnp-int 4230  chg-taper-thr\n" +
                    "197:          5          0          0          0  qpnp-int 4231  chg-tcc-thr\n" +
                    "198:          0          0          0          0  qpnp-int 4240  batt-hot\n" +
                    "199:          0          0          0          0  qpnp-int 4241  batt-warm\n" +
                    "200:          0          0          0          0  qpnp-int 4242  batt-cold\n" +
                    "201:          0          0          0          0  qpnp-int 4243  batt-cool\n" +
                    "203:          0          0          0          0  qpnp-int 4245  batt-low\n" +
                    "204:          0          0          0          0  qpnp-int 4246  batt-missing\n" +
                    "206:          0          0          0          0  qpnp-int 4248  usbin-uv\n" +
                    "207:          0          0          0          0  qpnp-int 4249  usbin-ov\n" +
                    "208:          0          0          0          0  qpnp-int 4250  usbin-src-det\n" +
                    "209:          0          0          0          0  qpnp-int 4251  otg-fail\n" +
                    "210:          0          0          0          0  qpnp-int 4252  otg-oc\n" +
                    "211:          0          0          0          0  qpnp-int 4253  aicl-done\n" +
                    "212:          0          0          0          0  qpnp-int 4254  usbid-change\n" +
                    "213:          0          0          0          0  qpnp-int 4256  dcin-uv\n" +
                    "215:          4          0          0          0  qpnp-int 4272  power-ok\n" +
                    "216:          0          0          0          0  qpnp-int 4273  temp-shutdown\n" +
                    "217:          0          0          0          0  qpnp-int 4274  wdog-timeout\n" +
                    "223:          5          0          0          0  qpnp-int 4610  full-soc\n" +
                    "224:          0          0          0          0  qpnp-int 4611  empty-soc\n" +
                    "225:          0          0          0          0  qpnp-int 4612  delta-soc\n" +
                    "226:          0          0          0          0  qpnp-int 4613  first-est-done\n" +
                    "229:          0          0          0          0  qpnp-int 4616  soft-cold\n" +
                    "230:          0          0          0          0  qpnp-int 4617  soft-hot\n" +
                    "231:          4          0          0          0  qpnp-int 4618  vbatt-low\n" +
                    "235:          0          0          0          0  qpnp-int 4622  batt-missing\n" +
                    "237:      81128          0          0          0  qpnp-int 4640  mem-avail\n" +
                    "239:          0          0          0          0  qpnp-int 7680  qpnp_sc_irq\n" +
                    "241:          0          0          0          0     GICv3 194  adsp\n" +
                    "242:          0          0          0          0     GICv3 422  slpi\n" +
                    "243:          0          0          0          0     GICv3 480  modem\n" +
                    "244:          0          0          0          0     GICv3 490  tsens_interrupt\n" +
                    "245:          0          0          0          0     GICv3 477  tsens_critical_interrupt\n" +
                    "247:          0          0          0          0     GICv3 238  sps\n" +
                    "248:        139        163        231        102     GICv3  23  arm-pmu\n" +
                    "249:          7          0          0          0     GICv3 484  qcom,glink-smem-native-xprt-modem\n" +
                    "250:          7          0          0          0     GICv3 189  qcom,glink-smem-native-xprt-adsp\n" +
                    "251:          7          0          0          0     GICv3 211  qcom,glink-smem-native-xprt-dsps\n" +
                    "252:          0          0          0          0     GICv3  16  l1_irq\n" +
                    "253:          0          0          0          0     GICv3  33  l2_irq_info_0\n" +
                    "254:          0          0          0          0     GICv3  41  l2_irq_info_1\n" +
                    "255:          0          0          0          0     GICv3  34  l2_irq_err_0\n" +
                    "256:          0          0          0          0     GICv3  42  l2_irq_err_1\n" +
                    "257:          0          0          0          0     GICv3  49  l3_irq\n" +
                    "258:          0          0          0          0     GICv3  54  m4m_irq\n" +
                    "259:          1         31          0          0     GICv3  55  lmh-interrupt\n" +
                    "260:      13172          0          0          0     GICv3  80  cpr3\n" +
                    "261:          0          0          0          0     GICv3  79  cpr3_ceiling\n" +
                    "262:         26          0          0          0     GICv3 198  cpr3\n" +
                    "263:          0          0          0          0     GICv3 110  csiphy\n" +
                    "264:          0          0          0          0     GICv3 111  csiphy\n" +
                    "265:          0          0          0          0     GICv3 112  csiphy\n" +
                    "266:          2          0          0          0     GICv3 328  csid\n" +
                    "267:          2          0          0          0     GICv3 329  csid\n" +
                    "268:          0          0          0          0     GICv3 330  csid\n" +
                    "269:          0          0          0          0     GICv3 331  csid\n" +
                    "270:          0          0          0          0     GICv3 341  ispif\n" +
                    "271:          6          0          0          0     GICv3 346  vfe\n" +
                    "272:          6          0          0          0     GICv3 347  vfe\n" +
                    "276:          2          0          0          0     GICv3 326\n" +
                    "277:          0          0          0          0     GICv3 325  fd\n" +
                    "278:        462          0          0          0     GICv3 327  cci\n" +
                    "279:        255       5270          0          0     GICv3 332  kgsl-3d0\n" +
                    "280:        351        168         59         48     GICv3 203  681b8.qcom,mpm\n" +
                    "281:          0          0          0          0     GICv3  99  arm-smmu global fault\n" +
                    "285:          0          0          0          0     GICv3 366  arm-smmu global fault\n" +
                    "286:          0          0          0          0     GICv3 361  arm-smmu-context-fault\n" +
                    "287:          0          0          0          0     GICv3 362  arm-smmu-context-fault\n" +
                    "288:          0          0          0          0     GICv3 108  arm-smmu global fault\n" +
                    "291:          0          0          0          0     GICv3 318  arm-smmu global fault\n" +
                    "292:          0          0          0          0     GICv3 367  arm-smmu-context-fault\n" +
                    "293:          0          0          0          0     GICv3 368  arm-smmu-context-fault\n" +
                    "294:          0          0          0          0     GICv3 369  arm-smmu-context-fault\n" +
                    "295:          0          0          0          0     GICv3 370  arm-smmu-context-fault\n" +
                    "299:          0          0          0          0     GICv3 105  arm-smmu global fault\n" +
                    "300:          0          0          0          0     GICv3 352  arm-smmu-context-fault\n" +
                    "301:          0          0          0          0     GICv3 353  arm-smmu-context-fault\n" +
                    "302:          0          0          0          0     GICv3 385  arm-smmu global fault\n" +
                    "303:          0          0          0          0     GICv3 380  arm-smmu-context-fault\n" +
                    "304:          0          0          0          0     GICv3 381  arm-smmu-context-fault\n" +
                    "305:          0          0          0          0     GICv3 296  arm-smmu global fault\n" +
                    "309:          0          0          0          0     GICv3 436  arm-smmu global fault\n" +
                    "310:          0          0          0          0     GICv3 258  arm-smmu-context-fault\n" +
                    "311:          0          0          0          0     GICv3 425  arm-smmu-context-fault\n" +
                    "312:          0          0          0          0     GICv3 426  arm-smmu-context-fault\n" +
                    "313:          0          0          0          0     GICv3 427  arm-smmu-context-fault\n" +
                    "314:          0          0          0          0     GICv3 428  arm-smmu-context-fault\n" +
                    "315:          0          0          0          0     GICv3 429  arm-smmu-context-fault\n" +
                    "316:          0          0          0          0     GICv3 430  arm-smmu-context-fault\n" +
                    "317:          0          0          0          0     GICv3 431  arm-smmu-context-fault\n" +
                    "318:          0          0          0          0     GICv3 432  arm-smmu-context-fault\n" +
                    "322:          0          0          0          0     GICv3 394  arm-smmu global fault\n" +
                    "330:          0          0          0          0     GICv3 403  arm-smmu global fault\n" +
                    "335:          0          0          0          0     GICv3 413  arm-smmu global fault\n" +
                    "344:          0        376          0          0     GICv3 319  msm_vidc\n" +
                    "345:          0          0          0          0     GICv3 461  vmem\n" +
                    "347:          0          0          0          0     GICv3  32  l2-cache-pmu\n" +
                    "348:          0          0          0          0     GICv3  40  l2-cache-pmu\n" +
                    "349:          2         87          0          0     GICv3 365  ipa\n" +
                    "350:        999          0          0          0     GICv3 464  sps\n" +
                    "352:          0          0          0          0     GICv3 216  tsens_interrupt\n" +
                    "353:          0          0          0          0     GICv3 462  tsens_critical_interrupt\n" +
                    "354:          5          0          0          0     GICv3 240\n" +
                    "365:       2760          0          0          0   msmgpio  10  VSYNC_GPIO\n" +
                    "392:          0          0          0          0   msmgpio  37  msm_pcie_wake\n" +
                    "395:          0          0          0          0   msmgpio  40  power_key\n" +
                    "405:         34          0          0          0   msmgpio  50  pn551-nfc\n" +
                    "409:          0          0          0          0   msmgpio  54  wcd9xxx\n" +
                    "417:          0          0          0          0   msmgpio  62  vl53l0_interrupt\n" +
                    "441:         90          0          0          0   msmgpio  86  soc:fp_fpc1020\n" +
                    "472:      12273          1          0          0   msmgpio 117  nanohub-irq2\n" +
                    "476:          0          0          0          0   msmgpio 121  volume_down\n" +
                    "478:      12413          1          0          0   msmgpio 123  nanohub-irq1\n" +
                    "479:          1          0          0          0   msmgpio 124  fsc_interrupt_int_n\n" +
                    "480:        506          0          0          0   msmgpio 125  synaptics_dsxv26\n" +
                    "481:          0          0          0          0   msmgpio 126  volume_up\n" +
                    "505:      15110          0          0          0     GICv3 132  757a000.spi\n" +
                    "506:        500       8146          0          0     GICv3 129  i2c-msm-v2-irq\n" +
                    "507:      22198          0          0          0     GICv3 135  i2c-msm-v2-irq\n" +
                    "636:          0          0          0          0  smp2p_gpio   0  modem\n" +
                    "637:          1          0          0          0  smp2p_gpio   1  error_ready_interrupt\n" +
                    "638:          1          0          0          0  smp2p_gpio   2  modem\n" +
                    "639:          0          0          0          0  smp2p_gpio   3  modem\n" +
                    "668:          0          0          0          0  smp2p_gpio   0  adsp\n" +
                    "669:          1          0          0          0  smp2p_gpio   1  error_ready_interrupt\n" +
                    "670:          1          0          0          0  smp2p_gpio   2  adsp\n" +
                    "671:          0          0          0          0  smp2p_gpio   3  adsp\n" +
                    "700:          0          0          0          0  smp2p_gpio   0  slpi\n" +
                    "701:          1          0          0          0  smp2p_gpio   1  error_ready_interrupt\n" +
                    "702:          1          0          0          0  smp2p_gpio   2  slpi\n" +
                    "703:          0          0          0          0  smp2p_gpio   3  slpi\n" +
                    "796:       3561     426035          0          0     GICv3 163  dwc3\n" +
                    "798:          0          0          0          0   wcd9xxx   0  SLIMBUS Slave\n" +
                    "800:          0          0          0          0   wcd9xxx   2  HPH_L OCP detect\n" +
                    "801:          0          0          0          0   wcd9xxx   3  HPH_R OCP detect\n" +
                    "806:          0          0          0          0   wcd9xxx   8  mbhc sw intr\n" +
                    "807:          0          0          0          0   wcd9xxx   9  Elect Remove\n" +
                    "808:          0          0          0          0   wcd9xxx  10  Button Press detect\n" +
                    "809:          0          0          0          0   wcd9xxx  11  Button Release detect\n" +
                    "810:          0          0          0          0   wcd9xxx  12  Elect Insert\n" +
                    "IPI0:    290303     292976     253912     220571       Rescheduling interrupts\n" +
                    "IPI1:        57         63         59         62       Function call interrupts\n" +
                    "IPI2:    134430     139294      88330      41461       Single function call interrupts\n" +
                    "IPI3:         0          0          0          0       CPU stop interrupts\n" +
                    "IPI4:       418        127        197        115       Timer broadcast interrupts\n" +
                    "IPI5:         3          4          0          0       IRQ work interrupts\n" +
                    "IPI6:         0          0          0          0       CPU wakeup interrupts\n" +
                    "IPI7:         0          0          0          0       CPU backtrace\n" +
                    "Err:          5\n").getBytes()));
        }
        if ("/proc/diskstats".equals(pathname)) { // check contains "sda"
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("   1       0 ram0 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       1 ram1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       2 ram2 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       3 ram3 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       4 ram4 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       5 ram5 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       6 ram6 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       7 ram7 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       8 ram8 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1       9 ram9 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1      10 ram10 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1      11 ram11 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1      12 ram12 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1      13 ram13 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1      14 ram14 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   1      15 ram15 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       0 loop0 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       1 loop1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       2 loop2 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       3 loop3 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       4 loop4 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       5 loop5 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       6 loop6 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   7       7 loop7 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 254       0 zram0 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       0 sda 9696 2036 1010288 8043 90293 46938 2278288 1654646 0 664930 1661750\n" +
                    "   8       1 sda1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       2 sda2 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       3 sda3 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       4 sda4 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       5 sda5 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       6 sda6 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       7 sda7 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       8 sda8 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8       9 sda9 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      10 sda10 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      11 sda11 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      12 sda12 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      13 sda13 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      14 sda14 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      15 sda15 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       0 sda16 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       1 sda17 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       2 sda18 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       3 sda19 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       4 sda20 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       5 sda21 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       6 sda22 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       7 sda23 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       8 sda24 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259       9 sda25 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      10 sda26 6 0 48 6 0 0 0 0 0 3 3\n" +
                    " 259      11 sda27 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      12 sda28 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      13 sda29 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      14 sda30 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      15 sda31 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      16 sda32 512 2 68296 326 0 0 0 0 0 296 326\n" +
                    " 259      17 sda33 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 259      18 sda34 1985 1992 301432 1696 0 0 0 0 0 1173 1666\n" +
                    " 259      19 sda35 7186 42 640456 5963 89205 46938 2278288 1653143 0 662380 1658200\n" +
                    " 259      20 sda36 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      16 sdb 5 0 40 26 0 0 0 0 0 26 26\n" +
                    "   8      17 sdb1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      80 sdf 11 0 1832 26 0 0 0 0 0 26 26\n" +
                    "   8      81 sdf1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      82 sdf2 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      83 sdf3 6 0 1792 6 0 0 0 0 0 6 6\n" +
                    "   8      84 sdf4 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      85 sdf5 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      32 sdc 5 0 40 30 0 0 0 0 0 30 30\n" +
                    "   8      33 sdc1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      48 sdd 39 0 4872 46 216 1022 8288 1440 0 1333 1483\n" +
                    "   8      49 sdd1 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      50 sdd2 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      51 sdd3 13 0 696 3 6 6 96 16 0 16 20\n" +
                    "   8      52 sdd4 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      53 sdd5 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      54 sdd6 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      55 sdd7 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      56 sdd8 1 0 8 0 0 0 0 0 0 0 0\n" +
                    "   8      57 sdd9 19 0 4096 13 4 508 4096 153 0 56 166\n" +
                    "   8      58 sdd10 1 0 32 0 4 508 4096 63 0 26 63\n" +
                    "   8      59 sdd11 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      60 sdd12 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      61 sdd13 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      64 sde 15 0 1088 26 0 0 0 0 0 26 26\n" +
                    "   8      65 sde1 10 0 1048 10 0 0 0 0 0 10 10\n" +
                    "   8      66 sde2 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      67 sde3 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      68 sde4 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      69 sde5 0 0 0 0 0 0 0 0 0 0 0\n" +
                    "   8      70 sde6 0 0 0 0 0 0 0 0 0 0 0\n" +
                    " 253       0 dm-0 4432 0 1010688 9020 0 0 0 0 0 4340 9020\n").getBytes()));
        }
        if ("/proc/filesystems".equals(pathname)) { // check contains "vboxsf"
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("nodev\tsysfs\n" +
                    "nodev\trootfs\n" +
                    "nodev\tramfs\n" +
                    "nodev\tbdev\n" +
                    "nodev\tproc\n" +
                    "nodev\tcgroup\n" +
                    "nodev\tcpuset\n" +
                    "nodev\ttmpfs\n" +
                    "nodev\tdebugfs\n" +
                    "nodev\tsockfs\n" +
                    "nodev\tpipefs\n" +
                    "nodev\tconfigfs\n" +
                    "nodev\tdevpts\n" +
                    "\text3\n" +
                    "\text2\n" +
                    "\text4\n" +
                    "\tvfat\n" +
                    "\tmsdos\n" +
                    "nodev\tecryptfs\n" +
                    "nodev\tsdcardfs\n" +
                    "\tfuseblk\n" +
                    "nodev\tfuse\n" +
                    "nodev\tfusectl\n" +
                    "nodev\tpstore\n" +
                    "nodev\tselinuxfs\n" +
                    "nodev\tfunctionfs\n").getBytes()));
        }
        if ("/sdcard/tencent/MicroMsg/browser/version_id.dat".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, "e0ca9f7f-07da-4d5f-9494-8d9f10ffa785".getBytes()));
        }
        if ("/proc/self/maps".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, ("57462e0000-5746309000 r--p 00000000 103:1e 512                           /system/bin/toybox\n" +
                    "5746309000-574634d000 --xp 00029000 103:1e 512                           /system/bin/toybox\n" +
                    "574634d000-5746351000 rw-p 0006d000 103:1e 512                           /system/bin/toybox\n" +
                    "5746351000-5746355000 r--p 00071000 103:1e 512                           /system/bin/toybox\n" +
                    "5746355000-574635a000 rw-p 00000000 00:00 0 \n" +
                    "7a3b400000-7a3bc00000 rw-p 00000000 00:00 0                              [anon:libc_malloc]\n" +
                    "7a3bd0d000-7a3bd0f000 r--p 00000000 103:1e 2486                          /system/lib64/libnetd_client.so\n" +
                    "7a3bd0f000-7a3bd11000 --xp 00002000 103:1e 2486                          /system/lib64/libnetd_client.so\n" +
                    "7a3bd11000-7a3bd12000 rw-p 00004000 103:1e 2486                          /system/lib64/libnetd_client.so\n" +
                    "7a3bd12000-7a3bd13000 r--p 00005000 103:1e 2486                          /system/lib64/libnetd_client.so\n" +
                    "7a3bd13000-7a3bd14000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3bd6d000-7a3bdd1000 rw-p 00000000 00:00 0                              [anon:linker_alloc]\n" +
                    "7a3bdd1000-7a3be11000 r--p 00000000 07:20 91                             /apex/com.android.runtime/lib64/bionic/libc.so\n" +
                    "7a3be11000-7a3beb9000 --xp 00040000 07:20 91                             /apex/com.android.runtime/lib64/bionic/libc.so\n" +
                    "7a3beb9000-7a3bebc000 rw-p 000e8000 07:20 91                             /apex/com.android.runtime/lib64/bionic/libc.so\n" +
                    "7a3bebc000-7a3bec3000 r--p 000eb000 07:20 91                             /apex/com.android.runtime/lib64/bionic/libc.so\n" +
                    "7a3bec3000-7a3bec7000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3bec7000-7a3bec8000 r--p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3bec8000-7a3c0da000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c108000-7a3c10f000 r--p 00000000 103:1e 2365                          /system/lib64/libcutils.so\n" +
                    "7a3c10f000-7a3c118000 --xp 00007000 103:1e 2365                          /system/lib64/libcutils.so\n" +
                    "7a3c118000-7a3c119000 rw-p 00010000 103:1e 2365                          /system/lib64/libcutils.so\n" +
                    "7a3c119000-7a3c11b000 r--p 00011000 103:1e 2365                          /system/lib64/libcutils.so\n" +
                    "7a3c11b000-7a3c11c000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c15c000-7a3c162000 r--p 00000000 103:1e 2450                          /system/lib64/liblog.so\n" +
                    "7a3c162000-7a3c170000 --xp 00006000 103:1e 2450                          /system/lib64/liblog.so\n" +
                    "7a3c170000-7a3c171000 rw-p 00014000 103:1e 2450                          /system/lib64/liblog.so\n" +
                    "7a3c171000-7a3c172000 r--p 00015000 103:1e 2450                          /system/lib64/liblog.so\n" +
                    "7a3c172000-7a3c173000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c183000-7a3c195000 r--p 00000000 07:20 93                             /apex/com.android.runtime/lib64/bionic/libm.so\n" +
                    "7a3c195000-7a3c1b9000 --xp 00012000 07:20 93                             /apex/com.android.runtime/lib64/bionic/libm.so\n" +
                    "7a3c1b9000-7a3c1ba000 rw-p 00036000 07:20 93                             /apex/com.android.runtime/lib64/bionic/libm.so\n" +
                    "7a3c1ba000-7a3c1bb000 r--p 00037000 07:20 93                             /apex/com.android.runtime/lib64/bionic/libm.so\n" +
                    "7a3c1bb000-7a3c1bc000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c1c4000-7a3c1cc000 r--p 00000000 103:1e 2584                          /system/lib64/libz.so\n" +
                    "7a3c1cc000-7a3c1db000 --xp 00008000 103:1e 2584                          /system/lib64/libz.so\n" +
                    "7a3c1db000-7a3c1dc000 rw-p 00017000 103:1e 2584                          /system/lib64/libz.so\n" +
                    "7a3c1dc000-7a3c1dd000 r--p 00018000 103:1e 2584                          /system/lib64/libz.so\n" +
                    "7a3c209000-7a3c20a000 r--p 00000000 07:20 92                             /apex/com.android.runtime/lib64/bionic/libdl.so\n" +
                    "7a3c20a000-7a3c20b000 --xp 00001000 07:20 92                             /apex/com.android.runtime/lib64/bionic/libdl.so\n" +
                    "7a3c20b000-7a3c20c000 r--p 00002000 07:20 92                             /apex/com.android.runtime/lib64/bionic/libdl.so\n" +
                    "7a3c20c000-7a3c20d000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c244000-7a3c2ba000 r--p 00000000 103:1e 2361                          /system/lib64/libcrypto.so\n" +
                    "7a3c2ba000-7a3c364000 --xp 00076000 103:1e 2361                          /system/lib64/libcrypto.so\n" +
                    "7a3c364000-7a3c365000 rw-p 00120000 103:1e 2361                          /system/lib64/libcrypto.so\n" +
                    "7a3c365000-7a3c376000 r--p 00121000 103:1e 2361                          /system/lib64/libcrypto.so\n" +
                    "7a3c376000-7a3c378000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c380000-7a3c396000 r--p 00000000 103:1e 2508                          /system/lib64/libprocessgroup.so\n" +
                    "7a3c396000-7a3c3ba000 --xp 00016000 103:1e 2508                          /system/lib64/libprocessgroup.so\n" +
                    "7a3c3ba000-7a3c3bb000 rw-p 0003a000 103:1e 2508                          /system/lib64/libprocessgroup.so\n" +
                    "7a3c3bb000-7a3c3bd000 r--p 0003b000 103:1e 2508                          /system/lib64/libprocessgroup.so\n" +
                    "7a3c3bd000-7a3c3be000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c3e5000-7a3c3ec000 r--p 00000000 103:1e 2522                          /system/lib64/libselinux.so\n" +
                    "7a3c3ec000-7a3c3fc000 --xp 00007000 103:1e 2522                          /system/lib64/libselinux.so\n" +
                    "7a3c3fc000-7a3c3fd000 rw-p 00017000 103:1e 2522                          /system/lib64/libselinux.so\n" +
                    "7a3c3fd000-7a3c3fe000 r--p 00018000 103:1e 2522                          /system/lib64/libselinux.so\n" +
                    "7a3c3fe000-7a3c400000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c407000-7a3c457000 r--p 00000000 103:1e 2340                          /system/lib64/libc++.so\n" +
                    "7a3c457000-7a3c4c9000 --xp 00050000 103:1e 2340                          /system/lib64/libc++.so\n" +
                    "7a3c4c9000-7a3c4ca000 rw-p 000c2000 103:1e 2340                          /system/lib64/libc++.so\n" +
                    "7a3c4ca000-7a3c4d2000 r--p 000c3000 103:1e 2340                          /system/lib64/libc++.so\n" +
                    "7a3c4d2000-7a3c4d6000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c50e000-7a3c510000 r--p 00000000 103:1e 2349                          /system/lib64/libcgrouprc.so\n" +
                    "7a3c510000-7a3c511000 --xp 00002000 103:1e 2349                          /system/lib64/libcgrouprc.so\n" +
                    "7a3c511000-7a3c512000 rw-p 00003000 103:1e 2349                          /system/lib64/libcgrouprc.so\n" +
                    "7a3c512000-7a3c513000 r--p 00004000 103:1e 2349                          /system/lib64/libcgrouprc.so\n" +
                    "7a3c513000-7a3c514000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c56e000-7a3c58e000 r--p 00000000 103:1e 2496                          /system/lib64/libpcre2.so\n" +
                    "7a3c58e000-7a3c5b9000 --xp 00020000 103:1e 2496                          /system/lib64/libpcre2.so\n" +
                    "7a3c5b9000-7a3c5ba000 rw-p 0004b000 103:1e 2496                          /system/lib64/libpcre2.so\n" +
                    "7a3c5ba000-7a3c5bb000 r--p 0004c000 103:1e 2496                          /system/lib64/libpcre2.so\n" +
                    "7a3c5c6000-7a3c5cf000 r--p 00000000 103:1e 2318                          /system/lib64/libbase.so\n" +
                    "7a3c5cf000-7a3c5d8000 --xp 00009000 103:1e 2318                          /system/lib64/libbase.so\n" +
                    "7a3c5d8000-7a3c5d9000 rw-p 00012000 103:1e 2318                          /system/lib64/libbase.so\n" +
                    "7a3c5d9000-7a3c5da000 r--p 00013000 103:1e 2318                          /system/lib64/libbase.so\n" +
                    "7a3c5da000-7a3c5db000 rw-p 00000000 00:00 0                              [anon:.bss]\n" +
                    "7a3c627000-7a3c628000 r--p 00000000 103:1e 2494                          /system/lib64/libpackagelistparser.so\n" +
                    "7a3c628000-7a3c629000 --xp 00001000 103:1e 2494                          /system/lib64/libpackagelistparser.so\n" +
                    "7a3c629000-7a3c62a000 rw-p 00002000 103:1e 2494                          /system/lib64/libpackagelistparser.so\n" +
                    "7a3c62a000-7a3c62b000 r--p 00003000 103:1e 2494                          /system/lib64/libpackagelistparser.so\n" +
                    "7a3c65f000-7a3c67f000 r--s 00000000 00:10 10218                          /dev/__properties__/u:object_r:heapprofd_prop:s0\n" +
                    "7a3c67f000-7a3c680000 rw-p 00000000 00:00 0                              [anon:bionic_alloc_small_objects]\n" +
                    "7a3c681000-7a3c6a1000 r--s 00000000 00:10 10190                          /dev/__properties__/u:object_r:exported2_default_prop:s0\n" +
                    "7a3c6a1000-7a3c6c1000 r--s 00000000 00:10 10173                          /dev/__properties__/u:object_r:debug_prop:s0\n" +
                    "7a3c6c1000-7a3c6e1000 r--s 00000000 00:10 19503                          /dev/__properties__/properties_serial\n" +
                    "7a3c6e1000-7a3c6ee000 r--s 00000000 00:10 10138                          /dev/__properties__/property_info\n" +
                    "7a3c752000-7a3c81a000 r--p 00000000 00:00 0                              [anon:linker_alloc]\n" +
                    "7a3c81a000-7a3c81e000 rw-p 00000000 00:00 0                              [anon:bionic_alloc_small_objects]\n" +
                    "7a3c81e000-7a3c81f000 r--p 00000000 00:00 0                              [anon:atexit handlers]\n" +
                    "7a3c81f000-7a3c822000 rw-p 00000000 00:00 0                              [anon:bionic_alloc_small_objects]\n" +
                    "7a3c822000-7a3c823000 ---p 00000000 00:00 0 \n" +
                    "7a3c823000-7a3c826000 rw-p 00000000 00:00 0 \n" +
                    "7a3c826000-7a3c827000 ---p 00000000 00:00 0 \n" +
                    "7a3c827000-7a3c82e000 rw-p 00000000 00:00 0                              [anon:bionic_alloc_small_objects]\n" +
                    "7a3c82e000-7a3c84e000 r--s 00000000 00:10 10201                          /dev/__properties__/u:object_r:exported_default_prop:s0\n" +
                    "7a3c84e000-7a3c850000 rw-p 00000000 00:00 0                              [anon:bionic_alloc_small_objects]\n" +
                    "7a3c850000-7a3c8b4000 r--p 00000000 00:00 0                              [anon:linker_alloc]\n" +
                    "7a3c8b4000-7a3c8d4000 r--s 00000000 00:10 10173                          /dev/__properties__/u:object_r:debug_prop:s0\n" +
                    "7a3c8d4000-7a3c8d5000 ---p 00000000 00:00 0 \n" +
                    "7a3c8d5000-7a3c8d6000 rw-p 00000000 00:00 0 \n" +
                    "7a3c8d6000-7a3c8d7000 ---p 00000000 00:00 0 \n" +
                    "7a3c8d7000-7a3c8f7000 r--s 00000000 00:10 19503                          /dev/__properties__/properties_serial\n" +
                    "7a3c8f7000-7a3c8f9000 rw-p 00000000 00:00 0                              [anon:System property context nodes]\n" +
                    "7a3c8f9000-7a3c906000 r--s 00000000 00:10 10138                          /dev/__properties__/property_info\n" +
                    "7a3c906000-7a3c96a000 r--p 00000000 00:00 0                              [anon:linker_alloc]\n" +
                    "7a3c96a000-7a3c96c000 rw-p 00000000 00:00 0                              [anon:bionic_alloc_small_objects]\n" +
                    "7a3c96c000-7a3c96d000 r--p 00000000 00:00 0                              [anon:atexit handlers]\n" +
                    "7a3c96d000-7a3d272000 ---p 00000000 00:00 0 \n" +
                    "7a3d272000-7a3d274000 rw-p 00000000 00:00 0 \n" +
                    "7a3d274000-7a3d96d000 ---p 00000000 00:00 0 \n" +
                    "7a3d96d000-7a3d96e000 ---p 00000000 00:00 0 \n" +
                    "7a3d96e000-7a3d976000 rw-p 00000000 00:00 0                              [anon:thread signal stack]\n" +
                    "7a3d976000-7a3d977000 rw-p 00000000 00:00 0                              [anon:arc4random data]\n" +
                    "7a3d977000-7a3d979000 rw-p 00000000 00:00 0                              [anon:System property context nodes]\n" +
                    "7a3d979000-7a3d97a000 rw-p 00000000 00:00 0                              [anon:arc4random data]\n" +
                    "7a3d97a000-7a3d97b000 r--p 00000000 00:00 0                              [vvar]\n" +
                    "7a3d97b000-7a3d97d000 r-xp 00000000 00:00 0                              [vdso]\n" +
                    "7a3d97d000-7a3d9b5000 r--p 00000000 07:20 26                             /apex/com.android.runtime/bin/linker64\n" +
                    "7a3d9b5000-7a3da86000 r-xp 00038000 07:20 26                             /apex/com.android.runtime/bin/linker64\n" +
                    "7a3da86000-7a3da87000 rw-p 00109000 07:20 26                             /apex/com.android.runtime/bin/linker64\n" +
                    "7a3da87000-7a3da8e000 r--p 0010a000 07:20 26                             /apex/com.android.runtime/bin/linker64\n" +
                    "7a3da8e000-7a3da95000 rw-p 00000000 00:00 0 \n" +
                    "7a3da95000-7a3da96000 r--p 00000000 00:00 0 \n" +
                    "7a3da96000-7a3da98000 rw-p 00000000 00:00 0 \n" +
                    "7ff960b000-7ff962c000 rw-p 00000000 00:00 0                              [stack]\n").getBytes()));
        }
        if ("/sys/class/android_usb/android0/state".equals(pathname)) { // 判断手机是否插上USB
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, "DISCONNECTED\n".getBytes()));
        }
//        if("/data/data/com.tencent.mm/icon_assets/meta-manifest.dat".equals(pathname)){
//            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags,pathname,"meta-mainfest.dat".getBytes()));
//        }
        if ("/sbin/su".equals(pathname) || "/system/bin/su".equals(pathname) || "/system/xbin/su".equals(pathname)) {
            return null;
        }
        if ("/sys/devices/system/cpu/present".equals(pathname) && device.getCpu_present() != null) {
            return FileResult.<AndroidFileIO>success(new ByteArrayFileIO(oflags, pathname, device.getCpu_present().getBytes()));
        }
        for (InstalledPackage installedPackage : device.getInstalled()) {
            if (("/data/app/" + installedPackage.getPackageName() + "-1/base.apk").equals(pathname)) {
                return FileResult.<AndroidFileIO>fallback(new ByteArrayFileIO(oflags, pathname, new byte[installedPackage.getFileSize()]));
            }
        }
        if (pathname.equals("/dev/__properties__") || pathname.equals("/proc") ||
                pathname.equals("/sys/bus/virtio") || pathname.equals("/system/bin") ||
                pathname.equals("/data/app") || pathname.equals("/data/system") || pathname.equals("/data/data") ||
                pathname.equals("/system/etc")
        ) {
            return FileResult.<AndroidFileIO>success(new DirectoryFileIO(oflags, pathname));
        }


        if ("/dev/socket/qemud".equals(pathname) ||
                "/system/lib/libc_malloc_debug_qemu.so".equals(pathname) ||
                "/dev/qemu_pipe".equals(pathname) ||
                "/sys/qemu_trace".equals(pathname) ||
                "/system/bin/qemu-props".equals(pathname) ||
                "/sys/module/virtio_net".equals(pathname) ||
                "/sys/module/virtio_pci".equals(pathname) ||
                "/sys/class/virtio_pt/virtiopt".equals(pathname) ||
                "/sys/devices/virtual/virtio_pt/virtiopt".equals(pathname) ||
                "/sys/class/virtio_pt".equals(pathname) ||
                "/dev/virtiopt".equals(pathname) ||
                "/sys/bus/pci/drivers/virtio-pci".equals(pathname) ||
                "/proc/sys/fs/binfmt_misc/arm".equals(pathname) ||
                // "/system/framework".equals(pathname) ||
                // "/system/framework/arm".equals(pathname) ||
                // "/system/framework/arm64".equals(pathname) ||
                "/storage/emulated".equals(pathname) ||
                "/system/lib/arm".equals(pathname) ||
                "/proc/net/route".equals(pathname)
        ) {
            return null;
        }

        if ("/system/framework".equals(pathname)
                || "/system/framework/arm".equals(pathname)
                || "/system/bin".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new DirectoryFileIO(oflags, pathname,
                    new DirectoryFileIO.DirectoryEntry(true, "maps"),
                    new DirectoryFileIO.DirectoryEntry(true, "cmdline"),
                    new DirectoryFileIO.DirectoryEntry(false, "task")));
        }

        if ("/system/framework/arm64".equals(pathname)) {
            return FileResult.<AndroidFileIO>success(new DirectoryFileIO(oflags, pathname,
                    new DirectoryFileIO.DirectoryEntry(true, "maps"),
                    new DirectoryFileIO.DirectoryEntry(true, "cmdline"),
                    new DirectoryFileIO.DirectoryEntry(true, "cmdline1"),
                    new DirectoryFileIO.DirectoryEntry(false, "taskk")));
        }


        log.error("error for did not find:" + pathname);
        return null;
    }
}

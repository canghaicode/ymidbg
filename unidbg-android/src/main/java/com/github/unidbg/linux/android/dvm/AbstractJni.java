package com.github.unidbg.linux.android.dvm;

import com.github.unidbg.linux.android.dvm.api.ClassLoader;
import com.github.unidbg.linux.android.dvm.api.*;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.linux.android.dvm.wrapper.DvmBoolean;
import com.github.unidbg.linux.android.dvm.wrapper.DvmInteger;
import com.github.unidbg.linux.android.dvm.wrapper.DvmLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;

public abstract class AbstractJni implements Jni {

    private static final Log log = LogFactory.getLog(AbstractJni.class);

    @Override
    public DvmObject<?> getStaticObjectField(BaseVM vm, DvmClass dvmClass, String signature) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.getStaticObjectField(vm, dvmClass, signature);
        }

        switch (signature) {
            case "android/content/Context->TELEPHONY_SERVICE:Ljava/lang/String;":
                return new StringObject(vm, SystemService.TELEPHONY_SERVICE);
            case "android/content/Context->WIFI_SERVICE:Ljava/lang/String;":
                return new StringObject(vm, SystemService.WIFI_SERVICE);
            case "android/content/Context->CONNECTIVITY_SERVICE:Ljava/lang/String;":
                return new StringObject(vm, SystemService.CONNECTIVITY_SERVICE);
            case "android/content/Context->ACCESSIBILITY_SERVICE:Ljava/lang/String;":
                return new StringObject(vm, SystemService.ACCESSIBILITY_SERVICE);
            case "android/content/Context->KEYGUARD_SERVICE:Ljava/lang/String;":
                return new StringObject(vm, SystemService.KEYGUARD_SERVICE);
            case "android/content/Context->ACTIVITY_SERVICE:Ljava/lang/String;":
                return new StringObject(vm, SystemService.ACTIVITY_SERVICE);
            case "java/lang/Void->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Void");
            case "java/lang/Boolean->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Boolean");
            case "java/lang/Byte->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Byte");
            case "java/lang/Character->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Character");
            case "java/lang/Short->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Short");
            case "java/lang/Integer->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Integer");
            case "java/lang/Long->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Long");
            case "java/lang/Float->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Float");
            case "java/lang/Double->TYPE:Ljava/lang/Class;":
                return vm.resolveClass("java/lang/Double");
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int getStaticIntField(BaseVM vm, DvmClass dvmClass, String signature) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.getStaticIntField(vm, dvmClass, signature);
        }
        switch (signature) {
            case "android/os/Build$VERSION->SDK_INT:I":
                return 19;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> getObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.getObjectField(vm, dvmObject, signature);
        }

        if ("android/content/pm/PackageInfo->signatures:[Landroid/content/pm/Signature;".equals(signature) &&
                dvmObject instanceof PackageInfo) {
            PackageInfo packageInfo = (PackageInfo) dvmObject;
            if (packageInfo.getPackageName().equals(vm.getPackageName())) {
                Signature[] signatures = vm.getSignatures();
                if (signatures != null) {
                    return new ArrayObject(signatures);
                }
            }
        }
        if ("android/content/pm/PackageInfo->versionName:Ljava/lang/String;".equals(signature) &&
                dvmObject instanceof PackageInfo) {
            PackageInfo packageInfo = (PackageInfo) dvmObject;
            if (packageInfo.getPackageName().equals(vm.getPackageName())) {
                String versionName = vm.getVersionName();
                if (versionName != null) {
                    return new StringObject(vm, versionName);
                }
            }
        }
        switch (signature) {
            case "android/content/pm/ApplicationInfo->nativeLibraryDir:Ljava/lang/String;":
                return new StringObject(vm, new File("target").getAbsolutePath());
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public boolean callStaticBooleanMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticBooleanMethod(vm, dvmClass, signature, varArg);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public boolean callStaticBooleanMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticBooleanMethodV(vm, dvmClass, signature, vaList);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int callStaticIntMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticIntMethod(vm, dvmClass, signature, varArg);
        }
        switch (signature) {
            case "com/alibaba/wireless/security/framework/utils/UserTrackMethodJniBridge->utAvaiable()I":
                return 1;
            case "com/taobao/wireless/security/adapter/common/SPUtility2->saveToFileUnifiedForNative(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)I":
                StringObject a1 = varArg.getObject(0);
                StringObject a2 = varArg.getObject(1);
                StringObject a3 = varArg.getObject(2);
                boolean b4 = varArg.getInt(3) != 0;
                System.out.println("saveToFileUnifiedForNative a1=" + a1 + ", a2=" + a2 + ", a3=" + a3 + ", b4=" + b4);
            case "com/uc/crashsdk/JNIBridge->registerInfoCallback(Ljava/lang/String;IJI)I":
                System.out.println("registerInfoCallback a1=" + varArg.getObject(0) + ", a2=" + varArg.getObject(1) + ", a3=" + varArg.getObject(2) + ", b4=" + varArg.getObject(3));
                return 257;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int callStaticIntMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticIntMethodV(vm, dvmClass, signature, vaList);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public long callLongMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callLongMethod(vm, dvmObject, signature, varArg);
        }

        if ("java/lang/Long->longValue()J".equals(signature)) {
            DvmLong val = (DvmLong) dvmObject;
            return val.value;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public long callLongMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callLongMethodV(vm, dvmObject, signature, vaList);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public float callFloatMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callFloatMethodV(vm, dvmObject, signature, vaList);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callObjectMethodV(vm, dvmObject, signature, vaList);
        }

        switch (signature) {
            case "android/app/Application->getAssets()Landroid/content/res/AssetManager;":
                return new AssetManager(vm, signature);
            case "android/app/Application->getClassLoader()Ljava/lang/ClassLoader;":
                return new ClassLoader(vm, signature);
            case "android/app/Application->getContentResolver()Landroid/content/ContentResolver;":
                return vm.resolveClass("android/content/ContentResolver").newObject(signature);
            case "java/util/ArrayList->get(I)Ljava/lang/Object;": {
                int index = vaList.getInt(0);
                ArrayListObject arrayList = (ArrayListObject) dvmObject;
                return arrayList.getValue().get(index);
            }
            case "android/app/Application->getSystemService(Ljava/lang/String;)Ljava/lang/Object;": {
                StringObject serviceName = vaList.getObject(0);
                assert serviceName != null;
                return new SystemService(vm, serviceName.getValue());
            }
            case "java/lang/String->toString()Ljava/lang/String;":
                return dvmObject;
            case "java/lang/Class->getName()Ljava/lang/String;":
                return new StringObject(vm, ((DvmClass) dvmObject).getName());
            case "android/view/accessibility/AccessibilityManager->getEnabledAccessibilityServiceList(I)Ljava/util/List;":
                return new ArrayListObject(vm, Collections.<DvmObject<?>>emptyList());
            case "java/util/Enumeration->nextElement()Ljava/lang/Object;":
                return ((Enumeration) dvmObject).nextElement();
            case "java/util/Locale->getLanguage()Ljava/lang/String;":
                Locale locale = (Locale) dvmObject.getValue();
                return new StringObject(vm, locale.getLanguage());
            case "java/util/Locale->getCountry()Ljava/lang/String;":
                locale = (Locale) dvmObject.getValue();
                return new StringObject(vm, locale.getCountry());
            case "android/os/IServiceManager->getService(Ljava/lang/String;)Landroid/os/IBinder;": {
                ServiceManager serviceManager = (ServiceManager) dvmObject;
                StringObject serviceName = vaList.getObject(0);
                assert serviceName != null;
                return serviceManager.getService(vm, serviceName.getValue());
            }
            case "java/io/File->getAbsolutePath()Ljava/lang/String;":
                File file = (File) dvmObject.getValue();
                return new StringObject(vm, file.getAbsolutePath());
            case "android/app/Application->getPackageManager()Landroid/content/pm/PackageManager;":
            case "android/content/ContextWrapper->getPackageManager()Landroid/content/pm/PackageManager;":
            case "android/content/Context->getPackageManager()Landroid/content/pm/PackageManager;":
                DvmClass clazz = vm.resolveClass("android/content/pm/PackageManager");
                return clazz.newObject(signature);
            case "android/content/pm/PackageManager->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;": {
                StringObject packageName = vaList.getObject(0);
                assert packageName != null;
                int flags = vaList.getInt(4);
                if (log.isDebugEnabled()) {
                    log.debug("getPackageInfo packageName=" + packageName.getValue() + ", flags=0x" + Integer.toHexString(flags));
                }
                return new PackageInfo(vm, packageName.value, flags);
            }
            case "android/app/Application->getPackageName()Ljava/lang/String;":
            case "android/content/ContextWrapper->getPackageName()Ljava/lang/String;":
            case "android/content/Context->getPackageName()Ljava/lang/String;": {
                String packageName = vm.getPackageName();
                if (packageName != null) {
                    return new StringObject(vm, packageName);
                }
                break;
            }
            case "android/content/pm/Signature->toByteArray()[B":
                if (dvmObject instanceof Signature) {
                    Signature sig = (Signature) dvmObject;
                    return new ByteArray(vm, sig.toByteArray());
                }
                break;
            case "android/content/pm/Signature->toCharsString()Ljava/lang/String;":
                if (dvmObject instanceof Signature) {
                    Signature sig = (Signature) dvmObject;
                    return new StringObject(vm, sig.toCharsString());
                }
                break;
            case "java/lang/String->getBytes()[B": {
                String str = (String) dvmObject.getValue();
                return new ByteArray(vm, str.getBytes());
            }
            case "java/lang/String->getBytes(Ljava/lang/String;)[B":
                String str = (String) dvmObject.getValue();
                StringObject charsetName = vaList.getObject(0);
                assert charsetName != null;
                try {
                    return new ByteArray(vm, str.getBytes(charsetName.value));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            case "java/security/cert/CertificateFactory->generateCertificate(Ljava/io/InputStream;)Ljava/security/cert/Certificate;":
                CertificateFactory factory = (CertificateFactory) dvmObject.value;
                DvmObject<?> stream = vaList.getObject(0);
                assert stream != null;
                InputStream inputStream = (InputStream) stream.value;
                try {
                    return vm.resolveClass("java/security/cert/Certificate").newObject(factory.generateCertificate(inputStream));
                } catch (CertificateException e) {
                    throw new IllegalStateException(e);
                }
            case "java/security/cert/Certificate->getEncoded()[B": {
                Certificate certificate = (Certificate) dvmObject.value;
                try {
                    return new ByteArray(vm, certificate.getEncoded());
                } catch (CertificateEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }
            case "java/security/MessageDigest->digest([B)[B": {
                MessageDigest messageDigest = (MessageDigest) dvmObject.value;
                ByteArray array = vaList.getObject(0);
                assert array != null;
                return new ByteArray(vm, messageDigest.digest(array.value));
            }
            case "java/util/ArrayList->remove(I)Ljava/lang/Object;": {
                int index = vaList.getInt(0);
                ArrayListObject list = (ArrayListObject) dvmObject;
                return list.value.remove(index);
            }
            case "java/util/List->get(I)Ljava/lang/Object;":
                List<?> list = (List<?>) dvmObject.getValue();
                return (DvmObject<?>) list.get(vaList.getInt(0));
            case "java/util/Map->entrySet()Ljava/util/Set;":
                Map<?, ?> map = (Map<?, ?>) dvmObject.getValue();
                return vm.resolveClass("java/util/Set").newObject(map.entrySet());
            case "java/util/Set->iterator()Ljava/util/Iterator;":
                Set<?> set = (Set<?>) dvmObject.getValue();
                return vm.resolveClass("java/util/Iterator").newObject(set.iterator());
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> callStaticObjectMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticObjectMethod(vm, dvmClass, signature, varArg);
        }
        switch (signature) {
            case "com/alibaba/wireless/security/securitybody/SecurityGuardSecurityBodyPlugin->getPluginClassLoader()Ljava/lang/ClassLoader;":
                return vm.resolveClass("java/lang/ClassLoader").newObject(null);
            case "com/taobao/dp/util/CallbackHelper->getInstance()Lcom/taobao/dp/util/CallbackHelper;":
                return vm.resolveClass("com/taobao/dp/util/CallbackHelper").newObject(null);
            case "com/taobao/wireless/security/adapter/common/SPUtility2->readFromSPUnified(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;":
                StringObject a1 = varArg.getObject(0);
                StringObject a2 = varArg.getObject(1);
                StringObject a3 = varArg.getObject(2);
                System.out.println("readFromSPUnified a1=" + a1 + ", a2=" + a2 + ", a3=" + a3);
                return null;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
        }

        switch (signature) {
            case "com/android/internal/os/BinderInternal->getContextObject()Landroid/os/IBinder;":
                return new Binder(vm, signature);
            case "android/app/ActivityThread->currentActivityThread()Landroid/app/ActivityThread;":
                return dvmClass.newObject(null);
            case "android/app/ActivityThread->currentApplication()Landroid/app/Application;":
                return vm.resolveClass("android/app/Application", vm.resolveClass("android/content/ContextWrapper", vm.resolveClass("android/content/Context"))).newObject(signature);
            case "java/util/Locale->getDefault()Ljava/util/Locale;":
                return dvmClass.newObject(Locale.getDefault());
            case "android/os/ServiceManagerNative->asInterface(Landroid/os/IBinder;)Landroid/os/IServiceManager;":
                return new ServiceManager(vm, signature);
            case "com/android/internal/telephony/ITelephony$Stub->asInterface(Landroid/os/IBinder;)Lcom/android/internal/telephony/ITelephony;":
                return vaList.getObject(0);
            case "java/security/cert/CertificateFactory->getInstance(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;": {
                StringObject type = vaList.getObject(0);
                assert type != null;
                try {
                    return vm.resolveClass("java/security/cert/CertificateFactory").newObject(CertificateFactory.getInstance(type.value));
                } catch (CertificateException e) {
                    throw new IllegalStateException(e);
                }
            }
            case "java/security/MessageDigest->getInstance(Ljava/lang/String;)Ljava/security/MessageDigest;": {
                StringObject type = vaList.getObject(0);
                assert type != null;
                try {
                    return vm.resolveClass("java/security/MessageDigest").newObject(MessageDigest.getInstance(type.value));
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int callIntMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callIntMethodV(vm, dvmObject, signature, vaList);
        }

        switch (signature) {
            case "android/os/Bundle->getInt(Ljava/lang/String;)I":
                Bundle bundle = (Bundle) dvmObject;
                StringObject key = vaList.getObject(0);
                assert key != null;
                return bundle.getInt(key.getValue());
            case "java/util/ArrayList->size()I": {
                ArrayListObject list = (ArrayListObject) dvmObject;
                return list.size();
            }
            case "android/content/pm/Signature->hashCode()I": {
                if (dvmObject instanceof Signature) {
                    Signature sig = (Signature) dvmObject;
                    return sig.getHashCode();
                }
                break;
            }
            case "java/lang/Integer->intValue()I": {
                DvmInteger integer = (DvmInteger) dvmObject;
                return integer.value;
            }
            case "java/util/List->size()I":
                List<?> list = (List<?>) dvmObject.getValue();
                return list.size();
            case "java/util/Map->size()I":
                Map<?, ?> map = (Map<?, ?>) dvmObject.getValue();
                return map.size();
        }


        throw new UnsupportedOperationException(signature);
    }

    @Override
    public long callStaticLongMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticLongMethod(vm, dvmClass, signature, varArg);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public long callStaticLongMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.callStaticLongMethodV(vm, dvmClass, signature, vaList);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public boolean callBooleanMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callBooleanMethod(vm, dvmObject, signature, varArg);
        }

        if ("java/lang/Boolean->booleanValue()Z".equals(signature)) {
            DvmBoolean dvmBoolean = (DvmBoolean) dvmObject;
            return dvmBoolean.value;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public boolean callBooleanMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callBooleanMethodV(vm, dvmObject, signature, vaList);
        }

        switch (signature) {
            case "java/util/Enumeration->hasMoreElements()Z":
                return ((Enumeration) dvmObject).hasMoreElements();
            case "java/util/ArrayList->isEmpty()Z":
                return ((ArrayListObject) dvmObject).isEmpty();
            case "java/util/Iterator->hasNext()Z":
                Iterator<?> iterator = (Iterator<?>) dvmObject.getValue();
                return iterator.hasNext();
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int getIntField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.getIntField(vm, dvmObject, signature);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public long getLongField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.getLongField(vm, dvmObject, signature);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void callStaticVoidMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            jni.callStaticVoidMethod(vm, dvmClass, signature, varArg);
            return;
        }
        switch (signature) {
            case "com/taobao/dp/util/CallbackHelper->onCallBack(ILjava/lang/String;I)V":
                int i1 = varArg.getInt(0);
                StringObject str = varArg.getObject(1);
                int i2 = varArg.getInt(2);
                System.out.println("com/taobao/dp/util/CallbackHelper->onCallBack i1=" + i1 + ", str=" + str + ", i2=" + i2);
                return;
            case "com/alibaba/wireless/security/open/edgecomputing/ECMiscInfo->registerAppLifeCyCleCallBack()V":
                System.out.println("registerAppLifeCyCleCallBack");
                return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void callStaticVoidMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            jni.callStaticVoidMethodV(vm, dvmClass, signature, vaList);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void setObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature, DvmObject<?> value) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.setObjectField(vm, dvmObject, signature, value);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public boolean getBooleanField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.getBooleanField(vm, dvmObject, signature);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> newObject(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.newObject(vm, dvmClass, signature, varArg);
        }

        switch (signature) {
            case "java/lang/String-><init>([B)V":
                ByteArray array = varArg.getObject(0);
                return new StringObject(vm, new String(array.getValue()));
            case "java/lang/String-><init>([BLjava/lang/String;)V":
                array = varArg.getObject(0);
                StringObject string = varArg.getObject(1);
                try {
                    return new StringObject(vm, new String(array.getValue(), string.getValue()));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            case "com/alibaba/wireless/security/open/SecException-><init>(Ljava/lang/String;I)V": {
                StringObject msg = varArg.getObject(0);
                int value = varArg.getInt(1);
                return dvmClass.newObject(msg.getValue() + "[" + value + "]");
            }
            case "java/lang/Integer-><init>(I)V":
                int value = varArg.getInt(0);
                return DvmInteger.valueOf(vm, value);
            case "java/lang/Long-><init>(J)V": {
                StringObject msg = varArg.getObject(0);
                int val = varArg.getInt(1);
                return dvmClass.newObject(val);
            }
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> newObjectV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.newObjectV(vm, dvmClass, signature, vaList);
        }

        switch (signature) {
            case "java/io/ByteArrayInputStream-><init>([B)V": {
                ByteArray array = vaList.getObject(0);
                assert array != null;
                return vm.resolveClass("java/io/ByteArrayInputStream").newObject(new ByteArrayInputStream(array.value));
            }
            case "java/lang/String-><init>([B)V": {
                ByteArray array = vaList.getObject(0);
                assert array != null;
                return new StringObject(vm, new String(array.value));
            }
            case "java/lang/String-><init>([BLjava/lang/String;)V": {
                ByteArray array = vaList.getObject(0);
                assert array != null;
                StringObject charsetName = vaList.getObject(4);
                assert charsetName != null;
                try {
                    return new StringObject(vm, new String(array.value, charsetName.value));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> allocObject(BaseVM vm, DvmClass dvmClass, String signature) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.allocObject(vm, dvmClass, signature);
        }

        if ("java/util/HashMap->allocObject".equals(signature)) {
            return dvmClass.newObject(new HashMap<>());
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void setIntField(BaseVM vm, DvmObject<?> dvmObject, String signature, int value) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.setIntField(vm, dvmObject, signature, value);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void setLongField(BaseVM vm, DvmObject<?> dvmObject, String signature, long value) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.setLongField(vm, dvmObject, signature, value);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void setBooleanField(BaseVM vm, DvmObject<?> dvmObject, String signature, boolean value) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.setBooleanField(vm, dvmObject, signature, value);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void setDoubleField(BaseVM vm, DvmObject<?> dvmObject, String signature, double value) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.setDoubleField(vm, dvmObject, signature, value);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> callObjectMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callObjectMethod(vm, dvmObject, signature, varArg);
        }

        switch (signature) {
            case "java/lang/String->getBytes(Ljava/lang/String;)[B": {
                StringObject string = (StringObject) dvmObject;
                StringObject encoding = varArg.getObject(0);
                System.err.println("string=" + string.getValue() + ", encoding=" + encoding.getValue());
                try {
                    return new ByteArray(vm, string.getValue().getBytes(encoding.value));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }
            case "android/content/Context->getPackageManager()Landroid/content/pm/PackageManager;":
                return vm.resolveClass("android/content/pm/PackageManager").newObject(null);
            case "android/content/Context->getPackageName()Ljava/lang/String;": {
                String packageName = vm.getPackageName();
                if (packageName != null) {
                    return new StringObject(vm, packageName);
                }
                break;
            }
            case "android/content/pm/PackageManager->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;": {
                StringObject packageName = varArg.getObject(0);
                int flags = varArg.getInt(1);
                if (log.isDebugEnabled()) {
                    log.debug("getPackageInfo packageName=" + packageName.getValue() + ", flags=0x" + Integer.toHexString(flags));
                }
                return new PackageInfo(vm, packageName.value, flags);
            }
            case "android/content/pm/Signature->toByteArray()[B": {
                if (dvmObject instanceof Signature) {
                    Signature sig = (Signature) dvmObject;
                    return new ByteArray(vm, sig.toByteArray());
                }
                break;
            }
            case "android/content/pm/Signature->toCharsString()Ljava/lang/String;": {
                if (dvmObject instanceof Signature) {
                    Signature sig = (Signature) dvmObject;
                    return new StringObject(vm, sig.toCharsString());
                }
                break;
            }
            case "java/lang/Class->getName()Ljava/lang/String;": {
                DvmClass clazz = (DvmClass) dvmObject;
                return new StringObject(vm, clazz.getName());
            }
            case "java/lang/String->getClass()Ljava/lang/Class;":
            case "java/lang/Integer->getClass()Ljava/lang/Class;": {
                return dvmObject.getObjectType();
            }
            case "java/lang/Class->getClassLoader()Ljava/lang/ClassLoader;":
                return new ClassLoader(vm, signature);
            case "java/util/HashMap->keySet()Ljava/util/Set;": {
                HashMap map = (HashMap) dvmObject.getValue();
                return vm.resolveClass("java/util/Set").newObject(map.keySet());
            }
            case "java/util/Set->toArray()[Ljava/lang/Object;":
                Set set = (Set) dvmObject.getValue();
                Object[] array = set.toArray();
                DvmObject[] objects = new DvmObject[array.length];
                for (int i = 0; i < array.length; i++) {
                    if (array[i] instanceof String) {
                        objects[i] = new StringObject(vm, (String) array[i]);
                    } else {
                        throw new IllegalStateException("array=" + array[i]);
                    }
                }
                return new ArrayObject(objects);
            case "java/util/HashMap->get(Ljava/lang/Object;)Ljava/lang/Object;": {
                HashMap map = (HashMap) dvmObject.getValue();
                Object key = varArg.getObject(0).getValue();
                Object obj = map.get(key);
                if (obj instanceof String) {
                    return new StringObject(vm, (String) obj);
                } else {
                    throw new IllegalStateException("array=" + obj);
                }
            }
            case "com/taobao/sns/ISApplication->getPackageCodePath()Ljava/lang/String;":
                return new StringObject(vm, "/data/app/com.taobao.etao-9d70gDD153ymsdrZ8eSAnA==/base.apk");
            case "com/uc/crashsdk/JNIBridge->registerInfoCallback(Ljava/lang/String;IJI)I":
                return DvmInteger.valueOf(vm, 257);
            case "com/taobao/sns/ISApplication->getFilesDir()Ljava/io/File;":
                return vm.resolveClass("java/io/File").newObject(new File("target"));
            case "com/taobao/sns/ISApplication->getApplicationInfo()Landroid/content/pm/ApplicationInfo;":

                return vm.resolveClass("android/content/pm/ApplicationInfo").newObject(new File("target"));
            case "java/io/File->getAbsolutePath()Ljava/lang/String;":
                File file = (File) dvmObject.getValue();
                return new StringObject(vm, file.getAbsolutePath());
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int callIntMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            return jni.callIntMethod(vm, dvmObject, signature, varArg);
        }

        switch (signature) {
            case "java/lang/Integer->intValue()I":
                DvmInteger integer = (DvmInteger) dvmObject;
                return integer.value;
            case "java/io/InputStream->read([B)I": {
                try {
                    java.io.InputStream inputStream = (java.io.InputStream) dvmObject.getValue();
                    ByteArray array = varArg.getObject(0);
                    return inputStream.read(array.getValue());
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void callVoidMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.callVoidMethod(vm, dvmObject, signature, varArg);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public void callVoidMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        DvmClass dvmClass = dvmObject.getObjectType();
        Jni jni = dvmClass == null ? null : dvmClass.getJni();
        if (jni != null) {
            jni.callVoidMethodV(vm, dvmObject, signature, vaList);
            return;
        }

        throw new UnsupportedOperationException(signature);
    }

    private long count;

    @Override
    public void setStaticLongField(BaseVM vm, DvmClass dvmClass, String signature, long value) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            jni.setStaticLongField(vm, dvmClass, signature, value);
            return;
        }
        switch (signature) {
            case "com/alibaba/wireless/security/framework/SGPluginExtras->slot:J":
                System.out.println("set -> slot:" + value);
                count = value;
                return;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public long getStaticLongField(BaseVM vm, DvmClass dvmClass, String signature) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.getStaticLongField(vm, dvmClass, signature);
        }
        switch (signature) {
            case "com/alibaba/wireless/security/framework/SGPluginExtras->slot:J":
                System.out.println("get -> slot:" + count);
                return count;
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public DvmObject<?> toReflectedMethod(BaseVM vm, DvmClass dvmClass, String signature) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.toReflectedMethod(vm, dvmClass, signature);
        }

        throw new UnsupportedOperationException(signature);
    }

    @Override
    public boolean acceptMethod(DvmClass dvmClass, String signature, boolean isStatic) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.acceptMethod(dvmClass, signature, isStatic);
        }

        return true;
    }

    @Override
    public boolean acceptField(DvmClass dvmClass, String signature, boolean isStatic) {
        Jni jni = dvmClass.getJni();
        if (jni != null) {
            return jni.acceptField(dvmClass, signature, isStatic);
        }

        return true;
    }
}

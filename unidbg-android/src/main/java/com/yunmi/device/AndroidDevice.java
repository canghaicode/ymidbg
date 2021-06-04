package com.yunmi.device;


import com.yunmi.device.android.Audio;
import com.yunmi.device.android.CameraInfo;
import com.yunmi.device.android.CpuCore;
import com.yunmi.device.android.Device;
import com.yunmi.device.android.Gsm;
import com.yunmi.device.android.InputMethodInfo;
import com.yunmi.device.android.NetworkInfo;
import com.yunmi.device.android.NetworkInterface;
import com.yunmi.device.android.Sensor;
import com.yunmi.device.android.StatFs;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidDevice implements Device, Serializable, Cloneable {
    private static Pattern PROPERTY_PATTERN = null;
    private String activeNetInfo;
    private NetworkInfo activeNetworkInfo;
    private String androidId;
    private Audio audio;
    private String binMd5;
    private String bluetoothAddress;
    private String bluetoothName;
    private String board;
    private String bootId;
    private String bootloader;
    private String brand;
    private String bssid;
    private String buildProp;
    private CameraInfo[] cameraInfos;
    private String cid;
    private String codename;
    private CpuCore[] cpu;
    private String cpuAbi;
    private String cpuAbi2;
    private String cpu_possible;
    private String cpu_present;
    private String cpuInfo;
    private String cpuInfoMaxFreq;
    private String cpuInfoMinFreq;
    private StatFs dataStatFs;
    private String defaultInputMethod;
    private String defaultProp;
    private int density;
    private String device;
    private transient String deviceJson;
    private String deviceSoftwareVersion;
    private byte[] deviceUniqueId;
    private String display;
    private StatFs externalStatFs;
    private String externalStorageState;
    private String fingerprint;
    private String frameworkArm64Md5;
    private String frameworkArmMd5;
    private String frameworkMd5;
    private String fsid;
    private String glExtensions;
    private String glRenderer;
    private String glVendor;
    private String glVersion;
    private Gsm gsm;
    private String hardware;
    private int height;
    private String host;
    private String iSerial;
    private String id;
    private String idProduct;
    private String idVendor;
    private String imei;
    private String imsi;
    private String incremental;
    private InputMethodInfo[] inputMethods;
    private InstalledPackage[] installed;
    private String ipAddress;
    private String kernel;
    private double latitude;
    private String line1Number;
    private double longitude;
    private String macAddress;
    private String manufacturer;
    private String memInfo;
    private NetworkInfo mobileNetworkInfo;
    private String model;
    private String networkCountryIso;
    private List<NetworkInterface> networkInterfaces;
    private String networkOperator;
    private String networkOperatorName;
    private transient MobileOperator operator;
    private String product;
    private String properties;
    private byte[] provisioningUniqueId;
    private String radio;
    private String release;
    private StatFs rootStatFs;
    private int sdk;
    private Sensor[] sensors;
    private String serial;
    private static final long serialVersionUID = 0x6F5A0A870E769E03L;
    private String simCountryIso;
    private String simOperator;
    private String simOperatorName;
    private String simSerialNumber;
    private String ssid;
    private String[] systemAvailableFeatures;
    private final transient Properties systemProperties;
    private String[] systemSharedLibraryNames;
    private String tags;
    private long time;
    private String type;
    private String unknown;
    private String usbState;
    private String user;
    private String userAgent;
    private int width;

    static {
        AndroidDevice.PROPERTY_PATTERN = Pattern.compile("\\[(.+)]:\\s\\[(.*)]");
    }

    public AndroidDevice() {
        this.systemProperties = new Properties();
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getActiveNetInfo() {
        return this.activeNetInfo;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public NetworkInfo getActiveNetworkInfo() {
        return this.activeNetworkInfo;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getAndroidId() {
        return this.androidId;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public Audio getAudio() {
        return this.audio;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getBinMd5() {
        return this.binMd5;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getBluetoothAddress() {
        return this.bluetoothAddress;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getBluetoothName() {
        return this.bluetoothName;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getBoard() {
        return this.board;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getBootId() {
        return this.bootId;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getBootloader() {
        return this.bootloader;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getBrand() {
        return this.brand;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getBssid() {
        return this.bssid;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getBuildProp() {
        return this.buildProp;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public CameraInfo[] getCameraInfos() {
        return this.cameraInfos;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getCid() {
        return this.cid;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getCodename() {
        return this.codename;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public CpuCore[] getCpu() {
        return this.cpu;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getCpuAbi() {
        return this.cpuAbi;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getCpuAbi2() {
        return this.cpuAbi2;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getCpu_possible() {
        return this.cpu_possible;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getCpu_present() {
        return this.cpu_present;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getCpuInfo() {
        return this.cpuInfo;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getCpuInfoMaxFreq() {
        return this.cpuInfoMaxFreq;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getCpuInfoMinFreq() {
        return this.cpuInfoMinFreq;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public StatFs getDataStatFs() {
        return this.dataStatFs;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getDefaultInputMethod() {
        return this.defaultInputMethod;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getDefaultProp() {
        return this.defaultProp;
    }

    private String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(0x40);
        result.append("Dalvik/");
        result.append("1.6");
        result.append(" (Linux; U; Android ");
        String version = this.release;
        if(version == null || version.length() <= 0) {
            version = "1.0";
        }

        result.append(version);
        String model = this.model;
        if(model != null && model.length() > 0 && (this.isValidModel(model))) {
            result.append("; ");
            result.append(model);
        }

        String id = this.id;
        if(id != null && id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }

        result.append(")");
        return result.toString();
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public int getDensity() {
        return this.density;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getDescription() {
        StringBuilder buffer = new StringBuilder();
        if(this.product != null) {
            buffer.append(this.product);
        }

        if(this.type != null) {
            buffer.append('-').append(this.type);
        }

        if(this.release != null) {
            buffer.append(' ').append(this.release);
        }

        if(this.display != null) {
            buffer.append(' ').append(this.display);
        }

        if(this.incremental != null) {
            buffer.append(' ').append(this.incremental);
        }

        if(this.tags != null) {
            buffer.append(' ').append(this.tags);
        }

        return buffer.toString();
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getDevice() {
        return this.device;
    }

    @Override  // com.fuzhu8.device.android.Device
    public String getDeviceJson() {
        return this.deviceJson;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getDeviceSoftwareVersion() {
        return this.deviceSoftwareVersion;
    }

    @Override  // com.fuzhu8.device.android.OS
    public byte[] getDeviceUniqueId() {
        return this.deviceUniqueId;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getDisplay() {
        return this.display;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public StatFs getExternalStatFs() {
        return this.externalStatFs;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getExternalStorageState() {
        return this.externalStorageState;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getFingerprint() {
        return this.fingerprint;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getFrameworkArm64Md5() {
        return this.frameworkArm64Md5;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getFrameworkArmMd5() {
        return this.frameworkArmMd5;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getFrameworkMd5() {
        return this.frameworkMd5;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getFsid() {
        return this.fsid;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getGlExtensions() {
        return this.glExtensions;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getGlRenderer() {
        return this.glRenderer;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getGlVendor() {
        return this.glVendor;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getGlVersion() {
        return this.glVersion;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public Gsm getGsm() {
        return this.gsm;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getHardware() {
        return this.hardware;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public int getHeight() {
        return this.height;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getHost() {
        return this.host;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getId() {
        return this.id;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getIdProduct() {
        return this.idProduct;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getIdVendor() {
        return this.idVendor;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getImei() {
        return this.imei;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getImsi() {
        return this.imsi;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getIncremental() {
        return this.incremental;
    }

    @Override  // com.fuzhu8.device.android.OS
    public InputMethodInfo[] getInputMethods() {
        return this.inputMethods;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public InstalledPackage[] getInstalled() {
        return this.installed;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getKernel() {
        return this.kernel;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public double getLatitude() {
        return this.latitude;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getLine1Number() {
        return this.line1Number;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public double getLongitude() {
        return this.longitude;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getMacAddress() {
        return this.macAddress;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getManufacturer() {
        return this.manufacturer;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getMemInfo() {
        return this.memInfo;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public NetworkInfo getMobileNetworkInfo() {
        return this.mobileNetworkInfo;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getModel() {
        return this.model;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getNetworkCountryIso() {
        return this.networkCountryIso;
    }

    @Override  // com.fuzhu8.device.android.Device
    public List<NetworkInterface> getNetworkInterfaces() {
        return this.networkInterfaces;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getNetworkOperator() {
        return this.networkOperator;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getNetworkOperatorName() {
        return this.networkOperatorName;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public MobileOperator getOperator() {
        try {
            if(this.operator == null) {
                this.operator = MobileOperator.parseOperator(Long.parseLong(this.imsi));
                return this.operator;
            }
        }
        catch(Throwable ignored) {
            this.operator = MobileOperator.UNKNOWN;
            return this.operator;
        }

        return this.operator;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getProduct() {
        return this.product;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getProperties() {
        return this.properties;
    }

    @Override  // com.fuzhu8.device.android.OS
    public byte[] getProvisioningUniqueId() {
        return this.provisioningUniqueId;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getRadio() {
        return this.radio;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getRelease() {
        return this.release;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public StatFs getRootStatFs() {
        return this.rootStatFs;
    }

    @Override  // com.fuzhu8.device.android.OS
    public int getSdk() {
        return this.sdk;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public Sensor[] getSensors() {
        return this.sensors;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getSerial() {
        return this.serial;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getSimCountryIso() {
        return this.simCountryIso;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getSimOperator() {
        return this.simOperator;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getSimOperatorName() {
        return this.simOperatorName;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getSimSerialNumber() {
        return this.simSerialNumber;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getSsid() {
        return this.ssid;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String[] getSystemAvailableFeatures() {
        return this.systemAvailableFeatures;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getSystemProperty(String name) {
        return this.systemProperties.getProperty(name);
    }

    @Override  // com.fuzhu8.device.android.OS
    public String[] getSystemSharedLibraryNames() {
        return this.systemSharedLibraryNames;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getTags() {
        return this.tags;
    }

    @Override  // com.fuzhu8.device.android.OS
    public long getTime() {
        return this.time;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getType() {
        return this.type;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getUnknown() {
        return this.unknown;
    }

    @Override  // com.fuzhu8.device.android.Enviorment
    public String getUsbState() {
        return this.usbState;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getUser() {
        return this.user;
    }

    @Override  // com.fuzhu8.device.android.OS
    public String getUserAgent() {
        if(this.userAgent == null) {
            this.userAgent = this.getDefaultUserAgent();
        }

        return this.userAgent;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public int getWidth() {
        return this.width;
    }

    @Override  // com.fuzhu8.device.android.Hardware
    public String getISerial() {
        return this.iSerial;
    }

    private boolean isValidModel(String model) {
        char[] v3 = model.toCharArray();
        int v2;
        for(v2 = 0; v2 < v3.length; ++v2) {
            if(v3[v2] >= 0x7F || v3[v2] <= 0) {
                return false;
            }
        }

        return true;
    }

    public void setActiveNetInfo(String activeNetInfo) {
        this.activeNetInfo = activeNetInfo;
    }

    public void setActiveNetworkInfo(NetworkInfo activeNetworkInfo) {
        this.activeNetworkInfo = activeNetworkInfo;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public void setBinMd5(String binMd5) {
        this.binMd5 = binMd5;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public void setBootId(String bootId) {
        this.bootId = bootId;
    }

    public void setBootloader(String bootloader) {
        this.bootloader = bootloader;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public void setBuildProp(String buildProp) {
        this.buildProp = buildProp;
    }

    public void setCameraInfos(CameraInfo[] cameraInfos) {
        this.cameraInfos = cameraInfos;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public void setCpu(CpuCore[] cpu) {
        this.cpu = cpu;
    }

    public void setCpuAbi(String cpuAbi) {
        this.cpuAbi = cpuAbi;
    }

    public void setCpuAbi2(String cpuAbi2) {
        this.cpuAbi2 = cpuAbi2;
    }

    public void setCpu_possible(String cpu_possible) {
        this.cpu_possible = cpu_possible;
    }

    public void setCpu_present(String cpu_present) {
        this.cpu_present = cpu_present;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public void setCpuInfoMaxFreq(String cpuInfoMaxFreq) {
        this.cpuInfoMaxFreq = cpuInfoMaxFreq;
    }

    public void setCpuInfoMinFreq(String cpuInfoMinFreq) {
        this.cpuInfoMinFreq = cpuInfoMinFreq;
    }

    public void setDataStatFs(StatFs dataStatFs) {
        this.dataStatFs = dataStatFs;
    }

    public void setDefaultInputMethod(String defaultInputMethod) {
        this.defaultInputMethod = defaultInputMethod;
    }

    public void setDefaultProp(String defaultProp) {
        this.defaultProp = defaultProp;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setDeviceJson(String deviceJson) {
        this.deviceJson = deviceJson;
    }

    public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
        this.deviceSoftwareVersion = deviceSoftwareVersion;
    }

    public void setDeviceUniqueId(byte[] deviceUniqueId) {
        this.deviceUniqueId = deviceUniqueId;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setExternalStatFs(StatFs externalStatFs) {
        this.externalStatFs = externalStatFs;
    }

    public void setExternalStorageState(String externalStorageState) {
        this.externalStorageState = externalStorageState;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public void setFrameworkArm64Md5(String frameworkArm64Md5) {
        this.frameworkArm64Md5 = frameworkArm64Md5;
    }

    public void setFrameworkArmMd5(String frameworkArmMd5) {
        this.frameworkArmMd5 = frameworkArmMd5;
    }

    public void setFrameworkMd5(String frameworkMd5) {
        this.frameworkMd5 = frameworkMd5;
    }

    public void setFsid(String fsid) {
        this.fsid = fsid;
    }

    public void setGlExtensions(String glExtensions) {
        this.glExtensions = glExtensions;
    }

    public void setGlRenderer(String glRenderer) {
        this.glRenderer = glRenderer;
    }

    public void setGlVendor(String glVendor) {
        this.glVendor = glVendor;
    }

    public void setGlVersion(String glVersion) {
        this.glVersion = glVersion;
    }

    public void setGsm(Gsm gsm) {
        this.gsm = gsm;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public void setIdVendor(String idVendor) {
        this.idVendor = idVendor;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public void setIncremental(String incremental) {
        this.incremental = incremental;
    }

    public void setInputMethods(InputMethodInfo[] inputMethods) {
        this.inputMethods = inputMethods;
    }

    public void setInstalled(InstalledPackage[] installed) {
        this.installed = installed;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setMemInfo(String memInfo) {
        this.memInfo = memInfo;
    }

    public void setMobileNetworkInfo(NetworkInfo mobileNetworkInfo) {
        this.mobileNetworkInfo = mobileNetworkInfo;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNetworkCountryIso(String networkCountryIso) {
        this.networkCountryIso = networkCountryIso;
    }

    public void setNetworkInterfaces(List<NetworkInterface> arg1) {
        this.networkInterfaces = arg1;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setProperties(String properties) {
        this.properties = properties;
        this.systemProperties.clear();
        if(properties != null) {
            Matcher matcher = AndroidDevice.PROPERTY_PATTERN.matcher(properties);
            while(matcher.find()) {
                this.systemProperties.setProperty(matcher.group(1), matcher.group(2));
            }
        }
    }

    public void setProvisioningUniqueId(byte[] provisioningUniqueId) {
        this.provisioningUniqueId = provisioningUniqueId;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setRootStatFs(StatFs rootStatFs) {
        this.rootStatFs = rootStatFs;
    }

    public void setSdk(int sdk) {
        this.sdk = sdk;
    }

    public void setSensors(Sensor[] sensors) {
        this.sensors = sensors;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setSimCountryIso(String simCountryIso) {
        this.simCountryIso = simCountryIso;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setSystemAvailableFeatures(String[] systemAvailableFeatures) {
        this.systemAvailableFeatures = systemAvailableFeatures;
    }

    public void setSystemSharedLibraryNames(String[] systemSharedLibraryNames) {
        this.systemSharedLibraryNames = systemSharedLibraryNames;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public void setUsbState(String usbState) {
        this.usbState = usbState;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setISerial(String iSerial) {
        this.iSerial = iSerial;
    }

    public InstalledPackage getPackageInfo(String packageName) {
        for (InstalledPackage installedPackage : installed) {
            if(Objects.equals(installedPackage.getPackageName(), packageName)){
                return installedPackage;
            }
        }
        return null;
    }
}


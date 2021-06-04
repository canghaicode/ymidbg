package com.yunmi.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunmi.device.AndroidDevice;
import com.yunmi.device.InstalledPackage;
import com.yunmi.device.android.Audio;
import com.yunmi.device.android.CameraInfo;
import com.yunmi.device.android.CpuCore;
import com.yunmi.device.android.Device;
import com.yunmi.device.android.InputMethodInfo;
import com.yunmi.device.android.NetworkInfo;
import com.yunmi.device.android.NetworkInterface;
import com.yunmi.device.android.Sensor;
import com.yunmi.device.android.StatFs;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class JsonFileToSerializable {
    private final File file;
    private final JSONObject data;

    public JsonFileToSerializable(String filePath) throws Exception {
        if (filePath == null || filePath.isEmpty()) {
            throw new Exception("file path is null");
        }
        this.file = new File(filePath);
        this.data = getJsonObj();
    }

    private JSONObject getJsonObj() {
        try {
            Long length = file.length();
            byte[] fileData = new byte[length.intValue()];
            FileInputStream in = new FileInputStream(file);
            int read = in.read(fileData);
            if (read == length) {
                return JSON.parseObject(new String(fileData));
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void addMobileNetworkInfo(AndroidDevice androidDevice){
        NetworkInfo mobileNetworkInfo = new NetworkInfo();
        JSONObject dataInfo = data.getJSONObject("mobileNetworkInfo");
        mobileNetworkInfo.setType(dataInfo.getInteger("type"));
        mobileNetworkInfo.setTypeName(dataInfo.getString("typeName"));
        mobileNetworkInfo.setState(dataInfo.getString("state"));
        mobileNetworkInfo.setRoaming(dataInfo.getBooleanValue("roaming"));
        mobileNetworkInfo.setFailover(dataInfo.getBooleanValue("failover"));
        mobileNetworkInfo.setAvailable(dataInfo.getBooleanValue("available"));
        mobileNetworkInfo.setConnected(dataInfo.getBooleanValue("connected"));
        mobileNetworkInfo.setSubtype(dataInfo.getInteger("subtype"));
        mobileNetworkInfo.setSubtypeName(dataInfo.getString("subtypeName"));
        androidDevice.setMobileNetworkInfo(mobileNetworkInfo);
    }

    private void addActiveNetWorkInfo(AndroidDevice androidDevice) {
        androidDevice.setActiveNetInfo(getString("activeNetInfo"));
        NetworkInfo activeNetWork = new NetworkInfo();
        JSONObject dataInfo = data.getJSONObject("activeNetworkInfo");
        activeNetWork.setType(dataInfo.getInteger("type"));
        activeNetWork.setTypeName(dataInfo.getString("typeName"));
        activeNetWork.setState(dataInfo.getString("state"));
        activeNetWork.setExtraInfo(dataInfo.getString("extraInfo"));
        activeNetWork.setRoaming(dataInfo.getBooleanValue("roaming"));
        activeNetWork.setFailover(dataInfo.getBooleanValue("failover"));
        activeNetWork.setAvailable(dataInfo.getBooleanValue("available"));
        activeNetWork.setConnected(dataInfo.getBooleanValue("connected"));
        activeNetWork.setSubtype(dataInfo.getInteger("subtype"));
        activeNetWork.setSubtypeName(dataInfo.getString("subtypeName"));
        androidDevice.setActiveNetworkInfo(activeNetWork);
    }

    private void addInstallPackages(AndroidDevice androidDevice) {
//        JSONObject installedPackages = data.getJSONObject("installed");
        List<InstalledPackage> installedPackagesList = new ArrayList<>();
        for (Object o : data.getJSONArray("installed")) {
            JSONObject temp = (JSONObject) o;
            InstalledPackage now = new InstalledPackage();
            now.setName(temp.getString("name"));
            now.setFileSize(temp.getIntValue("fileSize"));
            now.setFirstInstallTime(temp.getLongValue("firstInstallTime"));
            now.setFlags(temp.getIntValue("flags"));
            now.setLastUpdateTime(temp.getLongValue("lastUpdateTime"));
            now.setPackageName(temp.getString("packageName"));
            now.setSignatures(temp.getString("signatures"));
            now.setVersionCode(temp.getIntValue("versionCode"));
            now.setVersionName(temp.getString("versionName"));
            installedPackagesList.add(now);
        }
        InstalledPackage[] array = new InstalledPackage[installedPackagesList.size()];
        installedPackagesList.toArray(array);
        androidDevice.setInstalled(array);
    }

    private void addCpu(AndroidDevice androidDevice){
        List<CpuCore> cpuList = new ArrayList<>();
        for (Object o : data.getJSONArray("cpu")) {
            JSONObject temp = (JSONObject) o;
            CpuCore cpuCore = new CpuCore();
            cpuCore.setCpuinfo_max_freq(temp.getString("cpuinfo_max_freq"));
            cpuCore.setCpuinfo_min_freq(temp.getString("cpuinfo_min_freq"));
            cpuCore.setScaling_min_freq(temp.getString("scaling_min_freq"));
            cpuCore.setScaling_max_freq(temp.getString("scaling_max_freq"));
            cpuCore.setScaling_available_frequencies(temp.getString("scaling_available_frequencies"));
            cpuCore.setIndex(temp.getIntValue("index"));
            cpuList.add(cpuCore);
        }
        CpuCore[] array = new CpuCore[cpuList.size()];
        cpuList.toArray(array);
        androidDevice.setCpu(array);
    }

    private void addSensors(AndroidDevice androidDevice){
        List<Sensor> sensorList = new ArrayList<>();
        for (Object o : data.getJSONArray("sensors")) {
            JSONObject temp = (JSONObject) o;
            Sensor sensor = new Sensor();
            sensor.setName(temp.getString("name"));
            sensor.setVendor(temp.getString("vendor"));
            sensor.setMaximumRange(temp.getDoubleValue("maximumRange"));
            sensor.setMinDelay(temp.getIntValue("minDelay"));
            sensor.setPower(temp.getFloatValue("power"));
            sensor.setResolution(temp.getDoubleValue("resolution"));
            sensor.setType(temp.getIntValue("type"));
            sensor.setVersion(temp.getIntValue("version"));
            sensorList.add(sensor);
        }
        Sensor[] array = new Sensor[sensorList.size()];
        sensorList.toArray(array);
        androidDevice.setSensors(array);
    }

    private String getString(String key){
        return data.getString(key);
    }

    public AndroidDevice toObject() {
        AndroidDevice androidDevice = new AndroidDevice();
        androidDevice.setImei(getString("imei"));
        androidDevice.setImsi(getString("imsi"));
        androidDevice.setBrand(getString("brand"));
        androidDevice.setManufacturer(getString("manufacturer"));
        androidDevice.setModel(getString("model"));
        androidDevice.setRelease(getString("release"));
        androidDevice.setSdk(data.getInteger("sdk"));
        androidDevice.setFingerprint(getString("fingerprint"));
        androidDevice.setBoard(getString("board"));
        androidDevice.setSerial(data.getString("serial"));
        androidDevice.setDisplay(getString("display"));
        androidDevice.setId(getString("id"));
        androidDevice.setAndroidId(getString("androidId"));
        androidDevice.setRadio(data.getString("radio"));
        androidDevice.setHost(getString("host"));
        androidDevice.setBootloader(getString("bootloader"));
        androidDevice.setCpuAbi(getString("cpuAbi"));
        androidDevice.setCpuAbi2(getString("cpuAbi2"));
        androidDevice.setDevice(getString("device"));
        androidDevice.setHardware(getString("hardware"));
        androidDevice.setProduct(getString("product"));
        androidDevice.setTags(getString("tags"));
        androidDevice.setType(getString("type"));
        androidDevice.setUser(getString("user"));
        androidDevice.setCodename(getString("codename"));
        androidDevice.setIncremental(getString("incremental"));
        androidDevice.setUnknown(getString("unknown"));
        androidDevice.setMemInfo(getString("meminfo"));
        androidDevice.setCpuInfo(getString("cpuinfo"));
        androidDevice.setNetworkCountryIso(getString("networkCountryIso"));
        androidDevice.setNetworkOperator(getString("networkOperator"));
        androidDevice.setNetworkOperatorName(getString("networkOperatorName"));
        androidDevice.setSimCountryIso(getString("simCountryIso"));
        androidDevice.setSimOperatorName(getString("simOperatorName"));
        androidDevice.setSimOperator(getString("simOperator"));
        androidDevice.setDeviceSoftwareVersion(getString("deviceSoftwareVersion"));
        androidDevice.setMacAddress(getString("macAddress"));
        androidDevice.setSsid(getString("ssid"));
        androidDevice.setBssid(getString("bssid"));
        androidDevice.setIpAddress(getString("ipAddress"));
        addActiveNetWorkInfo(androidDevice);
        androidDevice.setHeight(data.getIntValue("height"));
        androidDevice.setWidth(data.getIntValue("width"));
        androidDevice.setDensity(data.getIntValue("density"));
        addInstallPackages(androidDevice);
        androidDevice.setUserAgent(getString("userAgent"));
        androidDevice.setSystemSharedLibraryNames(data.getObject("systemSharedLibraryNames",String[].class));
        androidDevice.setSystemAvailableFeatures(data.<String[]>getObject("systemAvailableFeatures",String[].class));
        androidDevice.setProperties(getString("properties"));
        androidDevice.setFsid(getString("fsid"));
        addCpu(androidDevice);
        androidDevice.setCpu_possible(getString("cpu_possible"));
        androidDevice.setCpu_present(getString("cpu_present"));
        addSensors(androidDevice);
        addRootStatFs(androidDevice);
        addDataStatFs(androidDevice);
        addExternalStatFs(androidDevice);
        androidDevice.setExternalStorageState(getString("externalStorageState"));
        androidDevice.setBluetoothAddress(getString("bluetoothAddress"));
        androidDevice.setDefaultInputMethod(getString("defaultInputMethod"));
        addInputMethods(androidDevice);
        androidDevice.setDeviceUniqueId(Base64.decodeBase64(getString("deviceUniqueId")));
        androidDevice.setProvisioningUniqueId(Base64.decodeBase64(getString("provisioningUniqueId")));
        androidDevice.setFrameworkMd5(getString("frameworkMd5"));
        androidDevice.setFrameworkArmMd5(getString("frameworkArmMd5"));
        androidDevice.setFrameworkArm64Md5(getString("frameworkArm64Md5"));
        androidDevice.setBinMd5(getString("binMd5"));
        androidDevice.setLatitude(data.getDouble("latitude"));
        androidDevice.setLongitude(data.getDouble("longitude"));
        addCameraInfos(androidDevice);
        addAudio(androidDevice);
        addNetWorkInterfaces(androidDevice);
        androidDevice.setBluetoothName(getString("bluetoothName"));
        androidDevice.setBuildProp(getString("bluetoothName"));
        androidDevice.setDefaultProp(getString("defaultProp"));

        return androidDevice;
    }

    private void addNetWorkInterfaces(AndroidDevice androidDevice) {
        ArrayList<NetworkInterface> networkInterfaceArrayList = new ArrayList<>();
        for (Object o : data.getJSONArray("networkInterfaces")) {
            JSONObject value = (JSONObject) o;
            NetworkInterface temp = new NetworkInterface();
            temp.setAddresses(value.getObject("addresses",String[].class));
            temp.setMtu(value.getIntValue("mtu"));
            temp.setHardwareAddress(value.getString("hardwareAddress"));
            temp.setName(value.getString("name"));
            networkInterfaceArrayList.add(temp);
        }
        androidDevice.setNetworkInterfaces(networkInterfaceArrayList);
    }

    private void addAudio(AndroidDevice androidDevice) {
        JSONObject value = data.getJSONObject("audio");
        Audio audio = new Audio();
        audio.setWiredHeadsetOn(value.getBooleanValue("wiredHeadsetOn"));
        audio.setMusicActive(value.getBooleanValue("musicActive"));
        audio.setSpeakerphoneOn(value.getBooleanValue("speakerphoneOn"));
        androidDevice.setAudio(audio);
    }

    private void addCameraInfos(AndroidDevice androidDevice) {
        ArrayList<CameraInfo> cameraInfoList = new ArrayList<>();
        for (Object o : data.getJSONArray("cameraInfos")) {
            JSONObject object = (JSONObject) o;
            CameraInfo cameraInfo = new CameraInfo();
            cameraInfo.setFacing(object.getIntValue("facing"));
            cameraInfo.setOrientation(object.getIntValue("orientation"));
            cameraInfoList.add(cameraInfo);
        }
        CameraInfo[] cameraInfos = new CameraInfo[cameraInfoList.size()];
        cameraInfoList.toArray(cameraInfos);
        androidDevice.setCameraInfos(cameraInfos);
    }

    private void addInputMethods(AndroidDevice androidDevice) {
        ArrayList<InputMethodInfo> inputMethodList = new ArrayList<>();
        for (Object inputMethods : data.getJSONArray("inputMethods")) {
            JSONObject object = (JSONObject) inputMethods;
            InputMethodInfo temp = new InputMethodInfo();
            temp.setServiceName(object.getString("serviceName"));
            temp.setPackageName(object.getString("packageName"));
            temp.setSettingsActivity(object.getString("settingsActivity"));
            inputMethodList.add(temp);
        }
        InputMethodInfo[] inputMethodInfos = new InputMethodInfo[inputMethodList.size()];
        inputMethodList.toArray(inputMethodInfos);
        androidDevice.setInputMethods(inputMethodInfos);
    }

    private void addExternalStatFs(AndroidDevice androidDevice) {
        JSONObject externalStatFs = data.getJSONObject("externalStatFs");
        StatFs statFs = new StatFs();
        statFs.setAvailableBlocks(externalStatFs.getIntValue("availableBlocks"));
        statFs.setBlockCount(externalStatFs.getIntValue("blockCount"));
        statFs.setBlockSize(externalStatFs.getIntValue("blockSize"));
        statFs.setFreeBlocks(externalStatFs.getIntValue("freeBlocks"));
        androidDevice.setExternalStatFs(statFs);
    }

    private void addDataStatFs(AndroidDevice androidDevice) {
        JSONObject dataStatFs = data.getJSONObject("dataStatFs");
        StatFs statFs = new StatFs();
        statFs.setAvailableBlocks(dataStatFs.getIntValue("availableBlocks"));
        statFs.setBlockCount(dataStatFs.getIntValue("blockCount"));
        statFs.setBlockSize(dataStatFs.getIntValue("blockSize"));
        statFs.setFreeBlocks(dataStatFs.getIntValue("freeBlocks"));
        androidDevice.setDataStatFs(statFs);
    }

    private void addRootStatFs(AndroidDevice androidDevice) {
        JSONObject rootStatFs = data.getJSONObject("rootStatFs");
        StatFs statFs = new StatFs();
        statFs.setAvailableBlocks(rootStatFs.getIntValue("availableBlocks"));
        statFs.setBlockCount(rootStatFs.getIntValue("blockCount"));
        statFs.setBlockSize(rootStatFs.getIntValue("blockSize"));
        statFs.setFreeBlocks(rootStatFs.getIntValue("freeBlocks"));
        androidDevice.setRootStatFs(statFs);
    }


}

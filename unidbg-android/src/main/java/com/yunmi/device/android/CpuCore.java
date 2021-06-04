package com.yunmi.device.android;

import java.io.Serializable;

public class CpuCore implements Serializable {
    private String cpuinfo_max_freq;
    private String cpuinfo_min_freq;
    private int index;
    private String scaling_available_frequencies;
    private String scaling_max_freq;
    private String scaling_min_freq;

    public String getCpuinfo_max_freq() {
        return this.cpuinfo_max_freq;
    }

    public String getCpuinfo_min_freq() {
        return this.cpuinfo_min_freq;
    }

    public int getIndex() {
        return this.index;
    }

    public String getScaling_available_frequencies() {
        return this.scaling_available_frequencies;
    }

    public String getScaling_max_freq() {
        return this.scaling_max_freq;
    }

    public String getScaling_min_freq() {
        return this.scaling_min_freq;
    }

    public void setCpuinfo_max_freq(String cpuinfo_max_freq) {
        this.cpuinfo_max_freq = cpuinfo_max_freq;
    }

    public void setCpuinfo_min_freq(String cpuinfo_min_freq) {
        this.cpuinfo_min_freq = cpuinfo_min_freq;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setScaling_available_frequencies(String scaling_available_frequencies) {
        this.scaling_available_frequencies = scaling_available_frequencies;
    }

    public void setScaling_max_freq(String scaling_max_freq) {
        this.scaling_max_freq = scaling_max_freq;
    }

    public void setScaling_min_freq(String scaling_min_freq) {
        this.scaling_min_freq = scaling_min_freq;
    }
}
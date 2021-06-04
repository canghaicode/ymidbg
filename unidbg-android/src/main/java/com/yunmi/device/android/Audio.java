package com.yunmi.device.android;

import java.io.Serializable;

public class Audio implements Serializable {
    private boolean musicActive;
    private boolean speakerphoneOn;
    private boolean wiredHeadsetOn;

    public boolean isMusicActive() {
        return this.musicActive;
    }

    public boolean isSpeakerphoneOn() {
        return this.speakerphoneOn;
    }

    public boolean isWiredHeadsetOn() {
        return this.wiredHeadsetOn;
    }

    public void setMusicActive(boolean musicActive) {
        this.musicActive = musicActive;
    }

    public void setSpeakerphoneOn(boolean speakerphoneOn) {
        this.speakerphoneOn = speakerphoneOn;
    }

    public void setWiredHeadsetOn(boolean wiredHeadsetOn) {
        this.wiredHeadsetOn = wiredHeadsetOn;
    }
}
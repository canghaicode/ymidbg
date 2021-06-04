package com.yunmi.device.android;

import java.io.Serializable;

public class StatFs implements Serializable {
    private int availableBlocks;
    private int blockCount;
    private int blockSize;
    private int freeBlocks;

    public int getAvailableBlocks() {
        return this.availableBlocks;
    }

    public int getBlockCount() {
        return this.blockCount;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public int getFreeBlocks() {
        return this.freeBlocks;
    }

    public void setAvailableBlocks(int availableBlocks) {
        this.availableBlocks = availableBlocks;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setFreeBlocks(int freeBlocks) {
        this.freeBlocks = freeBlocks;
    }
}

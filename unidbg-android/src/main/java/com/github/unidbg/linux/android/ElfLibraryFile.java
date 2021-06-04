package com.github.unidbg.linux.android;

import com.github.unidbg.Emulator;
import com.github.unidbg.spi.LibraryFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ElfLibraryFile implements LibraryFile {

    private final File elfFile;

    public ElfLibraryFile(File elfFile) {
        this.elfFile = elfFile;
    }

    @Override
    public String getName() {
        return elfFile.getName();
    }

    @Override
    public String getMapRegionName() {
        String name = getName();
        if (name.endsWith(".so")) {
            return "/system/lib/" + name;
        } else {
            return "/system/bin/" + name;
        }
    }

    @Override
    public LibraryFile resolveLibrary(Emulator<?> emulator, String soName) {
        File file = new File(elfFile.getParentFile(), soName);
        return file.canRead() ? new ElfLibraryFile(file) : null;
    }

    @Override
    public byte[] readToByteArray() throws IOException {
        return FileUtils.readFileToByteArray(elfFile);
    }

    @Override
    public ByteBuffer mapBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {
        return "/system/lib";
    }

}

package com.github.unidbg.linux.android;

import com.github.unidbg.Emulator;
import com.github.unidbg.spi.LibraryFile;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

public class URLibraryFile implements LibraryFile {

    private final URL url;
    private final String name;
    private final int sdk;

    public URLibraryFile(URL url, String name, int sdk) {
        this.url = url;
        this.name = name;
        this.sdk = sdk;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMapRegionName() {
        return getPath();
    }

    @Override
    public LibraryFile resolveLibrary(Emulator<?> emulator, String soName) {
        if (sdk <= 0) {
            return null;
        }
        return AndroidResolver.resolveLibrary(emulator, soName, sdk);
    }

    @Override
    public byte[] readToByteArray() throws IOException {
        return IOUtils.toByteArray(url);
    }

    @Override
    public ByteBuffer mapBuffer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {
        return "/system/lib/" + getName();
    }
}

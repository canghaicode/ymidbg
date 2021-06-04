package com.github.unidbg.ios.file;

import com.github.unidbg.Emulator;
import com.github.unidbg.arm.backend.Backend;
import com.github.unidbg.file.FileIO;
import com.github.unidbg.file.ios.BaseDarwinFileIO;
import com.github.unidbg.file.ios.IOConstants;
import com.github.unidbg.file.ios.StatStructure;
import com.github.unidbg.unix.IO;
import com.github.unidbg.utils.Inspector;
import com.sun.jna.Pointer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;

public class SimpleFileIO extends BaseDarwinFileIO implements FileIO {

    private static final Log log = LogFactory.getLog(SimpleFileIO.class);

    protected final File file;
    protected final String path;
    private final RandomAccessFile randomAccessFile;

    public SimpleFileIO(int oflags, File file, String path) {
        super(oflags);
        this.file = file;
        this.path = path;

        if (file.isDirectory()) {
            throw new IllegalArgumentException("file is directory: " + file);
        }

        try {
            FileUtils.forceMkdir(file.getParentFile());
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("createNewFile failed: " + file);
            }

            randomAccessFile = new RandomAccessFile(file, "rws");
        } catch (IOException e) {
            throw new IllegalStateException("process file failed: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(randomAccessFile);

        if (debugStream != null) {
            try {
                debugStream.flush();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public int write(byte[] data) {
        try {
            if (debugStream != null) {
                debugStream.write(data);
                debugStream.flush();
            }

            if (log.isDebugEnabled() && data.length < 0x3000) {
                Inspector.inspect(data, "write");
            }

            if ((oflags & IOConstants.O_APPEND) != 0) {
                randomAccessFile.seek(randomAccessFile.length());
            }
            randomAccessFile.write(data);
            return data.length;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    OutputStream debugStream;

    void setDebugStream(OutputStream stream) {
        this.debugStream = new BufferedOutputStream(stream);
    }

    @Override
    public int read(Backend backend, Pointer buffer, final int _count) {
        try {
            int count = _count;
            if (count > randomAccessFile.length() - randomAccessFile.getFilePointer()) {
                count = (int) (randomAccessFile.length() - randomAccessFile.getFilePointer());

                /*
                 * lseek() allows the file offset to be set beyond the end of the file
                 *        (but this does not change the size of the file).  If data is later
                 *        written at this point, subsequent reads of the data in the gap (a
                 *        "hole") return null bytes ('\0') until data is actually written into
                 *        the gap.
                 */
                if (count < 0) {
                    log.warn("read path=" + file + ", fp=" + randomAccessFile.getFilePointer() + ", _count=" + _count + ", length=" + randomAccessFile.length());
                    return 0;
                }
            }

            byte[] data = new byte[count];
            int read = randomAccessFile.read(data);
            if (read <= 0) {
                if (log.isDebugEnabled()) {
                    log.debug("read path=" + file + ", fp=" + randomAccessFile.getFilePointer() + ", _count=" + _count + ", length=" + randomAccessFile.length());
                }
                return read;
            }

            if (randomAccessFile.getFilePointer() > randomAccessFile.length()) {
                throw new IllegalStateException("fp=" + randomAccessFile.getFilePointer() + ", length=" + randomAccessFile.length());
            }

            final byte[] buf;
            if (read == count) {
                buf = data;
            } else if(read < count) {
                buf = Arrays.copyOf(data, read);
            } else {
                throw new IllegalStateException("count=" + count + ", read=" + read);
            }
            if (log.isDebugEnabled() && buf.length < 0x3000) {
                Inspector.inspect(buf, "read path=" + file + ", fp=" + randomAccessFile.getFilePointer() + ", _count=" + _count + ", length=" + randomAccessFile.length());
            }
            buffer.write(0, buf, 0, buf.length);
            return buf.length;
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    protected byte[] getMmapData(int offset, int length) throws IOException {
        randomAccessFile.seek(offset);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        byte[] buf = new byte[10240];
        do {
            int count = length - baos.size();
            if (count == 0) {
                break;
            }

            if (count > buf.length) {
                count = buf.length;
            }

            int read = randomAccessFile.read(buf, 0, count);
            if (read == -1) {
                break;
            }

            baos.write(buf, 0, read);
        } while (true);
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public int ioctl(Emulator<?> emulator, long request, long argp) {
        if (IO.STDOUT.equals(path) || IO.STDERR.equals(path)) {
            return 0;
        }

        return super.ioctl(emulator, request, argp);
    }

    @Override
    public FileIO dup2() {
        SimpleFileIO dup = new SimpleFileIO(oflags, file, path);
        dup.debugStream = debugStream;
        dup.op = op;
        dup.oflags = oflags;
        return dup;
    }

    @Override
    public int lseek(int offset, int whence) {
        try {
            switch (whence) {
                case SEEK_SET:
                    randomAccessFile.seek(offset);
                    return (int) randomAccessFile.getFilePointer();
                case SEEK_CUR:
                    randomAccessFile.seek(randomAccessFile.getFilePointer() + offset);
                    return (int) randomAccessFile.getFilePointer();
                case SEEK_END:
                    randomAccessFile.seek(randomAccessFile.length() + offset);
                    return (int) randomAccessFile.getFilePointer();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return super.lseek(offset, whence);
    }

    @Override
    public int llseek(long offset, Pointer result, int whence) {
        try {
            switch (whence) {
                case SEEK_SET:
                    randomAccessFile.seek(offset);
                    result.setLong(0, randomAccessFile.getFilePointer());
                    return 0;
                case SEEK_END:
                    randomAccessFile.seek(randomAccessFile.length() - offset);
                    result.setLong(0, randomAccessFile.getFilePointer());
                    return 0;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return super.llseek(offset, result, whence);
    }

    @Override
    public int fstat(Emulator<?> emulator, StatStructure stat) {
        int blockSize = emulator.getPageAlign();
        int st_mode;
        if (IO.STDOUT.equals(file.getName())) {
            st_mode = IO.S_IFCHR | 0x777;
        } else if(Files.isSymbolicLink(file.toPath())) {
            st_mode = IO.S_IFLNK;
        } else {
            st_mode = IO.S_IFREG;
        }
        stat.st_dev = 1;
        stat.st_mode = (short) (st_mode);
        stat.setSize(file.length());
        stat.setBlockCount((file.length() + blockSize - 1) / blockSize);
        stat.st_blksize = blockSize;
        stat.st_ino = 7;
        stat.st_uid = 0;
        stat.st_gid = 0;
        stat.setLastModification(file.lastModified());
        stat.pack();
        return 0;
    }

    @Override
    public int ftruncate(int length) {
        try (FileChannel channel = new FileOutputStream(file, true).getChannel()) {
            channel.truncate(length);
            return 0;
        } catch (IOException e) {
            log.debug("ftruncate failed", e);
            return -1;
        }
    }

    @Override
    public int listxattr(Pointer namebuf, int size, int options) {
        return listxattr(file, namebuf, size);
    }

    @Override
    public int chown(int uid, int gid) {
        return chown(file, uid, gid);
    }

    @Override
    public int chmod(int mode) {
        return chmod(file, mode);
    }

    @Override
    public int chflags(int flags) {
        return chflags(file, flags);
    }
}

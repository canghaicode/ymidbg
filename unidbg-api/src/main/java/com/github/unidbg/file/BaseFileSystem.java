package com.github.unidbg.file;

import com.github.unidbg.Emulator;
import com.github.unidbg.unix.IO;
import com.github.unidbg.unix.UnixEmulator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

public abstract class BaseFileSystem<T extends NewFileIO> implements FileSystem<T> {

    private static final Log log = LogFactory.getLog(BaseFileSystem.class);

    protected final Emulator<T> emulator;
    protected final File rootDir;

    public BaseFileSystem(Emulator<T> emulator, File rootDir) {
        this.emulator = emulator;
        this.rootDir = rootDir;

        try {
            initialize(this.rootDir);
        } catch (IOException e) {
            throw new IllegalStateException("initialize file system failed", e);
        }
    }

    protected void initialize(File rootDir) throws IOException {
        FileUtils.forceMkdir(new File(rootDir, "tmp"));
    }

    @Override
    public FileResult<T> open(String pathname, int oflags) {
        if ("".equals(pathname)) {
            return FileResult.failed(UnixEmulator.ENOENT); // No such file or directory
        }

        if (IO.STDIN.equals(pathname)) {
            return FileResult.success(createStdin(oflags));
        }

        if (IO.STDOUT.equals(pathname) || IO.STDERR.equals(pathname)) {
            try {
                File stdio = new File(rootDir, pathname + ".txt");
                if (!stdio.exists() && !stdio.createNewFile()) {
                    throw new IOException("create new file failed: " + stdio);
                }
                return FileResult.success(createStdout(oflags, stdio, pathname));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        File file = new File(rootDir, pathname);
        return createFileIO(file, oflags, pathname);
    }

    protected abstract T createStdout(int oflags, File stdio, String pathname);

    protected abstract T createStdin(int oflags);

    private FileResult<T> createFileIO(File file, int oflags, String path) {
        boolean directory = hasDirectory(oflags);
        if (file.isFile() && directory) {
            return FileResult.failed(UnixEmulator.ENOTDIR);
        }

        boolean create = hasCreat(oflags);
        if (file.exists()) {
            if (create && hasExcl(oflags)) {
                return FileResult.failed(UnixEmulator.EEXIST);
            }
            return FileResult.success(file.isDirectory() ? createDirectoryFileIO(file, oflags, path) : createSimpleFileIO(file, oflags, path));
        }

        if (!create) {
            return FileResult.failed(UnixEmulator.ENOENT);
        }

        try {
            if (directory) {
                FileUtils.forceMkdir(file);
                return FileResult.success(createDirectoryFileIO(file, oflags, path));
            } else {
                if (!file.getParentFile().exists()) {
                    FileUtils.forceMkdir(file.getParentFile());
                }
                FileUtils.touch(file);
                return FileResult.success(createSimpleFileIO(file, oflags, path));
            }
        } catch (IOException e) {
            throw new IllegalStateException("createNewFile failed: " + file, e);
        }
    }

    @Override
    public boolean mkdir(String path) {
        File dir = new File(rootDir, path);
        if (emulator.getSyscallHandler().isVerbose()) {
            System.out.printf("mkdir '%s'%n", path);
        }

        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }

    @Override
    public void rmdir(String path) {
        File dir = new File(rootDir, path);
        FileUtils.deleteQuietly(dir);

        if (emulator.getSyscallHandler().isVerbose()) {
            System.out.printf("rmdir '%s'%n", path);
        }
    }

    protected abstract boolean hasCreat(int oflags);
    protected abstract boolean hasDirectory(int oflags);
    @SuppressWarnings("unused")
    protected abstract boolean hasAppend(int oflags);
    protected abstract boolean hasExcl(int oflags);

    @Override
    public final void unlink(String path) {
        File file = new File(rootDir, path);
        FileUtils.deleteQuietly(file);
        if (log.isDebugEnabled()) {
            log.debug("unlink path=" + path + ", file=" + file);
        }
        if (emulator.getSyscallHandler().isVerbose()) {
            System.out.printf("unlink '%s'%n", path);
        }
    }

    @Override
    public File getRootDir() {
        return rootDir;
    }

    @Override
    public File createWorkDir() {
        File workDir = new File(rootDir, DEFAULT_WORK_DIR);
        if (!workDir.exists() && !workDir.mkdirs()) {
            throw new IllegalStateException("mkdirs failed: " + workDir);
        }
        return workDir;
    }

    @Override
    public int rename(String oldPath, String newPath) {
        File oldFile = new File(rootDir, oldPath);
        File newFile = new File(rootDir, newPath);
        try {
            FileUtils.forceMkdir(newFile.getParentFile());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        if (!oldFile.renameTo(newFile)) {
            throw new IllegalStateException("rename failed: old=" + oldFile + ", new=" + newFile);
        }
        return 0;
    }
}

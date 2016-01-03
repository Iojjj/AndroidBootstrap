package iojjj.android.bootstrap.utils;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    /**
     * Copy file from source to destination
     * @param src source file
     * @param dst destination file
     * @return result of operation
     */
    @CopyFileResult
    public static int copyFile(@NonNull File src, @NonNull File dst) {
        if (!src.exists()) {
            return CopyFileResult.RESULT_SRC_NOT_EXISTS;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try (FileInputStream fis = new FileInputStream(src)) {
                try (FileOutputStream fos = new FileOutputStream(dst)) {
                    copyStream(fis, fos);
                    fos.flush();
                }
                return CopyFileResult.RESULT_SUCCESS;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileInputStream srcStream = null;
            FileOutputStream dstStream = null;
            try {
                srcStream = new FileInputStream(src);
                dstStream = new FileOutputStream(dst);
                copyStream(srcStream, dstStream);
                dstStream.flush();
                return CopyFileResult.RESULT_SUCCESS;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(srcStream);
                close(dstStream);
            }
        }
        return CopyFileResult.RESULT_GENERAL_ERROR;
    }

    /**
     * Copy streams
     * @param is input stream
     * @param os output stream
     * @throws IOException
     */
    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        final int buffer_size = 1024;
        byte[] bytes = new byte[buffer_size];
        int count;
        while ((count = is.read(bytes, 0, buffer_size)) != -1) {
            os.write(bytes, 0, count);
        }
    }

    /**
     * Close any closeable silently
     * @param closeable any closeable
     */
    public static void close(@Nullable Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Checks if external storage is available for read and write
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     *  Checks if external storage is available to at least read
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}

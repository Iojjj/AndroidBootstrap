package iojjj.androidbootstrap.utils.storage;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import iojjj.androidbootstrap.annotations.CopyFileResult;

public class FileUtils {

    /**
     * Copy source file to destination
     * @param src source file
     * @param dst destination file
     * @return result of operation
     */
    @CopyFileResult
    public static int copyFile(@NonNull File src, @NonNull File dst) {
        if (!src.exists()) {
            return CopyFileResult.RESULT_SRC_NOT_EXISTS;
        }
        FileInputStream srcStream = null;
        FileOutputStream dstStream = null;
        try {
            srcStream = new FileInputStream(src);
            dstStream = new FileOutputStream(dst);

            copyStream(srcStream, dstStream);

            dstStream.flush();
            srcStream.close();
            dstStream.close();

            return CopyFileResult.RESULT_SUCCESS;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return CopyFileResult.RESULT_GENERAL_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return CopyFileResult.RESULT_GENERAL_ERROR;
        } finally {
            if (srcStream != null) {
                try {
                    srcStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dstStream != null) {
                try {
                    dstStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

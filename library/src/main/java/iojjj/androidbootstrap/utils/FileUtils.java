package iojjj.androidbootstrap.utils;

import android.os.Environment;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static final int RESULT_SUCCESS = 0x00;
    public static final int RESULT_GENERAL_ERROR = 0x01;
    public static final int RESULT_SRC_NOT_EXISTS = 0x02;

    @MagicConstant(valuesFromClass = FileUtils.class)
    public static int copyFile(@NotNull File src, @NotNull File dst) {
        if (!src.exists()) {
            return RESULT_SRC_NOT_EXISTS;
        }
        try {
            FileInputStream srcStream = new FileInputStream(src);
            FileOutputStream dstStream = new FileOutputStream(dst);

            int read;
            byte[] buf = new byte[2048];
            while ((read = srcStream.read(buf, 0, 1024)) > 0)
                dstStream.write(buf, 0, read);

            dstStream.flush();
            srcStream.close();
            dstStream.close();

            return RESULT_SUCCESS;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return RESULT_GENERAL_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return RESULT_GENERAL_ERROR;
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}

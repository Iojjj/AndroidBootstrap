package com.github.iojjj.bootstrap.core;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Alexander Vlasov on 03.01.2016.
 */
public class BSIoManager {

    private BSIoManager() {}

    /**
     * Copy source file to destination file.
     * @param src source file
     * @param dst destination file
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public static void copyFile(@NonNull File src, @NonNull File dst) throws IOException {
        try (FileInputStream fis = new FileInputStream(src)) {
            try (FileOutputStream fos = new FileOutputStream(dst)) {
                copyStream(fis, fos);
            }
        }
    }

    /**
     * Copy input stream to output stream. This method doesn't close those streams after completion of operation.
     * @param inputStream input stream
     * @param outputStream output stream
     * @throws IOException
     */
    public static void copyStream(@NonNull InputStream inputStream, @NonNull OutputStream outputStream) throws IOException {
        BSAssertions.assertNotNull(inputStream, "inputStream");
        BSAssertions.assertNotNull(outputStream, "outputStream");
        final int buffer_size = 1024;
        byte[] bytes = new byte[buffer_size];
        int count;
        while ((count = inputStream.read(bytes, 0, buffer_size)) != -1) {
            outputStream.write(bytes, 0, count);
        }
    }

    public static void closeSilently(@NonNull Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Checks if external storage is available for read and write.
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     *  Checks if external storage is available to at least read.
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}

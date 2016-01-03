package iojjj.android.bootstrap.utils;

import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Alexander Vlasov on 03.01.2016.
 */
public class StreamUtils {

    private StreamUtils() {}

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
}

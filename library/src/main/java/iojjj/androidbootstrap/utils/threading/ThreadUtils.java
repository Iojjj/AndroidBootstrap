package iojjj.androidbootstrap.utils.threading;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import iojjj.androidbootstrap.utils.misc.Config;

/**
 * Helper class for threading
 */
public class ThreadUtils {
    private static final class MainThreadHandlerHolder {
        public static final Handler INSTANCE = new Handler(Looper.getMainLooper());
    }

    private static final ExecutorService BACKGROUND_EXECUTOR = Executors.newFixedThreadPool(Config.CORES_COUNT * 2 + 1);

    /**
     * Get handler for UI thread
     * @return handler for UI thread
     */
    public static Handler getMainHandler() {
        return MainThreadHandlerHolder.INSTANCE;
    }

    /**
     * Execute runnable on UI thread
     * @param r runnable
     */
    public static void runOnUiThread(@NonNull final Runnable r) {
        if (Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            r.run();

            return;
        }
        getMainHandler().post(r);
    }

    /**
     * Execute runnable on UI thread with delay
     * @param r runnable
     * @param delayMillis delay in milliseconds
     */
    public static void runOnUiThread(@NonNull final Runnable r, final long delayMillis) {
        getMainHandler().postDelayed(r, delayMillis);
    }

    /**
     * Execute runnable in background thread
     * @param runnable runnable
     * @return a Future representing pending completion of the task
     */
    @Nullable
    public static Future runInBackground(@NonNull final Runnable runnable) {
        return runInBackground(runnable, true);
    }

    /**
     * Execute runnable in background thread
     * @param runnable runnable
     * @param immediately if set to true, runnable will be executed immediately if current thread is not a UI thread
     * @return a Future representing pending completion of the task or null if runnable was executed immediately
     */
    @Nullable
    public static Future runInBackground(@NonNull final Runnable runnable, boolean immediately) {
        if (immediately && !Thread.currentThread().equals(Looper.getMainLooper().getThread())) {
            runnable.run();
            return null;
        }
        return BACKGROUND_EXECUTOR.submit(runnable);
    }
}

package com.github.iojjj.bootstrap.core.threading;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Helper class for threading.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSThreadingManager {

    private BSThreadingManager() {
        //no instance
    }

    /**
     * Get handler for UI thread.
     *
     * @return handler for UI thread
     */
    public static Handler getMainHandler() {
        return MainThreadHandlerHolder.INSTANCE;
    }

    /**
     * Execute runnable on UI thread.
     *
     * @param r runnable
     */
    public static void runOnUiThread(@NonNull final Runnable r) {
        if (isUiThread()) {
            r.run();
            return;
        }
        getMainHandler().post(r);
    }

    /**
     * Execute runnable on UI thread with delay.
     *
     * @param r           runnable
     * @param delayMillis delay in milliseconds
     */
    public static void runOnUiThread(@NonNull final Runnable r, final long delayMillis) {
        getMainHandler().postDelayed(r, delayMillis);
    }

    /**
     * Execute runnable in background thread.
     *
     * @param runnable runnable
     * @param executor any executor
     * @return a Future representing pending completion of the task
     * @deprecated use <a href="https://github.com/ReactiveX/RxJava">RxJava</a> instead.
     */
    @Nullable
    public static Future runOnExecutor(@NonNull final Runnable runnable, @NonNull ExecutorService executor) {
        return runOnExecutor(runnable, executor, false);
    }

    /**
     * Execute runnable in background thread.
     *
     * @param runnable    runnable
     * @param immediately if set to true, runnable will be executed immediately if current thread is not a UI thread
     * @param executor    any executor
     * @return a Future representing pending completion of the task or null if runnable was executed immediately
     * @deprecated use <a href="https://github.com/ReactiveX/RxJava">RxJava</a> instead.
     */
    @Nullable
    public static Future runOnExecutor(@NonNull final Runnable runnable, @NonNull ExecutorService executor, boolean immediately) {
        if (immediately && !isUiThread()) {
            runnable.run();
            return null;
        }
        return executor.submit(runnable);
    }

    /**
     * Checks if current thread is the UI thread.
     *
     * @return true if current thread is the UI thread, false otherwise
     */
    public static boolean isUiThread() {
        return Thread.currentThread().equals(Looper.getMainLooper().getThread());
    }

    private static final class MainThreadHandlerHolder {
        public static final Handler INSTANCE = new Handler(Looper.getMainLooper());
    }
}

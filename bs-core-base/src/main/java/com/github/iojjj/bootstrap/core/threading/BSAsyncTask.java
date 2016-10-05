package com.github.iojjj.bootstrap.core.threading;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Implementation of {@link AsyncTask} that runs in parallel and starts on main thread.
 * <b>You must apply {@link #executeEx(TIn...)} instead of {@link #execute(TIn...)}</b>.
 *
 * @since 1.0
 * @deprecated use <a href="https://github.com/ReactiveX/RxJava">RxJava</a> instead.
 */
@SuppressWarnings({"WeakerAccess", "unused", "deprecation"})
public abstract class BSAsyncTask<TIn, TProgress, TOut> extends AsyncTask<TIn, TProgress, TOut> {

    /**
     * The executor to use for API >= 11.
     */
    private Executor mExecutor;

    public BSAsyncTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
        }
    }

    /**
     * Executor that will be used at API level {@link Build.VERSION_CODES#HONEYCOMB} or higher
     *
     * @param executor executor for async task
     */
    public BSAsyncTask(@NonNull Executor executor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mExecutor = executor;
        }
    }

    @SafeVarargs
    public final void executeEx(final TIn... params) {
        BSThreadingManager.runOnUiThread(() -> executeExSafe(params));
    }

    @SafeVarargs
    private final BSAsyncTask<TIn, TProgress, TOut> executeExSafe(final TIn... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (BSAsyncTask<TIn, TProgress, TOut>) super.executeOnExecutor(mExecutor, params);
        }
        return (BSAsyncTask<TIn, TProgress, TOut>) super.execute(params);
    }
}

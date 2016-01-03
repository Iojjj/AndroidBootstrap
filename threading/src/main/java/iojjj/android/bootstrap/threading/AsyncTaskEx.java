package iojjj.android.bootstrap.threading;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Implementation of AsyncTask that runs in parallel and starts on main thread.
 * You must call {@link #executeEx(TIn...)} instead of {@link #execute(TIn...)}.
 */
public abstract class AsyncTaskEx<TIn, TProgress, TOut> extends AsyncTask<TIn, TProgress, TOut> {

    /**
     * The executor to use for API >= 11.
     */
    private Executor executor;

    public AsyncTaskEx() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            this.executor = AsyncTask.THREAD_POOL_EXECUTOR;
    }

    /**
     * Executor that will be used at API level {@link android.os.Build.VERSION_CODES#HONEYCOMB} or higher
     * @param executor executor for async task
     */
    public AsyncTaskEx(@NonNull Executor executor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executor = executor;
        }
    }

    @SafeVarargs
    private final AsyncTaskEx<TIn, TProgress, TOut> executeExSafe(final TIn... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (AsyncTaskEx<TIn, TProgress, TOut>) super.executeOnExecutor(executor, params);
        }
        return (AsyncTaskEx<TIn, TProgress, TOut>) super.execute(params);
    }

    @SafeVarargs
    public final void executeEx(final TIn... params) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                executeExSafe(params);
            }
        });
    }
}

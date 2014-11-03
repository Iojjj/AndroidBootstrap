package iojjj.androidbootstrap.utils.threading;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;

/**
 * Implementation of AsyncTask that runs in parallel and starts on main thread
 */
public abstract class AsyncTaskEx<TIn, TProgress, TOut> extends AsyncTask<TIn, TProgress, TOut> {

    @NonNull
    private Executor executor;

    public AsyncTaskEx() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            this.executor = AsyncTask.THREAD_POOL_EXECUTOR;
    }

    protected AsyncTaskEx(@Nullable Executor executor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.executor = executor == null ? AsyncTask.THREAD_POOL_EXECUTOR : executor;
        }
    }

    private AsyncTaskEx<TIn, TProgress, TOut> executeExSafe(final TIn... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (AsyncTaskEx<TIn, TProgress, TOut>) super.executeOnExecutor(executor, params);
        }
        return (AsyncTaskEx<TIn, TProgress, TOut>) super.execute(params);
    }

    public void executeEx(final TIn... params) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                executeExSafe(params);
            }
        });
    }
}

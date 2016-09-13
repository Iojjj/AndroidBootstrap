package com.github.iojjj.bootstrap.core.threading;

import android.os.Process;

/**
 * Runnable that set thread priority before execution. See {@link Process} for list of available priority levels.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BSPriorityRunnable implements Runnable {
    private int mThreadPriority;

    public BSPriorityRunnable() {
        mThreadPriority = Process.THREAD_PRIORITY_BACKGROUND;
    }

    public BSPriorityRunnable(@BSProcessPriority int threadPriority) {
        this.mThreadPriority = threadPriority;
    }

    @Override
    public void run() {
        Process.setThreadPriority(mThreadPriority);
        runImpl();
    }

    protected abstract void runImpl();
}

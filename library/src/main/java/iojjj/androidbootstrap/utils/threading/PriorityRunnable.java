package iojjj.androidbootstrap.utils.threading;

import android.os.Process;

import iojjj.androidbootstrap.annotations.ProcessPriority;

/**
* Runnable that set thread priority before execution
*/
public abstract class PriorityRunnable implements Runnable {
    private int threadPriority;

    public PriorityRunnable() {
        threadPriority = Process.THREAD_PRIORITY_BACKGROUND;
    }

    public PriorityRunnable(@ProcessPriority int threadPriority) {
        this.threadPriority = threadPriority;
    }

    @Override
    public void run() {
        Process.setThreadPriority(threadPriority);
        runImpl();
    }

    protected abstract void runImpl();
}

package com.github.iojjj.bootstrap.mvp;

import android.content.Loader;

/**
 * Loader delegate interface.
 *
 * @param <P> type of presenter
 * @since 1.0
 */
interface LoaderDelegate<P extends BSMvpPresenter> {

    /**
     * Take the current flag indicating whether the loader's content had
     * changed while it was stopped.  If it had, true is returned and the
     * flag is cleared.
     */
    boolean takeContentChanged();

    /**
     * Force an asynchronous load. Unlike {@link Loader#startLoading()} this will ignore a previously
     * loaded data set and load a new one.  This simply calls through to the
     * implementation's {@link Loader#onForceLoad()}.  You generally should only call this
     * when the loader is started -- that is, {@link Loader#isStarted()} returns true.
     * <p>Must be called from the process's main thread.
     */
    void forceLoad();

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     * Must be called from the process's main thread.
     *
     * @param presenter the result of the load
     */
    void deliverResult(P presenter);

    /**
     * Attempt to cancel the current load task.
     * Must be called on the main thread of the process.
     * <p>Cancellation is not an immediate operation, since the load is performed
     * in a background thread.  If there is currently a load in progress, this
     * method requests that the load be canceled, and notes this is the case;
     * once the background thread has completed its work its remaining state
     * will be cleared.  If another load request comes in during this time,
     * it will be held until the canceled load is complete.
     */
    void cancelLoadImpl();
}

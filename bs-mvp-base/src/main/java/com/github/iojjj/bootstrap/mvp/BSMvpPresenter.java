package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;

/**
 * MVP presenter interface.
 *
 * @since 1.0
 */
public interface BSMvpPresenter<V extends BSMvpView> extends AndroidPresenterCallbacks {

    /**
     * Get view linked to this presenter.
     *
     * @return view linked to this presenter
     */
    @Nullable
    V getView();

    /**
     * Set view linked to this presenter
     *
     * @param view view linked to this presenter
     */
    void setView(@Nullable V view);

    /**
     * Called when presenter is created.
     */
    void onCreated();

    /**
     * Called when presenter is about to be destroyed. All allocated resources must be released here.
     */
    void onDestroyed();
}

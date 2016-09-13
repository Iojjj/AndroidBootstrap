package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;

/**
 * MVP presenter interface.
 *
 * @since 1.0
 */
public interface BSMvpPresenter<TView extends BSMvpView> extends AndroidLifecycle {

    /**
     * Get view linked to this presenter.
     *
     * @return view linked to this presenter
     */
    @Nullable
    TView getView();

    /**
     * Set view linked to this presenter
     *
     * @param view view linked to this presenter
     */
    void setView(@Nullable TView view);

    /**
     * Called when presenter is created.
     */
    void onCreated();

    /**
     * Called when presenter is about to be destroyed. All allocated resources must be released here.
     */
    void onDestroyed();
}

package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;

/**
 * MVP view interface.
 *
 * @param <P> type of presenter
 * @since 1.0
 */
public interface BSMvpView<P extends BSMvpPresenter> {

    /**
     * Set presenter for this view.
     *
     * @param presenter instance of BSMvpPresenter or null
     */
    void setPresenter(@Nullable P presenter);

    /**
     * Get presenter of this view.
     * @return instance of BSMvpPresenter or null
     */
    @Nullable
    P getPresenter();
}

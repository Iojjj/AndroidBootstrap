package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;

/**
 * MVP view interface.
 *
 * @param <TPresenter> type of presenter
 * @since 1.0
 */
public interface BSMvpView<TPresenter extends BSMvpPresenter> {

    /**
     * Set presenter for this view.
     *
     * @param presenter instance of BSMvpPresenter or null
     */
    void setPresenter(@Nullable TPresenter presenter);
}

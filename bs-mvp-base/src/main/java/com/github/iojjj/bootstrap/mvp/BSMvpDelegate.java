package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * MVP pattern delegate interface.
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
public interface BSMvpDelegate extends AndroidPresenterCallbacks {

    /**
     * Initialize loader.
     *
     * @param loaderId          unique loader ID
     * @param view              instance of BSMvpView
     * @param presenterProvider functional interface that creates an instance of BSMvpPresenter
     */
    <V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
    void initPresenter(int loaderId, @NonNull V view, @NonNull BSFunction0<P> presenterProvider);
}

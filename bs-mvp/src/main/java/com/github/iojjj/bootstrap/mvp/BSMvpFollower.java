package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * MVP pattern follower interface.
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
public interface BSMvpFollower extends AndroidLifecycle {

    /**
     * Initialize loader.
     *
     * @param loaderId          unique loader ID
     * @param view              instance of BSMvpView
     * @param presenterProvider functional interface that creates an instance of BSMvpPresenter
     * @param <TPresenter>      type of presenter
     * @param <TView>           type of view
     */
    <TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
    void initPresenter(int loaderId, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterProvider);
}

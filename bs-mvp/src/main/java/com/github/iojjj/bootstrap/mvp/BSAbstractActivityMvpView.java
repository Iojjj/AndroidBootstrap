package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.support.annotation.Nullable;

/**
 * Implementation of {@link BSMvpView} for {@link Activity}.
 *
 * @param <P> type of presenter
 *
 * @since 1.0
 */
public abstract class BSAbstractActivityMvpView<P extends BSMvpPresenter> extends BSAbstractActivity
        implements BSMvpView<P> {

    @Nullable
    private P mPresenter;

    @Nullable
    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(@Nullable P presenter) {
        mPresenter = presenter;
    }
}

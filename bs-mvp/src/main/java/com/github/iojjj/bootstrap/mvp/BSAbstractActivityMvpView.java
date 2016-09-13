package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.support.annotation.Nullable;

/**
 * Implementation of {@link BSMvpView} for {@link Activity}.
 *
 * @param <TPresenter> type of presenter
 * @since 1.0
 */
public abstract class BSAbstractActivityMvpView<TPresenter extends BSMvpPresenter> extends BSAbstractActivity
        implements BSMvpView<TPresenter> {

    @Nullable
    private TPresenter mPresenter;

    @Nullable
    public TPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(@Nullable TPresenter presenter) {
        mPresenter = presenter;
    }
}

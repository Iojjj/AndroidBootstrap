package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Implementation of {@link BSMvpView} for {@link AppCompatActivity}.
 *
 * @param <TPresenter> type of presenter
 * @since 1.0
 */
public abstract class BSAbstractActivityMvpView<TPresenter extends BSMvpPresenter> extends BSAbstractActivity
        implements BSMvpView<TPresenter> {

    @Nullable
    private TPresenter mPresenter;

    @Nullable
    @Override
    public TPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(@Nullable TPresenter presenter) {
        mPresenter = presenter;
    }
}

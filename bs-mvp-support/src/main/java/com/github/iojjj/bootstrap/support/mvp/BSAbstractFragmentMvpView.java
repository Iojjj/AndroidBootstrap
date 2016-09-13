package com.github.iojjj.bootstrap.support.mvp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Implementation of {@link BSMvpView} for {@link Fragment}.
 *
 * @param <TPresenter>
 * @since 1.0
 */
public abstract class BSAbstractFragmentMvpView<TPresenter extends BSMvpPresenter> extends BSAbstractFragment implements
        BSMvpView<TPresenter> {

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

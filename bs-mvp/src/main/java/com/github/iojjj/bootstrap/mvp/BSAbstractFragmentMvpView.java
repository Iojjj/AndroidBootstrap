package com.github.iojjj.bootstrap.mvp;

import android.app.Fragment;
import android.support.annotation.Nullable;

/**
 * Implementation of {@link BSMvpView} for {@link Fragment}.
 *
 * @param <TPresenter>
 * @since 1.0
 */
public abstract class BSAbstractFragmentMvpView<TPresenter extends BSMvpPresenter> extends BSAbstractFragment
        implements BSMvpView<TPresenter> {

    @Nullable
    private TPresenter mPresenter;

    @Override
    public void setPresenter(@Nullable TPresenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    public TPresenter getPresenter() {
        return mPresenter;
    }
}

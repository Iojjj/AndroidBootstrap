package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Implementation of {@link BSMvpView} for {@link Fragment}.
 *
 * @param <P>
 *
 * @since 1.0
 */
public abstract class BSAbstractFragmentMvpView<P extends BSMvpPresenter> extends BSAbstractFragment implements
        BSMvpView<P> {

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

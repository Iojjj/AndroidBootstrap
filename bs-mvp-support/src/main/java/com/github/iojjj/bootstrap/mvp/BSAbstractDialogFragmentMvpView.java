package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Implementation of {@link BSMvpView} for {@link DialogFragment}.
 *
 * @param <P>
 * @since 1.0
 */
public abstract class BSAbstractDialogFragmentMvpView<P extends BSMvpPresenter> extends BSAbstractDialogFragment
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

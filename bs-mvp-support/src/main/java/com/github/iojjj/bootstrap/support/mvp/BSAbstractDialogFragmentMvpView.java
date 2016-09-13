package com.github.iojjj.bootstrap.support.mvp;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Implementation of {@link BSMvpView} for {@link DialogFragment}.
 *
 * @param <TPresenter>
 * @since 1.0
 */
public abstract class BSAbstractDialogFragmentMvpView<TPresenter extends BSMvpPresenter> extends BSAbstractDialogFragment
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

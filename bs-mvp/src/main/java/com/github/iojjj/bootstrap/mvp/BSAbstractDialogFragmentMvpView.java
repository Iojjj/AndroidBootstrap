package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.Nullable;
import android.app.DialogFragment;

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
    @Override
    public TPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(@Nullable TPresenter presenter) {
        mPresenter = presenter;
    }
}
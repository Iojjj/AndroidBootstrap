package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Abstract loader callbacks for MVP pattern.
 * <p>This class holds a link to {@link BSMvpView}. Every time {@link AndroidPresenterCallbacks#onResume()} called,
 * the view links to {@link BSMvpPresenter} and the presenter links to view. Every time
 * {@link AndroidPresenterCallbacks#onPause()} called, the view unlinks from the presenter and the presenter
 * unlinks from the view. So presenter is always linked to proper view.</p>
 *
 * @since 1.0
 */
abstract class AbstractPresenterLoaderCallbacks<TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
        implements AndroidPresenterCallbacks {

    @NonNull
    private final TView mView;
    @Nullable
    private TPresenter mPresenter;
    private boolean mResumed;

    AbstractPresenterLoaderCallbacks(@NonNull TView view) {
        mView = view;
    }

    @Override
    public void onResume() {
        mResumed = true;
        checkAndCallResume();
    }

    @Override
    public void onPause() {
        mResumed = false;
        TPresenter presenter = mPresenter;
        if (presenter != null) {
            final TView view = presenter.getView();
            if (view == mView) {
                mView.setPresenter(null);
            }
            presenter.setView(null);
            presenter.onPause();
        }
    }

    private void checkAndCallResume() {
        TPresenter presenter = mPresenter;
        if (presenter != null) {
            mView.setPresenter(presenter);
            presenter.setView(mView);
            presenter.onResume();
        }
    }

    void onLoadFinished(TPresenter data) {
        mPresenter = data;
        if (mResumed) {
            checkAndCallResume();
        }
    }

    void onLoaderReset() {
        TPresenter presenter = mPresenter;
        if (presenter != null) {
            presenter.setView(null);
        }
        mPresenter = null;
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        final TPresenter presenter = mPresenter;
        if (presenter != null) {
            presenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        TPresenter presenter = mPresenter;
        if (presenter != null) {
            presenter.onSaveInstanceState(outState);
        }
    }
}

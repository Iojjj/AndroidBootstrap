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
abstract class AbstractPresenterLoaderCallbacks<V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
        implements AndroidPresenterCallbacks {

    @NonNull
    private final V mView;
    @Nullable
    private P mPresenter;
    private boolean mResumed;

    AbstractPresenterLoaderCallbacks(@NonNull V view) {
        mView = view;
    }

    @Override
    public void onPause() {
        mResumed = false;
        P presenter = mPresenter;
        if (presenter != null) {
            final V view = presenter.getView();
            if (view == mView) {
                mView.setPresenter(null);
            }
            presenter.setView(null);
            presenter.onPause();
        }
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        final P presenter = mPresenter;
        if (presenter != null) {
            presenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        mResumed = true;
        checkAndCallResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        P presenter = mPresenter;
        if (presenter != null) {
            presenter.onSaveInstanceState(outState);
        }
    }

    void onLoadFinished(P data) {
        mPresenter = data;
        if (mResumed) {
            checkAndCallResume();
        }
    }

    void onLoaderReset() {
        P presenter = mPresenter;
        if (presenter != null) {
            presenter.setView(null);
        }
        mPresenter = null;
    }

    private void checkAndCallResume() {
        P presenter = mPresenter;
        if (presenter != null) {
            mView.setPresenter(presenter);
            presenter.setView(mView);
            presenter.onResume();
        }
    }
}

package com.github.iojjj.bootstrap.support.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link LoaderCallbacks} for MVP pattern.
 * <p>This class holds a link to {@link BSMvpView}. Every time {@link AndroidLifecycle#onResume()} called,
 * the view links to {@link BSMvpPresenter} and the presenter links to view. Every time
 * {@link AndroidLifecycle#onPause()} called, the view unlinks from the presenter and the presenter
 * unlinks from the view. So presenter is always linked to proper view.</p>
 *
 * @since 1.0
 */
class PresenterLoaderCallbacks<TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
        implements LoaderManager.LoaderCallbacks<TPresenter>, AndroidLifecycle {

    @NonNull
    private final Context mContext;
    @NonNull
    private final BSFunction0<TPresenter> mPresenterCallable;
    @NonNull
    private final TView mView;
    @Nullable
    private TPresenter mPresenter;
    private boolean mResumed;

    private PresenterLoaderCallbacks(@NonNull Context context, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterCallable) {
        mContext = context.getApplicationContext();
        mView = view;
        mPresenterCallable = presenterCallable;
    }

    static <TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>> PresenterLoaderCallbacks<TPresenter, TView>
    create(@NonNull Context context, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterCallable) {
        return new PresenterLoaderCallbacks<>(context, view, presenterCallable);
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

    @Override
    public Loader<TPresenter> onCreateLoader(int id, Bundle args) {
        return new AbstractPresenterLoader<>(mContext, mPresenterCallable);
    }

    @Override
    public void onLoadFinished(Loader<TPresenter> loader, TPresenter data) {
        mPresenter = data;
        if (mResumed) {
            checkAndCallResume();
        }
    }

    @Override
    public void onLoaderReset(Loader<TPresenter> loader) {
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

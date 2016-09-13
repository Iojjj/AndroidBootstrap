package com.github.iojjj.bootstrap.support.mvp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import com.github.iojjj.bootstrap.core.BSAssertions;
import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link Loader} that loads and stores {@link BSMvpPresenter} instance.
 * @param <TPresenter> type of presenter
 */
final class AbstractPresenterLoader<TPresenter extends BSMvpPresenter> extends Loader<TPresenter> {

    private TPresenter mPresenter;
    private final BSFunction0<TPresenter> mPresenterProvider;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     * @param presenterProvider provider of presenter instances
     */
    AbstractPresenterLoader(@NonNull Context context, @NonNull BSFunction0<TPresenter> presenterProvider) {
        super(context);
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        mPresenterProvider = presenterProvider;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged() || mPresenter == null) {
            forceLoad();
            return;
        }
        deliverResult(mPresenter);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cancelLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cancelLoad();
        }
        mPresenter = mPresenterProvider.apply();
        mPresenter.onCreated();
        deliverResult(mPresenter);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mPresenter.onDestroyed();
        mPresenter = null;
    }
}

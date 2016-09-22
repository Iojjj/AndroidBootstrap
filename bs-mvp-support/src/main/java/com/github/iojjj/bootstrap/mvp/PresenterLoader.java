package com.github.iojjj.bootstrap.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link Loader} that loads and stores {@link BSMvpPresenter} instance.
 * @param <TPresenter> type of presenter
 */
final class PresenterLoader<TPresenter extends BSMvpPresenter> extends Loader<TPresenter>
        implements LoaderDelegate<TPresenter> {

    private final PresenterLoaderDelegate<TPresenter> mPresenterLoaderDelegate;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *  @param context used to retrieve the application context.
     * @param presenterProvider provider of presenter instances
     */
    PresenterLoader(@NonNull Context context, @NonNull BSFunction0<TPresenter> presenterProvider) {
        super(context);
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        mPresenterLoaderDelegate = new PresenterLoaderDelegate<>(presenterProvider, this);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mPresenterLoaderDelegate.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        mPresenterLoaderDelegate.onStopLoading();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mPresenterLoaderDelegate.onForceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        mPresenterLoaderDelegate.onReset();
    }

    @Override
    public void cancelLoadImpl() {
        cancelLoad();
    }
}

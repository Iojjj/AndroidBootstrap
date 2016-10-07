package com.github.iojjj.bootstrap.mvp;

import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link Loader} that loads and stores {@link BSMvpPresenter} instance.
 *
 * @param <P> type of presenter
 */
final class PresenterLoader<P extends BSMvpPresenter> extends Loader<P>
        implements LoaderDelegate<P> {

    private final PresenterLoaderDelegate<P> mPresenterLoaderDelegate;

    PresenterLoader(@NonNull Context context, @NonNull BSFunction0<P> presenterProvider) {
        super(context);
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        mPresenterLoaderDelegate = new PresenterLoaderDelegate<>(presenterProvider, this);
    }

    @Override
    public void cancelLoadImpl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cancelLoad();
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mPresenterLoaderDelegate.onStartLoading();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mPresenterLoaderDelegate.onForceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        mPresenterLoaderDelegate.onStopLoading();
    }

    @Override
    protected void onReset() {
        super.onReset();
        mPresenterLoaderDelegate.onReset();
    }
}
package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Shared presenter loader delegate.
 *
 * @param <P> type of presenter
 * @since 1.0
 */
class PresenterLoaderDelegate<P extends BSMvpPresenter> {

    private final BSFunction0<P> mPresenterProvider;
    private final LoaderDelegate<P> mLoaderDelegate;
    private P mPresenter;

    PresenterLoaderDelegate(@NonNull BSFunction0<P> presenterProvider,
                            @NonNull LoaderDelegate<P> loaderDelegate) {
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        BSAssertions.assertNotNull(loaderDelegate, "loaderDelegate");
        mPresenterProvider = presenterProvider;
        mLoaderDelegate = loaderDelegate;
    }

    void onStartLoading() {
        if (mLoaderDelegate.takeContentChanged() || mPresenter == null) {
            mLoaderDelegate.forceLoad();
            return;
        }
        mLoaderDelegate.deliverResult(mPresenter);
    }

    void onStopLoading() {
        mLoaderDelegate.cancelLoadImpl();
    }

    void onForceLoad() {
        mLoaderDelegate.cancelLoadImpl();
        mPresenter = mPresenterProvider.apply();
        mPresenter.onCreated();
        mLoaderDelegate.deliverResult(mPresenter);
    }

    void onReset() {
        onStopLoading();
        mPresenter.onDestroyed();
        mPresenter = null;
    }
}

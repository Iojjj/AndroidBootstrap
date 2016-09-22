package com.github.iojjj.bootstrap.mvp;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

// TODO: 21.09.2016 documentation
class PresenterLoaderDelegate<P extends BSMvpPresenter> {

    private P mPresenter;
    private final BSFunction0<P> mPresenterProvider;
    private final LoaderDelegate<P> mLoaderDelegate;

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

package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

public class BSUiActivityDelegate implements BSMvpDelegateImpl.UIDelegate {

    @NonNull
    private final Activity mActivity;

    public BSUiActivityDelegate(@NonNull Activity activity) {
        BSAssertions.assertNotNull(activity, "activity");
        mActivity = activity;
    }

    @Override
    public <V extends BSMvpView<P>, P extends BSMvpPresenter<V>> AndroidPresenterCallbacks
    initLoader(int loaderId, V view, BSFunction0<P> presenterProvider) {
        final PresenterLoaderCallbacks<P, V> loaderCallbacks =
                PresenterLoaderCallbacks.create(mActivity, view, presenterProvider);
        mActivity.getLoaderManager().initLoader(loaderId, null, loaderCallbacks);
        return loaderCallbacks;
    }
}

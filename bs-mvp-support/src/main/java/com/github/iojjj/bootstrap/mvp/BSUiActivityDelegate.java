package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

public class BSUiActivityDelegate implements BSMvpDelegateImpl.UIDelegate {

    @NonNull
    private final AppCompatActivity mActivity;

    public BSUiActivityDelegate(@NonNull AppCompatActivity activity) {
        BSAssertions.assertNotNull(activity, "activity");
        mActivity = activity;
    }

    @Override
    public <V extends BSMvpView<P>, P extends BSMvpPresenter<V>> AndroidPresenterCallbacks
    initLoader(int loaderId, @Nullable Bundle args, V view, BSFunction0<P> presenterProvider) {
        final PresenterLoaderCallbacks<V, P> loaderCallbacks =
                PresenterLoaderCallbacks.create(mActivity, view, presenterProvider);
        mActivity.getSupportLoaderManager().initLoader(loaderId, args, loaderCallbacks);
        return loaderCallbacks;
    }
}

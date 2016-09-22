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
    public <TView extends BSMvpView<TPresenter>, TPresenter extends BSMvpPresenter<TView>> AndroidPresenterCallbacks
    initLoader(int loaderId, @Nullable Bundle args, TView view, BSFunction0<TPresenter> presenterProvider) {
        final PresenterLoaderCallbacks<TPresenter, TView> loaderCallbacks =
                PresenterLoaderCallbacks.create(mActivity, view, presenterProvider);
        mActivity.getSupportLoaderManager().initLoader(loaderId, args, loaderCallbacks);
        return loaderCallbacks;
    }
}

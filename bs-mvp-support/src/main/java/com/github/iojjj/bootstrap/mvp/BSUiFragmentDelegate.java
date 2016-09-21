package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.function.BSFunction0;

public class BSUiFragmentDelegate implements BSMvpDelegateImpl.UIDelegate {

    @NonNull
    private final Fragment mFragment;

    public BSUiFragmentDelegate(@NonNull Fragment fragment) {
        BSAssertions.assertNotNull(fragment, "fragment");
        mFragment = fragment;
    }

    @Override
    public <TView extends BSMvpView<TPresenter>, TPresenter extends BSMvpPresenter<TView>> AndroidPresenterCallbacks
    initLoader(int loaderId, @Nullable Bundle args, TView view, BSFunction0<TPresenter> presenterProvider) {
        final PresenterLoaderCallbacks<TPresenter, TView> loaderCallbacks =
                PresenterLoaderCallbacks.create(mFragment.getContext(), view, presenterProvider);
        mFragment.getLoaderManager().initLoader(loaderId, args, loaderCallbacks);
        return loaderCallbacks;
    }
}

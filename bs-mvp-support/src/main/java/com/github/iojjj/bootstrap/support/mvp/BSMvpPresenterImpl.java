package com.github.iojjj.bootstrap.support.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Implementation of {@link BSMvpPresenter}.
 *
 * @param <TView> type of view.
 * @since 1.0
 */
public class BSMvpPresenterImpl<TView extends BSMvpView> implements BSMvpPresenter<TView> {

    @Nullable
    private TView mView;

    @Nullable
    @Override
    public TView getView() {
        return mView;
    }

    @Override
    public void setView(@Nullable TView view) {
        mView = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onCreated() {

    }

    @Override
    public void onDestroyed() {

    }
}

package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Implementation of {@link BSMvpPresenter}.
 *
 * @param <V> type of view.
 *
 * @since 1.0
 */
public abstract class BSMvpPresenterImpl<V extends BSMvpView> implements BSMvpPresenter<V> {

    @Nullable
    private V mView;

    @Nullable
    @Override
    public V getView() {
        return mView;
    }

    @Override
    public void setView(@Nullable V view) {
        mView = view;
    }

    @Override
    public void onCreated() {

    }

    @Override
    public void onDestroyed() {

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
}

package com.github.iojjj.bootstrap.mvp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link Fragment} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractFragment extends Fragment implements BSMvpDelegate {

    private final BSMvpDelegate mMvpDelegate;

    protected BSAbstractFragment() {
        mMvpDelegate = new BSMvpDelegateImpl(new BSUiFragmentDelegate(this));
    }

    @Override
    public <V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
    void initPresenter(int loaderId, @NonNull V view, @NonNull BSFunction0<P> presenterProvider) {
        mMvpDelegate.initPresenter(loaderId, view, presenterProvider);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMvpDelegate.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMvpDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMvpDelegate.onPause();
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        mMvpDelegate.onRestoreInstanceState(savedInstanceState);
    }
}

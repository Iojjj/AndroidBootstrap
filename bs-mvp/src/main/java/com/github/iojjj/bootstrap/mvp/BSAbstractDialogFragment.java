package com.github.iojjj.bootstrap.mvp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link DialogFragment} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractDialogFragment extends DialogFragment implements BSMvpDelegate {

    private final BSMvpDelegate mMvpDelegate;

    protected BSAbstractDialogFragment() {
        mMvpDelegate = new BSMvpDelegateImpl(new BSUiFragmentDelegate(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMvpDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMvpDelegate.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMvpDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        mMvpDelegate.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public <V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
    void initPresenter(int loaderId, @NonNull V view, @NonNull BSFunction0<P> presenterProvider) {
        mMvpDelegate.initPresenter(loaderId, view, presenterProvider);
    }
}

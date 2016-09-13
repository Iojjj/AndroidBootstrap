package com.github.iojjj.bootstrap.support.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link DialogFragment} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractDialogFragment extends DialogFragment implements BSMvpFollower {

    private final BSMvpFollower mMvpFollower;

    protected BSAbstractDialogFragment() {
        mMvpFollower = new BSMvpFollowerImpl(this);
    }

    @Override
    public <TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
    void initPresenter(int loaderId, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterProvider) {
        mMvpFollower.initPresenter(loaderId, view, presenterProvider);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMvpFollower.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMvpFollower.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMvpFollower.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        mMvpFollower.onRestoreInstanceState(savedInstanceState);
    }
}

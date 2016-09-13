package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link Activity} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractActivity extends Activity implements BSMvpFollower {

    private final BSMvpFollower mMvpFollower;

    protected BSAbstractActivity() {
        mMvpFollower = new BSMvpFollowerImpl(this);
    }

    @Override
    public <TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>> void
    initPresenter(int loaderId, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterProvider) {
        mMvpFollower.initPresenter(loaderId, view, presenterProvider);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMvpFollower.onRestoreInstanceState(savedInstanceState);
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
}

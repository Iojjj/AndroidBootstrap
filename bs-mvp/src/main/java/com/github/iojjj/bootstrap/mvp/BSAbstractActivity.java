package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link Activity} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractActivity extends Activity implements BSMvpDelegate {

    private final BSMvpDelegate mMvpDelegate;

    protected BSAbstractActivity() {
        mMvpDelegate = new BSMvpDelegateImpl(new BSUiActivityDelegate(this));
    }

    @Override
    public <V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
    void initPresenter(int loaderId, @NonNull V view, @NonNull BSFunction0<P> presenterProvider) {
        mMvpDelegate.initPresenter(loaderId, view, presenterProvider);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMvpDelegate.onRestoreInstanceState(savedInstanceState);
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
}

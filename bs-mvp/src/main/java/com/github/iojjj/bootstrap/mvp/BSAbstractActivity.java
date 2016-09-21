package com.github.iojjj.bootstrap.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link Activity} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractActivity extends Activity implements BSMvpDelegate {

    private final BSMvpDelegate mMvpDelegate;

    protected BSAbstractActivity() {
        mMvpDelegate = new BSMvpDelegateImpl(new BSMvpDelegateImpl.UIDelegate() {
            @Override
            public <TView extends BSMvpView<TPresenter>, TPresenter extends BSMvpPresenter<TView>> AndroidPresenterCallbacks
            initLoader(int loaderId, @Nullable Bundle args, TView view, BSFunction0<TPresenter> presenterProvider) {
                final PresenterLoaderCallbacks<TPresenter, TView> loaderCallbacks =
                        PresenterLoaderCallbacks.create(BSAbstractActivity.this, view, presenterProvider);
                getLoaderManager().initLoader(loaderId, args, loaderCallbacks);
                return loaderCallbacks;
            }
        });
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
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMvpDelegate.onRestoreInstanceState(savedInstanceState);
    }
}

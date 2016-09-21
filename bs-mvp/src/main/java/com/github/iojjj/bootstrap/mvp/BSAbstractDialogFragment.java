package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;

import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link DialogFragment} with MVP pattern.
 *
 * @since 1.0
 */
public abstract class BSAbstractDialogFragment extends DialogFragment implements BSMvpDelegate {

    private final BSMvpDelegate mMvpDelegate;

    protected BSAbstractDialogFragment() {
        mMvpDelegate = new BSMvpDelegateImpl(new BSMvpDelegateImpl.UIDelegate() {
            @Override
            public <TView extends BSMvpView<TPresenter>, TPresenter extends BSMvpPresenter<TView>> AndroidPresenterCallbacks
            initLoader(int loaderId, @Nullable Bundle args, TView view, BSFunction0<TPresenter> presenterProvider) {
                final PresenterLoaderCallbacks<TPresenter, TView> loaderCallbacks =
                        PresenterLoaderCallbacks.create(getActivity(), view, presenterProvider);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        mMvpDelegate.onRestoreInstanceState(savedInstanceState);
    }
}

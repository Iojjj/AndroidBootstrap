package com.github.iojjj.bootstrap.support.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.github.iojjj.bootstrap.core.BSAssertions;
import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link BSMvpFollower}.
 *
 * @since 1.0
 */
public final class BSMvpFollowerImpl implements BSMvpFollower {

    private final SparseArray<PresenterLoaderCallbacks<?, ?>> mPresenterLoaderCallbacks = new SparseArray<>();
    private final AppCompatActivity mActivity;
    private final Fragment mFragment;

    public BSMvpFollowerImpl(@NonNull AppCompatActivity activity) {
        BSAssertions.assertNotNull(activity, "activity");
        mActivity = activity;
        mFragment = null;
    }

    public BSMvpFollowerImpl(@NonNull Fragment fragment) {
        BSAssertions.assertNotNull(fragment, "fragment");
        mFragment = fragment;
        mActivity = null;
    }

    @Override
    public <TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
    void initPresenter(int loaderId, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterProvider) {
        BSAssertions.assertNotNull(view, "view");
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        final PresenterLoaderCallbacks<TPresenter, TView> loaderCallbacks;
        if (mActivity != null) {
            loaderCallbacks = PresenterLoaderCallbacks.create(mActivity, view, presenterProvider);
            mActivity.getSupportLoaderManager().initLoader(loaderId, null, loaderCallbacks);
        } else if (mFragment != null) {
            loaderCallbacks = PresenterLoaderCallbacks.create(mFragment.getActivity(), view, presenterProvider);
            mFragment.getLoaderManager().initLoader(loaderId, null, loaderCallbacks);
        } else {
            throw new IllegalArgumentException("Both activity and fragment are null.");
        }
        mPresenterLoaderCallbacks.put(loaderId, loaderCallbacks);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final PresenterLoaderCallbacks<?, ?> presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final PresenterLoaderCallbacks<?, ?> presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            if (mActivity != null) {
                presenterLoaderHandler.onResume();
            } else if (mFragment != null) {
                presenterLoaderHandler.onResume();
            } else {
                throw new IllegalArgumentException("Both activity and fragment are null.");
            }
        }
    }

    @Override
    public void onPause() {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final PresenterLoaderCallbacks<?, ?> presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final PresenterLoaderCallbacks<?, ?> presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onSaveInstanceState(outState);
        }
    }
}

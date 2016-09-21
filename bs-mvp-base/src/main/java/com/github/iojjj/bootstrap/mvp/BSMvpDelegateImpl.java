package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link BSMvpDelegate}.
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
public final class BSMvpDelegateImpl implements BSMvpDelegate {

    private final SparseArray<AndroidPresenterCallbacks> mPresenterLoaderCallbacks = new SparseArray<>();
    @NonNull
    private final UIDelegate mUIDelegate;

    public BSMvpDelegateImpl(@NonNull UIDelegate uiDelegate) {
        BSAssertions.assertNotNull(uiDelegate, "uiDelegate");
        mUIDelegate = uiDelegate;
    }

    @Override
    public <TView extends BSMvpView<TPresenter>, TPresenter extends BSMvpPresenter<TView>>
    void initPresenter(int loaderId, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterProvider) {
        BSAssertions.assertNotNull(view, "view");
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        AndroidPresenterCallbacks loaderCallbacks = mUIDelegate.initLoader(loaderId, null, view, presenterProvider);
        mPresenterLoaderCallbacks.put(loaderId, loaderCallbacks);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AndroidPresenterCallbacks presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AndroidPresenterCallbacks presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onResume();
        }
    }

    @Override
    public void onPause() {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AndroidPresenterCallbacks presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AndroidPresenterCallbacks presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onSaveInstanceState(outState);
        }
    }

    public interface UIDelegate {

        <TView extends BSMvpView<TPresenter>, TPresenter extends BSMvpPresenter<TView>>
        AndroidPresenterCallbacks initLoader(int loaderId, @Nullable Bundle args, TView view, BSFunction0<TPresenter> presenterProvider);
    }
}

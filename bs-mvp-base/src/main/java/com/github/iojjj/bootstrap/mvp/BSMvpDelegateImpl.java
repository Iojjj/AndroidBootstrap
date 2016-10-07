package com.github.iojjj.bootstrap.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link BSMvpDelegate}.
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
public final class BSMvpDelegateImpl implements BSMvpDelegate {

    private static final String KEY_LOADER_IDS = "LOADER_IDS";
    private final SparseIntArray mLoaderIds = new SparseIntArray();
    private final SparseArray<AndroidPresenterCallbacks> mPresenterLoaderCallbacks = new SparseArray<>();
    @NonNull
    private final UIDelegate mUIDelegate;

    public BSMvpDelegateImpl(@NonNull UIDelegate uiDelegate) {
        BSAssertions.assertNotNull(uiDelegate, "uiDelegate");
        mUIDelegate = uiDelegate;
    }

    @Override
    public <V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
    void initPresenter(int loaderId, @NonNull V view, @NonNull BSFunction0<P> presenterProvider) {
        BSAssertions.assertNotNull(view, "view");
        BSAssertions.assertNotNull(presenterProvider, "presenterProvider");
        AndroidPresenterCallbacks loaderCallbacks = mUIDelegate.initLoader(loaderId, view, presenterProvider);
        mPresenterLoaderCallbacks.put(loaderId, loaderCallbacks);
        mLoaderIds.put(loaderId, loaderId);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        final int size = mPresenterLoaderCallbacks.size();
        for (int i = 0; i < size; i++) {
            final AndroidPresenterCallbacks presenterLoaderHandler = mPresenterLoaderCallbacks.valueAt(i);
            presenterLoaderHandler.onSaveInstanceState(outState);
        }
        if (mLoaderIds.size() > 0) {
            final int[] loaderIds = new int[mLoaderIds.size()];
            for (int i = 0; i < mLoaderIds.size(); i++) {
                loaderIds[i] = mLoaderIds.valueAt(i);
            }
            outState.putIntArray(KEY_LOADER_IDS, loaderIds);
        }
    }

    /**
     * UI delegate that allows to communicate with Android system.
     */
    public interface UIDelegate {

        /**
         * Initialize a new loader.
         *
         * @param <V>               type of BSMvpView
         * @param <P>               type of BSMvpPresenter
         * @param loaderId          ID of loader
         * @param view              instance of BSMvpView
         * @param presenterProvider instance of presenter provider
         * @return instance of AndroidPresenterCallbacks
         */
        <V extends BSMvpView<P>, P extends BSMvpPresenter<V>>
        AndroidPresenterCallbacks initLoader(int loaderId, V view, BSFunction0<P> presenterProvider);
    }
}

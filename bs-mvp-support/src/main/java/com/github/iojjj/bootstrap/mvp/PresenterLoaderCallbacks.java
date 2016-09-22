package com.github.iojjj.bootstrap.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

/**
 * Implementation of {@link LoaderCallbacks} for MVP pattern.
 * <p>This class holds a link to {@link BSMvpView}. Every time {@link AndroidPresenterCallbacks#onResume()} called,
 * the view links to {@link BSMvpPresenter} and the presenter links to view. Every time
 * {@link AndroidPresenterCallbacks#onPause()} called, the view unlinks from the presenter and the presenter
 * unlinks from the view. So presenter is always linked to proper view.</p>
 *
 * @since 1.0
 */
class PresenterLoaderCallbacks<P extends BSMvpPresenter<V>, V extends BSMvpView<P>>
        extends AbstractPresenterLoaderCallbacks<P, V>
        implements LoaderCallbacks<P> {

    @NonNull
    private final Context mContext;
    @NonNull
    private final BSFunction0<P> mPresenterCallable;

    private PresenterLoaderCallbacks(@NonNull Context context, @NonNull V view,
                                     @NonNull BSFunction0<P> presenterCallable) {
        super(view);
        BSAssertions.assertNotNull(context, "context");
        BSAssertions.assertNotNull(presenterCallable, "presenterCallable");
        mContext = context.getApplicationContext();
        mPresenterCallable = presenterCallable;
    }

    static <P extends BSMvpPresenter<V>, V extends BSMvpView<P>> PresenterLoaderCallbacks<P, V>
    create(@NonNull Context context, @NonNull V view, @NonNull BSFunction0<P> presenterCallable) {
        return new PresenterLoaderCallbacks<>(context, view, presenterCallable);
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(mContext, mPresenterCallable);
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        onLoaderReset();
    }
}

package com.github.iojjj.bootstrap.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.github.iojjj.bootstrap.core.BSAssertions;
import com.github.iojjj.bootstrap.core.function.BSFunction0;

/**
 * Implementation of {@link LoaderCallbacks} for MVP pattern.
 * <p>This class holds a link to {@link BSMvpView}. Every time {@link AndroidPresenterCallbacks#onResume()} called,
 * the view links to {@link BSMvpPresenter} and the presenter links to view. Every time
 * {@link AndroidPresenterCallbacks#onPause()} called, the view unlinks from the presenter and the presenter
 * unlinks from the view. So presenter is always linked to proper view.</p>
 *
 * @since 1.0
 */
class PresenterLoaderCallbacks<TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
        extends AbstractPresenterLoaderCallbacks<TPresenter, TView>
        implements LoaderCallbacks<TPresenter> {

    @NonNull
    private final Context mContext;
    @NonNull
    private final BSFunction0<TPresenter> mPresenterCallable;

    private PresenterLoaderCallbacks(@NonNull Context context, @NonNull TView view,
                                     @NonNull BSFunction0<TPresenter> presenterCallable) {
        super(view);
        BSAssertions.assertNotNull(context, "context");
        BSAssertions.assertNotNull(presenterCallable, "presenterCallable");
        mContext = context.getApplicationContext();
        mPresenterCallable = presenterCallable;
    }

    static <TPresenter extends BSMvpPresenter<TView>, TView extends BSMvpView<TPresenter>>
    PresenterLoaderCallbacks<TPresenter, TView>
    create(@NonNull Context context, @NonNull TView view, @NonNull BSFunction0<TPresenter> presenterCallable) {
        return new PresenterLoaderCallbacks<>(context, view, presenterCallable);
    }

    @Override
    public Loader<TPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(mContext, mPresenterCallable);
    }

    @Override
    public void onLoadFinished(Loader<TPresenter> loader, TPresenter data) {
        onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<TPresenter> loader) {
        onLoaderReset();
    }
}

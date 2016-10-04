package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * AdapterBuilder class for {@link BSArrayAdapterImpl}.
 *
 * @param <T> type of items
 */
abstract class AdapterBuilder<T, B extends AdapterBuilder<T, ?>> {

    private BSAdapterRenderer<T, ? extends BSViewHolder> mRenderer;
    private BSFilterPredicate<T> mFilterPredicate;

    /**
     * Set renderer for items.
     *
     * @param renderer instance of Renderer
     */
    public <TR extends BSAdapterRenderer<T, ? extends BSViewHolder>> B withRenderer(@NonNull TR renderer) {
        mRenderer = renderer;
        return getThis();
    }

    /**
     * Set filter predicate.
     *
     * @param filterPredicate instance of BSFilterPredicate
     */
    public B withFilterPredicate(@Nullable BSFilterPredicate<T> filterPredicate) {
        mFilterPredicate = filterPredicate;
        return getThis();
    }

    BSAdapterRenderer<T, ? extends BSViewHolder> getRenderer() {
        return mRenderer;
    }

    BSFilterPredicate<T> getFilterPredicate() {
        return mFilterPredicate;
    }

    protected abstract B getThis();
}

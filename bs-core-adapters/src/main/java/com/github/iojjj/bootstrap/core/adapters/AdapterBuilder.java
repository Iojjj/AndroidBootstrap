package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * AdapterBuilder class for {@link BSArrayAdapterImpl}.
 *
 * @param <T> type of items
 *
 * @since 1.0
 */
abstract class AdapterBuilder<T, B extends AdapterBuilder<T, ?>> {

    private BSFilterPredicate<T> mFilterPredicate;
    private BSAdapterRenderer<T, ? extends BSViewHolder> mRenderer;

    /**
     * Set filter predicate.
     *
     * @param filterPredicate instance of BSFilterPredicate
     */
    public B withFilterPredicate(@Nullable BSFilterPredicate<T> filterPredicate) {
        mFilterPredicate = filterPredicate;
        return getThis();
    }

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
     * Get a filter predicate.
     *
     * @return filter predicate or null
     */
    @Nullable
    BSFilterPredicate<T> getFilterPredicate() {
        return mFilterPredicate;
    }

    /**
     * Get a renderer.
     *
     * @return renderer
     */
    @NonNull
    BSAdapterRenderer<T, ? extends BSViewHolder> getRenderer() {
        return mRenderer;
    }

    /**
     * Get <code>this</code> of a builder.
     *
     * @return <code>this</code> of a builder
     */
    protected abstract B getThis();
}

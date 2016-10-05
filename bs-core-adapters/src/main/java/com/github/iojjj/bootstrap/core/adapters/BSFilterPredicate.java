package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Filtering predicate.
 *
 * @since 1.0
 */
@FunctionalInterface
public interface BSFilterPredicate<T> {

    /**
     * Checks if item applicable with filtering predicate.
     *
     * @param query some non-null query
     * @param item  some item
     *
     * @return true if item applicable with filtering predicate, false otherwise
     */
    boolean apply(@NonNull String query, @Nullable T item);
}

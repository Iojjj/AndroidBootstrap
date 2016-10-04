package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;

import java.util.Collection;

interface Adapter<T> {

    /**
     * Set filter predicate.
     *
     * @param predicate some filter predicate or null
     */
    void setFilterPredicate(@Nullable BSFilterPredicate<T> predicate);

    long getItemId(int position);

    int getItemViewType(int position);

    /**
     * Filter list of items.
     *
     * @param charSequence filter query. Pass <code>null</code> to reset filter.
     */
    void filter(@Nullable CharSequence charSequence);

    T getItem(int position);

    /**
     * Add item to adapter.
     *
     * @param item any item
     * @return true if adapter's collection was modified, false otherwise
     */
    boolean addItem(T item);

    /**
     * Add items to adapter.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
    boolean addItems(@NonNull Collection<T> items);

    /**
     * Add items to adapter.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
    boolean addItems(@NonNull T[] items);

    /**
     * Remove item at specific position.
     *
     * @param position position between <code>0</code> and <code>{@link #getCount()} - 1</code>
     * @return removed item
     */
    T removeItemAt(int position);

    /**
     * Remove item.
     *
     * @param item some item
     * @return true if adapter's collection was modified, false otherwise
     */
    boolean removeItem(T item);

    /**
     * Remove all items.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
    boolean removeAll(@NonNull Collection<T> items);

    /**
     * Remove all items that are not contained in the specified collection.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
    boolean retainAll(@NonNull Collection<T> items);

    /**
     * Clear all items.
     */
    void clear();


    /**
     * Check if adapter is filtered.
     *
     * @return true if adapter is filtered, false otherwise
     */
    boolean isFiltered();

    /**
     * Highlight filter query in specified char sequence with specified color.
     *
     * @param charSequence   any non-null char sequence
     * @param highlightColor highlight color
     * @return new Spannable object with highlighted filter query
     */
    Spannable highlightFilterQuery(@NonNull CharSequence charSequence, @ColorInt int highlightColor);

    /**
     * Get item's position in adapter.
     *
     * @param item some item
     * @return position of item in adapter
     */
    int getItemPosition(T item);
}

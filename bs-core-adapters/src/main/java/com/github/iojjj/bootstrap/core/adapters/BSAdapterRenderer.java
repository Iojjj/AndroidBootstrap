package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;

import java.util.List;

/**
 * Renderer for items stored in adapter.
 *
 * @param <T>  type of items
 * @param <VH> type of ViewHolder
 *
 * @since 1.0
 */
public abstract class BSAdapterRenderer<T, VH extends BSViewHolder> implements Renderer<T, VH> {

    private AdapterImpl mAdapter;

    @Override
    public void bind(@NonNull VH viewHolder, T item, int position) {
        /* no-op */
    }

    @Override
    public void bind(@NonNull VH viewHolder, T item, int position, @NonNull List<Object> payloads) {
        bind(viewHolder, item, position);
    }

    @Override
    public int getItemViewType(T item, int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * Highlight filter query in specified char sequence with specified color.
     *
     * @param charSequence   any non-null char sequence
     * @param highlightColor highlight color
     *
     * @return new Spannable object with highlighted filter query
     */
    public Spannable highlightFilterQuery(@NonNull CharSequence charSequence, @ColorInt int highlightColor) {
        return mAdapter.highlightFilterQuery(charSequence, highlightColor);
    }

    /**
     * Check if adapter is filtered.
     *
     * @return true if adapter is filtered, false otherwise
     */
    public boolean isFiltered() {
        return mAdapter.isFiltered();
    }

    void setAdapter(@NonNull AdapterImpl adapter) {
        mAdapter = adapter;
    }
}

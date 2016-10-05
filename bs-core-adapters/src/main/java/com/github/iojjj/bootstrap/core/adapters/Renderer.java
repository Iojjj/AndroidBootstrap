package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

/**
 * Renderer interface.
 *
 * @param <T>  type of item
 * @param <VH> type of ViewHolder
 *
 * @since 1.0
 */
interface Renderer<T, VH extends BSViewHolder> {

    /**
     * Bind an item to the instance of ViewHolder.
     *
     * @param viewHolder instance of BSViewHolder
     * @param item       item itself
     * @param position   position of item
     */
    void bind(@NonNull VH viewHolder, T item, int position);

    /**
     * Partially bind an item to the instance of ViewHolder. This method will be called only for
     * RecyclerView's adapter.
     *
     * @param viewHolder instance of BSViewHolder
     * @param item       item itself
     * @param position   position of item
     * @param payloads   list of payloads
     */
    void bind(@NonNull VH viewHolder, T item, int position, @NonNull List<Object> payloads);

    /**
     * Create a new ViewHolder instance.
     *
     * @param parent       parent view
     * @param itemViewType type of view
     * @param isDropDown   flag indicates that this view is a drop down view. It will be equals to
     *                     <code>false</code> always for RecyclerView's adapters.
     *
     * @return new instance of BSViewHolder
     */
    VH createViewHolder(ViewGroup parent, int itemViewType, boolean isDropDown);

    /**
     * Get a type of view.
     *
     * @param item     item itself
     * @param position position of item
     *
     * @return type of view
     */
    int getItemViewType(T item, int position);

    /**
     * Get a number of view types. This method will be called only for old-fashioned adapters like
     * {@link android.widget.BaseAdapter}.
     *
     * @return number of view types.
     */
    int getViewTypeCount();
}

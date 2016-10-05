package com.github.iojjj.bootstrap.core.adapters;

/**
 * Interface of a RecyclerView adapter's callbacks.
 *
 * @since 1.0
 */
interface RecyclerViewAdapterCallbacks {

    /**
     * Notify that data set has been changed.
     */
    void notifyDataSetChanged();

    /**
     * Notify that an item has been inserted at specified position.
     *
     * @param position position of item
     */
    void notifyItemInserted(int position);

    /**
     * Notify that a range of items has been inserted at specified position.
     *
     * @param position position of insertion
     * @param count    number of inserted items
     */
    void notifyItemRangeInserted(int position, int count);

    /**
     * Notify that a range of items has been removed at specified position.
     *
     * @param position position of removing
     * @param count    number of removed items
     */
    void notifyItemRangeRemoved(int position, int count);

    /**
     * Notify that an item has been removed at specified position.
     *
     * @param position position of item
     */
    void notifyItemRemoved(int position);
}

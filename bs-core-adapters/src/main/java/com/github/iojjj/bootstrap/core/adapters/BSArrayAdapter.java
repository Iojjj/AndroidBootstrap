package com.github.iojjj.bootstrap.core.adapters;

import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * Array adapter interface.
 *
 * @param <T> type of items
 *
 * @since 1.0
 */
public interface BSArrayAdapter<T> extends Adapter<T>, ListAdapter, SpinnerAdapter {

    /**
     * Set whenever to notify BSArrayAdapter about changes in adapter in any state. Default values is true.
     *
     * @param notifyDataSetChanges true or false
     */
    void setAutoNotifyDataSetChanges(boolean notifyDataSetChanges);
}

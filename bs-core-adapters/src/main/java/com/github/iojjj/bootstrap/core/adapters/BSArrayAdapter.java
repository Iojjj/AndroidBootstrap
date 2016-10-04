package com.github.iojjj.bootstrap.core.adapters;

import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

public interface BSArrayAdapter<T> extends Adapter<T>, ListAdapter, SpinnerAdapter {

    int getCount();

    int getViewTypeCount();

    /**
     * Set whenever to notify RecyclerView about changes in adapter in any state. Global switch. Default values is true.
     *
     * @param notifyDataSetChanges true or false
     */
    void setAutoNotifyDataSetChanges(boolean notifyDataSetChanges);
}

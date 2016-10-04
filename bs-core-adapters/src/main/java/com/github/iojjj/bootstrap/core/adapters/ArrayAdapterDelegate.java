package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

class ArrayAdapterDelegate<T> extends AdapterDelegate<T, AdapterCallbacks> {

    /**
     * Flag indicates that adapter will notify observers about data changes.
     */
    private boolean mAutoNotifyDataSetChanges = true;

    ArrayAdapterDelegate(@NonNull AdapterCallbacks adapterCallbacks,
                         @NonNull BSAdapterRenderer<T, ? extends BSViewHolder> renderer,
                         @Nullable BSFilterPredicate<T> predicate) {
        super(adapterCallbacks, renderer, predicate);
    }

    @Override
    protected void onItemAdded(T item) {
        final AdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final CharSequence lastFilterQuery = getLastFilterQuery();
            final BSFilterPredicate<T> filterPredicate = getFilterPredicate();
            if (lastFilterQuery != null &&
                    filterPredicate.apply(lastFilterQuery.toString().trim(), item)) {
                getFilteredItems().add(item);
                if (mAutoNotifyDataSetChanges) {
                    adapterCallbacks.notifyDataSetChanged();
                }
            }
        } else if (mAutoNotifyDataSetChanges) {
            adapterCallbacks.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCollectionAdded(Collection<T> items, int insertPosition) {
        final AdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final CharSequence lastFilterQuery = getLastFilterQuery();
            final BSFilterPredicate<T> filterPredicate = getFilterPredicate();
            if (lastFilterQuery != null) {
                final List<T> filteredItems = getFilteredItems();
                //noinspection Convert2streamapi
                for (final T item : items) {
                    if (filterPredicate.apply(lastFilterQuery.toString().trim(), item)) {
                        filteredItems.add(item);
                    }
                }
                if (mAutoNotifyDataSetChanges) {
                    adapterCallbacks.notifyDataSetChanged();
                }
            }
        } else if (mAutoNotifyDataSetChanges) {
            adapterCallbacks.notifyDataSetChanged();
        }
    }

    @Override
    protected void onItemRemoved(T item, int position) {
        final AdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final int pos = getFilteredItems().indexOf(item);
            if (pos > -1 && mAutoNotifyDataSetChanges) {
                adapterCallbacks.notifyDataSetChanged();
            }
        } else if (mAutoNotifyDataSetChanges) {
            adapterCallbacks.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCleared(int count) {
        final AdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final List<T> filteredItems = getFilteredItems();
            count = filteredItems.size();
            filteredItems.clear();
            setFiltered(false);
            setLastFilterQuery(null);
            if (mAutoNotifyDataSetChanges && count > 0) {
                adapterCallbacks.notifyDataSetChanged();
            }
        } else if (mAutoNotifyDataSetChanges && count > 0) {
            adapterCallbacks.notifyDataSetChanged();
        }
    }

    @Override
    protected void onFiltered() {
        getAdapterCallbacks().notifyDataSetChanged();
    }

    int getCount() {
        if (isFiltered()) {
            return getFilteredItems().size();
        }
        return getItems().size();
    }

    int getViewTypeCount() {
        return getRenderer().getViewTypeCount();
    }

    void setAutoNotifyDataSetChanges(boolean notifyDataSetChanges) {
        mAutoNotifyDataSetChanges = notifyDataSetChanges;
    }
}

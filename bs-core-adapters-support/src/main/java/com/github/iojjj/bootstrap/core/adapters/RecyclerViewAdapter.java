package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link AdapterImpl} used in {@link BSRecyclerViewAdapter}.
 *
 * @param <T> type of items
 *
 * @since 1.0
 */
class RecyclerViewAdapter<T> extends AdapterImpl<T, RecyclerViewAdapterCallbacks> {

    /**
     * Flag indicates that adapter will animate all data changes.
     */
    private boolean mAnimateDataSetChanges = true;

    /**
     * Flag indicates that adapter will animate all data changes if adapter is filtered.
     */
    private boolean mAnimateDataSetChangesWhenFiltered = true;

    /**
     * Flag indicates that adapter will notify observers about data changes.
     */
    private boolean mAutoNotifyDataSetChanges = true;

    RecyclerViewAdapter(@NonNull RecyclerViewAdapterCallbacks adapterCallbacks,
                        @NonNull BSAdapterRenderer<T, ? extends BSViewHolder> renderer,
                        @Nullable BSFilterPredicate<T> predicate) {
        super(adapterCallbacks, renderer, predicate);
    }

    @Override
    protected void onCleared(int count) {
        final RecyclerViewAdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final List<T> filteredItems = getFilteredItems();
            count = filteredItems.size();
            filteredItems.clear();
            setFiltered(false);
            setLastFilterQuery(null);
            if (mAutoNotifyDataSetChanges && count > 0) {
                if (mAnimateDataSetChangesWhenFiltered) {
                    adapterCallbacks.notifyItemRangeRemoved(0, count);
                } else {
                    adapterCallbacks.notifyDataSetChanged();
                }
            }
        } else if (mAutoNotifyDataSetChanges && count > 0) {
            if (mAnimateDataSetChanges) {
                adapterCallbacks.notifyItemRangeRemoved(0, count);
            } else {
                adapterCallbacks.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCollectionAdded(@NonNull Collection<T> items, int insertPosition) {
        final RecyclerViewAdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final List<T> filteredItems = getFilteredItems();
            final CharSequence lastFilterQuery = getLastFilterQuery();
            final BSFilterPredicate<T> filterPredicate = getFilterPredicate();
            insertPosition = filteredItems.size();
            if (lastFilterQuery != null) {
                //noinspection Convert2streamapi
                for (final T item : items) {
                    if (filterPredicate.apply(lastFilterQuery.toString().trim(), item)) {
                        filteredItems.add(item);
                    }
                }
                if (mAutoNotifyDataSetChanges) {
                    if (mAnimateDataSetChangesWhenFiltered) {
                        adapterCallbacks.notifyItemRangeInserted(insertPosition, filteredItems.size() - insertPosition);
                    } else {
                        adapterCallbacks.notifyDataSetChanged();
                    }
                }
            }
        } else if (mAutoNotifyDataSetChanges) {
            if (mAnimateDataSetChanges) {
                adapterCallbacks.notifyItemRangeInserted(insertPosition, getItems().size() - insertPosition);
            } else {
                adapterCallbacks.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onFiltered() {
        getAdapterCallbacks().notifyDataSetChanged();
    }

    @Override
    protected void onItemAdded(T item) {
        final RecyclerViewAdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final CharSequence lastFilterQuery = getLastFilterQuery();
            final BSFilterPredicate<T> filterPredicate = getFilterPredicate();
            if (lastFilterQuery != null &&
                    filterPredicate.apply(lastFilterQuery.toString().trim(), item)) {
                final List<T> filteredItems = getFilteredItems();
                filteredItems.add(item);
                if (mAutoNotifyDataSetChanges) {
                    if (mAnimateDataSetChangesWhenFiltered) {
                        int pos = filteredItems.size() - 1;
                        adapterCallbacks.notifyItemInserted(pos);
                    } else {
                        adapterCallbacks.notifyDataSetChanged();
                    }
                }
            }
        } else if (mAutoNotifyDataSetChanges) {
            if (mAnimateDataSetChanges) {
                adapterCallbacks.notifyItemInserted(getItems().size() - 1);
            } else {
                adapterCallbacks.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onItemRemoved(T item, int position) {
        final RecyclerViewAdapterCallbacks adapterCallbacks = getAdapterCallbacks();
        if (isFiltered()) {
            final int pos = getFilteredItems().indexOf(item);
            if (pos > -1 && mAutoNotifyDataSetChanges) {
                if (mAnimateDataSetChangesWhenFiltered) {
                    adapterCallbacks.notifyItemRemoved(pos);
                } else {
                    adapterCallbacks.notifyDataSetChanged();
                }
            }
        } else if (mAutoNotifyDataSetChanges) {
            if (mAnimateDataSetChanges) {
                adapterCallbacks.notifyItemRemoved(position);
            } else {
                adapterCallbacks.notifyDataSetChanged();
            }
        }
    }

    int getCount() {
        if (isFiltered()) {
            return getFilteredItems().size();
        }
        return getItems().size();
    }

    void setAnimateDataSetChanges(boolean animateDataSetChanges) {
        mAnimateDataSetChanges = animateDataSetChanges;
    }

    void setAnimateDataSetChangesWhenFiltered(boolean animateDataSetChangesWhenFiltered) {
        mAnimateDataSetChangesWhenFiltered = animateDataSetChangesWhenFiltered;
    }

    void setAutoNotifyDataSetChanges(boolean notifyDataSetChanged) {
        mAutoNotifyDataSetChanges = notifyDataSetChanged;
    }
}

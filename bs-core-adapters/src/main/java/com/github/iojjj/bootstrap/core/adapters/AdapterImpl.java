package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link Adapter}.
 *
 * @param <T>  type of items
 * @param <AC> type of adapter callbacks
 *
 * @since 1.0
 */
abstract class AdapterImpl<T, AC> implements Adapter<T> {

    /**
     * Adapter callbacks instance.
     */
    private final AC mAdapterCallbacks;

    /**
     * List of filtered items.
     */
    @NonNull
    private final List<T> mFilteredItems;

    /**
     * Inner filter object. It's used for simplifying filtering process.
     */
    @NonNull
    private final android.widget.Filter mInnerFilter;

    /**
     * List of items.
     */
    @NonNull
    private final List<T> mItems;

    /**
     * Instance of {@link BSAdapterRenderer}.
     */
    @NonNull
    private final BSAdapterRenderer<T, BSViewHolder> mRenderer;

    /**
     * Predicate for filtering.
     */
    private BSFilterPredicate<T> mFilterPredicate;

    /**
     * Flag that indicates that adapter is filtered.
     */
    private boolean mFiltered;

    /**
     * Last filter query.
     */
    @Nullable
    private CharSequence mLastFilterQuery;


    AdapterImpl(@NonNull AC adapterCallbacks,
                @NonNull BSAdapterRenderer<T, ? extends BSViewHolder> renderer,
                @Nullable BSFilterPredicate<T> predicate) {
        BSAssertions.assertNotNull(renderer, "renderer");
        mItems = new ArrayList<>();
        mFilteredItems = new ArrayList<>();
        //noinspection unchecked
        mRenderer = (BSAdapterRenderer<T, BSViewHolder>) renderer;
        setFilterPredicate(predicate);
        mInnerFilter = createNewFilter();
        mRenderer.setAdapter(this);
        mAdapterCallbacks = adapterCallbacks;
    }

    @Override
    public boolean addItem(T item) {
        final boolean result = mItems.add(item);
        if (result) {
            onItemAdded(item);
        }
        return result;
    }

    @Override
    public boolean addItems(@NonNull Collection<T> items) {
        BSAssertions.assertNotNull(items, "items");
        final int insertedPosition = mItems.size();
        final boolean result = mItems.addAll(items);
        if (result) {
            onCollectionAdded(items, insertedPosition);
        }
        return result;
    }

    @Override
    public boolean addItems(@NonNull T[] items) {
        BSAssertions.assertNotNull(items, "items");
        final List<T> list = Arrays.asList(items);
        return addItems(list);
    }

    @Override
    public void clear() {
        int count = mItems.size();
        mItems.clear();
        onCleared(count);
    }

    @Override
    public void filter(@Nullable CharSequence charSequence) {
        mInnerFilter.filter(charSequence);
    }

    public T getItem(int position) {
        if (isFiltered()) {
            return mFilteredItems.get(position);
        }
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemPosition(T item) {
        if (isFiltered()) {
            return mFilteredItems.indexOf(item);
        }
        return mItems.indexOf(item);
    }

    @Override
    public int getItemViewType(int position) {
        return mRenderer.getItemViewType(getItem(position), position);
    }

    @Override
    public Spannable highlightFilterQuery(@NonNull CharSequence charSequence, @ColorInt int highlightColor) {
        BSAssertions.assertNotNull(charSequence, "charSequence");
        final SpannableString spannableString = new SpannableString(charSequence);
        if (!isFiltered() || TextUtils.isEmpty(mLastFilterQuery)) {
            return spannableString;
        }
        final String filteredString = mLastFilterQuery.toString().trim().toLowerCase();
        final String lowercase = charSequence.toString().toLowerCase();
        final int length = filteredString.length();
        int index = -1, prevIndex;
        do {
            prevIndex = index;
            index = lowercase.indexOf(filteredString, prevIndex + 1);
            if (index == -1) {
                break;
            }
            spannableString.setSpan(new ForegroundColorSpan(highlightColor), index, index + length, 0);
        } while (true);
        return spannableString;
    }

    @Override
    public boolean isFiltered() {
        return mFiltered;
    }

    @Override
    public boolean removeAll(@NonNull Collection<T> items) {
        BSAssertions.assertNotNull(items, "items");
        // TODO: check if this works properly
        boolean result = false;
        //noinspection Convert2streamapi
        for (final T item : items) {
            result |= removeItem(item);
        }
        return result;
    }

    @Override
    public boolean removeItem(T item) {
        int position = mItems.indexOf(item);
        if (position > -1) {
            removeItemAt(position);
            return true;
        }
        return false;
    }

    @Override
    public T removeItemAt(int position) {
        final T item = mItems.remove(position);
        onItemRemoved(item, position);
        return item;
    }

    @Override
    public boolean retainAll(@NonNull Collection<T> items) {
        BSAssertions.assertNotNull(items, "items");
        // TODO: check if this works properly
        boolean result = false;
        final Collection<T> snapshot = getSnapshot();
        //noinspection Convert2streamapi
        for (final T item : snapshot) {
            if (!items.contains(item)) {
                result |= removeItem(item);
            }
        }
        return result;
    }

    void setFiltered(boolean filtered) {
        mFiltered = filtered;
    }

    AC getAdapterCallbacks() {
        return mAdapterCallbacks;
    }

    View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, true);
    }

    BSFilterPredicate<T> getFilterPredicate() {
        return mFilterPredicate;
    }

    @Override
    public void setFilterPredicate(@Nullable BSFilterPredicate<T> predicate) {
        if (predicate == null) {
            predicate = (query, item) -> item != null && item.toString().contains(query);
        }
        mFilterPredicate = predicate;
    }

    @NonNull
    List<T> getFilteredItems() {
        return mFilteredItems;
    }

    @NonNull
    List<T> getItems() {
        return mItems;
    }

    @Nullable
    CharSequence getLastFilterQuery() {
        return mLastFilterQuery;
    }

    void setLastFilterQuery(@Nullable CharSequence lastFilterQuery) {
        mLastFilterQuery = lastFilterQuery;
    }

    @NonNull
    BSAdapterRenderer<T, ? extends BSViewHolder> getRenderer() {
        return mRenderer;
    }

    View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, false);
    }

    @NonNull
    private android.widget.Filter createNewFilter() {
        return new android.widget.Filter() {

            @Nullable
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence == null) {
                    charSequence = "";
                }
                String trimmed = charSequence.toString().trim();
                final Collection<T> snapshot = getSnapshot();
                final Collection<T> filtered = new ArrayList<>();
                //noinspection Convert2streamapi
                for (final T item : snapshot) {
                    if (mFilterPredicate.apply(trimmed, item)) {
                        filtered.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.count = filtered.size();
                filterResults.values = filtered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(@Nullable CharSequence charSequence, @Nullable FilterResults filterResults) {
                Collection<T> filteredItems;
                if (filterResults == null) {
                    filteredItems = null;
                } else {
                    filteredItems = (Collection<T>) filterResults.values;
                }
                onFilteringCompleted(charSequence, filteredItems);
            }
        };
    }

    private Collection<T> getSnapshot() {
        return Collections.unmodifiableCollection(mItems);
    }

    private View getView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        final BSViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = mRenderer.createViewHolder(parent, getItemViewType(position), isDropDown);
            convertView = viewHolder.getRootView();
            viewHolder.getRootView().setTag(viewHolder);
        } else {
            //noinspection unchecked
            viewHolder = (BSViewHolder) convertView.getTag();
        }
        mRenderer.bind(viewHolder, getItem(position), position);
        return convertView;
    }

    private void onFilteringCompleted(@Nullable CharSequence charSequence, @Nullable Collection<T> items) {
        mFiltered = items != null;
        mLastFilterQuery = charSequence;
        mFilteredItems.clear();
        if (items != null) {
            mFilteredItems.addAll(items);
        }
        onFiltered();
    }

    /**
     * Called when an adapter has been cleared.
     *
     * @param count number of removed items
     */
    protected abstract void onCleared(int count);

    /**
     * Called when a collection of items has been added.
     *
     * @param items          collection of items
     * @param insertPosition start position of insertion
     */
    protected abstract void onCollectionAdded(@NonNull Collection<T> items, int insertPosition);

    /**
     * Called when an adapter has been filtered.
     */
    protected abstract void onFiltered();

    /**
     * Called when an item has been added.
     *
     * @param item any item
     */
    protected abstract void onItemAdded(T item);

    /**
     * Called when an item has been removed.
     *
     * @param item     any item
     * @param position position of item in adapter
     */
    protected abstract void onItemRemoved(T item, int position);
}

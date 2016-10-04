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

abstract class AdapterDelegate<T, AC> implements Adapter<T> {

    /**
     * List of items.
     */
    @NonNull
    private final List<T> mItems;
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
     * Instance of {@link BSAdapterRenderer}.
     */
    @NonNull
    private final BSAdapterRenderer<T, BSViewHolder> mRenderer;

    private final AC mAdapterCallbacks;

    /**
     * Last filter query.
     */
    @Nullable
    private CharSequence mLastFilterQuery;
    /**
     * Predicate for filtering.
     */
    private BSFilterPredicate<T> mFilterPredicate;
    /**
     * Flag that indicates that adapter is filtered.
     */
    private boolean mFiltered;


    AdapterDelegate(@NonNull AC adapterCallbacks,
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

    private void onFilteringCompleted(@Nullable CharSequence charSequence, @Nullable Collection<T> items) {
        mFiltered = items != null;
        mLastFilterQuery = charSequence;
        mFilteredItems.clear();
        if (items != null) {
            mFilteredItems.addAll(items);
        }
        onFiltered();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mRenderer.getItemViewType(position, getItem(position));
    }

    View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, false);
    }

    View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, true);
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

    /**
     * Filter list of items.
     *
     * @param charSequence filter query. Pass <code>null</code> to reset filter.
     */
    public void filter(@Nullable CharSequence charSequence) {
        mInnerFilter.filter(charSequence);
    }

    public T getItem(int position) {
        if (isFiltered()) {
            return mFilteredItems.get(position);
        }
        return mItems.get(position);
    }

    /**
     * Add item to adapter.
     *
     * @param item any item
     * @return true if adapter's collection was modified, false otherwise
     */
    public boolean addItem(T item) {
        final boolean result = mItems.add(item);
        if (result) {
            onItemAdded(item);
        }
        return result;
    }

    /**
     * Add items to adapter.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
    public boolean addItems(@NonNull Collection<T> items) {
        BSAssertions.assertNotNull(items, "items");
        final int insertedPosition = mItems.size();
        final boolean result = mItems.addAll(items);
        if (result) {
            onCollectionAdded(items, insertedPosition);
        }
        return result;
    }

    /**
     * Add items to adapter.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
    public boolean addItems(@NonNull T[] items) {
        BSAssertions.assertNotNull(items, "items");
        final List<T> list = Arrays.asList(items);
        return addItems(list);
    }

    /**
     * Remove item at specific position.
     *
     * @param position position in array
     * @return removed item
     */
    public T removeItemAt(int position) {
        final T item = mItems.remove(position);
        onItemRemoved(item, position);
        return item;
    }

    /**
     * Remove item.
     *
     * @param item some item
     * @return true if adapter's collection was modified, false otherwise
     */
    public boolean removeItem(T item) {
        int position = mItems.indexOf(item);
        if (position > -1) {
            removeItemAt(position);
            return true;
        }
        return false;
    }

    /**
     * Remove all items.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
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

    /**
     * Remove all items that are not contained in the specified collection.
     *
     * @param items collection of items
     * @return true if adapter's collection was modified, false otherwise
     */
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

    /**
     * Clear all items.
     */
    public void clear() {
        int count = mItems.size();
        mItems.clear();
        onCleared(count);
    }

    @Override
    public int getItemPosition(T item) {
        if (isFiltered()) {
            return mFilteredItems.indexOf(item);
        }
        return mItems.indexOf(item);
    }

    private Collection<T> getSnapshot() {
        return Collections.unmodifiableCollection(mItems);
    }

    /**
     * Check if adapter is filtered.
     *
     * @return true if adapter is filtered, false otherwise
     */
    public boolean isFiltered() {
        return mFiltered;
    }

    void setFiltered(boolean filtered) {
        mFiltered = filtered;
    }

    /**
     * Highlight filter query in specified char sequence with specified color.
     *
     * @param charSequence   any non-null char sequence
     * @param highlightColor highlight color
     * @return new Spannable object with highlighted filter query
     */
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

    AC getAdapterCallbacks() {
        return mAdapterCallbacks;
    }

    @Nullable
    CharSequence getLastFilterQuery() {
        return mLastFilterQuery;
    }

    void setLastFilterQuery(@Nullable CharSequence lastFilterQuery) {
        mLastFilterQuery = lastFilterQuery;
    }

    BSFilterPredicate<T> getFilterPredicate() {
        return mFilterPredicate;
    }

    /**
     * Set filter predicate.
     *
     * @param predicate some filter predicate or null
     */
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

    @NonNull
    BSAdapterRenderer<T, ? extends BSViewHolder> getRenderer() {
        return mRenderer;
    }

    protected abstract void onItemAdded(T item);

    protected abstract void onCollectionAdded(Collection<T> items, int insertPosition);

    protected abstract void onItemRemoved(T item, int position);

    protected abstract void onCleared(int count);

    protected abstract void onFiltered();
}

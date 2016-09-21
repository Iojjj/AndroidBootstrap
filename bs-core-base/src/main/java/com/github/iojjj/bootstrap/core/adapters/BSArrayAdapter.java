package com.github.iojjj.bootstrap.core.adapters;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link BaseAdapter} with {@link List} as a holder of items.
 * This adapter can be used with classic widgets like {@link ListView} and {@link Spinner}.
 *
 * @param <T> type of items
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class BSArrayAdapter<T> extends BaseAdapter implements ListAdapter, SpinnerAdapter {

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
     * Instance of {@link Renderer}.
     */
    @NonNull
    private final Renderer<T, ? extends ViewHolder> mRenderer;
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
    /**
     * Flag indicates that adapter will notify observers about data changes.
     */
    private boolean mAutoNotifyDataSetChanges = true;

    private BSArrayAdapter(@NonNull Renderer<T, ? extends ViewHolder> renderer,
                           @Nullable BSFilterPredicate<T> predicate) {
        BSAssertions.assertNotNull(renderer, "renderer");
        mItems = new ArrayList<>();
        mFilteredItems = new ArrayList<>();
        mRenderer = renderer;
        setFilterPredicate(predicate);
        mInnerFilter = createNewFilter();
        mRenderer.setAdapter(this);
    }

    /**
     * Create a new builder for adapter.
     *
     * @param <T> type of items
     * @return new builder object
     */
    public static <T> Builder<T> newBuilder() {
        return new Builder<>();
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

    private void onFilteringCompleted(@Nullable CharSequence charSequence, @Nullable Collection<T> items) {
        mFiltered = items != null;
        mLastFilterQuery = charSequence;
        mFilteredItems.clear();
        if (items != null) {
            mFilteredItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mRenderer.getItemViewType(position, getItem(position));
    }

    @Override
    public int getViewTypeCount() {
        return mRenderer.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, true);
    }

    private View getView(int position, View convertView, ViewGroup parent, boolean isDropDown) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = mRenderer.createViewHolder(parent, getItemViewType(position), isDropDown);
            convertView = viewHolder.getRootView();
            viewHolder.getRootView().setTag(viewHolder);
        } else {
            //noinspection unchecked
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mRenderer.bindInt(position, viewHolder, getItem(position));
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

    @Override
    public T getItem(int position) {
        if (isFiltered()) {
            return mFilteredItems.get(position);
        }
        return mItems.get(position);
    }

    /**
     * Get items count. This method will return number of filtered items if filtering was applied.
     *
     * @return items count
     */
    public int getItemCount() {
        if (isFiltered()) {
            return mFilteredItems.size();
        }
        return mItems.size();
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
            if (isFiltered()) {
                if (mLastFilterQuery != null &&
                        mFilterPredicate.apply(mLastFilterQuery.toString().trim(), item)) {
                    mFilteredItems.add(item);
                    if (mAutoNotifyDataSetChanges) {
                        notifyDataSetChanged();
                    }
                }
            } else if (mAutoNotifyDataSetChanges) {
                notifyDataSetChanged();
            }
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
        final boolean result = mItems.addAll(items);
        if (result) {
            if (isFiltered()) {
                if (mLastFilterQuery != null) {
                    //noinspection Convert2streamapi
                    for (final T item : items) {
                        if (mFilterPredicate.apply(mLastFilterQuery.toString().trim(), item)) {
                            mFilteredItems.add(item);
                        }
                    }
                    if (mAutoNotifyDataSetChanges) {
                        notifyDataSetChanged();
                    }
                }
            } else if (mAutoNotifyDataSetChanges) {
                notifyDataSetChanged();
            }
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
     * @param position position between <code>0</code> and <code>{@link #getItemCount()} - 1</code>
     * @return removed item
     */
    public T removeItemAt(int position) {
        final T item = mItems.remove(position);
        if (isFiltered()) {
            final int pos = mFilteredItems.indexOf(item);
            if (pos > -1 && mAutoNotifyDataSetChanges) {
                notifyDataSetChanged();
            }
        } else if (mAutoNotifyDataSetChanges) {
            notifyDataSetChanged();
        }
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
        if (isFiltered()) {
            count = mFilteredItems.size();
            mFilteredItems.clear();
            mFiltered = false;
            mLastFilterQuery = null;
            if (mAutoNotifyDataSetChanges && count > 0) {
                notifyDataSetChanged();
            }
        } else if (mAutoNotifyDataSetChanges && count > 0) {
            notifyDataSetChanged();
        }
    }

    private Collection<T> getSnapshot() {
        return Collections.unmodifiableCollection(mItems);
    }

    /**
     * Set whenever to notify RecyclerView about changes in adapter in any state. Global switch. Default values is true.
     *
     * @param notifyDataSetChanges true or false
     */
    public void setAutoNotifyDataSetChanges(boolean notifyDataSetChanges) {
        mAutoNotifyDataSetChanges = notifyDataSetChanges;
    }

    /**
     * Check if adapter is filtered.
     *
     * @return true if adapter is filtered, false otherwise
     */
    public boolean isFiltered() {
        return mFiltered;
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

    /**
     * View holder interface.
     */
    public interface ViewHolder {

        /**
         * Get root view of view holder.
         *
         * @return root view of view holder
         */
        @NonNull
        View getRootView();
    }

    /**
     * Builder class for {@link BSArrayAdapter}.
     *
     * @param <T> type of items
     */
    public static class Builder<T> {

        private Renderer<T, ? extends ViewHolder> mRenderer;
        private BSFilterPredicate<T> mFilterPredicate;

        private Builder() {
            //no instance
        }

        /**
         * Set renderer for items.
         *
         * @param renderer instance of Renderer
         */
        public Builder<T> setRenderer(@NonNull Renderer<T, ? extends ViewHolder> renderer) {
            mRenderer = renderer;
            return this;
        }

        /**
         * Set filter predicate.
         *
         * @param filterPredicate instance of BSFilterPredicate
         */
        public Builder<T> setFilterPredicate(@Nullable BSFilterPredicate<T> filterPredicate) {
            mFilterPredicate = filterPredicate;
            return this;
        }

        /**
         * Create a new adapter.
         *
         * @return new adapter object
         */
        public BSArrayAdapter<T> build() {
            BSAssertions.assertNotNull(mRenderer, "renderer");
            return new BSArrayAdapter<>(mRenderer, mFilterPredicate);
        }
    }

    /**
     * Renderer class that creates view holders and populates them.
     *
     * @since 1.0
     */
    public static abstract class Renderer<T, VH extends ViewHolder> {

        @NonNull
        private final Context mContext;
        private final LayoutInflater mLayoutInflater;
        private BSArrayAdapter mAdapter;

        protected Renderer(@NonNull Context context) {
            BSAssertions.assertNotNull(context, "context");
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        /**
         * Set adapter.
         *
         * @param adapter instance of BSArrayAdapter
         */
        void setAdapter(@NonNull BSArrayAdapter adapter) {
            BSAssertions.assertNotNull(adapter, "adapter");
            mAdapter = adapter;
        }

        /**
         * Checks if adapter currently filtered.
         *
         * @return true if adapter is filtered, false otherwise
         */
        protected boolean isFiltered() {
            return mAdapter.isFiltered();
        }

        /**
         * Highlights filtering query in provided CharSequence with specified highlight color.
         *
         * @param charSequence   any non-null CharSequence
         * @param highlightColor highlight color
         * @return spannable string with highlighted sub-sequences
         */
        @NonNull
        protected Spannable highlightFilterQuery(@NonNull CharSequence charSequence,
                                                 @ColorInt int highlightColor) {
            return mAdapter.highlightFilterQuery(charSequence, highlightColor);
        }

        /**
         * Get item view type.
         *
         * @param position position of item in adapter
         * @param item     item itself
         * @return item view type
         */
        protected int getItemViewType(int position, T item) {
            return 0;
        }

        /**
         * Get item view type count. Default value is 1.
         *
         * @return count of item view types
         */
        protected int getViewTypeCount() {
            return 1;
        }

        /**
         * Get context.
         *
         * @return instance of Context
         */
        @NonNull
        protected Context getContext() {
            return mContext;
        }

        /**
         * Get layout inflater.
         *
         * @return instance of LayoutInflater
         */
        @NonNull
        protected LayoutInflater getLayoutInflater() {
            return mLayoutInflater;
        }

        /**
         * Create a new view holder.
         *
         * @param parent     parent view
         * @param viewType   view type
         * @param isDropDown flag indicates that view should be created for drop down menu
         * @return new view holder
         * @see BaseAdapter#getView(int, View, ViewGroup)
         * @see BaseAdapter#getDropDownView(int, View, ViewGroup)
         */
        protected abstract VH createViewHolder(ViewGroup parent, int viewType, boolean isDropDown);

        @SuppressWarnings("unchecked")
        private void bindInt(int position, ViewHolder viewHolder, T item) {
            bind(position, (VH) viewHolder, item);
        }

        /**
         * Bind item to view holder.
         *
         * @param position   position of item in adapter
         * @param viewHolder instance of ViewHolder
         * @param item       item itself
         * @see BaseAdapter#getView(int, View, ViewGroup)
         * @see BaseAdapter#getDropDownView(int, View, ViewGroup)
         */
        protected abstract void bind(int position, VH viewHolder, T item);
    }
}

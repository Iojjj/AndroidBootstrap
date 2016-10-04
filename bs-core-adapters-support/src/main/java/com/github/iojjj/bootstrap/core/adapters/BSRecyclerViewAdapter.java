package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link RecyclerView.Adapter} with {@link List} as a holder of items.
 * This adapter can be used with {@link RecyclerView} only.
 *
 * @param <T> type of items
 * @since 1.0
 */
public final class BSRecyclerViewAdapter<T> extends RecyclerView.Adapter<BSRecyclerViewAdapter.ViewHolder> implements Adapter<T>, RecyclerViewAdapterCallbacks {

    private final RecyclerViewAdapterDelegate<T> mAdapterDelegate;



    BSRecyclerViewAdapter(@NonNull BSAdapterRenderer<T, ? extends BSViewHolder> renderer,
                          @Nullable BSFilterPredicate<T> predicate) {
        mAdapterDelegate = new RecyclerViewAdapterDelegate<>(this, renderer, predicate);
    }

    @Override
    public void setFilterPredicate(@Nullable BSFilterPredicate<T> predicate) {
        mAdapterDelegate.setFilterPredicate(predicate);
    }

    @Override
    public BSRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //noinspection unchecked
        final BSRenderer<T, BSViewHolder> renderer = (BSRenderer<T, BSViewHolder>) mAdapterDelegate.getRenderer();
        return new ViewHolder(renderer.createViewHolder(parent, viewType, false));
    }

    @Override
    public void onBindViewHolder(BSRecyclerViewAdapter.ViewHolder holder, int position) {
        //noinspection unchecked
        final BSRenderer<T, BSViewHolder> renderer = (BSRenderer<T, BSViewHolder>) mAdapterDelegate.getRenderer();
        renderer.bind(holder.mViewHolder, getItem(position), position);
    }

    @Override
    public void onBindViewHolder(BSRecyclerViewAdapter.ViewHolder holder, int position, List<Object> payloads) {
        //noinspection unchecked
        final BSRenderer<T, BSViewHolder> renderer = (BSRenderer<T, BSViewHolder>) mAdapterDelegate.getRenderer();
        renderer.bind(holder.mViewHolder, getItem(position), position, payloads);
    }

    @Override
    public void filter(@Nullable CharSequence charSequence) {
        mAdapterDelegate.filter(charSequence);
    }

    @Override
    public T getItem(int position) {
        return mAdapterDelegate.getItem(position);
    }


    @Override
    public int getItemPosition(T item) {
        return mAdapterDelegate.getItemPosition(item);
    }

    /**
     * Get items count. This method will return number of filtered items if filtering was applied.
     *
     * @return items count
     */
    @Override
    public int getItemCount() {
        return mAdapterDelegate.getCount();
    }

    @Override
    public boolean addItem(T item) {
        return mAdapterDelegate.addItem(item);
    }

    @Override
    public boolean addItems(@NonNull Collection<T> items) {
        return mAdapterDelegate.addItems(items);
    }

    @Override
    public boolean addItems(@NonNull T[] items) {
        return mAdapterDelegate.addItems(items);
    }

    @Override
    public T removeItemAt(int position) {
        return mAdapterDelegate.removeItemAt(position);
    }

    @Override
    public boolean removeItem(T item) {
        return mAdapterDelegate.removeItem(item);
    }

    @Override
    public boolean removeAll(@NonNull Collection<T> items) {
        return mAdapterDelegate.removeAll(items);
    }

    @Override
    public boolean retainAll(@NonNull Collection<T> items) {
        return mAdapterDelegate.retainAll(items);
    }

    @Override
    public void clear() {
        mAdapterDelegate.clear();
    }

    @Override
    public boolean isFiltered() {
        return mAdapterDelegate.isFiltered();
    }

    @Override
    public Spannable highlightFilterQuery(@NonNull CharSequence charSequence, @ColorInt int highlightColor) {
        return mAdapterDelegate.highlightFilterQuery(charSequence, highlightColor);
    }

    /**
     * Set whenever to notify RecyclerView about changes in adapter in filtered state.  Default values is true.
     *
     * @param animateDataSetChangesWhenFiltered true or false
     */
    public void setAnimateDataSetChangesWhenFiltered(boolean animateDataSetChangesWhenFiltered) {
        mAdapterDelegate.setAnimateDataSetChangesWhenFiltered(animateDataSetChangesWhenFiltered);
    }

    /**
     * Set whenever to notify RecyclerView about changes in adapter in normal state. Default values is true.
     *
     * @param animateDataSetChanges true or false
     */
    public void setAnimateDataSetChanges(boolean animateDataSetChanges) {
        mAdapterDelegate.setAnimateDataSetChanges(animateDataSetChanges);
    }

    /**
     * Set whenever to notify RecyclerView about changes in adapter in any state. Global switch. Default values is true.
     *
     * @param notifyDataSetChanged true or false
     */
    public void setAutoNotifyDataSetChanges(boolean notifyDataSetChanged) {
        mAdapterDelegate.setAutoNotifyDataSetChanges(notifyDataSetChanged);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements BSViewHolder {

        private final BSViewHolder mViewHolder;

        ViewHolder(@NonNull BSViewHolder viewHolder) {
            super(viewHolder.getRootView());
            mViewHolder = viewHolder;
        }

        @Override
        public View getRootView() {
            return mViewHolder.getRootView();
        }
    }
}

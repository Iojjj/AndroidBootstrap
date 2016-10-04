package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link BaseAdapter} with {@link List} as a holder of items.
 * This adapter can be used with classic widgets like {@link ListView} and {@link Spinner}.
 *
 * @param <T> type of items
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
final class BSArrayAdapterImpl<T> extends BaseAdapter implements AdapterCallbacks, BSArrayAdapter<T> {

    @NonNull
    private final ArrayAdapterDelegate<T> mAdapterDelegate;

    BSArrayAdapterImpl(@NonNull BSAdapterRenderer<T, ? extends BSViewHolder> renderer,
                       @Nullable BSFilterPredicate<T> predicate) {
        BSAssertions.assertNotNull(renderer, "renderer");
        mAdapterDelegate = new ArrayAdapterDelegate<>(this, renderer, predicate);
    }

    @Override
    public void setFilterPredicate(@Nullable BSFilterPredicate<T> predicate) {
        mAdapterDelegate.setFilterPredicate(predicate);
    }

    @Override
    public int getCount() {
        return mAdapterDelegate.getCount();
    }

    @Override
    public long getItemId(int position) {
        return mAdapterDelegate.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapterDelegate.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapterDelegate.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mAdapterDelegate.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return mAdapterDelegate.getDropDownView(position, convertView, parent);
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
    public void setAutoNotifyDataSetChanges(boolean notifyDataSetChanges) {
        mAdapterDelegate.setAutoNotifyDataSetChanges(notifyDataSetChanges);
    }

    @Override
    public boolean isFiltered() {
        return mAdapterDelegate.isFiltered();
    }

    @Override
    public Spannable highlightFilterQuery(@NonNull CharSequence charSequence, @ColorInt int highlightColor) {
        return mAdapterDelegate.highlightFilterQuery(charSequence, highlightColor);
    }

    @Override
    public int getItemPosition(T item) {
        return mAdapterDelegate.getItemPosition(item);
    }
}

package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;

import java.util.List;

public abstract class BSAdapterRenderer<T, VH extends BSViewHolder> implements BSRenderer<T, VH> {

    private AdapterDelegate mAdapterDelegate;

    @Override
    public int getItemViewType(int position, T item) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void bind(@NonNull VH viewHolder, T item, int position) {

    }

    @Override
    public void bind(@NonNull VH viewHolder, T item, int position, @NonNull List<Object> payloads) {
        bind(viewHolder, item, position);
    }

    void setAdapter(@NonNull AdapterDelegate adapterDelegate) {
        mAdapterDelegate = adapterDelegate;
    }

    public boolean isFiltered() {
        return mAdapterDelegate.isFiltered();
    }

    public Spannable highlightFilterQuery(@NonNull CharSequence charSequence, @ColorInt int highlightColor) {
        return mAdapterDelegate.highlightFilterQuery(charSequence, highlightColor);
    }
}

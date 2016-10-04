package com.github.iojjj.bootstrap.core.adapters;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

interface BSRenderer<T, VH extends BSViewHolder> {

    int getItemViewType(int position, T item);

    int getViewTypeCount();

    VH createViewHolder(ViewGroup parent, int itemViewType, boolean isDropDown);

    void bind(@NonNull VH viewHolder, T item, int position);

    void bind(@NonNull VH viewHolder, T item, int position, @NonNull List<Object> payloads);
}

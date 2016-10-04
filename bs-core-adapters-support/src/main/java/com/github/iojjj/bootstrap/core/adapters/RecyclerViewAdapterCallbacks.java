package com.github.iojjj.bootstrap.core.adapters;

interface RecyclerViewAdapterCallbacks {
    void notifyItemInserted(int position);

    void notifyDataSetChanged();

    void notifyItemRangeRemoved(int i, int count);

    void notifyItemRemoved(int position);

    void notifyItemRangeInserted(int position, int count);
}

package com.github.iojjj.bootstrap.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.iojjj.bootstrap.core.BSAssertions;

import java.lang.ref.WeakReference;

/**
 * Simple observer for displaying and hiding empty view.
 *
 * @since 1.0
 */
public class BSEmptyViewObserver {

    @NonNull
    private final WeakReference<View> mViewWeakReference;
    @NonNull
    private final RecyclerView.AdapterDataObserver mAdapterDataObserver;
    private WeakReference<RecyclerView> mRVWeakReference;

    private BSEmptyViewObserver(@NonNull View view) {
        mViewWeakReference = new WeakReference<>(view);
        mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                somethingChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                somethingChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                somethingChanged();
            }
        };
    }

    /**
     * Create a new observer for specified view.
     *
     * @param view some non-null view
     * @return new observer
     */
    public static BSEmptyViewObserver newObserver(@NonNull View view) {
        BSAssertions.assertNotNull(view, "view");
        return new BSEmptyViewObserver(view);
    }

    /**
     * Bind observer to RecyclerView. RecyclerView must have an adapter at this moment.
     *
     * @param recyclerView some non-null RecyclerView with adapter
     */
    public void bind(@NonNull RecyclerView recyclerView) {
        BSAssertions.assertNotNull(recyclerView, "recyclerView");
        unbind();
        mRVWeakReference = new WeakReference<>(recyclerView);
        recyclerView.getAdapter().registerAdapterDataObserver(mAdapterDataObserver);
    }

    /**
     * Unbind from RecyclerView.
     */
    public void unbind() {
        if (mRVWeakReference == null) {
            return;
        }
        final RecyclerView recyclerView = mRVWeakReference.get();
        if (recyclerView != null) {
            recyclerView.getAdapter().unregisterAdapterDataObserver(mAdapterDataObserver);
            mRVWeakReference.clear();
        }
    }

    private void somethingChanged() {
        View view = mViewWeakReference.get();
        RecyclerView recyclerView = mRVWeakReference.get();
        if (view != null && recyclerView != null) {
            if (recyclerView.getAdapter().getItemCount() == 0) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }
}

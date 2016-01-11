package iojjj.android.bootstrap.adapters;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Item padding decoration. Compatible with {@link LinearLayoutManager} and {@link GridLayoutManager} only.
 */
public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private final int padding;
    private final int halfPadding;
    private final int spanCount;
    private final boolean horizontal;

    /**
     * Create new decoration. Horizontal flag is set to false.
     * @param padding padding in pixels
     * @param spanCount span count. For {@link LinearLayoutManager} pass 1.
     * @see #PaddingItemDecoration(int, int, boolean)
     */
    public PaddingItemDecoration(int padding, int spanCount) {
        this.padding = padding;
        this.halfPadding = padding >> 1;
        this.spanCount = spanCount;
        this.horizontal = false;
    }

    /**
     * Create new decoration.
     * @param padding padding in pixels
     * @param spanCount span count. For {@link LinearLayoutManager} pass 1.
     * @param horizontal flag indicates whenever layout manager draws items in horizontal mode.
     * @see #PaddingItemDecoration(int, int)
     */
    public PaddingItemDecoration(int padding, int spanCount, boolean horizontal) {
        this.padding = padding;
        this.halfPadding = padding >> 1;
        this.spanCount = spanCount;
        this.horizontal = horizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int totalCount = parent.getAdapter().getItemCount();
        final int position = parent.getChildAdapterPosition(view);
        int lastItemsCount = totalCount % spanCount;
        if (lastItemsCount == 0) {
            lastItemsCount = spanCount;
        }
        final int left, right, top, bottom;

        // fX -> mX -> lX

        if (horizontal) {
            // span count = 4
            // L   X   X   X   X   R
            // f00 f10 f20 f30 f40 f50 T
            // f01 f11 f21 f31 f41 f51 Y
            // f02 f12 f22 f32 f42 f52 Y
            // f03 f13 f23 f33 f43 f53 B

            if (position < spanCount) { // L items
                left = padding;
                right = halfPadding;
            } else if (position >= totalCount - lastItemsCount) { // R items
                left = halfPadding;
                right = padding;
            } else { // Y items
                left = right = halfPadding;
            }

            if (position % spanCount == 0) { // L items
                top = padding;
                if (spanCount > 1)
                    bottom = halfPadding;
                else
                    bottom = padding;
            } else if (position % spanCount == spanCount - 1) { // R items
                if (spanCount > 1)
                    top = halfPadding;
                else
                    top = padding;
                bottom = padding;
            } else { // X items
                top = bottom = halfPadding;
            }
        } else {
            // span count = 4
            // L   X   X   R
            // f00 f01 f02 f03 T
            // f10 f11 f12 f13 Y
            // f20 f21 f22 f23 Y
            // f30 f31 f32 f33 Y
            // f40 f41 f42 f43 Y
            // f50 f51 f52 f53 Y
            // f60 f61 f62 f63 B

            if (position % spanCount == 0) { // L items
                left = padding;
                if (spanCount > 1)
                    right = halfPadding;
                else
                    right = padding;
            } else if (position % spanCount == spanCount - 1) { // R items
                if (spanCount > 1)
                    left = halfPadding;
                else
                    left = padding;
                right = padding;
            } else { // X items
                left = right = halfPadding;
            }

            if (position < spanCount) { // T items
                top = padding;
                bottom = halfPadding;
            } else if (position >= totalCount - lastItemsCount) { // B items
                top = halfPadding;
                bottom = padding;
            } else { // Y items
                top = bottom = halfPadding;

            }
        }
        outRect.set(left, top, right, bottom);
    }
}

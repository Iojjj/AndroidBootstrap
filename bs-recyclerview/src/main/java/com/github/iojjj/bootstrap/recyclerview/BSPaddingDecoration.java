package com.github.iojjj.bootstrap.recyclerview;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.iojjj.bootstrap.core.BSAssertions;

/**
 * Item margin decoration. Compatible with {@link LinearLayoutManager} and {@link GridLayoutManager} only.
 *
 * @since 1.0
 */
public class BSPaddingDecoration extends RecyclerView.ItemDecoration {

    private final int mPaddingTop;
    private final int mPaddingBottom;
    private final int mPaddingLeft;
    private final int mPaddingRight;
    private final int mPaddingTopHalf;
    private final int mPaddingBottomHalf;
    private final int mPaddingLeftHalf;
    private final int mPaddingRightHalf;
    private final int mSpanCount;
    private final boolean mHorizontal;

    private BSPaddingDecoration(@NonNull Builder builder) {
        mPaddingLeft = builder.mPaddingLeft;
        mPaddingRight = builder.mPaddingRight;
        mPaddingTop = builder.mPaddingTop;
        mPaddingBottom = builder.mPaddingBottom;
        mPaddingLeftHalf = mPaddingLeft >> 1;
        mPaddingRightHalf = mPaddingRight >> 1;
        mPaddingTopHalf = mPaddingTop >> 1;
        mPaddingBottomHalf = mPaddingBottom >> 1;
        mSpanCount = builder.mSpanCount;
        mHorizontal = builder.mHorizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int totalCount = parent.getAdapter().getItemCount();
        final int position = parent.getChildAdapterPosition(view);
        int lastItemsCount = totalCount % mSpanCount;
        if (lastItemsCount == 0) {
            lastItemsCount = mSpanCount;
        }
        final int left, right, top, bottom;

        // fX -> mX -> lX

        if (mHorizontal) {
            // span count = 4
            // L   X   X   X   X   R
            // f00 f10 f20 f30 f40 f50 T
            // f01 f11 f21 f31 f41 f51 Y
            // f02 f12 f22 f32 f42 f52 Y
            // f03 f13 f23 f33 f43 f53 B

            if (position < mSpanCount) { // L items
                left = mPaddingLeft;
                right = mPaddingRightHalf;
            } else if (position >= totalCount - lastItemsCount) { // R items
                left = mPaddingLeftHalf;
                right = mPaddingRight;
            } else { // Y items
                left = mPaddingLeftHalf;
                right = mPaddingRightHalf;
            }

            if (position % mSpanCount == 0) { // L items
                top = mPaddingTop;
                if (mSpanCount > 1)
                    bottom = mPaddingBottomHalf;
                else
                    bottom = mPaddingBottom;
            } else if (position % mSpanCount == mSpanCount - 1) { // R items
                if (mSpanCount > 1)
                    top = mPaddingTopHalf;
                else
                    top = mPaddingTop;
                bottom = mPaddingBottom;
            } else { // X items
                top = mPaddingTopHalf;
                bottom = mPaddingBottomHalf;
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

            if (position % mSpanCount == 0) { // L items
                left = mPaddingLeft;
                if (mSpanCount > 1)
                    right = mPaddingRightHalf;
                else
                    right = mPaddingRight;
            } else if (position % mSpanCount == mSpanCount - 1) { // R items
                if (mSpanCount > 1)
                    left = mPaddingLeftHalf;
                else
                    left = mPaddingLeft;
                right = mPaddingRight;
            } else { // X items
                left = mPaddingLeftHalf;
                right = mPaddingRightHalf;
            }

            if (position < mSpanCount) { // T items
                top = mPaddingTop;
                bottom = mPaddingBottomHalf;
            } else if (position >= totalCount - lastItemsCount) { // B items
                top = mPaddingTopHalf;
                bottom = mPaddingBottom;
            } else { // Y items
                top = mPaddingTopHalf;
                bottom = mPaddingBottomHalf;
            }
        }
        outRect.set(left, top, right, bottom);
    }

    public static class Builder {
        private int mPaddingTop;
        private int mPaddingBottom;
        private int mPaddingLeft;
        private int mPaddingRight;
        private int mSpanCount = 1;
        private boolean mHorizontal;

        /**
         * Set all padding to same value.
         *
         * @param padding padding in pixels
         */
        public Builder setAllPadding(int padding) {
            mPaddingTop = padding;
            mPaddingBottom = padding;
            mPaddingLeft = padding;
            mPaddingRight = padding;
            return this;
        }

        /**
         * Set vertical padding (top and bottom).
         *
         * @param padding padding in pixels
         */
        public Builder setVerticalPadding(int padding) {
            mPaddingTop = padding;
            mPaddingBottom = padding;
            return this;
        }

        /**
         * Set horizontal padding (left and right).
         *
         * @param padding padding in pixels
         */
        public Builder setHorizontalPadding(int padding) {
            mPaddingLeft = padding;
            mPaddingRight = padding;
            return this;
        }

        /**
         * Set top padding.
         *
         * @param paddingTop padding in pixels
         */
        public Builder setPaddingTop(int paddingTop) {
            mPaddingTop = paddingTop;
            return this;
        }

        /**
         * Set bottom padding.
         *
         * @param paddingBottom padding in pixels
         */
        public Builder setPaddingBottom(int paddingBottom) {
            mPaddingBottom = paddingBottom;
            return this;
        }

        /**
         * Set left padding.
         *
         * @param paddingLeft padding in pixels
         */
        public Builder setPaddingLeft(int paddingLeft) {
            mPaddingLeft = paddingLeft;
            return this;
        }

        /**
         * Set right padding.
         *
         * @param paddingRight padding in pixels
         */
        public Builder setPaddingRight(int paddingRight) {
            mPaddingRight = paddingRight;
            return this;
        }

        /**
         * Set span count. Used for {@link GridLayoutManager}. Default value is 1.
         *
         * @param spanCount span count
         */
        public Builder setSpanCount(int spanCount) {
            mSpanCount = spanCount;
            return this;
        }

        /**
         * Set flag if list or grid is horizontal one. Default value is false.
         *
         * @param horizontal flag if list or grid is horizontal one
         */
        public Builder setHorizontal(boolean horizontal) {
            mHorizontal = horizontal;
            return this;
        }

        /**
         * Create new decoration.
         *
         * @return new decoration
         */
        public BSPaddingDecoration build() {
            BSAssertions.assertTrue(mSpanCount >= 1, "Span count can't be less than 1.");
            return new BSPaddingDecoration(this);
        }
    }
}

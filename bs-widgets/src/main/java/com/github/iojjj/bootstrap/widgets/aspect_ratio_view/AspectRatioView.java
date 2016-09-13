package com.github.iojjj.bootstrap.widgets.aspect_ratio_view;

/**
 * Aspect ratio view interface.
 */

interface AspectRatioView {

    void onMeasureImpl(int widthMeasureSpec, int heightMeasureSpec);

    void setMeasuredDimensionImpl(int measuredWidth, int measuredHeight);
}

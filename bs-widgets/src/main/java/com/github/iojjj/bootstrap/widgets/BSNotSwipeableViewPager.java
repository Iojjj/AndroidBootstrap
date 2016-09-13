package com.github.iojjj.bootstrap.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Implementation of {@link ViewPager} that can't allow swipe the pages.
 *
 * @see #setSwipeEnabled(boolean)
 * @since 1.0
 */
public class BSNotSwipeableViewPager extends ViewPager {

    private boolean mSwipeEnabled = false;

    public BSNotSwipeableViewPager(Context context) {
        this(context, null);
    }

    public BSNotSwipeableViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Set swipe feature enabled.
     *
     * @param swipeEnabled true to enable swipe gestures, false otherwise
     */
    public void setSwipeEnabled(boolean swipeEnabled) {
        mSwipeEnabled = swipeEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mSwipeEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mSwipeEnabled && super.onTouchEvent(ev);
    }
}

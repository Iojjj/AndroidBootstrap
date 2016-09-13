package com.github.iojjj.bootstrap.widgets.pb;

import android.content.Context;
import android.util.AttributeSet;

import com.github.iojjj.bootstrap.widgets.R;

/**
 * Circular progress bar
 */
// TODO: check, update
public class BSFilledProgressBar extends BSProgressBar {

    public BSFilledProgressBar(Context context) {
        super(context);
    }

    public BSFilledProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BSFilledProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BSFilledProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String getText(int progress) {
        return getContext().getString(R.string.percent, progress);
    }
}

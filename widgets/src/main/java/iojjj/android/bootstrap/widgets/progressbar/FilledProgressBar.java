package iojjj.android.bootstrap.widgets.progressbar;

import android.content.Context;
import android.util.AttributeSet;

import iojjj.android.bootstrap.widgets.R;

/**
 * Circular progress bar
 */
public class FilledProgressBar extends AbstractFilledProgressBar {

    public FilledProgressBar(Context context) {
        super(context);
    }

    public FilledProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilledProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FilledProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String getText(int progress) {
        return getContext().getString(R.string.percent, progress);
    }
}

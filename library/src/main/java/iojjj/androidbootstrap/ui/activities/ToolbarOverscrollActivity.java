package iojjj.androidbootstrap.ui.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import iojjj.androidbootstrap.R;
import iojjj.androidbootstrap.ui.widgets.ObservableScrollView;

/**
 * Base activity with toolbar and scroll effect. For SDK {@link android.os.Build.VERSION_CODES#HONEYCOMB} and above
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ToolbarOverscrollActivity extends ToolbarActivity implements ObservableScrollView.IOnScrollChanged {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_lollipop_overscroll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup scroll
        final ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setOnScrollChanged(this);
    }

    @Override
    public void onScrollChanged(ObservableScrollView view, int l, int t, int oldl, int oldt) {
        final int toolbarHeight = 4 * toolbar.getHeight();
        int transition = - t / 2; // divided by 2 for parallax effect
        if (transition > 0) // prevent overscroll
            transition = 0;
        final float ratio = 1 - (float) Math.min(Math.max(t, 0), toolbarHeight) / toolbarHeight;
        //setting the new alpha value from 0-255 or transparent to opaque
        final int newAlpha = (int) (ratio * 255);
        toolbar.getBackground().setAlpha(newAlpha);
        toolbar.setTranslationY(transition);
        toolbarShadow.getBackground().setAlpha(newAlpha);
        toolbarShadow.setTranslationY(transition);
    }
}

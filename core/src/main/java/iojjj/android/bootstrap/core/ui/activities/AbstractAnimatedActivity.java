package iojjj.android.bootstrap.core.ui.activities;

import android.os.Bundle;

import iojjj.android.bootstrap.core.R;

/**
 * Activity with enter and exit animations
 */
public abstract class AbstractAnimatedActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(getEnterAnimation(), getExitAnimation());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(getReverseEnterAnimation(), getReverseExitAnimation());
    }

    protected int getEnterAnimation() {
        return R.anim.slide_up;
    }

    protected int getExitAnimation() {
        return R.anim.slide_down;
    }

    protected int getReverseEnterAnimation() {
        return R.anim.slide_up;
    }

    protected int getReverseExitAnimation() {
        return R.anim.slide_down;
    }
}

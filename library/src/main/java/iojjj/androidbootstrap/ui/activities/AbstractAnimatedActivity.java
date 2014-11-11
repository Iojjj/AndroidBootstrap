package iojjj.androidbootstrap.ui.activities;

import android.os.Bundle;

import iojjj.androidbootstrap.R;

/**
 * Activity with enter and exit animations
 */
public abstract class AbstractAnimatedActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }
}

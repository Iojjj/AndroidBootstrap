package iojjj.androidbootstrap.ui.activities;

import iojjj.androidbootstrap.utils.misc.AppFocusUtils;

/**
 * Base activity with detection if user is using app.
 */
public abstract class AbstractFocusableActivity extends AbstractActivity {

    private final AppFocusUtils appFocusUtils = new AppFocusUtils();


    @Override
    protected void onStart() {
        super.onStart();
        appFocusUtils.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        appFocusUtils.onStop(this);
    }
}

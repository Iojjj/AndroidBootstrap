package iojjj.android.bootstrap.core.app_focus;

import iojjj.android.bootstrap.core.ui.activities.AbstractActivity;

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

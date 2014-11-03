package iojjj.androidbootstrap.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import iojjj.androidbootstrap.services.AppFocusService;

/**
 * Base activity with detection if user is using app.
 */
public abstract class AbstractFocusableActivity extends AbstractActivity {

    private boolean isBound;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        isBound = bindService(new Intent(this, AppFocusService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound)
            unbindService(connection);
    }
}

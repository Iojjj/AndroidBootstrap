package iojjj.android.bootstrap.core.app_focus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;

/**
 * Utils for detecting if app has a focus
 */
public class AppFocusUtils {

    /**
     * Flag determined that activity was bound to service
     */
    private boolean isBound;

    /**
     * Connection with service
     */
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void onStart(@NonNull final Context context) {
        isBound = context.bindService(new Intent(context, AppFocusService.class), connection, Context.BIND_AUTO_CREATE);
    }

    public void onStop(@NonNull final Context context) {
        if (isBound)
            context.unbindService(connection);
    }
}

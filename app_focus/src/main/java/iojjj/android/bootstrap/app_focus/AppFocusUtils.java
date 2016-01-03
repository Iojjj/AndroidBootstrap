package iojjj.android.bootstrap.app_focus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Utils for detecting if app has a focus.<br /><br />
 * Create instance of this utils in your activity and call {@link #onStart(Context)} and {@link #onStop(Context)} methods.
 * Then register your instance of {@link ForegroundStateReceiver} with {@link #registerReceiver(Context, ForegroundStateReceiver)} method.
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

    public static void registerReceiver(@NonNull Context context, @NonNull ForegroundStateReceiver receiver) {
        AssertionUtils.assertNotNull(context, "Context");
        AssertionUtils.assertNotNull(receiver, "Receiver");
        IntentFilter filter = new IntentFilter(AppFocusService.ACTION_FOREGROUND_APPLICATION);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
        filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(new ScreenStateReceiver(), filter);
    }
}

package iojjj.android.bootstrap.core.app_focus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Service for detecting if app is visible to user
 */
public class AppFocusService extends Service {
    public static final String ACTION_FOREGROUND_APPLICATION = AppFocusService.class.getName() + "ACTION_FOREGROUND_APPLICATION";
    public static final String EXTRA_IS_FOREGROUND = AppFocusService.class.getName() + "EXTRA_IS_FOREGROUND";

    private final IBinder mBinder = new LocalBinder();

    private static boolean lastForegroundState = false;
    private static boolean foreground = false;

    public static boolean isForeground() {
        return lastForegroundState;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        foreground = true;
        checkAndSendBroadcast(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        foreground = false;
        checkAndSendBroadcast(this);
        return false;
    }

    public static void checkAndSendBroadcast(@NonNull Context context) {
        final boolean foregroundState = foreground && ScreenStateReceiver.IS_SCREEN_ON;
        if (lastForegroundState != foregroundState) {
            lastForegroundState = foregroundState;
            Intent intent = new Intent(ACTION_FOREGROUND_APPLICATION);
            intent.putExtra(EXTRA_IS_FOREGROUND, foregroundState);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    public class LocalBinder extends Binder {
        AppFocusService getService() {
            return AppFocusService.this;
        }
    }
}

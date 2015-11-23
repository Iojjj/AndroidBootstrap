package iojjj.android.bootstrap.core.app_focus;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Base application with detection if user is using app.
 */
public abstract class FocusableApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ForegroundStateReceiver receiver = getForegroundTaskReceiver();
        if (receiver != null) {
            IntentFilter filter = new IntentFilter(AppFocusService.ACTION_FOREGROUND_APPLICATION);
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
            filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(new ScreenStateReceiver(), filter);
        }
    }

    @Nullable
    protected abstract ForegroundStateReceiver getForegroundTaskReceiver();
}

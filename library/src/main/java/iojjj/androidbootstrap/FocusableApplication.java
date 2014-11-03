package iojjj.androidbootstrap;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import iojjj.androidbootstrap.receivers.ForegroundStateReceiver;
import iojjj.androidbootstrap.receivers.ScreenStateReceiver;
import iojjj.androidbootstrap.services.AppFocusService;

/**
 * Base application with detection if user is using app.
 */
public class FocusableApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(AppFocusService.ACTION_FOREGROUND_APPLICATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new ForegroundStateReceiver() {
            @Override
            protected void onForegroundStateChanged(boolean isForeground) {
                // nothing to do
            }
        }, filter);

        filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new ScreenStateReceiver(), filter);
    }
}

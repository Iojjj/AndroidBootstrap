package iojjj.android.bootstrap.core.app_focus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
* Receiver detects when user opens or closes app and sends presence
*/
public abstract class ForegroundStateReceiver extends BroadcastReceiver {

    private boolean isForeground;


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        final boolean isForeground;
        if (AppFocusService.ACTION_FOREGROUND_APPLICATION.equals(action)) {
            isForeground = intent.getBooleanExtra(AppFocusService.EXTRA_IS_FOREGROUND, false);
        } else {
            if (!AppFocusService.isForeground()) {
                return;
            }
            isForeground = Intent.ACTION_SCREEN_ON.equals(action);
        }

        if (this.isForeground != isForeground) {
            onForegroundStateChanged(this.isForeground = isForeground);
        }
    }

    protected abstract void onForegroundStateChanged(final boolean isForeground);
}

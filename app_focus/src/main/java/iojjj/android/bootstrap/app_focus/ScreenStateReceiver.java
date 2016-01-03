package iojjj.android.bootstrap.app_focus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receiver for {@link android.content.Intent#ACTION_SCREEN_ON} and {@link android.content.Intent#ACTION_SCREEN_OFF} intents.<br />
 * Receiver <b>MUST</b> be registered in code.
 */
class ScreenStateReceiver extends BroadcastReceiver {

    public static boolean IS_SCREEN_ON = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            IS_SCREEN_ON = true;
            AppFocusService.checkAndSendBroadcast(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            IS_SCREEN_ON = false;
            AppFocusService.checkAndSendBroadcast(context);
        }
    }
}

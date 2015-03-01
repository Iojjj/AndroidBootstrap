package iojjj.androidbootstrap.utils.misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Helper for miscellaneous operations
 */
public class MiscellaneousUtils {

    private static final String EXTRA_BASE = "EXTRA_";
    private static final String ACTION_BASE = "ACTION_";

    private static final String ACTION_TOAST = getAction("toast");
    public static final String EXTRA_TEXT = getExtra("text");
    public static final String EXTRA_DURATION = getExtra("duration");

    public static String getExtra(@NonNull String extraName) {
        return EXTRA_BASE + extraName.toUpperCase();
    }

    public static String getAction(@NonNull String actionName) {
        return ACTION_BASE + actionName.toUpperCase();
    }

    public static void showToast(@NonNull Context context, @NonNull String text, int duration) {
        Intent intent = new Intent(ACTION_TOAST);
        intent.putExtra(EXTRA_TEXT, text);
        intent.putExtra(EXTRA_DURATION, duration);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerToastReceiver(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(ACTION_TOAST));
    }

    public static void unregisterToastReceiver(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
}

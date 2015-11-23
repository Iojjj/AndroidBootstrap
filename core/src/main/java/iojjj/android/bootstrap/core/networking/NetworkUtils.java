package iojjj.android.bootstrap.core.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Network utils
 */
public class NetworkUtils {

    /**
     * Determine if device connected to network
     * @param context context
     * @return true if connected, false otherwise
     */
    @SuppressWarnings("deprecation")
    public static boolean isConnected(@NonNull final Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return wifi != null && wifi.isConnected() || mobile != null && mobile.isConnected();
        } else {
            boolean connected = false;
            for (Network network : connMgr.getAllNetworks()) {
                NetworkInfo info = connMgr.getNetworkInfo(network);
                connected |= info != null && info.isConnected();
            }
            return connected;
        }
    }
}

package iojjj.androidbootstrap.utils.misc;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import iojjj.androidbootstrap.ui.fragments.SimpleDialogFragment;

/**
 * Utils for detecting if Google Play Services is installed and up to date
 */
public class GooglePlayServicesUtils {

    private static final int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 0x1234;

    private static final IGooglePlayServicesCallback DEFAULT = new IGooglePlayServicesCallback() {
        @Override
        public void onGooglePlayServicesAvailable() {

        }

        @Override
        public void onGooglePlayServicesNotAvailable(int resultCode) {

        }

        @Override
        public void onGooglePlayServicesUpdateCancelled() {

        }
    };

    private IGooglePlayServicesCallback callback;
    private int requestCode;

    public GooglePlayServicesUtils() {
        callback = DEFAULT;
        requestCode = GOOGLE_PLAY_SERVICES_REQUEST_CODE;
    }

    public GooglePlayServicesUtils setCallback(IGooglePlayServicesCallback callback) {
        if (callback == null)
            callback = DEFAULT;
        this.callback = callback;
        return this;
    }

    public GooglePlayServicesUtils setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * Call this method in activity's onCreate
     * @param context context
     */
    public void onCreate(@NonNull final Context context) {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result == ConnectionResult.SUCCESS)
            callback.onGooglePlayServicesAvailable();
        else
            callback.onGooglePlayServicesNotAvailable(result);
    }

    /**
     * Call this method in activity's onActivityResult
     * @param context context
     * @param requestCode request code
     * @param resultCode result code
     */
    public void onActivityResult(@NonNull final Context context, final int requestCode, final int resultCode) {
        if (requestCode == this.requestCode) {
            int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (result == ConnectionResult.SUCCESS)
                callback.onGooglePlayServicesAvailable();
            else
                callback.onGooglePlayServicesUpdateCancelled();
        }
    }

    /**
     * Show Google Play Services error dialog
     * @param activity hosted activity
     * @param resultCode error code from Google Play Services
     */
    public void showErrorDialog(@NonNull final AppCompatActivity activity, int resultCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                resultCode,
                activity,
                requestCode);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            SimpleDialogFragment errorFragment = new SimpleDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(activity.getSupportFragmentManager(), "wl_update_google_play_services");
        }
    }

    public interface IGooglePlayServicesCallback {
        /**
         * This method will be called if GPS are ready to use
         */
        void onGooglePlayServicesAvailable();

        /**
         * This method will be called if there are any errors found.
         * @param resultCode error code from Google Play Services
         */
        void onGooglePlayServicesNotAvailable(final int resultCode);

        /**
         * This method will be called if user cancels GPS update process
         */
        void onGooglePlayServicesUpdateCancelled();
    }
}

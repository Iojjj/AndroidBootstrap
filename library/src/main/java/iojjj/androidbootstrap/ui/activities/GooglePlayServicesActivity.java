package iojjj.androidbootstrap.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import iojjj.androidbootstrap.ui.fragments.SimpleDialogFragment;


public abstract class GooglePlayServicesActivity extends AbstractActivity {

    protected static final int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 0x00001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS)
            onGooglePlayServicesAvailable();
        else
            onGooglePlayServicesNotAvailable(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PLAY_SERVICES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (result == ConnectionResult.SUCCESS)
                    onGooglePlayServicesAvailable();
                else
                    onGooglePlayServicesNotAvailable(resultCode);
            } else {
                onGooglePlayServicesUpdateCancelled();
            }
        }
    }

    protected abstract void onGooglePlayServicesAvailable();
    protected abstract void onGooglePlayServicesNotAvailable(final int resultCode);
    protected abstract void onGooglePlayServicesUpdateCancelled();

    /**
     * Show Google Play Services error dialog
     * @param resultCode error code from Google Play Services
     */
    public void showErrorDialog(int resultCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                resultCode,
                this,
                GOOGLE_PLAY_SERVICES_REQUEST_CODE);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            SimpleDialogFragment errorFragment = new SimpleDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "wl_update_google_play_services");
        }
    }
}

package iojjj.androidbootstrap.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import iojjj.androidbootstrap.ui.fragments.GooglePlayServicesFragment;


public abstract class GooglePlayServicesActivity extends AbstractActivity {

    protected static final int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 0x00001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkServicesConnected())
            onGooglePlayServicesAvailable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PLAY_SERVICES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (checkServicesConnected())
                    onGooglePlayServicesAvailable();
                else
                    finish();
            } else {
                finish();
            }
        }
    }

    protected abstract void onGooglePlayServicesAvailable();

    /**
     * Check if Google Play Services are available and have latest version
     * @return true if all ok, false otherwise
     */
    private boolean checkServicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }
        // Google Play services was not available for some reason
        // Get the error dialog from Google Play services
        showErrorDialog(resultCode);
        return false;
    }

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
            GooglePlayServicesFragment errorFragment = new GooglePlayServicesFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "wl_update_google_play_services");
        }
    }
}

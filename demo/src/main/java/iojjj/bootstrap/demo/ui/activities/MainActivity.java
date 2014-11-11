package iojjj.bootstrap.demo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import iojjj.androidbootstrap.ui.activities.ToolbarActivity;
import iojjj.androidbootstrap.utils.misc.GooglePlayServicesUtils;
import iojjj.androidbootstrap.utils.threading.ThreadUtils;
import iojjj.bootstrap.demo.R;
import iojjj.bootstrap.demo.ui.fragments.ListFragment;


public class MainActivity extends ToolbarActivity implements GooglePlayServicesUtils.IGooglePlayServicesCallback {

    private final GooglePlayServicesUtils gpsUtils = new GooglePlayServicesUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.activity_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        gpsUtils.setCallback(this)
                .setRequestCode(1)
                .onCreate(this);
    }

    @Override
    public void onGooglePlayServicesAvailable() {
        replaceFragment(new ListFragment(), null, false);
    }

    @Override
    public void onGooglePlayServicesNotAvailable(final int resultCode) {
        // Google Play services was not available for some reason
        // Get the error dialog from Google Play services
        gpsUtils.showErrorDialog(this, resultCode);
    }

    @Override
    public void onGooglePlayServicesUpdateCancelled() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gpsUtils.onActivityResult(this, requestCode, resultCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            startRotation(item);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stopRotation(item);
                }
            }, 2000);
            return true;
        } else if (item.getItemId() == R.id.action_activity) {
            startActivity(new Intent(this, SimpleActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_recycler_view) {
            startActivity(new Intent(this, RecyclerViewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContainerId() {
        return R.id.container;
    }
}

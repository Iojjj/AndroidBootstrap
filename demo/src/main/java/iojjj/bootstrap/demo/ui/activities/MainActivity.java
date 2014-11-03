package iojjj.bootstrap.demo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import iojjj.androidbootstrap.ui.activities.GooglePlayServicesActivity;
import iojjj.androidbootstrap.ui.fragments.SimpleWebViewFragment;
import iojjj.bootstrap.demo.R;


public class MainActivity extends GooglePlayServicesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onGooglePlayServicesAvailable() {
        replaceFragment(SimpleWebViewFragment.instance("https://google.com/"), null, false);
    }

    @Override
    protected void onGooglePlayServicesNotAvailable(final int resultCode) {
        // Google Play services was not available for some reason
        // Get the error dialog from Google Play services
        showErrorDialog(resultCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toolbar) {
            startActivity(new Intent(this, ToolbarActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onGooglePlayServicesUpdateCancelled() {
        finish();
    }

    @Override
    protected int getContainerId() {
        return R.id.root;
    }
}

package iojjj.android.bootstrap.core.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import iojjj.android.bootstrap.core.interfaces.IBackPressable;
import iojjj.android.bootstrap.utils.MiscellaneousUtils;

public abstract class AbstractActivity extends AppCompatActivity {

    private BroadcastReceiver toastReceiver;
    private Toast toast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (shouldSubscribeForToast()) {
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            toastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    showToast(intent.getStringExtra(MiscellaneousUtils.EXTRA_TEXT), intent.getIntExtra(MiscellaneousUtils.EXTRA_DURATION, Toast.LENGTH_SHORT));
                }
            };
            MiscellaneousUtils.registerToastReceiver(this, toastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        if (shouldSubscribeForToast())
            MiscellaneousUtils.unregisterToastReceiver(this, toastReceiver);
        super.onDestroy();
    }

    private void showToast(String text, int duration) {
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

    /**
     * Get id of fragment's container
     * @return id of container
     */
    protected abstract int getContainerId();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // default behavior for home button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(getContainerId());
        if (fragment instanceof IBackPressable && ((IBackPressable) fragment).onBackPressed())
            return;
        super.onBackPressed();
    }

    protected boolean shouldSubscribeForToast() {
        return true;
    }
}

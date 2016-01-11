package iojjj.android.bootstrap.core.ui.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // default behavior for home button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean shouldSubscribeForToast() {
        return true;
    }
}

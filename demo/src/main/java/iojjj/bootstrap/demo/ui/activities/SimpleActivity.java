package iojjj.bootstrap.demo.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import iojjj.androidbootstrap.ui.activities.AbstractAnimatedActivity;
import iojjj.androidbootstrap.utils.threading.ThreadUtils;
import iojjj.bootstrap.demo.R;

/**
 * Created by Александр on 03.11.2014.
 */
public class SimpleActivity extends AbstractAnimatedActivity {

    @Override
    protected int getContainerId() {
        return R.id.root;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_simple, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }
}

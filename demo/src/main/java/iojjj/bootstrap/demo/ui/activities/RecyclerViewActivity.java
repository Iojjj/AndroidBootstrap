package iojjj.bootstrap.demo.ui.activities;

import android.os.Bundle;

import iojjj.androidbootstrap.ui.activities.ToolbarActivity;
import iojjj.bootstrap.demo.R;
import iojjj.bootstrap.demo.ui.fragments.RecyclerViewFragment;

/**
 * Host activity for {@link iojjj.bootstrap.demo.ui.fragments.RecyclerViewFragment}
 */
public class RecyclerViewActivity extends ToolbarActivity {

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lollipop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null)
            addFragment(new RecyclerViewFragment(), null, false);
    }
}

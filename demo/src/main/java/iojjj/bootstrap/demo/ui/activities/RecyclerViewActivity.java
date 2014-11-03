package iojjj.bootstrap.demo.ui.activities;

import android.os.Bundle;

import iojjj.androidbootstrap.ui.activities.AbstractAnimatedActivity;
import iojjj.bootstrap.demo.R;
import iojjj.bootstrap.demo.ui.fragments.RecyclerViewFragment;

/**
 * Created by Александр on 03.11.2014.
 */
public class RecyclerViewActivity extends AbstractAnimatedActivity {

    @Override
    protected int getContainerId() {
        return R.id.root;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null)
            addFragment(new RecyclerViewFragment(), null, false);
    }
}

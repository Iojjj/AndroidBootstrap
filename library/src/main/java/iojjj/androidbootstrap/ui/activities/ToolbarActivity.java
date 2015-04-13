package iojjj.androidbootstrap.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import iojjj.androidbootstrap.R;
import iojjj.androidbootstrap.interfaces.IToolbarActivity;

/**
 * Base activity with toolbar
 */
public abstract class ToolbarActivity extends AbstractAnimatedActivity implements IToolbarActivity {

    public static final String EXTRA_COLOR = "color_extra";

    protected Toolbar toolbar;
    protected View toolbarShadow;

    @Override
    protected int getContainerId() {
        return R.id.container;
    }

    protected int getLayoutResource() {
        return R.layout.activity_lollipop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarShadow = findViewById(R.id.toolbar_shadow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        int color = getIntent().getIntExtra(EXTRA_COLOR, -1);
        if (color != -1) {
            toolbar.setBackgroundColor(color);
        }
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

}

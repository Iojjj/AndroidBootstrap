package iojjj.bootstrap.demo;

import org.androidannotations.annotations.EActivity;

import iojjj.androidbootstrap.ui.activities.GooglePlayServicesActivity;
import iojjj.androidbootstrap.ui.fragments.SimpleWebViewFragment_;


@EActivity(R.layout.activity_main)
public class MainActivity extends GooglePlayServicesActivity {

    @Override
    protected void onGooglePlayServicesAvailable() {
        setContentView(R.layout.activity_main);
        replaceFragment(SimpleWebViewFragment_.instance("https://google.com"), null, false);
    }

    @Override
    protected int getContainerId() {
        return R.id.root;
    }
}

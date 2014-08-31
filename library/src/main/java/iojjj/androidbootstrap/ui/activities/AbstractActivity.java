package iojjj.androidbootstrap.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;


public abstract class AbstractActivity extends ActionBarActivity {

    protected abstract int getContainerId();

    protected void replaceFragment(Fragment fragment, String tag, boolean addToStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(getContainerId(), fragment);
        if (addToStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }
}

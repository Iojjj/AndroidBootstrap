package iojjj.android.bootstrap.core.ui.fragments;

import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Base fragment
 */
public abstract class AbstractListFragment extends ListFragment {

    public ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}

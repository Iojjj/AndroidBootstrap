package iojjj.android.bootstrap.core.ui.fragments;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Base dialog fragment
 */
public abstract class AbstractDialogFragment extends DialogFragment {

    public ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}

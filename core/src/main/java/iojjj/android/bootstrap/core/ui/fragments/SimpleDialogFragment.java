package iojjj.android.bootstrap.core.ui.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Simple DialogFragment class that displays provided dialog
 */
public class SimpleDialogFragment extends AbstractDialogFragment {
    // Global field to contain the error dialog
    private Dialog mDialog;

    // Default constructor. Sets the dialog field to null
    public SimpleDialogFragment() {
        super();
        mDialog = null;
    }

    // Set the dialog to display
    public SimpleDialogFragment setDialog(Dialog dialog) {
        mDialog = dialog;
        return this;
    }

    // Return a Dialog to the DialogFragment.
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
}

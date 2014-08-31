package iojjj.androidbootstrap.ui.fragments;

import android.content.DialogInterface;

public class GooglePlayServicesFragment extends SimpleDialogFragment {

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }
}

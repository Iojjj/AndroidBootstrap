package iojjj.androidbootstrap.ui.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import iojjj.androidbootstrap.interfaces.IFragmentManager;

/**
 * Base dialog fragment
 */
public class AbstractDialogFragment extends DialogFragment implements IFragmentManager {

    private IFragmentManager fragmentManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IFragmentManager)
            fragmentManager = (IFragmentManager) activity;
        else
            throw new IllegalArgumentException("Activity must implement " + IFragmentManager.class.getName() + " interface");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentManager = null;
    }

    @Override
    public void addFragment(@NonNull Fragment fragment) {
        if (isAdded())
            fragmentManager.addFragment(fragment);
    }

    @Override
    public void addFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        if (isAdded())
            fragmentManager.addFragment(fragment, tag, addToBackStack);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment) {
        if (isAdded())
            fragmentManager.replaceFragment(fragment);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        if (isAdded())
            fragmentManager.replaceFragment(fragment, tag, addToBackStack);
    }
}

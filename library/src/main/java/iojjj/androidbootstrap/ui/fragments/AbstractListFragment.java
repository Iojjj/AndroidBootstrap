package iojjj.androidbootstrap.ui.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import iojjj.androidbootstrap.interfaces.IFragmentManager;

/**
 * Base fragment
 */
public class AbstractListFragment extends ListFragment implements IFragmentManager {

    private IFragmentManager fragmentManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof AppCompatActivity))
            throw new IllegalArgumentException("Activity must extend " + AppCompatActivity.class.getSimpleName());
        if (activity instanceof IFragmentManager)
            fragmentManager = (IFragmentManager) activity;
        else
            throw new IllegalArgumentException("Activity must implement " + IFragmentManager.class.getName() + " interface");
    }

    @Override
    public void onDetach() {
        fragmentManager = null;
        super.onDetach();
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
    public void addFragment(@NonNull Fragment fragment, int containerId) {
        if (isAdded())
            fragmentManager.addFragment(fragment, containerId);
    }

    @Override
    public void addFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack, int containerId) {
        if (isAdded())
            fragmentManager.addFragment(fragment, tag, addToBackStack, containerId);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment) {
        if (isAdded())
            fragmentManager.replaceFragment(fragment);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, int containerId) {
        if (isAdded())
            fragmentManager.replaceFragment(fragment, containerId);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack) {
        if (isAdded())
            fragmentManager.replaceFragment(fragment, tag, addToBackStack);
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addToBackStack, int containerId) {
        if (isAdded())
            fragmentManager.replaceFragment(fragment, tag, addToBackStack, containerId);
    }

    public ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}

package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.github.iojjj.bootstrap.core.ui.fragments.ProgressDialogFragmentDelegate.KEY_MESSAGE;

/**
 * Implementation of {@link ProgressDialog} wrapped in {@link DialogFragment}.
 *
 * @since 1.0
 */
public class BSProgressDialogFragment extends DialogFragment
        implements ProgressDialogFragmentDelegate.InnerDialogFragment<DialogFragment> {

    private final ProgressDialogFragmentDelegate<DialogFragment> mDelegate;

    public BSProgressDialogFragment() {
        mDelegate = new ProgressDialogFragmentDelegate<>(this);
    }

    @NonNull
    public static BSProgressDialogFragment newInstance() {
        Bundle args = new Bundle();
        BSProgressDialogFragment fragment = new BSProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static BSProgressDialogFragment newInstanceImpl(@Nullable String message) {
        final BSProgressDialogFragment fragment = newInstance();
        fragment.getArguments().putString(KEY_MESSAGE, message);
        return fragment;
    }

    /**
     * Get fragment's manager.
     * @return fragment's manager
     */
    public Manager getManager() {
        return mDelegate.getManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(getArguments());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDelegate.onCreateDialog(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(getArguments());
    }

    @NonNull
    @Override
    public DialogFragment newInstance(@Nullable String message) {
        return newInstanceImpl(message);
    }

    @Override
    public void show(DialogFragment dialogFragment, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dialogFragment.show(getChildFragmentManager(), tag);
        } else {
            dialogFragment.show(getFragmentManager(), tag);
        }
    }

    @Override
    public void dismissAllowingStateLoss(DialogFragment dialogFragment) {
        dialogFragment.dismissAllowingStateLoss();
    }

    @Override
    public boolean isAdded(DialogFragment dialogFragment) {
        return dialogFragment.isAdded();
    }

    @Override
    public DialogFragment findFragmentByTag(String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return (DialogFragment) getChildFragmentManager().findFragmentByTag(tag);
        }
        return (DialogFragment) getFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void setCancelable(DialogFragment dialogFragment, boolean cancelable) {
        dialogFragment.setCancelable(cancelable);
    }

    public interface Manager extends ProgressDialogFragmentDelegate.Manager {

    }
}

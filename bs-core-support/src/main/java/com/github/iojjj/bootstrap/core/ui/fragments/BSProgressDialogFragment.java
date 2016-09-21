package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import static com.github.iojjj.bootstrap.core.ui.fragments.ProgressDialogFragmentDelegate.KEY_MESSAGE;

/**
 * Implementation of {@link ProgressDialog} wrapped in {@link DialogFragment}.
 *
 * @since 1.0
 */
public class BSProgressDialogFragment extends DialogFragment implements ProgressDialogFragmentDelegate.InnerDialogFragment<DialogFragment> {

    private final ProgressDialogFragmentDelegate<DialogFragment> mDelegate;
    private final Manager mManager;

    public BSProgressDialogFragment() {
        mDelegate = new ProgressDialogFragmentDelegate<>(this);
        final ProgressDialogFragmentDelegate.Manager manager = mDelegate.getManager();
        mManager = new Manager() {
            @Override
            public void showProgressDialog(@Nullable String message) {
                manager.showProgressDialog(message);
            }

            @Override
            public void hideProgressDialog() {
                manager.hideProgressDialog();
            }

            @Override
            public void setMessage(@Nullable String message) {
                manager.setMessage(message);
            }

            @Override
            public void setCancelable(boolean isCancelable) {
                manager.setCancelable(isCancelable);
            }
        };
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
        return mManager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(getArguments());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDelegate.onCreateDialog(getContext());
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
        dialogFragment.show(getChildFragmentManager(), tag);
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
        return (DialogFragment) getChildFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void setCancelable(DialogFragment dialogFragment, boolean cancelable) {
        dialogFragment.setCancelable(cancelable);
    }

    public interface Manager extends ProgressDialogFragmentDelegate.Manager {

    }
}

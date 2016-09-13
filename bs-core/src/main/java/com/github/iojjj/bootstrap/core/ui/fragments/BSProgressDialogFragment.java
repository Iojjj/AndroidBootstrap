package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.DialogFragment;

/**
 * Implementation of {@link ProgressDialog} wrapped in {@link DialogFragment}.
 *
 * @since 1.0
 */
public class BSProgressDialogFragment extends DialogFragment {

    private static final String KEY_MESSAGE = "MESSAGE";

    private String mMessage;

    public static Manager newManager() {
        return new ManagerImpl();
    }

    static BSProgressDialogFragment newInstance(@Nullable String message) {
        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);
        BSProgressDialogFragment fragment = new BSProgressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString(KEY_MESSAGE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(mMessage);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(true);
        return pd;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getArguments().putString(KEY_MESSAGE, mMessage);
    }

    public void setMessage(@Nullable String message) {
        mMessage = message;
        final ProgressDialog dialog = (ProgressDialog) getDialog();
        if (dialog != null) {
            dialog.setMessage(message);
        }
    }

    /**
     * Progress dialog manager.
     */
    public interface Manager {

        /**
         * Show progress dialog.
         *
         * @param fragmentManager instance of FragmentManager to use to display a fragment
         * @param message         message to display in progress dialog
         */
        void showProgressDialog(@NonNull FragmentManager fragmentManager, @NonNull String message);

        /**
         * Hide progress dialog.
         */
        void hideProgressDialog();

        /**
         * Set cancelable flag for progress dialog.
         *
         * @param isCancellable true if dialog should be cancelable, false otherwise
         */
        void setCancellable(boolean isCancellable);
    }

    private static class ManagerImpl implements Manager {

        private static final String TAG_PROGRESS_DIALOG = "ProgressDialog";

        private BSProgressDialogFragment mProgressDialogFragment;
        private boolean mCancellable;

        @Override
        public void showProgressDialog(@NonNull FragmentManager fragmentManager, @NonNull String message) {
            if (mProgressDialogFragment == null) {
                mProgressDialogFragment = newInstance(message);
                mProgressDialogFragment.setCancelable(mCancellable);
            }
            mProgressDialogFragment.setMessage(message);
            final DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(TAG_PROGRESS_DIALOG);
            if (fragment != null && fragment.isAdded()) {
                fragment.dismissAllowingStateLoss();
            }
            if (!mProgressDialogFragment.isAdded()) {
                mProgressDialogFragment.show(fragmentManager, TAG_PROGRESS_DIALOG);
            }
        }

        @Override
        public void hideProgressDialog() {
            if (mProgressDialogFragment != null) {
                mProgressDialogFragment.dismissAllowingStateLoss();
            }
        }

        @Override
        public void setCancellable(boolean isCancellable) {
            if (mProgressDialogFragment != null) {
                mProgressDialogFragment.setCancelable(isCancellable);
            }
            mCancellable = isCancellable;
        }
    }
}

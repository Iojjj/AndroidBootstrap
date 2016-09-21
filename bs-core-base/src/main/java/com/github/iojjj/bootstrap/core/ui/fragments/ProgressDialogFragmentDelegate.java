package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

class ProgressDialogFragmentDelegate<TFragment> {

    static final String KEY_MESSAGE = "MESSAGE";
    private final InnerDialogFragment<TFragment> mInnerDialogFragment;
    private final Manager mManager;
    private String mMessage;

    ProgressDialogFragmentDelegate(@NonNull InnerDialogFragment<TFragment> innerDialogFragment) {
        BSAssertions.assertNotNull(innerDialogFragment, "innerDialogFragment");
        mInnerDialogFragment = innerDialogFragment;
        mManager = new ManagerImpl<>(this);
    }

    void onCreate(@NonNull Bundle args) {
        BSAssertions.assertNotNull(args, "args");
        mMessage = args.getString(KEY_MESSAGE);
    }

    @NonNull
    Dialog onCreateDialog(@NonNull Context context) {
        BSAssertions.assertNotNull(context, "context");
        android.app.ProgressDialog pd = new android.app.ProgressDialog(context);
        pd.setMessage(mMessage);
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(true);
        return pd;
    }

    void onSaveInstanceState(@NonNull Bundle args) {
        BSAssertions.assertNotNull(args, "args");
        args.putString(KEY_MESSAGE, mMessage);
    }

    private void setMessage(@Nullable String message) {
        mMessage = message;
        final ProgressDialog dialog = (ProgressDialog) mInnerDialogFragment.getDialog();
        if (dialog != null) {
            dialog.setMessage(message);
        }
    }

    @NonNull
    private TFragment newInstance(@Nullable String message) {
        return mInnerDialogFragment.newInstance(message);
    }

    private void show(TFragment fragment, String tag) {
        mInnerDialogFragment.show(fragment, tag);
    }

    private void dismissAllowingStateLoss(TFragment fragment) {
        mInnerDialogFragment.dismissAllowingStateLoss(fragment);
    }

    private boolean isAdded(TFragment fragment) {
        return mInnerDialogFragment.isAdded(fragment);
    }

    private TFragment findFragmentByTag(String tag) {
        return mInnerDialogFragment.findFragmentByTag(tag);
    }

    private void setCancelable(TFragment fragment, boolean cancelable) {
        mInnerDialogFragment.setCancelable(fragment, cancelable);
    }

    <T extends Manager> T getManager() {
        //noinspection unchecked
        return (T) mManager;
    }

    /**
     * Inner progress dialog fragment.
     * @param <TFragment> type of fragment
     */
    interface InnerDialogFragment<TFragment> {

        @Nullable
        Dialog getDialog();

        @NonNull
        TFragment newInstance(@Nullable String message);

        void show(TFragment fragment, String tag);

        void dismissAllowingStateLoss(TFragment fragment);

        boolean isAdded(TFragment fragment);

        TFragment findFragmentByTag(String tag);

        void setCancelable(TFragment fragment, boolean cancelable);
    }

    /**
     * Progress dialog manager.
     */
    interface Manager {

        /**
         * Show progress dialog.
         *
         * @param message         message to display in progress dialog
         */
        void showProgressDialog(@Nullable String message);

        /**
         * Hide progress dialog.
         */
        void hideProgressDialog();

        /**
         * Set message of progress dialog
         * @param message any message
         */
        void setMessage(@Nullable String message);

        /**
         * Set cancelable flag for progress dialog.
         *
         * @param isCancelable true if dialog should be cancelable, false otherwise
         */
        void setCancelable(boolean isCancelable);
    }

    private static class ManagerImpl<TFragment> implements Manager {

        private static final String TAG_PROGRESS_DIALOG = "ProgressDialog";

        private final ProgressDialogFragmentDelegate<TFragment> mDelegate;
        private TFragment mInnerDialogFragment;
        private boolean mCancellable;

        ManagerImpl(@NonNull ProgressDialogFragmentDelegate<TFragment> delegate) {
            mDelegate = delegate;
        }

        @Override
        public void showProgressDialog(@Nullable String message) {
            if (mInnerDialogFragment == null) {
                mInnerDialogFragment = mDelegate.newInstance(message);
                mDelegate.setCancelable(mInnerDialogFragment, mCancellable);
            }
            mDelegate.setMessage(message);
            TFragment fragment = mDelegate.findFragmentByTag(TAG_PROGRESS_DIALOG);
            if (fragment != null && mDelegate.isAdded(fragment)) {
                mDelegate.dismissAllowingStateLoss(fragment);
            }
            if (!mDelegate.isAdded(mInnerDialogFragment)) {
                mDelegate.show(mInnerDialogFragment, TAG_PROGRESS_DIALOG);
            }
        }

        @Override
        public void setMessage(@Nullable String message) {
            mDelegate.setMessage(message);
        }

        @Override
        public void hideProgressDialog() {
            if (mInnerDialogFragment != null) {
                mDelegate.dismissAllowingStateLoss(mInnerDialogFragment);
            }
        }

        @Override
        public void setCancelable(boolean isCancelable) {
            if (mInnerDialogFragment != null) {
                mDelegate.setCancelable(mInnerDialogFragment, mCancellable);
            }
            mCancellable = isCancelable;
        }
    }
}

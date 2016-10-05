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
    private final Manager mManager;
    private final OuterDelegate<TFragment> mOuterDelegate;
    private String mMessage;

    ProgressDialogFragmentDelegate(@NonNull OuterDelegate<TFragment> outerDelegate) {
        BSAssertions.assertNotNull(outerDelegate, "outerDelegate");
        mOuterDelegate = outerDelegate;
        mManager = new ManagerImpl<>(this);
    }

    Manager getManager() {
        return mManager;
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

    private void dismissAllowingStateLoss(TFragment fragment) {
        mOuterDelegate.dismissAllowingStateLoss(fragment);
    }

    private TFragment findFragmentByTag(String tag) {
        return mOuterDelegate.findFragmentByTag(tag);
    }

    private boolean isAdded(TFragment fragment) {
        return mOuterDelegate.isAdded(fragment);
    }

    @NonNull
    private TFragment newInstance(@Nullable String message) {
        return mOuterDelegate.newInstance(message);
    }

    private void setCancelable(TFragment fragment, boolean cancelable) {
        mOuterDelegate.setCancelable(fragment, cancelable);
    }

    private void setMessage(@Nullable String message) {
        mMessage = message;
        final ProgressDialog dialog = (ProgressDialog) mOuterDelegate.getDialog();
        if (dialog != null) {
            dialog.setMessage(message);
        }
    }

    private void show(TFragment fragment, String tag) {
        mOuterDelegate.show(fragment, tag);
    }

    /**
     * Inner progress dialog fragment.
     *
     * @param <TFragment> type of fragment
     */
    interface OuterDelegate<TFragment> {

        void dismissAllowingStateLoss(TFragment fragment);

        TFragment findFragmentByTag(String tag);

        @Nullable
        Dialog getDialog();

        boolean isAdded(TFragment fragment);

        @NonNull
        TFragment newInstance(@Nullable String message);

        void setCancelable(TFragment fragment, boolean cancelable);

        void show(TFragment fragment, String tag);
    }

    /**
     * Progress dialog manager.
     */
    interface Manager {

        /**
         * Hide progress dialog.
         */
        void hideProgressDialog();

        /**
         * Set cancelable flag for progress dialog.
         *
         * @param isCancelable true if dialog should be cancelable, false otherwise
         */
        void setCancelable(boolean isCancelable);

        /**
         * Set message of progress dialog
         *
         * @param message any message
         */
        void setMessage(@Nullable String message);

        /**
         * Show progress dialog.
         *
         * @param message message to display in progress dialog
         */
        void showProgressDialog(@Nullable String message);
    }

    private static class ManagerImpl<TFragment> implements Manager {

        private static final String TAG_PROGRESS_DIALOG = "ProgressDialog";

        private final ProgressDialogFragmentDelegate<TFragment> mDelegate;
        private boolean mCancellable;
        private TFragment mInnerDialogFragment;

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
        public void hideProgressDialog() {
            if (mInnerDialogFragment != null) {
                mDelegate.dismissAllowingStateLoss(mInnerDialogFragment);
            }
        }

        @Override
        public void setMessage(@Nullable String message) {
            mDelegate.setMessage(message);
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

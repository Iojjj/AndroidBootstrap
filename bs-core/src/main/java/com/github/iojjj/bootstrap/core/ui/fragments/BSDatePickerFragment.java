package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import static com.github.iojjj.bootstrap.core.ui.fragments.DatePickerFragmentDelegate.EXTRA_DATE;

/**
 * Implementation of {@link DialogFragment} with {@link DatePickerDialog}. Result of date picking
 * will be returned in intent with {@link #RESULT_DATE} key.
 *
 * @since 1.0
 */
// TODO: 10.09.2016 set date picker mode
public class BSDatePickerFragment extends DialogFragment {

    public static final String RESULT_DATE = DatePickerFragmentDelegate.RESULT_DATE;

    private final DatePickerFragmentDelegate mDelegate;

    public BSDatePickerFragment() {
        mDelegate = new DatePickerFragmentDelegate(new OuterDelegateImpl(this));
    }

    /**
     * Create a new instance of date picker dialog fragment.
     *
     * @param date initial date in milliseconds
     *
     * @return new instance of date picker dialog fragment
     */
    public static BSDatePickerFragment newInstance(long date) {
        Bundle args = new Bundle();
        BSDatePickerFragment fragment = new BSDatePickerFragment();
        args.putLong(EXTRA_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDelegate.onCreateDialog(getArguments(), getActivity());
    }

    private static final class OuterDelegateImpl implements DatePickerFragmentDelegate.OuterDelegate {

        @NonNull
        private final BSDatePickerFragment mFragment;

        private OuterDelegateImpl(@NonNull BSDatePickerFragment fragment) {
            mFragment = fragment;
        }

        @Override
        public void onDateSet(int result, Intent data) {
            mFragment.getTargetFragment().onActivityResult(mFragment.getTargetRequestCode(), Activity.RESULT_OK, data);
        }

        @Override
        public void dismiss() {
            mFragment.dismiss();
        }
    }
}

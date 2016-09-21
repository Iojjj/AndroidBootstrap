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
public class DatePickerFragment extends DialogFragment implements DatePickerFragmentDelegate.InnerFragment {

    public static final String RESULT_DATE = DatePickerFragmentDelegate.RESULT_DATE;

    private final DatePickerFragmentDelegate mDelegate;

    public DatePickerFragment() {
        mDelegate = new DatePickerFragmentDelegate(this);
    }

    /**
     * Create a new instance of date picker dialog fragment.
     * @param date initial date in milliseconds
     * @return new instance of date picker dialog fragment
     */
    public static DatePickerFragment newInstance(long date) {
        Bundle args = new Bundle();
        DatePickerFragment fragment = new DatePickerFragment();
        args.putLong(EXTRA_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDelegate.onCreateDialog(getArguments(), getActivity());
    }

    @Override
    public void onDateSet(int result, Intent data) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
    }
}

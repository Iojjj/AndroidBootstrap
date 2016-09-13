package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.github.iojjj.bootstrap.core.BSConstantGenerator;

import java.util.Calendar;

/**
 * Implementation of {@link DialogFragment} with {@link DatePickerDialog}.
 *
 * @since 1.0
 */
// TODO: 10.09.2016 set date picker mode
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String EXTRA_DATE = BSConstantGenerator.extra("date");
    public static final String RESULT_DATE = BSConstantGenerator.result("date");

    /**
     * Create a new instance of date picker dialog fragment.
     * @param date initial date in milliseconds
     * @return new instance of date picker dialog fragment
     */
    public static DatePickerFragment newInstance(long date) {
        final Bundle args = new Bundle();
        final DatePickerFragment fragment = new DatePickerFragment();
        args.putLong(EXTRA_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final long date = getArguments().getLong(EXTRA_DATE);
        calendar.setTimeInMillis(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Intent data = new Intent();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        data.putExtra(RESULT_DATE, calendar.getTimeInMillis());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        dismiss();
    }
}

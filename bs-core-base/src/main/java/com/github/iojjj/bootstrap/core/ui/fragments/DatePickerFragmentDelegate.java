package com.github.iojjj.bootstrap.core.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.cg.BSConstantGenerator;

import java.util.Calendar;

class DatePickerFragmentDelegate implements DatePickerDialog.OnDateSetListener {

    static final String EXTRA_DATE = BSConstantGenerator.extra("date");
    static final String RESULT_DATE = BSConstantGenerator.result("date");
    private final OuterDelegate mOuterDelegate;

    DatePickerFragmentDelegate(@NonNull OuterDelegate outerDelegate) {
        BSAssertions.assertNotNull(outerDelegate, "outerDelegate");
        mOuterDelegate = outerDelegate;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Intent data = new Intent();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        data.putExtra(RESULT_DATE, calendar.getTimeInMillis());
        mOuterDelegate.onDateSet(Activity.RESULT_OK, data);
        mOuterDelegate.dismiss();
    }

    @NonNull
    Dialog onCreateDialog(@NonNull Bundle args, @NonNull Context context) {
        BSAssertions.assertNotNull(args, "args");
        final Calendar calendar = Calendar.getInstance();
        final long date = args.getLong(EXTRA_DATE);
        calendar.setTimeInMillis(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(context, this, year, month, day);
    }

    interface OuterDelegate {

        void dismiss();

        void onDateSet(int result, Intent data);
    }
}

package com.github.iojjj.bootstrap.core.utils;

import android.support.annotation.NonNull;

import com.github.iojjj.bootstrap.assertions.BSAssertions;
import com.github.iojjj.bootstrap.core.functions.BSFunction0;

import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link BSFunction0} that checks if an activity can be closed after user taps "Back" twice.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSBackPressUtil implements BSFunction0<Boolean> {

    private long mTimeout;
    private long mPrevCheck = -1;

    /**
     * Constructor with a timeout parameter.
     *
     * @param timeout timeout in milliseconds
     */
    public BSBackPressUtil(long timeout) {
        mTimeout = timeout;
    }

    /**
     * Constructor with a time span parameter.
     *
     * @param timeSpan time span
     * @param timeUnit instance of TimeUnit
     */
    public BSBackPressUtil(long timeSpan, @NonNull TimeUnit timeUnit) {
        this(TimeUnit.MILLISECONDS.convert(timeSpan, checkAndReturn(timeUnit)));
    }

    @NonNull
    private static TimeUnit checkAndReturn(@NonNull TimeUnit timeUnit) {
        BSAssertions.assertNotNull(timeUnit, "timeUnit");
        return timeUnit;
    }

    @Override
    public Boolean apply() {
        long timestamp = System.currentTimeMillis();
        if (mPrevCheck < 0 || timestamp - mPrevCheck > mTimeout) {
            mPrevCheck = timestamp;
            return false;
        }
        return true;
    }
}

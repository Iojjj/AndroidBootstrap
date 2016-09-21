package com.github.iojjj.bootstrap.core;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Helper class for checking if we can close an activity after user taps "Back" twice.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSFinishActivityManager {

    private long mTimeout;
    private long mPrevCheck = -1;

    public BSFinishActivityManager(long timeout) {
        mTimeout = timeout;
    }

    public BSFinishActivityManager(long timeSpan, @NonNull TimeUnit timeUnit) {
        this(TimeUnit.MILLISECONDS.convert(timeSpan, timeUnit));
    }

    public boolean shouldBeFinished() {
        long timestamp = System.currentTimeMillis();
        if (mPrevCheck < 0 || timestamp - mPrevCheck > mTimeout) {
            mPrevCheck = timestamp;
            return false;
        }
        return true;
    }
}

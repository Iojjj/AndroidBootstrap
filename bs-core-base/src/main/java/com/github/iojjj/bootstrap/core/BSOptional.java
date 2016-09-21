package com.github.iojjj.bootstrap.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.iojjj.bootstrap.assertions.BSAssertions;

import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a non-null value.
 * If a value is present, isPresent() will return true and get() will return the value.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BSOptional<T> {

    private final T mObject;

    private BSOptional(@Nullable T object) {
        mObject = object;
    }

    public static <T> BSOptional<T> from(@Nullable T object) {
        return new BSOptional<>(object);
    }

    public static <T> BSOptional<T> empty() {
        return new BSOptional<>(null);
    }

    /**
     * Check if wrapped object is not null.
     *
     * @return true if object is not null, false otherwise
     */
    public boolean isPresent() {
        return mObject != null;
    }

    /**
     * Get wrapped object or return default one passed as <code>_else</code> parameter.
     *
     * @param _else default object
     * @return wrapped or default object
     */
    public T orElse(@NonNull T _else) {
        BSAssertions.assertNotNull(_else, "Else parameter");
        if (mObject == null) {
            return _else;
        }
        return mObject;
    }

    /**
     * Get wrapped object.
     *
     * @return wrapped object
     * @throws NoSuchElementException if wrapped object is null
     */
    @NonNull
    public T get() throws NoSuchElementException {
        if (mObject == null) {
            throw new NoSuchElementException("Object is null");
        }
        return mObject;
    }
}

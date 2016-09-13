package com.github.iojjj.bootstrap.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Assertion functions useful for debugging.
 *
 * @since 1.0
 */
@SuppressWarnings({"WeakerAccess", "unused", "ConstantConditions"})
public class  BSAssertions {

    private static boolean sAssertionsEnabled = true;

    private BSAssertions() {
        //no instance
    }

    /**
     * Set flag that indicates if assertion checks should be performed.
     * @param assertionsEnabled true or false
     */
    public static void setAssertionsEnabled(boolean assertionsEnabled) {
        sAssertionsEnabled = assertionsEnabled;
    }

    /**
     * Check if object is not null or throws an exception otherwise.
     *
     * @param object        some object
     * @param parameterName parameter name
     * @throws AssertionError if object is null
     */
    public static <T> void assertNotNull(@Nullable T object, @NonNull String parameterName) throws AssertionError {
        assert object != null;
        if (sAssertionsEnabled && object == null) {
            throw new AssertionError("Parameter \"" + parameterName + "\"" + " can't be null.");
        }
    }

    /**
     * Check if object is null or throws an exception otherwise.
     *
     * @param object        some object
     * @param parameterName parameter name
     * @throws AssertionError if object is not null
     */
    public static <T> void assertNull(@Nullable T object, @NonNull String parameterName) throws AssertionError {
        assert object == null;
        if (sAssertionsEnabled && object != null) {
            throw new AssertionError("Parameter \"" + parameterName + "\"" + " must be null.");
        }
    }

    /**
     * Check if object is instance of specific class or throws an exception otherwise.
     *
     * @param object        some object
     * @param clazz         some class
     * @param parameterName parameter name
     * @throws AssertionError if object is not instance of specified class
     */
    public static <T> void assertInstanceOf(@NonNull T object, @NonNull Class<?> clazz, @NonNull String parameterName) throws AssertionError {
        assertNotNull(object, parameterName);
        assertNotNull(clazz, parameterName);
        if (sAssertionsEnabled && !clazz.isInstance(object)) {
            throw new AssertionError("Parameter \"" + parameterName + "\"" + " is not instance of " + clazz.getName() + ".");
        }
    }

    /**
     * Check if object not equals to another object or throws exception otherwise.
     *
     * @param object        some object
     * @param anotherObject some another object
     * @param parameterName parameter name
     * @throws AssertionError if object equals to another object
     */
    public static <T> void assertNotEquals(@NonNull T object, @NonNull T anotherObject, @NonNull String parameterName) throws AssertionError {
        assertNotNull(object, parameterName);
        assertNotNull(anotherObject, "anotherObject");
        if (sAssertionsEnabled && (object == anotherObject || object.equals(anotherObject))) {
            throw new AssertionError(parameterName + " can't be equal to " + String.valueOf(anotherObject) + ".");
        }
    }

    /**
     * Check if string is not empty or throw exception otherwise.
     *
     * @param string        some string
     * @param parameterName parameter name
     * @throws AssertionError if string is empty
     */
    public static void assertNotEmpty(@Nullable String string, @NonNull String parameterName) throws AssertionError {
        if (sAssertionsEnabled && (TextUtils.isEmpty(string) || TextUtils.isEmpty(string.trim()))) {
            throw new AssertionError("Parameter \"" + parameterName + "\"" + " can't be empty.");
        }
    }

    /**
     * Throws exception if condition is <code>false</code>.
     *
     * @param condition some condition
     * @param message   exception message
     * @throws AssertionError if condition is <code>false</code>
     */
    public static void assertTrue(boolean condition, @NonNull String message) throws AssertionError {
        if (sAssertionsEnabled && !condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Throws exception if condition is <code>true</code>.
     *
     * @param condition some condition
     * @param message   exception message
     * @throws AssertionError if condition is <code>true</code>
     */
    public static void assertFalse(boolean condition, @NonNull String message) throws AssertionError {
        if (sAssertionsEnabled && condition) {
            throw new AssertionError(message);
        }
    }
}

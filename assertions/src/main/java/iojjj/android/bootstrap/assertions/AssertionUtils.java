package iojjj.android.bootstrap.assertions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Simple assertion utils.
 */
public class AssertionUtils {

    private static boolean ASSERTIONS_ENABLED = true;

    public static void setAssertionsEnabled(boolean assertionsEnabled) {
        ASSERTIONS_ENABLED = assertionsEnabled;
    }

    private AssertionUtils() {}

    public static <T> void assertNotNull(@Nullable T object, @NonNull String parameterName) throws AssertionError {
        if (ASSERTIONS_ENABLED && object == null)
            throw new AssertionError(parameterName + " can't be null.");
    }

    public static <T> void assertInstanceOf(@NonNull T object, @NonNull Class<?> clazz, @NonNull String parameterName) throws AssertionError {
        if (ASSERTIONS_ENABLED && !clazz.isInstance(object))
            throw new AssertionError(parameterName + " is not instance of " + clazz.getName() + ".");
    }

    public static <T> void assertNotEquals(@NonNull T object, @NonNull T anotherObject, @NonNull String parameterName) throws AssertionError {
        if (ASSERTIONS_ENABLED && object == anotherObject || object.equals(anotherObject))
            throw new AssertionError(parameterName + " can't be equal to " + String.valueOf(anotherObject) + ".");
    }

    public static void assertNotEmpty(String text, String parameterName) throws AssertionError {
        if (ASSERTIONS_ENABLED && TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim()))
            throw new AssertionError(parameterName + " can't be empty.");
    }

    public static void assertTrue(boolean condition, @NonNull String message) throws AssertionError {
        if (ASSERTIONS_ENABLED && !condition)
            throw new AssertionError(message);
    }

    public static void assertFalse(boolean condition, @NonNull String message) throws AssertionError {
        if (ASSERTIONS_ENABLED && condition)
            throw new AssertionError(message);
    }
}

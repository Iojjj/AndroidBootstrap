package iojjj.android.bootstrap.core.utils.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Alexander Vlasov on 14.09.2015.
 */
public class AssertionUtils {
    private AssertionUtils() {}

    public static <T> void assertNotNull(@Nullable T object, @NonNull String parameterName) throws AssertionError {
        if (object == null)
            throw new AssertionError(parameterName + " can't be null.");
    }

    public static <T> void assertInstanceOf(@NonNull T object, @NonNull Class<?> clazz, @NonNull String parameterName) throws AssertionError {
        if (!clazz.isInstance(object))
            throw new AssertionError(parameterName + " is not instance of " + clazz.getName() + ".");
    }

    public static <T> void assertNotEquals(@NonNull T object, @NonNull T anotherObject, @NonNull String parameterName) throws AssertionError {
        if (object == anotherObject || object.equals(anotherObject))
            throw new AssertionError(parameterName + " can't be equal to " + String.valueOf(anotherObject) + ".");
    }

    public static void assertNotEmpty(String text, String parameterName) throws AssertionError {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim()))
            throw new AssertionError(parameterName + " can't be empty.");
    }

    public static void check(boolean b, @NonNull String message) {
        if (b)
            throw new AssertionError(message);
    }
}

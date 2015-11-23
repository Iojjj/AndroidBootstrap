package iojjj.android.bootstrap.core.utils.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a non-null value. If a value is present, isPresent() will return true and get() will return the value.
 */
public class Optional<T> {

    private final T object;

    public static <T> Optional<T> from(@Nullable T value) {
        return new Optional<>(value);
    }

    protected Optional(@Nullable T object) {
        this.object = object;
    }

    public boolean isPresent() {
        return object != null;
    }

    public T orElse(T _else) {
        if (!isPresent())
            return _else;
        return object;
    }

    @NonNull
    public T get() {
        if (object == null)
            throw new NoSuchElementException("Object is null");
        return object;
    }
}

package iojjj.android.bootstrap.core.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import iojjj.android.bootstrap.core.utils.misc.AssertionUtils;

/**
 * Simple ArrayAdapter that works on top of {@link Set}.
 */
public abstract class BaseSetAdapter<T> extends BaseArrayAdapter<T> {

    private final Set<T> set = new HashSet<>();

    public BaseSetAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void add(T object) {
        if (set.add(object))
            super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        AssertionUtils.assertNotNull(collection, "Collection");
        for (T object : collection)
            add(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addAll(@NonNull T... collection) {
        AssertionUtils.assertNotNull(collection, "Collection");
        for (T object : collection)
            add(object);
    }

    @Override
    public void remove(T object) {
        set.remove(object);
        super.remove(object);
    }

    public void clear() {
        super.clear();
        set.clear();
    }
}

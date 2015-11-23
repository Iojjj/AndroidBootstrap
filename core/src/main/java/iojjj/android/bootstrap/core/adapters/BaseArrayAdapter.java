package iojjj.android.bootstrap.core.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.Collection;

import iojjj.android.bootstrap.core.utils.misc.AssertionUtils;

/**
 * Simple ArrayAdapter that propose methods to add collections on pre-Honeycomb devices.
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> implements BaseFilter.FilterableAdapter<T> {

    private LayoutInflater inflater;
    private BaseFilter<T> filter;

    public BaseArrayAdapter(Context context) {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    protected LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public void withFilter(@Nullable BaseFilter<T> filter) {
        if (this.filter != null)
            unregisterDataSetObserver(this.filter.getDataSetObserver());
        this.filter = filter;
        if (this.filter != null) {
            this.filter.init(this);
            registerDataSetObserver(this.filter.getDataSetObserver());
        }
    }

    @Override
    public Filter getFilter() {
        if (filter != null)
            return filter;
        return super.getFilter();
    }

    @Override
    public boolean isFiltered() {
        return filter != null && filter.isFiltered();
    }

    @Override
    public Spannable highlightFilteredSubstring(String text) {
        return isFiltered() ? filter.highlightFilteredSubstring(text) : new SpannableString(text);
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public T getItem(int position) {
        if (filter != null && filter.isFiltered())
            return filter.getItem(position);
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        if (filter != null && filter.isFiltered())
            return filter.getCount();
        return super.getCount();
    }

    @Override
    public int getNonFilteredCount() {
        return super.getCount();
    }

    @Override
    public T getNonFilteredItem(int position) {
        return super.getItem(position);
    }

    /**
     * Adds the specified items at the end of the array.
     * @param collection The items to add at the end of the array.
     */
    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        AssertionUtils.assertNotNull(collection, "Collection");
        if (!collection.isEmpty()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                setNotifyOnChange(false);
                for (T el : collection) {
                    add(el);
                }
                notifyDataSetChanged();
            } else {
                super.addAll(collection);
            }
        }
    }

    /**
     * Adds the specified items at the end of the array.
     * @param collection The items to add at the end of the array.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addAll(T... collection) {
        AssertionUtils.assertNotNull(collection, "Collection");
        if (collection.length > 0) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                setNotifyOnChange(false);
                for (T el : collection) {
                    add(el);
                }
                notifyDataSetChanged();
            } else {
                super.addAll(collection);
            }
        }
    }
}

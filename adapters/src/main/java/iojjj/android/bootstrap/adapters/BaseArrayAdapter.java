package iojjj.android.bootstrap.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import iojjj.android.bootstrap.assertions.AssertionUtils;


/**
 * Simple ArrayAdapter that propose methods to add collections on pre-Honeycomb devices.
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> implements IFilterableAdapter<T, BaseArrayAdapter.Filter<T>> {

    private LayoutInflater inflater;
    private Filter<T> filter;

    public BaseArrayAdapter(Context context) {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    protected LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public void setFilter(@Nullable Filter<T> filter) {
        if (this.filter != null)
            unregisterDataSetObserver(this.filter.getDataSetObserver());
        this.filter = filter;
        if (this.filter != null) {
            this.filter.init(this);
            registerDataSetObserver(this.filter.getDataSetObserver());
        }
    }

    @Override
    public android.widget.Filter getFilter() {
        if (filter != null)
            return filter;
        return super.getFilter();
    }

    @Override
    public boolean isFiltered() {
        return filter != null && filter.isFiltered();
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public T getItem(int position) {
        if (isFiltered())
            return filter.getItem(position);
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        if (isFiltered())
            return filter.getItemCount();
        return super.getCount();
    }

    @Override
    public synchronized Collection<T> getSnapshot() {
        List<T> data = new ArrayList<>();
        int size = super.getCount();
        for (int i=0; i<size; i++) {
            data.add(super.getItem(i));
        }
        return data;
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

    /**
     * Implementation of filter for {@link ArrayAdapter}.
     */
    public abstract static class Filter<T> extends BaseFilter<T, Filter<T>> implements IAAFilter<T, Filter<T>> {

        private DataSetObserver dataSetObserver;

        @Override
        public void init(@NonNull IFilterableAdapter<T, Filter<T>> adapter) throws AssertionError {
            super.init(adapter);
            dataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (!isFiltered())
                        return;
                    refresh();
                }

                @Override
                public void onInvalidated() {
                    super.onInvalidated();
                    if (!isFiltered())
                        return;
                    invalidate();
                }
            };
        }

        @Override
        public DataSetObserver getDataSetObserver() {
            return dataSetObserver;
        }
    }
}

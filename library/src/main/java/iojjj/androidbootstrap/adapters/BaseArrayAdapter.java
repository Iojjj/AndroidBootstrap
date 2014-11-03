package iojjj.androidbootstrap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collection;

/**
 * Simple ArrayAdapter that propose methods to add collections on pre-Honeycomb devices.
 *
 * @param <T>
 */
public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {

    protected LayoutInflater inflater;

    public BaseArrayAdapter(Context context) {
        super(context, 0);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public synchronized void add(T object, int pos) {
        pos = pos >= getCount() ? Math.max(getCount() - 1, 0) : pos;
        super.insert(object, pos);
    }

    public synchronized void addAll(Collection<? extends T> collection) {
        addAll(collection, true);
    }

    public synchronized void addAll(Collection<? extends T> collection, boolean skipExisted) {
        if (collection == null) {
            return;
        }
        setNotifyOnChange(false);
        for (T el : collection) {
            if (skipExisted) {
                if (getPosition(el) == -1) {
                    add(el);
                }
            } else {
                add(el);
            }
        }
        notifyDataSetChanged();
    }

    public synchronized void addAll(T... collection) {
        addAll(true, collection);
    }

    public synchronized void addAll(boolean skipExisted, T... collection) {
        if (collection == null) {
            return;
        }
        setNotifyOnChange(false);
        for (T el : collection) {
            if (skipExisted) {
                if (getPosition(el) == -1) {
                    add(el);
                }
            } else {
                add(el);
            }
        }
        notifyDataSetChanged();
    }

    public synchronized void insertAll(int position, T... collection) {
        if (collection == null) {
            return;
        }
        setNotifyOnChange(false);
        int pos = position;
        for (T el : collection) {
            add(el, pos);
            pos++;
        }
        notifyDataSetChanged();
    }
}

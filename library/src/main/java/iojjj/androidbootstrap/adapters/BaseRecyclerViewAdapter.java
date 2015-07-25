package iojjj.androidbootstrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base adapter for recycler view
 */
public abstract class BaseRecyclerViewAdapter<TData, TViewHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<TViewHolder> {

    private Context context;
    private List<TData> data;

    public BaseRecyclerViewAdapter(@NotNull final Context context) {
        this.context = context.getApplicationContext();
        data = new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(@NotNull final Context context, List<TData> data) {
        this.context = context.getApplicationContext();
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public TData getItem(final int position) throws ArrayIndexOutOfBoundsException {
        return data.get(position);
    }

    public boolean add(TData object) {
        return data.add(object);
    }

    public boolean remove(TData object) {
        return data.remove(object);
    }

    public TData remove(int position) {
        return data.remove(position);
    }

    public void clear() {
        data.clear();
    }

    public boolean addAll(Collection<? extends TData> objects) {
        return data.addAll(objects);
    }
}

package iojjj.android.bootstrap.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Base adapter for recycler view
 */
public abstract class BaseRecyclerViewAdapter<TData, TViewHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<TViewHolder> implements IFilterableAdapter<TData, BaseRecyclerViewAdapter.Filter<TData>> {

    private Context context;
    private final List<TData> data;
    private Filter<TData> filter;

    public BaseRecyclerViewAdapter(@NonNull final Context context) {
        this.context = context.getApplicationContext();
        data = new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(@NonNull final Context context, @NonNull List<TData> data) {
        this.context = context.getApplicationContext();
        this.data = data;
    }

    @Override
    public void setFilter(@Nullable Filter<TData> filter) {
        if (this.filter != null)
            unregisterAdapterDataObserver(this.filter.getAdapterDataObserver());
        this.filter = filter;
        if (this.filter != null) {
            this.filter.init(this);
            registerAdapterDataObserver(this.filter.getAdapterDataObserver());
        }
    }

    protected Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        if (isFiltered())
            return filter.getItemCount();
        return data.size();
    }

    public TData getItem(final int position) throws ArrayIndexOutOfBoundsException {
        if (isFiltered())
            return filter.getItem(position);
        return data.get(position);
    }

    @Override
    public boolean isFiltered() {
        return filter != null && filter.isFiltered();
    }

    @Override
    public android.widget.Filter getFilter() {
        return filter;
    }

    @Override
    public synchronized Collection<TData> getSnapshot() {
        return new ArrayList<>(data);
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

    public boolean addAll(@NonNull Collection<? extends TData> collection) {
        AssertionUtils.assertNotNull(collection, "Collection");
        return data.addAll(collection);
    }

    public boolean addAll(@NonNull TData... collection) {
        return Collections.addAll(data, collection);
    }

    /**
     * Created by Alexander Vlasov on 03.01.2016.
     */
    public abstract static class Filter<T> extends BaseFilter<T, Filter<T>> implements IRVAFilter<T, Filter<T>> {

        private RecyclerView.AdapterDataObserver adapterDataObserver;

        @Override
        public void init(@NonNull IFilterableAdapter<T, Filter<T>> adapter) {
            super.init(adapter);
            adapterDataObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (!isFiltered())
                        return;
                    refresh();
                }
            };
        }

        @Override
        public RecyclerView.AdapterDataObserver getAdapterDataObserver() {
            return adapterDataObserver;
        }
    }
}

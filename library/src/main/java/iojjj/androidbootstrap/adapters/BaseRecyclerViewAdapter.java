package iojjj.androidbootstrap.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Александр on 03.11.2014.
 */
public abstract class BaseRecyclerViewAdapter<TData, TViewHolder extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<TViewHolder> {

    private List<TData> data;

    public BaseRecyclerViewAdapter() {
    }

    public BaseRecyclerViewAdapter(List<TData> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public TData getItem(final int position) throws ArrayIndexOutOfBoundsException {
        return data.get(position);
    }
}

package iojjj.android.bootstrap.adapters;

import android.support.annotation.Nullable;
import android.widget.Filter;

/**
 * Created by Alexander Vlasov on 03.01.2016.
 */
public interface IFilterableAdapter<T, K extends IFilter<T, K>> extends ISnapshotAdapter<T> {

    void notifyDataSetChanged();

    void setFilter(@Nullable K filter);

    boolean isFiltered();

    @Nullable
    Filter getFilter();
}

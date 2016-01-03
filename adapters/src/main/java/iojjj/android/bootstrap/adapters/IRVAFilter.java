package iojjj.android.bootstrap.adapters;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Alexander Vlasov on 03.01.2016.
 */
interface IRVAFilter<T, K extends IFilter<T, K>> extends IFilter<T, K> {

    RecyclerView.AdapterDataObserver getAdapterDataObserver();
}

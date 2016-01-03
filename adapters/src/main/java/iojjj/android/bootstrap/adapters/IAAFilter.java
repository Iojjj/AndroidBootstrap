package iojjj.android.bootstrap.adapters;

import android.database.DataSetObserver;

/**
 * Created by Alexander Vlasov on 03.01.2016.
 */
interface IAAFilter<T, K extends IFilter<T, K>> extends IFilter<T, K> {

    DataSetObserver getDataSetObserver();
}

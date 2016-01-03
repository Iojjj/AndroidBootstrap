package iojjj.android.bootstrap.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;

/**
 * Created by Alexander Vlasov on 03.01.2016.
 */
public interface IFilter<T, K extends IFilter<T, K>> {

    void init(@NonNull IFilterableAdapter<T, K> adapter);

    boolean isFiltered();

    int getItemCount();

    T getItem(int position);

    Spannable highlight(@NonNull String string, @ColorInt int color);
}

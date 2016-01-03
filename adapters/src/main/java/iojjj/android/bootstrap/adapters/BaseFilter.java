package iojjj.android.bootstrap.adapters;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Filter;

import java.util.Collections;
import java.util.List;

import iojjj.android.bootstrap.assertions.AssertionUtils;

/**
 * Base implementation of filter.
 */
abstract class BaseFilter<T, K extends IFilter<T, K>> extends Filter implements IFilter<T, K> {

    private IFilterableAdapter<T, K> adapter;
    private CharSequence lastConstraint;
    private FilterResults lastResults;

    @Override
    public void init(@NonNull IFilterableAdapter<T, K> adapter) {
        AssertionUtils.assertNotNull(adapter, "Adapter");
        this.adapter = adapter;
    }

    protected void refresh() {
        performFiltering(lastConstraint);
    }

    protected void invalidate() {
        lastResults = new FilterResults();
        lastResults.count = -1;
        lastResults.values = Collections.emptyList();
        adapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    protected final FilterResults performFiltering(CharSequence constraint) {
        return performFilteringImpl(constraint, adapter);
    }

    /**
     * Perform filtering as always. Returned {@link android.widget.Filter.FilterResults} object must be non-null.
     * @param constraint the constraint used to filter the data
     * @return filtering results. <br />
     * You can set {@link android.widget.Filter.FilterResults#count} to -1 to specify that no filtering was applied.<br />
     * {@link android.widget.Filter.FilterResults#values} must be instance of {@link List}.
     */
    @NonNull
    protected abstract FilterResults performFilteringImpl(CharSequence constraint, ISnapshotAdapter<T> adapter);

    @Override
    protected final void publishResults(CharSequence constraint, FilterResults results) throws AssertionError {
        AssertionUtils.assertNotNull(results, "Results");
        if (results.count > -1)
            AssertionUtils.assertInstanceOf(results.values, List.class, "Values");
        lastConstraint = constraint;
        lastResults = results;
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean isFiltered() {
        return lastResults != null && lastResults.count > -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getItem(int position) throws ArrayIndexOutOfBoundsException {
        return ((List<T>)lastResults.values).get(position);
    }

    @Override
    public int getItemCount() {
        return lastResults == null ? 0 : lastResults.count;
    }

    public Spannable highlight(@NonNull String string, @ColorInt int highlightColor) {
        AssertionUtils.assertNotNull(string, "String");
        SpannableString spannableString = new SpannableString(string);
        if (!isFiltered())
            return spannableString;
        String filteredString = lastConstraint.toString().trim().toLowerCase();
        String lowercase = string.toLowerCase();
        int length = filteredString.length();
        int index = -1, prevIndex;
        do {
            prevIndex = index;
            index = lowercase.indexOf(filteredString, prevIndex + 1);
            if (index == -1) {
                break;
            }
            spannableString.setSpan(new ForegroundColorSpan(highlightColor), index, index + length, 0);
        } while (true);
        return spannableString;
    }
}

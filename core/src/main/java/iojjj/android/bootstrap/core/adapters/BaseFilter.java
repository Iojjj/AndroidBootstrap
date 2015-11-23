package iojjj.android.bootstrap.core.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Filter;

import java.util.Collections;
import java.util.List;

import iojjj.android.bootstrap.core.R;
import iojjj.android.bootstrap.core.utils.misc.AssertionUtils;
import iojjj.android.bootstrap.core.utils.misc.VersionUtils;

/**
 * Base filter that can be easily integrated with {@link BaseArrayAdapter}.<br/><br/>
 * For iterating through adapter's data use {@link #getNonFilteredCount()} and {@link #getNonFilteredItem(int)}.
 */
public abstract class BaseFilter<T> extends Filter {

    private FilterableAdapter<T> adapter;
    private CharSequence lastConstraint;
    private FilterResults lastResults;
    private DataSetObserver dataSetObserver;
    private RecyclerView.AdapterDataObserver adapterDataObserver;
    private int highlightColor;

    public BaseFilter(@NonNull Context context) throws AssertionError {
        AssertionUtils.assertNotNull(context, "Context");
        highlightColor = VersionUtils.getColor(context.getResources(), R.color.theme_color_accent);
    }

    public BaseFilter(int highlightColor) throws AssertionError {
        setHighlightColor(highlightColor);
    }

    public BaseFilter setHighlightColor(int highlightColor) throws AssertionError {
        AssertionUtils.assertNotEquals(highlightColor, 0, "HighlightColor");
        this.highlightColor = highlightColor;
        return this;
    }

    void init(@NonNull FilterableAdapter<T> adapter) throws AssertionError {
        AssertionUtils.assertNotNull(adapter, "Adapter");
        this.adapter = adapter;
        dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (!isFiltered())
                    return;
                performFiltering(lastConstraint);
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                if (!isFiltered())
                    return;
                lastResults = new FilterResults();
                lastResults.count = -1;
                lastResults.values = Collections.emptyList();
            }
        };
        adapterDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (!isFiltered())
                    return;
                performFiltering(lastConstraint);
            }
        };
    }

    protected int getNonFilteredCount() {
        return adapter.getNonFilteredCount();
    }

    protected T getNonFilteredItem(int position) {
        return adapter.getNonFilteredItem(position);
    }

    @NonNull
    @Override
    protected final FilterResults performFiltering(CharSequence constraint) {
        return performFilteringImpl(constraint);
    }

    /**
     * Perform filtering as always. Returned {@link android.widget.Filter.FilterResults} object must be non-null.
     * @param constraint the constraint used to filter the data
     * @return filtering results. <br />
     * You can set {@link android.widget.Filter.FilterResults#count} to -1 to specify that no filtering was applied.<br />
     * {@link android.widget.Filter.FilterResults#values} must be instance of {@link List}.
     */
    @NonNull
    protected abstract FilterResults performFilteringImpl(CharSequence constraint);

    @Override
    protected final void publishResults(CharSequence constraint, FilterResults results) throws AssertionError {
        AssertionUtils.assertNotNull(results, "Results");
        if (results.count > -1)
            AssertionUtils.assertInstanceOf(results.values, List.class, "Values");
        lastConstraint = constraint;
        lastResults = results;
        adapter.notifyDataSetChanged();
    }

    public boolean isFiltered() {
        return lastResults != null && lastResults.count > -1;
    }

    @SuppressWarnings("unchecked")
    public T getItem(int position) throws ArrayIndexOutOfBoundsException {
        return ((List<T>)lastResults.values).get(position);
    }

    public int getCount() {
        return lastResults.count;
    }

    public DataSetObserver getDataSetObserver() {
        return dataSetObserver;
    }

    public RecyclerView.AdapterDataObserver getAdapterDataObserver() {
        return adapterDataObserver;
    }

    public Spannable highlightFilteredSubstring(String name) {
        SpannableString string = new SpannableString(name);
        if (!isFiltered())
            return string;
        String filteredString = lastConstraint.toString().trim().toLowerCase();
        String lowercase = name.toLowerCase();
        int length = filteredString.length();
        int index = -1, prevIndex;
        do {
            prevIndex = index;
            index = lowercase.indexOf(filteredString, prevIndex + 1);
            if (index == -1) {
                break;
            }
            string.setSpan(new ForegroundColorSpan(highlightColor), index, index + length, 0);
        } while (true);
        return string;
    }

    interface FilterableAdapter<T> {
        int getNonFilteredCount();
        T getNonFilteredItem(int position);
        void notifyDataSetChanged();
        void withFilter(@Nullable BaseFilter<T> filter);
        boolean isFiltered();
        Spannable highlightFilteredSubstring(String text);
    }
}

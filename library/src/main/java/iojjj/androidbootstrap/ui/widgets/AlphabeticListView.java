package iojjj.androidbootstrap.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import iojjj.androidbootstrap.R;

/**
 * ListView with alphabetic sections
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AlphabeticListView extends FrameLayout {

    /**
     * Previously visible item
     */
    int prevVisible = -1;
    /**
     * Previously visible header letter
     */
    String prevIndicator;
    /**
     * Flag indicates that last time list was filtered
     */
    boolean prevFiltered;

    private ListView list;
    private TextView header;
    private IAlphabeticListAdapter adapter;

    public AlphabeticListView(Context context) {
        super(context);
    }

    public AlphabeticListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public AlphabeticListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphabeticListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlphabeticListView);
        int headerId = typedArray.getResourceId(R.styleable.AlphabeticListView_alv_header_layout, 0);
        typedArray.recycle();
        if (headerId == 0) {
            throw new IllegalArgumentException("You must declare alv_header_layout attribute in your XML code");
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_alphabetic_list_view, this, false);
        FrameLayout headerHolder = (FrameLayout) view.findViewById(R.id.header_holder);
        header = (TextView) inflater.inflate(headerId, headerHolder, false);
        headerHolder.addView(header);
        addView(view);
        list = (ListView) view.findViewById(R.id.list);
        list.setOverScrollMode(OVER_SCROLL_NEVER);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    checkScroll(view.getFirstVisiblePosition());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                checkScroll(firstVisibleItem);
            }
        });
    }

    private void checkScroll(int firstVisibleItem) {
        if (adapter == null)
            return;
        // if new visible item or new filtered state
        if (prevVisible != firstVisibleItem || prevFiltered != adapter.isFiltered()) {
            prevFiltered = adapter.isFiltered();
            if (adapter.getCount() > firstVisibleItem) {
                prevVisible = firstVisibleItem;
                prevIndicator = adapter.getLetter(firstVisibleItem);
                header.setText(prevIndicator);
                header.setTranslationY(0);
                Log.d("Scroll", "new item at top: " + prevVisible + ", [" + prevIndicator + "]");
            }
        } else {
            if (adapter.getCount() > prevVisible + 1) {
                String nextIndicator = adapter.getLetter(prevVisible + 1);
                if (TextUtils.equals(prevIndicator, nextIndicator)) {
                    updateHeaderVisibility(-1, firstVisibleItem);
                    Log.d("Scroll", "don't scroll");
                } else {
                    View child = list.getChildAt(0);
                    if (child != null) {
                        updateHeaderVisibility(child.getTop(), firstVisibleItem);
                        header.setTranslationY(child.getTop());
                    }
                }
                header.setVisibility(View.VISIBLE);
            } else {
                header.setVisibility(View.GONE);
            }
        }
    }

    private void updateHeaderVisibility(int scroll, int firstVisible) {
        for (int i=0; i<list.getChildCount(); i++) {
            int position = firstVisible + i;
            if (i == 0 && scroll != 0 || i == 0 && i == firstVisible)
                adapter.updateLetterVisibility(position, View.GONE);
            else
                adapter.updateLetterVisibility(position, adapter.getLetterVisibility(position));
        }
    }

    public ListView getList() {
        return list;
    }

    public TextView getHeader() {
        return header;
    }

    public void setAdapter(ListAdapter adapter) {
        if (!(adapter instanceof IAlphabeticListAdapter))
            throw new ClassCastException("Adapter must implement " + IAlphabeticListAdapter.class.getSimpleName() + " interface");
        this.adapter = (IAlphabeticListAdapter) adapter;
        list.setAdapter(adapter);
    }

    public interface IAlphabeticListAdapter {
        String getLetter(int position);

        void updateLetterVisibility(int positionInList, int visibility);

        boolean isFiltered();

        int getCount();

        int getLetterVisibility(int position);
    }
}

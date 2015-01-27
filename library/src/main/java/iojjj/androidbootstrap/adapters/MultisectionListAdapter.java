package iojjj.androidbootstrap.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * List adapter with multiple sections
 */
public abstract class MultisectionListAdapter extends BaseAdapter {

    private static final int TYPE_SECTION = 0;
    private static final int TYPE_ITEM = 1;
    public static final int SECTION_ALL = -1;

    private Context context;
    private boolean notifyOnChange;
    private ArrayList<ArrayList<ListItem>> sections;

    public MultisectionListAdapter(final Context context) {
        this.context = context;
        sections = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public final int getCount() {
        int count = 0;
        for (List<ListItem> list : sections) {
            // if section isn't empty
            if (list.size() > 1)
                count += list.size();
        }
        return count;
    }

    @Override
    public final ListItem getItem(int position) {
        return getItemAndSection(position).item;
    }

    public ListItem getItem(int position, int section) {
        return sections.get(section).get(position + 1);
    }

    private Result getItemAndSection(int position) {
        int count = 0;
        int section = 0;
        for (List<ListItem> list : sections) {
            if (position < count + list.size()) {
                Result result = new Result();
                result.item = list.get(position - count);
                result.section = section;
                return result;
            }
            count += list.size();
            section++;
        }
        throw new ArrayIndexOutOfBoundsException(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public final int getViewTypeCount() {
        return 2;
    }

    public void add(ListItem item, int section) {
        checkAndAddSection(section);
        sections.get(section).add(item);
        if (notifyOnChange)
            notifyDataSetChanged();
    }

    private void checkAndAddSection(int section) {
        while (sections.size() <= section) {
            ArrayList<ListItem> list = new ArrayList<>();
            ListItem header = new ListItem();
            header.isSection = true;
            list.add(header); // add section's object
            sections.add(list);
        }
    }

    public void insert(ListItem item, int section, int position) {
        checkAndAddSection(section);
        sections.get(section).add(position + 1, item);
        if (notifyOnChange)
            notifyDataSetChanged();
    }

    public void remove(ListItem item, int section) {
        if (sections.size() <= section) {
            Log.w(MultisectionListAdapter.class.getSimpleName(), "Invalid section " + section + " with number of sections " + sections.size());
            return;
        }
        sections.get(section).remove(item);
        if (notifyOnChange)
            notifyDataSetChanged();
    }

    @Override
    public final int getItemViewType(int position) {
        ListItem item = getItem(position);
        if (item.isSection)
            return TYPE_SECTION;
        return TYPE_ITEM;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_SECTION:
                return getSectionView(position, convertView, parent, getItemAndSection(position).section);
            case TYPE_ITEM:
                return getItemView(position, convertView, parent);
        }
        return null;
    }

    protected abstract View getSectionView(int position, View convertView, ViewGroup parent, int section);

    protected abstract View getItemView(int position, View convertView, ViewGroup parent);

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notifyOnChange)
            notifyOnChange = true;
    }

    public boolean isNotifyOnChange() {
        return notifyOnChange;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }

    public void clear() {
        for (List<ListItem> list : sections)
            list.clear();
        if (notifyOnChange)
            notifyDataSetChanged();
    }

    public int getItemCount(int section) {
        if (section == SECTION_ALL)
            return getCount() - sections.size();
        return sections.get(section).size() - 1;
    }

    public static abstract class OnItemClickListener implements AbsListView.OnItemClickListener {

        private MultisectionListAdapter adapter;

        public OnItemClickListener(MultisectionListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Result result = adapter.getItemAndSection(position);
            if (!result.item.isSection)
                onItemClickImpl(parent, view, position, id, result.section);
        }

        public abstract void onItemClickImpl(AdapterView<?> parent, View view, int position, long id, int section);
    }

    public static abstract class OnItemLongClickListener implements AbsListView.OnItemLongClickListener {

        private MultisectionListAdapter adapter;

        public OnItemLongClickListener(MultisectionListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public final boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Result result = adapter.getItemAndSection(position);
            return !result.item.isSection && onItemLongClickImpl(parent, view, position, id, result.section);
        }

        public abstract boolean onItemLongClickImpl(AdapterView<?> parent, View view, int position, long id, int section);
    }

    public static class ListItem {
        private boolean isSection;
    }

    private static class Result {
        int section;
        ListItem item;
    }
}

package iojjj.bootstrap.demo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import iojjj.androidbootstrap.adapters.BaseRecyclerViewAdapter;
import iojjj.androidbootstrap.ui.fragments.AbstractFragment;
import iojjj.androidbootstrap.utils.misc.ItemClickSupport;
import iojjj.bootstrap.demo.R;

/**
 * Fragment with RecyclerView
 */
public class RecyclerViewFragment extends AbstractFragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<DataViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<String> data = new ArrayList<String>();
        for (int i=0; i<1000; i++) {
            data.add("Item #" + (i + 1));
        }
        adapter = new RecyclerAdapter(getActivity(), data);
        recyclerView.setAdapter(adapter);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerView);
    }

    public static class RecyclerAdapter extends BaseRecyclerViewAdapter<String, DataViewHolder> {

        public RecyclerAdapter(@NotNull Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new DataViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_simple, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(DataViewHolder data, int i) {
            String item = getItem(i);
            data.text.setText(item);
            data.setData(item);
        }
    }

    private static class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text;
        private String data;

        public DataViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "" + data, Toast.LENGTH_SHORT).show();
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}

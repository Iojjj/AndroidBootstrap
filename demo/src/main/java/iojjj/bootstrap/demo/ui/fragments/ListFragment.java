package iojjj.bootstrap.demo.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Random;

import iojjj.androidbootstrap.adapters.BaseArrayAdapter;
import iojjj.androidbootstrap.ui.activities.ToolbarOverscrollActivity;

/**
 * Created by Александр on 08.11.2014.
 */
public class ListFragment extends android.support.v4.app.ListFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ColorAdapter adapter = new ColorAdapter(getActivity());
        Random rnd = new Random();
        for (int i=0; i<100; i++) {
            adapter.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
        setListAdapter(adapter);
        setListShown(true);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer color = adapter.getItem(i);
                Intent intent = new Intent(getActivity(), ToolbarOverscrollActivity.class);
                intent.putExtra(ToolbarOverscrollActivity.EXTRA_COLOR, color);
                startActivity(intent);
            }
        });
    }

    public static class ColorAdapter extends BaseArrayAdapter<Integer> {

        public ColorAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            convertView.setBackgroundColor(getItem(position));
            return convertView;
        }
    }
}

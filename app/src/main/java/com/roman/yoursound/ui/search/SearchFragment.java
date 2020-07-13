package com.roman.yoursound.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.TrackAdapter;
import com.roman.yoursound.models.Track;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel SearchViewModel;

    private ArrayList<Track> tracks = new ArrayList<>();
    private TrackAdapter adapter;
    public ListView listViewTracks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        TextView listViewHeader = (TextView)getLayoutInflater().inflate(R.layout.header_template, null);
        listViewHeader.setText("Search Results");
        listViewTracks = (ListView)root.findViewById(R.id.search_results);
        populateListView(listViewTracks);
        listViewTracks.addHeaderView(listViewHeader);

        return root;
    }

    public void populateListView(ListView listViewTracks){
//        listViewTracks = (ListView) getView().findViewById(R.id.listView_tracks);
        adapter = new TrackAdapter(getActivity(), tracks);
        listViewTracks.setAdapter(adapter);
        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Click to item: " + position,Toast.LENGTH_SHORT).show();
            }
        });

    }
}

package com.roman.yoursound.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.TrackAdapter;
import com.roman.yoursound.models.Track;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public static ArrayList<Track> tracks = new ArrayList<>();
    public static TrackAdapter adapter;
    private ListView listViewTracks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //fetching from server
        GetTracksHome process = new GetTracksHome();
        process.execute();

        TextView listViewHeader = (TextView)getLayoutInflater().inflate(R.layout.header_template, null);
        listViewTracks = (ListView)root.findViewById(R.id.listView_tracks);
        populateListViewTracks(listViewTracks);
        listViewTracks.addHeaderView(listViewHeader);

        return root;
    }

    public void populateListViewTracks(final ListView listViewTracks){
        adapter = new TrackAdapter(getActivity(), tracks);
        listViewTracks.setAdapter(adapter);
        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Track currentTrack = tracks.get(position - 1);
                    ((MainActivity)getActivity()).playerPreparing(currentTrack);
                }
            }
        });
    }
}
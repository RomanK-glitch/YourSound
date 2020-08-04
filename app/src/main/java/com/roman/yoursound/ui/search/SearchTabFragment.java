package com.roman.yoursound.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.TrackAdapter;
import com.roman.yoursound.adapters.UserAdapter;
import com.roman.yoursound.models.Track;
import com.roman.yoursound.models.User;
import com.roman.yoursound.ui.user.UserFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchTabFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    TextView noResults;
    public ArrayList<Track> tracks = new ArrayList<>();
    public TrackAdapter trackAdapter;
    public ArrayList<User> users = new ArrayList<>();
    public UserAdapter userAdapter;
    public ListView listView;

    private int mPage;

    public static SearchTabFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SearchTabFragment fragment = new SearchTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_sound_page, container, false);

        noResults = view.findViewById(R.id.search_no_results);
        listView = view.findViewById(R.id.search_results);
        if (mPage == 1) {
            populateListViewTracks(listView);
        } else if (mPage == 2){
            populateListViewUsers(listView);
        }

        return view;
    }

    public void setResultTracks(String response) {
        tracks.clear();
        try {
            noResults.setText("No results found");
            if (response.equals("[]")) {
                noResults.setVisibility(View.VISIBLE);
            } else {
                noResults.setVisibility(View.GONE);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        tracks.add(new Track(jo.getInt("id"), jo.getString("track_name"), jo.getString("path"), jo.getString("image_path"), jo.getString("date"), jo.getInt("listenings"), jo.getString("user_name"), jo.getString("duration")));
                    }
                    trackAdapter.updateList(tracks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setResultUsers(String response) {
        users.clear();
        try {
            noResults.setText("No results found");
            if (response.equals("[]")) {
                noResults.setVisibility(View.VISIBLE);
            } else {
                noResults.setVisibility(View.GONE);
                try {
                    JSONArray ja = new JSONArray(response);
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = (JSONObject) ja.get(i);
                        users.add(new User(jo.getInt("id"), jo.getString("name"), jo.getString("image_path")));
                    }
                    userAdapter.updateList(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void populateListViewTracks(final ListView listViewTracks){
        trackAdapter = new TrackAdapter(getActivity(), tracks);
        listViewTracks.setAdapter(trackAdapter);
        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Track currentTrack = tracks.get(position);
                    ((MainActivity)getActivity()).playerPreparing(currentTrack);
            }
        });
    }

    public void populateListViewUsers(ListView userList){
        userAdapter = new UserAdapter(getActivity(), users);
        userList.setAdapter(userAdapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);
                UserFragment userFragment = new UserFragment();
                FragmentManager fragmentManager = SearchFragment.fragmentManager;
                Bundle selectedUserId = new Bundle();
                selectedUserId.putInt("userId", selectedUser.id);
                userFragment.setArguments(selectedUserId);
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                        .replace(R.id.nav_host_fragment, userFragment, userFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
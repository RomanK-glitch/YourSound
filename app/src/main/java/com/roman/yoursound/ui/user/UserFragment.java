package com.roman.yoursound.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.roman.yoursound.ui.EditProfile.EditProfileActivity;
import com.roman.yoursound.ui.followers.FollowersFragment;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.TrackAdapter;
import com.roman.yoursound.models.Track;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    public ArrayList<Track> tracks = new ArrayList<>();
    public TrackAdapter adapter;
    public ListView listViewTracks;
    int currentUserId = MainActivity.userLocalStore.getLoggedInUser().id;

    //Views
    TextView userNameTV;
    Button aboutBtn, followersBtn, followingsBtn, followBtn, followedBtn, editProfileBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.user_fragment, container, false);

        //Initialize views
        userNameTV = root.findViewById(R.id.user_name);
        listViewTracks = (ListView)root.findViewById(R.id.user_track_list);
        aboutBtn = (Button)root.findViewById(R.id.user_more_information);
        followersBtn = (Button)root.findViewById(R.id.user_followers);
        followingsBtn = (Button)root.findViewById(R.id.user_following);
        followBtn = (Button)root.findViewById(R.id.user_follow);
        followedBtn = (Button)root.findViewById(R.id.user_followed);
        editProfileBtn = (Button)root.findViewById(R.id.user_edit_profile);

        //User track list
        populateListViewTracks(listViewTracks);
        final UserFragment userFragment = this;

        //Show about artist
        final TextView aboutTextView = (TextView)root.findViewById(R.id.user_about);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutTextView.getVisibility() == View.VISIBLE){
                    aboutTextView.setVisibility(View.GONE);
                }
                else {
                    aboutTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        //to followers fragment
        followersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowersFragment followersFragment = new FollowersFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                Bundle params = new Bundle();
                params.putInt("userId", currentUserId);
                params.putString("followType", "followers");
                followersFragment.setArguments(params);
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                        .replace(R.id.nav_host_fragment, followersFragment, followersFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        //to followings fragment
        followingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowersFragment followersFragment = new FollowersFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                Bundle params = new Bundle();
                params.putInt("userId", currentUserId);
                params.putString("followType", "followings");
                followersFragment.setArguments(params);
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                        .replace(R.id.nav_host_fragment, followersFragment, followersFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });

        //follow
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFollow postFollow = new PostFollow (currentUserId, userFragment);
                postFollow.execute();
            }
        });

        //unfollow
        followedBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PostUnfollow postUnfollow = new PostUnfollow(currentUserId, userFragment);
                postUnfollow.execute();
            }
        });

        //to edit profile activity
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChangeInfo = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(toChangeInfo);
            }
        });

        return root;
    }

    public void populateListViewTracks(ListView listViewTracks){
        adapter = new TrackAdapter(getActivity(), tracks);
        listViewTracks.setAdapter(adapter);
        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track currentTrack = tracks.get(position);
                ((MainActivity)getActivity()).playerPreparing(currentTrack);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle userId = this.getArguments();
        if (userId != null){
            currentUserId = userId.getInt("userId", -1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        UserFragment userFragment = this;
        GetUser getUser = new GetUser(currentUserId, userFragment);
        getUser.execute();
        GetTracksUser getTracks = new GetTracksUser(currentUserId, userFragment);
        getTracks.execute();
    }
}
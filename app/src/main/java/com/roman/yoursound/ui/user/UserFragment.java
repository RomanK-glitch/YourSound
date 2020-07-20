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
import com.roman.yoursound.ui.AddTrack.AddTrackActivity;
import com.roman.yoursound.models.User;
import com.roman.yoursound.ui.EditProfile.EditProfileActivity;
import com.roman.yoursound.ui.followers.FollowersFragment;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.TrackAdapter;
import com.roman.yoursound.models.Track;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    public ArrayList<Track> tracks = new ArrayList<>();
    public TrackAdapter adapter;
    public ListView listViewTracks;
    int currentUserId = MainActivity.userLocalStore.getLoggedInUser().id;

    //Views
    TextView userNameTV, noSoundsTV;
    Button aboutBtn, followersBtn, followingsBtn, followBtn, followedBtn, editProfileBtn, addTrackBtn;
    CircleImageView userImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.user_fragment, container, false);

        //Initialize views
        userNameTV = root.findViewById(R.id.user_name);
        noSoundsTV = root.findViewById(R.id.user_text_view_no_sounds);
        listViewTracks = (ListView)root.findViewById(R.id.user_track_list);
        aboutBtn = (Button)root.findViewById(R.id.user_more_information);
        followersBtn = (Button)root.findViewById(R.id.user_followers);
        followingsBtn = (Button)root.findViewById(R.id.user_following);
        followBtn = (Button)root.findViewById(R.id.user_follow);
        followedBtn = (Button)root.findViewById(R.id.user_followed);
        editProfileBtn = (Button)root.findViewById(R.id.user_edit_profile);
        addTrackBtn = (Button)root.findViewById(R.id.user_add_track);
        userImage = (CircleImageView)root.findViewById(R.id.user_image);

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

        //open image full screen
        userImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                
            }
        });

        //to add track activity
        addTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddTrack = new Intent(getActivity(), AddTrackActivity.class);
                startActivity(toAddTrack);
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
        tracks.clear();
        GetTracksUser getTracks = new GetTracksUser(currentUserId, userFragment);
        getTracks.execute();
    }

    //set listView height
    public static void setListViewHeightBasedOnChildren(ListView myListView) {
        ListAdapter adapter = myListView.getAdapter();
        if (myListView != null) {

            int totalHeight = 172 * adapter.getCount();

            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = totalHeight;
            myListView.setLayoutParams(params);
        }
    }

    //show tracks on server response
    public void showTracks (String tracksJson){
        if (tracksJson.equals("[]")){
            noSoundsTV.setVisibility(View.VISIBLE);
            if (currentUserId == MainActivity.userLocalStore.getLoggedInUser().id){
                addTrackBtn.setVisibility(View.VISIBLE);
            }
            setListViewHeightBasedOnChildren(listViewTracks);
        } else {
            try {
                JSONArray ja = new JSONArray(tracksJson);
                for (int i = 0; i < ja.length(); i++){
                    JSONObject jo =(JSONObject) ja.getJSONObject(i);
                    tracks.add(new Track(jo.getString("track_name"), jo.getString("path"), jo.getString("image_path"), jo.getString("date"), jo.getInt("listenings"), jo.getString("user_name"), jo.getString("duration")));
                }
                adapter.updateList(tracks);
                setListViewHeightBasedOnChildren(listViewTracks);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //show user data on server response
    public void showUserData(String userJson) {

        TextView userNameTV = getActivity().findViewById(R.id.user_name);
        TextView aboutTV = getActivity().findViewById(R.id.user_about);
        CircleImageView userImageCIV = getActivity().findViewById(R.id.user_image);
        Button followButton = getActivity().findViewById(R.id.user_follow);
        Button followedButton = getActivity().findViewById(R.id.user_followed);
        Button editProfileButton = getActivity().findViewById(R.id.user_edit_profile);

        try {
            JSONObject jo = new JSONObject(userJson);
            User currentUser = new User(Integer.parseInt(jo.getString("id")), jo.getString("name"), jo.getString("password"), jo.getString("email"), jo.getString("about"), jo.getString("image_path"));

            boolean isFollowed = Boolean.parseBoolean(jo.getString("is_followed"));

            if (currentUserId == MainActivity.userLocalStore.getLoggedInUser().id){
                editProfileButton.setVisibility(View.VISIBLE);
            } else {
                if (isFollowed){
                    followedButton.setVisibility(View.VISIBLE);
                } else {
                    followButton.setVisibility(View.VISIBLE);
                }
            }

            userNameTV.setText(currentUser.userName);
            aboutTV.setText(currentUser.about);
            Picasso.get().load(currentUser.imagePath).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(userImageCIV);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
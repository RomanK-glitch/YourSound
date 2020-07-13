package com.roman.yoursound.ui.followers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.UserAdapter;
import com.roman.yoursound.models.User;
import com.roman.yoursound.ui.user.UserFragment;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {

    public ArrayList<User> users = new ArrayList<>();
    public UserAdapter adapter;
    public ListView listViewUsers;
    int currentUserId;
    String followType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.followers_fragment, container, false);

        ListView userList = (ListView)root.findViewById(R.id.user_list);
        populateListView(userList);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = this.getArguments();
        if (param != null){
            currentUserId = param.getInt("userId");
            followType = param.getString("followType");
        }
    }

    public void populateListView(ListView userList){
        adapter = new UserAdapter(getActivity(), users);
        userList.setAdapter(adapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = users.get(position);
                UserFragment userFragment = new UserFragment();
                FragmentManager fragmentManager = getFragmentManager();
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

    @Override
    public void onStart() {
        super.onStart();
        FollowersFragment followersFragment = this;
        GetFollowers getFollowers = new GetFollowers(currentUserId, followType, followersFragment);
        getFollowers.execute();
    }
}

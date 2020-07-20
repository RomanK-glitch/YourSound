package com.roman.yoursound.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.roman.yoursound.*;
import com.roman.yoursound.ui.AddTrack.AddTrackActivity;
import com.roman.yoursound.ui.EditProfile.EditProfileActivity;

public class SettingsFragment extends Fragment {

    private com.roman.yoursound.ui.settings.SettingsViewModel SettingsViewModel;

    //Views
    Button to_changeInfo, to_addTrack, logOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        //Initialize views
        to_changeInfo = (Button)root.findViewById(R.id.settings_change_info);
        to_addTrack = (Button)root.findViewById(R.id.settings_add_track);
        logOut = (Button)root.findViewById(R.id.settings_logout_button);

        //Change user information activity open
        to_changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChangeInfo = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(toChangeInfo);
            }
        });

        //Add track activity open
        to_addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddTrack = new Intent(getActivity(), AddTrackActivity.class);
                startActivity(toAddTrack);
            }
        });

        //log out
        logOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.userLocalStore.clearUserData();
                MainActivity.userLocalStore.setUserLoggedIn(false);
                Toast.makeText(getActivity(), "Logged Out Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}

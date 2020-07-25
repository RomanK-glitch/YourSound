package com.roman.yoursound.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.models.User;
import com.roman.yoursound.ui.register.RegisterFragment;
import com.roman.yoursound.ui.user.UserFragment;
import com.roman.yoursound.models.UserLocalStore;

public class ProfileFragment extends Fragment {

    //Views
    EditText userNameET, passwordET;
    UserLocalStore userLocalStore;
    Button goTo_register;

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //Initializing views
        goTo_register = (Button)root.findViewById(R.id.button_logIn_signUp);
        userNameET = (EditText)root.findViewById(R.id.editText_login_userName);
        passwordET = (EditText)root.findViewById(R.id.editText_login_password);
        userLocalStore = new UserLocalStore(getActivity());

        //To register activity
        goTo_register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RegisterFragment registerFragment = new RegisterFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                                .replace(R.id.nav_host_fragment, registerFragment, registerFragment.getTag())
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );

        //To user fragment
        Button logIn = (Button)root.findViewById(R.id.button_logIn_logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameET.getText().toString();
                String password = passwordET.getText().toString();
                password = User.hashPassword(password);

                if (userName.isEmpty()) {
                    Toast.makeText(getActivity(), "User name field is not filled",Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Password field is not filled",Toast.LENGTH_SHORT).show();
                } else {
                    loginRequest(userName, password);
                }
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        //if user logged in change profile fragment to user fragment
        if (userLocalStore.isUserLoggedIn() == true){
            //changing fragment
            UserFragment userFragment = new UserFragment();
            FragmentManager fragmentManager = getFragmentManager();
            User loggedInUser = MainActivity.userLocalStore.getLoggedInUser();
            Bundle userId = new Bundle();
            userId.putInt("userId", loggedInUser.id);
            userFragment.setArguments(userId);
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, userFragment, userFragment.getTag())
                    .commit();
        }
    }

    public void loginRequest (final String userName, final String password) {
        ProfileFragment profileFragment = this;
        PostLogin postLogin = new PostLogin(userName, password, profileFragment);
        postLogin.execute();
    }
}

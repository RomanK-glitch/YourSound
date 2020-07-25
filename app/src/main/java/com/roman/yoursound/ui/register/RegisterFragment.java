package com.roman.yoursound.ui.register;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.roman.yoursound.R;
import com.roman.yoursound.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterFragment extends Fragment {

    //Views
    EditText userNameET, eMailET, passwordET, repPasswordET;
    Button registerButton;

    String userName, eMail, password, repPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        //Initialize views
        userNameET = (EditText)root.findViewById(R.id.editText_signUp_userName);
        eMailET = (EditText)root.findViewById(R.id.editText_signUp_eMail);
        passwordET = (EditText)root.findViewById(R.id.editText_signUp_password);
        repPasswordET = (EditText)root.findViewById(R.id.editText_signUp_repeatPassword);
        registerButton = (Button)root.findViewById(R.id.button_signUp_signUp);

        //Register
        registerButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        userName = userNameET.getText().toString();
                        eMail = eMailET.getText().toString();
                        password = passwordET.getText().toString();
                        repPassword = repPasswordET.getText().toString();


                        if (userName.isEmpty()){
                            Toast.makeText(getActivity(), "User name field is not filled",Toast.LENGTH_SHORT).show();
                        } else if ( eMail.isEmpty()) {
                            Toast.makeText(getActivity(), "E-mail field is not filled",Toast.LENGTH_SHORT).show();
                        } else if (password.isEmpty()) {
                            Toast.makeText(getActivity(), "Password field is not filled",Toast.LENGTH_SHORT).show();
                        } else if (repPassword.isEmpty()) {
                            Toast.makeText(getActivity(), "Repeat Password field is not filled",Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(repPassword)){
                            Toast.makeText(getActivity(), "Password and confirm password are not the same",Toast.LENGTH_SHORT).show();
                        } else {
                            //hash password
                            password = User.hashPassword(password);
                            registerRequest(userName, password, eMail);
                        }
                    }
                });
        return root;
    }

    public void registerRequest (final String userName,final String password,final String eMail) {
        RegisterFragment registerFragment = this;
        PostRegister postRegister = new PostRegister(userName, password, eMail, registerFragment);
        postRegister.execute();
    }
}

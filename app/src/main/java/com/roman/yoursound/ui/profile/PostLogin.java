package com.roman.yoursound.ui.profile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.ui.user.UserFragment;
import com.roman.yoursound.models.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class PostLogin extends AsyncTask<String, Void, String> {

    String result = "";
    String userName;
    String password;
    ProfileFragment profileFragment;

    public PostLogin(String userName, String password, ProfileFragment profileFragment){
        this.userName = userName;
        this.password = password;
        this.profileFragment = profileFragment;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL("http://mrkoste6.beget.tech/login.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonLogin = "{\"name\":" + "\"" + userName + "\"" + ",\"password\":" + "\"" + password + "\"" + "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(jsonLogin);
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                if (line != null) {
                    result = result + line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //save user in local store
        if (result.equals("failure")){
            Toast.makeText(profileFragment.getActivity(), "Wrong name or password",Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jo = new JSONObject(result);
                User loginUser = new User(Integer.parseInt(jo.getString("id")), jo.getString("name"), jo.getString("password"), jo.getString("email"), jo.getString("about"), jo.getString("image_path"));
                MainActivity.userLocalStore.storeUserData(loginUser);
                MainActivity.userLocalStore.setUserLoggedIn(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //changing fragment
            UserFragment userFragment = new UserFragment();
            FragmentManager fragmentManager = profileFragment.getFragmentManager();
            User loggedInUser = MainActivity.userLocalStore.getLoggedInUser();
            Bundle userId = new Bundle();
            userId.putInt("userId", loggedInUser.id);
            userFragment.setArguments(userId);
            //fragmentManager.beginTransaction().remove(profileFragment);
            //fragmentManager.popBackStack();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                    .replace(R.id.nav_host_fragment, userFragment, "firstUserFragment")
                    .commit();
        }
    }
}

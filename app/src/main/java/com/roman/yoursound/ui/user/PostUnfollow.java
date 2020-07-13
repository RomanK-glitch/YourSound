package com.roman.yoursound.ui.user;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostUnfollow extends AsyncTask<String, Void, String> {
    String result = "";
    int currentUserId;
    UserFragment userFragment;

    public PostUnfollow (int currentUserId, UserFragment userFragment){
        this.currentUserId = currentUserId;
        this.userFragment = userFragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://mrkoste6.beget.tech/unfollow.php");
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String usersJson = "{\"currentUserId\":" + "\"" + currentUserId + "\"" + ",\"loggedUser\":" + "\"" + MainActivity.userLocalStore.getLoggedInUser().id + "\"" + "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(usersJson);
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                if (line != null) {
                    result = result + line;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (result.equals("Success")){
            Button followedButton = userFragment.getActivity().findViewById(R.id.user_followed);
            Button followButton = userFragment.getActivity().findViewById(R.id.user_follow);
            followedButton.setVisibility(View.GONE);
            followButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(userFragment.getActivity(), result,Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(s);
    }
}

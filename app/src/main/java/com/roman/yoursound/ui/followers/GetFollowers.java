package com.roman.yoursound.ui.followers;

import android.os.AsyncTask;
import com.roman.yoursound.models.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetFollowers extends AsyncTask<String, Void, String> {
    int userId;
    String followType;
    String result = "";
    FollowersFragment followersFragment;
    ArrayList<User> users = new ArrayList<>();

    public GetFollowers(int userId, String followType, FollowersFragment followersFragment) {
        this.userId = userId;
        this.followType = followType;
        this.followersFragment = followersFragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://mrkoste6.beget.tech/followers.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonFollowType = "{\"userId\":" + "\"" + userId + "\"" + ",\"followType\":" + "\"" + followType + "\"" + "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(jsonFollowType);
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = input.readLine();
                if (line != null){
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
        super.onPostExecute(s);
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++){
                JSONObject jo = (JSONObject) ja.get(i);
                if (followType == "followers")
                {
                    users.add(new User(jo.getInt("follower_id"), jo.getString("name"), jo.getString("image_path")));
                } else if (followType == "followings"){
                    users.add(new User(jo.getInt("user_id"), jo.getString("name"), jo.getString("image_path")));
                }
            }
            followersFragment.users = users;
            followersFragment.adapter.updateList(users);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}

package com.roman.yoursound.ui.user;

import android.os.AsyncTask;
import android.widget.Toast;
import com.roman.yoursound.models.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetTracksUser extends AsyncTask<String, Void, String> {
    int userId;
    String result = "";
    UserFragment userFragment;
    ArrayList<Track> tracks = new ArrayList<>();

    public GetTracksUser(int userId, UserFragment userFragment){
        this.userFragment = userFragment;
        this.userId = userId;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://mrkoste6.beget.tech/user_tracks.php");
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonUserId = "{\"id\":" + "\"" + userId + "\"" + "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(jsonUserId);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (result.equals("[]")){
            //Toast.makeText(userFragment.getActivity(), "There are no tracks yet",Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONArray ja = new JSONArray(result);
                for (int i = 0; i < ja.length(); i++){
                    JSONObject jo =(JSONObject) ja.getJSONObject(i);
                    tracks.add(new Track(jo.getString("track_name"), jo.getString("path"), jo.getString("image_path"), jo.getString("date"), jo.getInt("listenings"), jo.getString("user_name")));
                }
                userFragment.tracks = tracks;
                userFragment.adapter.updateList(tracks);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

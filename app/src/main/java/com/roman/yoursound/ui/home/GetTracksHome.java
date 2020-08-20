package com.roman.yoursound.ui.home;

import android.os.AsyncTask;
import com.roman.yoursound.models.Track;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetTracksHome extends AsyncTask<Void,Void, Void> {
    String data = "";
    private ArrayList<Track> tracks = new ArrayList<>();

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new  URL("http://mrkoste6.beget.tech/home.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                if (line != null) {
                    data = data + line;
                }
            }

            try {
                JSONArray ja = new JSONArray(data);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    tracks.add(new Track(jo.getInt("id"), jo.getString("track_name"), jo.getString("path"), jo.getString("image_path"), jo.getString("date"), jo.getInt("listenings"), jo.getInt("userId"), jo.getString("user_name"), jo.getString("duration")));
                }
            } catch (JSONException e) {

            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        HomeFragment.tracks = tracks;
        HomeFragment.adapter.updateList(tracks);
    }
}

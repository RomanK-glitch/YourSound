package com.roman.yoursound.ui.AddTrack;

import android.os.AsyncTask;
import android.widget.Toast;
import com.roman.yoursound.MainActivity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostTrack extends AsyncTask <String, Void, String> {

    String trackName, duration, trackType, imageType, userId;
    String result = "";
    AddTrackActivity addTrackActivity;

    public PostTrack (AddTrackActivity addTrackActivity, String trackName, String duration, String trackType, String imageType) {
        this.addTrackActivity = addTrackActivity;
        this.trackName = trackName;
        this.duration = duration;
        this.trackType =trackType;
        this.imageType = imageType;
        userId = String.valueOf(MainActivity.userLocalStore.getLoggedInUser().id);
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            URL url = new URL("http://mrkoste6.beget.tech/add_track.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonChanges = "{\"track_name\":" + "\"" + trackName + "\"" + ",\"user_id\":" + "\"" + userId + "\"" + ",\"duration\":" + "\"" + duration + "\"" + ",\"track_type\":" + "\"" + trackType + "\"" + ",\"image_type\":" + "\"" + imageType + "\"" + "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(jsonChanges);
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
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
        addTrackActivity.onPostTrackResponse(result);
    }
}

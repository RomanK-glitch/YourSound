package com.roman.yoursound.ui.search;

import android.os.AsyncTask;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchGetSounds extends AsyncTask<String, Void, String> {

    String result = "";
    String searchForSounds;
    SearchFragment searchFragment;

    public SearchGetSounds(String searchForSounds, SearchFragment searchFragment) {
        this.searchForSounds = searchForSounds;
        this.searchFragment = searchFragment;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL("http://mrkoste6.beget.tech/search_tracks.php");
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonUserId = "{\"search_for\":" + "\"" + searchForSounds + "\"" + "}";
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
        searchFragment.soundsOnServerResponse(result);
    }
}

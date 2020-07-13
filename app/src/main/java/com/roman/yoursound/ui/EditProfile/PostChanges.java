package com.roman.yoursound.ui.EditProfile;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostChanges extends AsyncTask<String, Void, String> {
    String userName, about, imagePath;
    String result = "";
    int userId;
    EditProfileActivity editProfileActivity;
    Uri newImageUri;

    public PostChanges(int userId, String userName, String about, Uri newImageUri, EditProfileActivity editProfileActivity) {
        this.userId = userId;
        this.userName = userName;
        this.about = about;
        this.newImageUri = newImageUri;
        this.editProfileActivity = editProfileActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            if (newImageUri != null) {
                //send image to server
                //change imagePath
                String newImagePath;
            }
            URL url = new URL("http://mrkoste6.beget.tech/edit_profile.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonChanges = "{\"userName\":" + "\"" + userName + "\"" + ",\"about\":" + "\"" + about + "\"" + ",\"imagePath\":" + "\"" + imagePath + "\"" + ",\"userId\":" + "\"" + userId + "\"" + "}";
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
        editProfileActivity.onSave(result);
    }
}
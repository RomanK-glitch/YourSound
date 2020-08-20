package com.roman.yoursound.ui.Comments;

import android.os.AsyncTask;
import android.widget.Toast;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.models.Comment;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostComment extends AsyncTask<String, Void, String> {

    String result = "";
    CommentsActivity commentsActivity;
    String commentText;
    int trackId;

    public PostComment(CommentsActivity commentsActivity, String commentText, int trackId) {
        this.commentsActivity = commentsActivity;
        this.commentText = commentText;
        this.trackId = trackId;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("http://mrkoste6.beget.tech/newComment.php");
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonUserId = "{\"trackId\":" + "\"" + trackId + "\"" + ",\"userId\":" + "\"" + MainActivity.userLocalStore.getLoggedInUser().id + "\"" + ",\"text\":" + "\"" + commentText + "\"" + "}";
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
        commentsActivity.newComment(result);
    }
}

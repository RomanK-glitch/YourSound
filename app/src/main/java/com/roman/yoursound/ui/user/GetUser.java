package com.roman.yoursound.ui.user;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.models.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetUser extends AsyncTask<String, Void, String> {

    int userId;
    public String result = "";
    UserFragment userFragment;

    public GetUser (int userId, UserFragment userFragment){
        this.userId = userId;
        this.userFragment = userFragment;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL("http://mrkoste6.beget.tech/user.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            String jsonUserId = "{\"id\":" + "\"" + userId + "\"" + ",\"logged\":" + "\"" + MainActivity.userLocalStore.getLoggedInUser().id + "\"" + "}";
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(jsonUserId);
            out.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
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

        TextView userNameTV = userFragment.getActivity().findViewById(R.id.user_name);
        TextView aboutTV = userFragment.getActivity().findViewById(R.id.user_about);
        CircleImageView userImageCIV = userFragment.getActivity().findViewById(R.id.user_image);
        Button followButton = userFragment.getActivity().findViewById(R.id.user_follow);
        Button followedButton = userFragment.getActivity().findViewById(R.id.user_followed);
        Button editProfileButton = userFragment.getActivity().findViewById(R.id.user_edit_profile);

        try {
            JSONObject jo = new JSONObject(result);
            User currentUser = new User(Integer.parseInt(jo.getString("id")), jo.getString("name"), jo.getString("password"), jo.getString("email"), jo.getString("about"), jo.getString("image_path"));

            boolean isFollowed = Boolean.parseBoolean(jo.getString("is_followed"));

            if (userId == MainActivity.userLocalStore.getLoggedInUser().id){
                editProfileButton.setVisibility(View.VISIBLE);
            } else {
                if (isFollowed){
                    followedButton.setVisibility(View.VISIBLE);
                } else {
                    followButton.setVisibility(View.VISIBLE);
                }
            }

            userNameTV.setText(currentUser.userName);
            aboutTV.setText(currentUser.about);
            Picasso.get().load(currentUser.imagePath).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(userImageCIV);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
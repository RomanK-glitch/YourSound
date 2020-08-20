package com.roman.yoursound.ui.Comments;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.roman.yoursound.adapters.CommentAdapter;
import com.roman.yoursound.models.Comment;
import com.roman.yoursound.models.Track;
import com.roman.yoursound.ui.user.UserFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    //Views
    TextView commentTV;
    Button sendBtn;
    public ListView listViewComments;

    private ArrayList<Comment> comments = new ArrayList<>();
    private CommentAdapter commentAdapter;
    int trackId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //initialize views
        commentTV = findViewById(R.id.edit_comment);
        sendBtn = findViewById(R.id.send_comment);
        listViewComments = (ListView)findViewById(R.id.comments_list);


        Bundle commentsParam = getIntent().getExtras();
        trackId = -1;
        if (commentsParam != null){
            trackId = commentsParam.getInt("trackId");
        }

        //get comments
        final CommentsActivity commentsActivity = this;
        GetComments getComments = new GetComments(trackId, commentsActivity);
        getComments.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateListViewComments(listViewComments);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.userLocalStore.isUserLoggedIn()){
                    if (!commentTV.getText().toString().isEmpty()){
                        //clear text view
                        //hide keyboard
                        //send comment
                        PostComment postComment = new PostComment(commentsActivity, commentTV.getText().toString(), trackId);
                        postComment.execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Log in to leave a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //on server response
    public void setComments(String commentsJson){
        if (commentsJson.equals("[]")){

        } else {
            try {
                JSONArray ja = new JSONArray(commentsJson);
                for (int i = 0; i < ja.length(); i++){
                    JSONObject jo =(JSONObject) ja.getJSONObject(i);
                    comments.add(new Comment(jo.getString("text"), jo.getString("image_path"), jo.getInt("user_id")));
                }
                commentAdapter.updateList(comments);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void newComment(String serverResponse) {
        if (serverResponse.equals("Success")){

            //update list
            comments.add(new Comment(commentTV.getText().toString(), MainActivity.userLocalStore.getLoggedInUser().imagePath, MainActivity.userLocalStore.getLoggedInUser().id));
            commentAdapter.updateList(comments);

            //clear textView
            commentTV.setText("");

            //hide keyboard
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void populateListViewComments(ListView listViewComments){
        commentAdapter = new CommentAdapter(this, comments);
        listViewComments.setAdapter(commentAdapter);
        listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserFragment userFragment = new UserFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                        .replace(R.id.nav_host_fragment, userFragment, userFragment.getTag())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
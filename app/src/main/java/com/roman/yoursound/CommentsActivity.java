package com.roman.yoursound;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.roman.yoursound.adapters.CommentAdapter;
import com.roman.yoursound.models.Comment;
import com.roman.yoursound.ui.user.UserFragment;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    private ArrayList<Comment> comments = new ArrayList<>();
    private CommentAdapter commentAdapter;
    public ListView listViewComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listViewComments = (ListView)findViewById(R.id.comments_list);
        populateListViewComments(listViewComments);
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
        comments.add(new Comment("nice"));
        comments.add(new Comment("safhua asgfuigaueighsg esughsei hsielfhseilfu sef"));
        comments.add(new Comment("hfieshfse hfilsehfisle fhseu fhsiuefh siefhsieh"));
        comments.add(new Comment("hseiuh"));
        comments.add(new Comment("hgeiluhg sehg seiugh sieugh siugl shegiuhdgd,gdhglsgu sheguilh siugh"));
        comments.add(new Comment("iusegh espghsehpgaphg shsreilgdr gd gldurig"));
        comments.add(new Comment("driughrhwlhg weiugh lwiehgwlinlgb eslgbisebg"));
        comments.add(new Comment("hgrdilhg rdlihgidlrniwlihgwuil hilsfheliufhsfsd"));
        comments.add(new Comment("hldirhgdr gulidrhguilhdrgigughqilg islueghslugildh hislughis"));
        comments.add(new Comment("hgislug hesglihqlugsehls lsigherils gheilugh seiugh esuilrghliusehgiln idfublgsligrlseirguu hlisuerghifb"));
        comments.add(new Comment("iugdrhliughr glihsdrgilrglgcjgfoihgerg ulidrh gdligidbigldrhg dlrhgdrbguildrghdui"));
        comments.add(new Comment("drhlgi drhg drihgdiurhdgblgh luhgrsuighbdrlgbdrig drgiudrbg"));
        comments.add(new Comment("rhgludrhgi lighiulhwgluihg ;dghd;ohorhdrlgh drh jbcv,mbndrilrhg"));
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

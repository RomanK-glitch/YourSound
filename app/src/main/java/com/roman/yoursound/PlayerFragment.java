package com.roman.yoursound;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.roman.yoursound.ui.Comments.CommentsActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class PlayerFragment extends Fragment {

    //Views
    Button goToComments;
    TextView goToArtist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.player_fragment, container, false);

        //initialize views
        goToComments = (Button)root.findViewById(R.id.player_button_comment);
        goToArtist = (TextView)root.findViewById(R.id.player_track_author);

        goToComments.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (MainActivity.mainCurrentTrack != null) {
                    Intent toComments = new Intent(getActivity(), CommentsActivity.class);
                    Bundle commentParam = new Bundle();
                    commentParam.putInt("trackId", MainActivity.mainCurrentTrack.id);
                    toComments.putExtras(commentParam);
                    startActivity(toComments);
                }
            }
        });

        goToArtist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //collapse player
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeFragment();
                //change fragment in main activity
            }
        });

        return root;
    }
}

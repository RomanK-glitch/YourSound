package com.roman.yoursound;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.player_fragment, container, false);

        Button goTo_comments = (Button)root.findViewById(R.id.player_button_comment);
        goTo_comments.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent toComments = new Intent(getActivity(), CommentsActivity.class);
                startActivity(toComments);
            }
        });

        return root;
    }
}

package com.roman.yoursound.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.roman.yoursound.R;
import com.roman.yoursound.models.Comment;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    Context context;
    ArrayList<Comment> comments;
    private static LayoutInflater inflater = null;

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        this.context = context;
        this.comments = comments;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null){
            itemView = inflater.inflate(R.layout.comment_list, parent, false);
        }

        TextView commentTextView = (TextView)itemView.findViewById(R.id.text_view_comment);
        Comment selectedComment = comments.get(position);
        commentTextView.setText(selectedComment.commentText);
        return itemView;
    }
}

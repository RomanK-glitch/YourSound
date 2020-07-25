package com.roman.yoursound.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.roman.yoursound.R;
import com.roman.yoursound.models.User;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    Context context;
    ArrayList<User> users;
    private static LayoutInflater inflater = null;

    public UserAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(ArrayList<User> newUsers){
        users = newUsers;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null){
            itemView = inflater.inflate(R.layout.user_list, parent, false);
        }

        TextView userNameView = (TextView)itemView.findViewById(R.id.user_list_user_name);
        ImageView userImage = itemView.findViewById(R.id.user_image_list);
        User selectedUser = users.get(position);
        userNameView.setText(selectedUser.userName);
        Picasso.get().load(selectedUser.imagePath).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(userImage);
        return itemView;
    }
}

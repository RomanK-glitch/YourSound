package com.roman.yoursound.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.roman.yoursound.R;
import com.roman.yoursound.models.Track;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class TrackAdapter extends BaseAdapter {
    Context context;
    ArrayList<Track> tracks;
    private static LayoutInflater inflater = null;

    public TrackAdapter(Context context, ArrayList<Track> tracks){
        this.context = context;
        this.tracks = tracks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateList(ArrayList<Track> newTracks){
        tracks = newTracks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Track getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null){
            itemView = inflater.inflate(R.layout.track_list, parent, false);
        }
        TextView textViewTrackName = (TextView)itemView.findViewById(R.id.textViewTrackName);
        TextView textViewTrackAuthor = (TextView)itemView.findViewById(R.id.textViewTrackAuthor);
        TextView textViewDuration = (TextView)itemView.findViewById(R.id.textViewDuration);
        TextView textViewTracksPlayed = (TextView)itemView.findViewById(R.id.textViewTrackPlayed);
        TextView textViewTracksLikes = (TextView)itemView.findViewById(R.id.textViewTrackLikes);
        ImageView trackImage = itemView.findViewById(R.id.trackImage);
        Track selectedTrack = tracks.get(position);
        textViewTrackName.setText(selectedTrack.name);
        textViewTrackAuthor.setText(selectedTrack.author);
        textViewDuration.setText(selectedTrack.duration);
        textViewTracksLikes.setText(Integer.toString(selectedTrack.listening));
        textViewTracksPlayed.setText(Integer.toString(selectedTrack.listening));
        Picasso.get().load(selectedTrack.image_path).fit().centerCrop().placeholder(R.drawable.song_image).error(R.drawable.song_image).into(trackImage);
        return itemView;
    }
}

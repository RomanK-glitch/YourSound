package com.roman.yoursound.ui.AddTrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.roman.yoursound.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class AddTrackActivity extends AppCompatActivity {

    //Views
    Button choseImageBtn, choseFileBtn, addTrackBtn;
    ImageView trackImageIV;
    EditText trackNameET;
    ProgressDialog dialog;

    String trackFilePath = "", trackName = "", imageFilePath = "", imageType = "", trackType = "", newFileName = "";
    Uri trackImageUri;
    Uri trackFileUri;
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int TRACK_REQUEST_CODE = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        final AddTrackActivity addTrackActivity = this;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize views
        choseImageBtn = (Button)findViewById(R.id.add_track_chose_image_btn);
        choseFileBtn = (Button)findViewById(R.id.add_track_chose_file_btn);
        addTrackBtn = (Button)findViewById(R.id.add_track_add);
        trackImageIV = (ImageView)findViewById(R.id.add_track_image);
        trackNameET = (EditText)findViewById(R.id.add_track_name);

        //chose image
        choseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Chose image"), GALLERY_REQUEST_CODE);
            }
        });

        //chose track file
        choseFileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Chose file"), TRACK_REQUEST_CODE);
            }
        });

        //add track
        addTrackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                trackName = trackNameET.getText().toString();
                if (trackFilePath.isEmpty()){
                    Toast.makeText(addTrackActivity, "Chose file", Toast.LENGTH_SHORT).show();
                } else if (trackName.isEmpty()) {
                    Toast.makeText(addTrackActivity, "Input track name", Toast.LENGTH_SHORT).show();
                } else {

                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(addTrackActivity, trackFileUri);
                    String durationMilliseconds = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    String durationStr = formatMillisecond(Long.parseLong(durationMilliseconds));

                    if (!imageFilePath.isEmpty()) {
                        imageType = imageFilePath.substring(imageFilePath.lastIndexOf("."));
                    }
                    trackType = trackFilePath.substring(trackFilePath.lastIndexOf("."));

                    //loading dialog
                    dialog = new ProgressDialog(addTrackActivity);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setTitle("Uploading");
                    dialog.setMessage("Uploading track. Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    PostTrack postTrack = new PostTrack(addTrackActivity, trackName, durationStr, trackType, imageType);
                    postTrack.execute();
                }
            }
        });
    }

    //on post track response
    public void onPostTrackResponse (String response) {

        if (response.contains("Success")){
            newFileName = response.replace("Success", "");
        } else {
            Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
        }

        if (!imageFilePath.isEmpty()) {
            PostTrackImage postTrackImage = new PostTrackImage(imageFilePath, this, newFileName);
            postTrackImage.execute();
        }
        PostTrackFile postTrackFile = new PostTrackFile(trackFilePath, this, newFileName);
        postTrackFile.execute();

    }

    //on track file upload
    public void onTrackFileUpload() {
        dialog.dismiss();
        this.finish();
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //on image select
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            trackImageUri = data.getData();
            imageFilePath = getRealPathFromURI(this, trackImageUri);
            Picasso.get().load(trackImageUri).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(trackImageIV);
        }
        if (requestCode == TRACK_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            trackFileUri = data.getData();
            trackFilePath = getRealPathFromURI(this, trackFileUri);
            choseFileBtn.setText(trackFilePath);
        }
    }

    //get file path from uri
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //Convert milliseconds to time format
    public String formatMillisecond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
}

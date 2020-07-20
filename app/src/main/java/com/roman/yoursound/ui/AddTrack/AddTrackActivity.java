package com.roman.yoursound.ui.AddTrack;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.roman.yoursound.R;

public class AddTrackActivity extends AppCompatActivity {

    //Views
    Button choseImageBtn, choseFileBtn, addTrackBtn;
    ImageView trackImageIV;

    String imageFilePath = "";
    String trackFilePath = "";
    Uri trackImageUri;
    private static final int GALLERY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize views
        choseImageBtn = (Button)findViewById(R.id.add_track_chose_image_btn);
        choseFileBtn = (Button)findViewById(R.id.add_track_chose_file_btn);
        addTrackBtn = (Button)findViewById(R.id.add_track_add);
        trackImageIV = (ImageView)findViewById(R.id.add_track_image);

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
                //chose track file
            }
        });

        //add track
        addTrackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //if imageFilePath is not empty post image file
                //if trackFilePath is empty toast chose file
                //else post track
            }
        });
    }

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
            trackImageIV.setImageURI(trackImageUri);
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
}

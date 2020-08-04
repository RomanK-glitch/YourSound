package com.roman.yoursound.ui.EditProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.roman.yoursound.MainActivity;
import com.roman.yoursound.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {

    //Views
    Button choseImageBtn;
    Button saveChangesBtn;
    TextView nameTV;
    TextView aboutTV;
    ImageView userImageIV;
    ProgressDialog dialog;

    String newName, newAbout;
    String filePath = "";
    String newImagePath = "";
    Uri newImageUri;
    private static final int GALLERY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        final EditProfileActivity etActivity = this;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing views
        choseImageBtn = findViewById(R.id.change_image_btn);
        saveChangesBtn = findViewById(R.id.save_changes);
        nameTV = findViewById(R.id.change_info_name);
        aboutTV = findViewById(R.id.change_info_about);
        userImageIV = findViewById(R.id.change_info_image);

        //set user data to views
        nameTV.setText(MainActivity.userLocalStore.getLoggedInUser().userName);
        aboutTV.setText(MainActivity.userLocalStore.getLoggedInUser().about);
        Picasso.get().load(MainActivity.userLocalStore.getLoggedInUser().imagePath).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(userImageIV);

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

        //save changes
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName = nameTV.getText().toString();
                newAbout = aboutTV.getText().toString();
                if (!filePath.isEmpty()) {
                    //loading dialog
                    dialog = new ProgressDialog(etActivity);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setTitle("Uploading");
                    dialog.setMessage("Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    PostUserImage postUserImage = new PostUserImage(filePath, etActivity);
                    postUserImage.execute();
                    newImagePath = "http://mrkoste6.beget.tech/user_image/" + String.valueOf(MainActivity.userLocalStore.getLoggedInUser().id) + filePath.substring(filePath.indexOf("."));
                    MainActivity.userLocalStore.changeUserImage(newImagePath);
                }
                PostChanges postChanges = new PostChanges(MainActivity.userLocalStore.getLoggedInUser().id, newName, newAbout, newImagePath, etActivity);
                postChanges.execute();

            }
        });
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
            newImageUri = data.getData();
            filePath = getRealPathFromURI(this, newImageUri);
            userImageIV.setImageURI(newImageUri);
        }
    }

    //on server response
    public void onSave(String result){
        if (result.equals("Success")){
            MainActivity.userLocalStore.changeUserData(newName, newAbout);
            this.finish();
        } else if (result.contains("Duplicate entry")) {
            Toast.makeText(this, "This name alreay taken", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
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

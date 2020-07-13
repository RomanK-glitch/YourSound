package com.roman.yoursound.ui.EditProfile;

import android.content.Intent;
import android.net.Uri;
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
import com.roman.yoursound.models.User;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 123;

    //Views
    Button choseImageBtn;
    Button saveChangesBtn;
    TextView nameTV;
    TextView aboutTV;
    ImageView userImageIV;

    String newName, newAbout;
    Uri newImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            newImageUri = data.getData();
            userImageIV.setImageURI(newImageUri);
        }
    }

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
        Picasso.get().load(MainActivity.userLocalStore.getLoggedInUser().imagePath).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(userImageIV);

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
                //if newImageUri not null, create new PostImage() execute
                PostChanges postChanges = new PostChanges(MainActivity.userLocalStore.getLoggedInUser().id, newName, newAbout, newImageUri, etActivity);
                postChanges.execute();
                //PostUserImage postUserImage = new PostUserImage(newImageUri, etActivity);
                //postUserImage.execute();
                MainActivity.userLocalStore.changeUserData(newName, newAbout);
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

    public void onSave(String result){
        if (result.equals("Success")){
            this.finish();
        } else {
            Toast.makeText(this, "Sorry, something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}

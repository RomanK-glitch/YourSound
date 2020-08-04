package com.roman.yoursound;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    String userName, imagePath;

    //Views
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize views
        image = findViewById(R.id.full_screen_image);

        //get parameters
        Bundle parameters = getIntent().getExtras();
        if (parameters != null){
            userName = parameters.getString("userName");
            imagePath = parameters.getString("imagePath");
        }

        //change action bar
        getSupportActionBar().setTitle(userName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        //show image
        Picasso.get().load(imagePath).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.purple_user).error(R.drawable.purple_user).into(image);
    }

    //back
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

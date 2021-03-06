package com.roman.yoursound;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.roman.yoursound.models.Track;
import com.roman.yoursound.models.UserLocalStore;
import com.roman.yoursound.ui.user.UserFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    //Views
    ConstraintLayout dragView;
    public SlidingUpPanelLayout slidingUpPanelLayout;
    View playerFragment;
    Button bottomBtnPlay, playBtn;
    SeekBar positionBar;
    TextView elapsedTimeLabel, remainingTimeLabel, currentTrackNameBottom, currentTrackAuthorBottom, currentTrackNamePlayer, currentTrackAuthorPlayer;
    ImageView player_trackImage;
    public static Track mainCurrentTrack;

    public static UserLocalStore userLocalStore;
    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Local user data
        userLocalStore = new UserLocalStore(this);

        //Draggable player

        dragView = (ConstraintLayout)findViewById(R.id.dragView);
        playerFragment = findViewById(R.id.player_fragment);
        playerFragment.setAlpha(0);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_panel);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            //hide drag view
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                dragView.setAlpha(1 - slideOffset);
                playerFragment.setAlpha(slideOffset);
            }

            //change drag view
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    //mke drag view bigger
                    slidingUpPanelLayout.setDragView(findViewById(R.id.player_imageView));
                } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    //make drag view smaller
                    playerFragment.setVisibility(View.GONE);
                    slidingUpPanelLayout.setDragView(dragView);
                } else {
                    playerFragment.setVisibility(View.VISIBLE);
                }
            }

        });

        //Initialize Views
        bottomBtnPlay = (Button)findViewById(R.id.bottom_buttonPlay);
        playBtn = (Button)findViewById(R.id.player_button_play);
        elapsedTimeLabel = (TextView)findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);
        positionBar = (SeekBar)findViewById(R.id.positionBar);
        currentTrackNameBottom = findViewById(R.id.textViewCurrentTrackName);
        currentTrackAuthorBottom = findViewById(R.id.textViewCurrentTrackAuthor);
        currentTrackNamePlayer = findViewById(R.id.player_track_name);
        currentTrackAuthorPlayer = findViewById(R.id.player_track_author);
        player_trackImage = findViewById(R.id.player_imageView);
        Picasso.get().load("http://mrkoste6.beget.tech/track_image/song.png").fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.song_image).error(R.drawable.song_image).into(player_trackImage);

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void changeFragment() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //change fragment
        UserFragment userFragment = new UserFragment();
        Bundle selectedUserId = new Bundle();
        selectedUserId.putInt("userId", mainCurrentTrack.userId);
        userFragment.setArguments(selectedUserId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_left, R.animator.enter_from_left, R.animator.exit_to_right)
                .replace(R.id.nav_host_fragment, userFragment, userFragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    //Player preparing
    public void playerPreparing(Track currentTrack){

        mainCurrentTrack = currentTrack;

        PostListened postListened = new PostListened(currentTrack.id);
        postListened.execute();

        currentTrackAuthorBottom.setText(currentTrack.author);
        currentTrackNameBottom.setText(currentTrack.name);
        currentTrackNamePlayer.setText(currentTrack.name);
        currentTrackAuthorPlayer.setText(currentTrack.author);
        Picasso.get().load(currentTrack.image_path).fit().centerCrop().memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.song_image).error(R.drawable.song_image).into(player_trackImage);

        mp.stop();
        mp.reset();
        mp.setLooping(true);

        try {
            mp.setDataSource(currentTrack.path);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.seekTo(0);
                    totalTime = mp.getDuration();
                    mp.start();
                    playBtn.setBackgroundResource(R.drawable.control_pause_black);
                    bottomBtnPlay.setBackgroundResource(R.drawable.control_pause_white);
                    positionBar.setMax(totalTime);
                }
            });

        }
        catch(Exception ex){

        }

        //PositionBar
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Thread(Update positionBar and TimeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp != null){
                    try{
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int currentPosition = msg.what;

            //Update positionBar
            positionBar.setProgress(currentPosition);

            //Update labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time){
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    //Play and pause button
    public void playBtnClick(View view){
        if(!mp.isPlaying()){
            //Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.control_pause_black);
            bottomBtnPlay.setBackgroundResource(R.drawable.control_pause_white);
        }
        else {
            //Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.control_play_black);
            bottomBtnPlay.setBackgroundResource(R.drawable.control_play_white);
        }
    }

    //close player on back button
    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }  else  {
            super.onBackPressed();
        }
    }
}
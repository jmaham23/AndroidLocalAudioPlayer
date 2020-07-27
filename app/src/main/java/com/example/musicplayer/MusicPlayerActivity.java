package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {
    private Handler h = new Handler();
    static ArrayList<Music> songList = new ArrayList<>();
    static ArrayList<Music> likedList = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mp;
    private Thread playPauseThread;
    private Thread skipNextThread;
    private Thread skipPrevThread;

    //all the buttons and views in our activity xml
    FloatingActionButton playPause;
    SeekBar seek;
    ImageView albumArt;
    ImageView shuffle;
    ImageView back;
    ImageView prev;
    ImageView next;
    ImageView like;
    TextView songName;
    TextView albumName;
    TextView artistName;
    TextView currentTime;
    TextView endTime;
    int pos = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        loadData();

        //initializing views
        playPause = findViewById(R.id.play_pause);
        seek = findViewById(R.id.seek_bar);
        albumArt = findViewById(R.id.album_cover);
        shuffle = findViewById(R.id.off_shuffle);
        back = findViewById(R.id.back);
        prev = findViewById(R.id.skip_prev);
        next = findViewById(R.id.skip_next);
        like = findViewById(R.id.off_favorite);


        songName = findViewById(R.id.player_song_name);
        albumName = findViewById(R.id.player_album_name);
        artistName = findViewById(R.id.player_artist_name);
        currentTime = findViewById(R.id.duration_current);
        endTime = findViewById(R.id.duration_total);

        pos = getIntent().getIntExtra("position", -1);
        songList = MainActivity.getMusic();
        //https://stackoverflow.com/questions/51837927/sending-music-file-from-one-application-to-another
        if (songList != null) {
            playPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(songList.get(pos).getPath());
        }
        if (mp == null) {
            mp = MediaPlayer.create(getApplicationContext(), uri);
            mp.start();
        } else {
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(getApplicationContext(), uri);
            mp.start();
        }

        //set in seconds
        seek.setMax(mp.getDuration() / 1000);
        int end = seek.getMax();
        String total = "";
        String s = String.valueOf(end % 60);
        String m = String.valueOf(end / 60);
        if (s.length() == 2) {
            total = m + ":" + s;
        } else {
            total = m + ":0" + s;
        }
        endTime.setText(total);

        if(likedList.contains(songList.get(pos))){
            like.setColorFilter(getApplicationContext().getResources().getColor(R.color.orange));
        }
        else{
            like.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorAccent));
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likedList.contains(songList.get(pos))){
                    like.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorAccent));
                    likedList.remove(songList.get(pos));
                    saveData();
                }
                else{
                    like.setColorFilter(getApplicationContext().getResources().getColor(R.color.orange));
                    likedList.add(songList.get(pos));
                    saveData();
                }
            }
        });


        //keep seek bar updated, methods generated by android studio
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mp != null && fromUser == true) {
                    mp.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //will automatically update the current song time
        https://stackoverflow.com/questions/11140285/how-do-we-use-runonuithread-in-android
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mp != null) {
                    int current = mp.getCurrentPosition() / 1000;
                    seek.setProgress(current);
                    //formatting time
                    String total = "";
                    String s = String.valueOf(current % 60);
                    String m = String.valueOf(current / 60);

                    if (s.length() == 2) {
                        total = m + ":" + s;
                    } else {
                        total = m + ":0" + s;
                    }

                    currentTime.setText(total);


                }
                h.postDelayed(this, 1000);
            }
        });
        metaData(uri);
        //setting views to appropriate meta data
        songName.setText(songList.get(pos).getTitle());
        albumName.setText(songList.get(pos).getAlbum());
        artistName.setText(songList.get(pos).getArtist());

        //item click listener for back button, just ends activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void metaData(Uri uri){
        MediaMetadataRetriever r = new MediaMetadataRetriever();
        r.setDataSource(uri.toString());

        byte[] album_art = r.getEmbeddedPicture();
        if(album_art == null){
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.default_album_art)
                    .into(albumArt);
        }
        else {
            Glide.with(this)
                    .asBitmap()
                    .load(album_art)
                    .into(albumArt);
        }
    }

    //auto generated overrided function
    @Override
    protected void onResume() {
        playPauseThreadBtn();
        skipNextThreadBtn();
        skipPrevThreadBtn();
        super.onResume();
    }

    private void skipPrevThreadBtn() {
    }

    private void skipNextThreadBtn() {
    }

    private void playPauseThreadBtn() {
        playPauseThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mp.isPlaying()){
                            playPause.setImageResource(R.drawable.ic_play);
                            mp.pause();
                            seek.setMax(mp.getDuration() / 1000);

                            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mp != null) {
                                        int current = mp.getCurrentPosition() / 1000;
                                        seek.setProgress(current);
                                    }
                                    h.postDelayed(this, 1000);
                                }
                            });
                        }
                        else{
                            playPause.setImageResource(R.drawable.ic_pause);
                            mp.start();
                            seek.setMax(mp.getDuration() / 1000);

                            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mp != null) {
                                        int current = mp.getCurrentPosition() / 1000;
                                        seek.setProgress(current);
                                    }
                                    h.postDelayed(this, 1000);
                                }
                            });
                        }
                    }
                });


            }
        };
        playPauseThread.start();
    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(likedList);
        editor.putString("music list", json);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("music list", null);
        Type type = new TypeToken<ArrayList<Music>>() {}.getType();
        likedList = gson.fromJson(json, type);

        if(likedList == null){
            likedList = new ArrayList<>();
        }

    }
}



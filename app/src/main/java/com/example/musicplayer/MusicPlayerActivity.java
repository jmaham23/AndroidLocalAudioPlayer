package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {
    private Handler h = new Handler();
    static ArrayList<Music> songList = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mp;

    //all the buttons and views in our activity xml
    FloatingActionButton playPause;
    SeekBar seek;
    ImageView albumArt;
    ImageView shuffle;
    ImageView back;
    ImageView prev;
    ImageView next;
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


        //initializing views
        playPause = findViewById(R.id.play_pause);
        seek = findViewById(R.id.seek_bar);
        albumArt = findViewById(R.id.album_cover);
        shuffle = findViewById(R.id.off_shuffle);
        back = findViewById(R.id.back);
        prev = findViewById(R.id.skip_prev);
        next = findViewById(R.id.skip_next);

        songName = findViewById(R.id.player_song_name);
        albumName = findViewById(R.id.player_album_name);
        artistName = findViewById(R.id.player_artist_name);
        currentTime = findViewById(R.id.duration_current);
        endTime = findViewById(R.id.duration_total);

        pos = getIntent().getIntExtra("position", -1);
        songList = MainActivity.getMusic();
        //https://stackoverflow.com/questions/51837927/sending-music-file-from-one-application-to-another
        if (songList != null){
            playPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(songList.get(pos).getPath());
        }
        if(mp == null){
            mp = MediaPlayer.create(getApplicationContext(), uri);
            mp.start();
        }
        else {
            mp.stop();
            mp.release();
            mp = MediaPlayer.create(getApplicationContext(), uri);
            mp.start();
        }

        //set in seconds
        seek.setMax(mp.getDuration() / 1000);

        //keep seek bar updated, methods generated by android studio
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mp != null && fromUser==true){
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


        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mp != null){
                    int current = mp.getCurrentPosition() / 1000;
                    seek.setProgress(current);
                    //formatting time
                    String total="";
                    String s = String.valueOf(current % 60);
                    String m = String.valueOf(current / 60);

                    if (s.length() == 2){ total = m + ":" + s; }
                    else{ total = m + ":0" + s; }

                    currentTime.setText(total);

                }
                h.postDelayed(this, 1000);
            }
        });
    }
}
package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.getMusic;

public class AlbumViewActivity extends AppCompatActivity {
    String albumName;
    ArrayList<Music> songsInAlbum = new ArrayList<>();
    AlbumAdapter albumAdapter;
    ConstraintLayout albumsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}

package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongFragment extends Fragment {

    private ListView songs;
    private MusicAdapter adapter;
    ArrayList<Music> musicFiles;
    public SongFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);
        songs = (ListView) rootView.findViewById(R.id.songs);
        musicFiles = MainActivity.getMusic();
        adapter = new MusicAdapter(getContext(), R.layout.audio, musicFiles);
        songs.setAdapter(adapter);
        return rootView;
    }

}

package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    private ListView albums;
    private MusicAdapter adapter;
    ArrayList<Music> musicFiles;


    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);

        // Inflate the layout for this fragment
        albums = (ListView) v.findViewById(R.id.albumListview);
        musicFiles = MainActivity.getMusic();
        adapter = new MusicAdapter(getContext(), R.layout.audio, musicFiles);
        albums.setAdapter(adapter);

        return v;
    }
}

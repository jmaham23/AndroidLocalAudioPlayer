package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musicplayer.MainActivity;
import com.example.musicplayer.Music;
import com.example.musicplayer.MusicAdapter;
import com.example.musicplayer.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends Fragment {

    private ListView songs;
    private MusicAdapter adapter;
    public static ArrayList<Music> musicFiles;
    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song, container, false);
        songs = (ListView) rootView.findViewById(R.id.songs);
        musicFiles = MusicPlayerActivity.likedList;
        adapter = new MusicAdapter(getContext(), R.layout.audio, musicFiles);
        songs.setAdapter(adapter);
        return rootView;
    }

}

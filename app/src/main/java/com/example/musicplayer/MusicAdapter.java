package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//adapting music items for list view
public class MusicAdapter extends BaseAdapter {
    //layout
    private int lyt;
    private Context cnxt;
    private ArrayList<Music> musicList;

    //constructor
    public MusicAdapter(Context cnxt, int lyt, ArrayList<Music> musicList){
        this.cnxt = cnxt;
        this.lyt = lyt;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class MyViewHolder{
        TextView songName;
        TextView artistName;

        ImageView playIcon;
        ImageView pauseIcon;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder myViewHolder;

        if(convertView != null){
            myViewHolder = (MyViewHolder)convertView.getTag();
        }

        else{
            myViewHolder = new MyViewHolder();

            LayoutInflater lI = (LayoutInflater) cnxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lI.inflate(lyt, null);


            myViewHolder.songName=(TextView)convertView.findViewById(R.id.song_name);
            myViewHolder.artistName=(TextView)convertView.findViewById(R.id.artist);
            myViewHolder.playIcon=(ImageView) convertView.findViewById(R.id.play_icon);
            myViewHolder.pauseIcon=(ImageView)convertView.findViewById(R.id.pause_icon);

            convertView.setTag(myViewHolder);
        }
        Music music = musicList.get(position);

        myViewHolder.songName.setText(music.getTitle());
        myViewHolder.artistName.setText(music.getArtist());
        return convertView;
    }
}

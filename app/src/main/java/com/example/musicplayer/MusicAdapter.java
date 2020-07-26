package com.example.musicplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//adapting music items for list view
public class MusicAdapter extends BaseAdapter {
    //layout
    private int lyt;
    private Context cnxt;
    private ArrayList<Music> musicList;
    private MediaPlayer mp;

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
        ImageView albumArt;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            //myViewHolder.pauseIcon=(ImageView)convertView.findViewById(R.id.pause_icon);
            myViewHolder.albumArt=(ImageView)convertView.findViewById(R.id.album_art);

            convertView.setTag(myViewHolder);
        }
        Music music = musicList.get(position);

        myViewHolder.songName.setText(music.getTitle());
        myViewHolder.artistName.setText(music.getArtist());
        byte[] art = getAlbumArt(musicList.get(position).getPath());

        if(art == null){
            //default artwork
            Glide.with(cnxt).asBitmap()
                    //embedded album art
                    .load(R.drawable.default_album_art)
                    .into(myViewHolder.albumArt);
        }
        else{
            //http://www.skholingua.com/android-basic/other-sdk-n-libs/glide
            Glide.with(cnxt).asBitmap()
                    //embedded album art
                    .load(art)
                    .into(myViewHolder.albumArt);
        }

        //set up onclicklisteners to play music
        myViewHolder.playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mp = MediaPlayer.create(cnxt, Uri.parse(musicList.get(position).getPath()));
                mp.start();
            }
        });


        /*
        myViewHolder.pauseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mp.pause();
            }
        });
        */
        return convertView;
    }

    //get album art
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever r = new MediaMetadataRetriever();
        r.setDataSource(uri);
        byte [] albumArtArray = r.getEmbeddedPicture();
        r.release();
        return albumArtArray;
    }
}

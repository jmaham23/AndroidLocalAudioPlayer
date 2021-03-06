package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.function.LongPredicate;

//adapting music items for list view
public class AlbumAdapter extends BaseAdapter {
    //layout
    private int lyt;
    private Context cnxt;
    private ArrayList<Music> albumList;
    private MediaPlayer mp;

    //constructor
    public AlbumAdapter(Context cnxt, int lyt, ArrayList<Music> musicList){
        this.cnxt = cnxt;
        this.lyt = lyt;
        this.albumList = musicList;
    }

    @Override
    public int getCount() {
        if(!albumList.isEmpty())
            return albumList.size();
        else{
            return 0;
        }
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
        TextView artistName;
        TextView albumName;
        ImageView dropDown;
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

            myViewHolder.artistName=(TextView)convertView.findViewById(R.id.artist);
            myViewHolder.dropDown=(ImageView) convertView.findViewById(R.id.drop_down);
            myViewHolder.albumArt=(ImageView)convertView.findViewById(R.id.album_art);
            myViewHolder.albumName=(TextView)convertView.findViewById(R.id.album_name);

            convertView.setTag(myViewHolder);
        }
        Music music = albumList.get(position);

        myViewHolder.artistName.setText(music.getArtist());
        byte[] art = getAlbumArt(albumList.get(position).getPath());

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
        myViewHolder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cnxt, AlbumViewActivity.class);
                intent.putExtra("songs in album", albumList.get(position).getAlbum());
                cnxt.startActivity(intent);
            }
        });


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

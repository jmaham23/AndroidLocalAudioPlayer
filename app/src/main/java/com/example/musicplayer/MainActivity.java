package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActivityManager;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_audio);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

        //initialize view pager
        if(!permsDenied()){
            musicFilesPermanent = getAudio(this);
            initVP();
        }
    }

    private static final int REQUEST_PERMS = 0; //request code
    private static final int PERMS_COUNT = 1;
    private static ArrayList<Music> musicFilesPermanent;

    public static ArrayList<Music> getMusic(){
        return musicFilesPermanent;
    }

    public static ArrayList<Music> getLikedMusic(){
        return musicFilesPermanent;
    }


    //permission to read files like music files from devices storage
    private static final String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE};


    private void initVP(){
        VPAdapter vpa = new VPAdapter(getSupportFragmentManager());
        ViewPager vp = findViewById(R.id.viewpager);
        TabLayout tl = findViewById(R.id.tab_layout);


        //adding albums and songs
        vpa.addFrags(new SongFragment(), "Songs");
        vpa.addFrags(new AlbumFragment(), "Albums");
        vpa.addFrags(new LikeFragment(), "Liked");

        //set viewpager to one initialized
        vp.setAdapter(vpa);
        tl.setupWithViewPager(vp);
    }

    //view pager adapter
    public static class VPAdapter extends FragmentPagerAdapter{

        private ArrayList<String> names;
        private ArrayList<Fragment> frags;

        public VPAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.frags = new ArrayList<>();
            this.names = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return frags.get(position);
        }

        @Override
        public int getCount() {
            return frags.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int pos){
            //return title position
            return names.get(pos);
        }

        void addFrags(Fragment frag, String name){
            names.add(name);
            frags.add(frag);
        }


    }

    public static ArrayList<Music> getAudio(Context context){
        ArrayList<Music> audioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        //https://riptutorial.com/android/example/23916/fetch-audio-mp3-files-from-specific-folder-of-device-or-fetch-all-files
        //read in music file information into Music instance
        //function to load local music files into array
        if(cursor != null){
            while(cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String  duration = cursor.getString(2);
                String artist = cursor.getString(3);
                String data = cursor.getString(4);

                Music music = new Music(title, album, artist, data, duration);
                //storing audio files in log cat for testing
                //audio files are stored in a virtual sd card within the emulator
                Log.e("Audio File Path:" + data, "Album: " + album);
                audioList.add(music);
            }
            cursor.close();
        }
        return audioList;
    }
    //check if permissions are denied
    private boolean permsDenied() {
        for(int i = 0; i< PERMS_COUNT ; i++){
            if(checkSelfPermission(PERMS[i]) != PackageManager.PERMISSION_GRANTED)
                return true;

        }
        return false;
    }

    //keep requesting permission until permission is granted
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] grantRes){
        super.onRequestPermissionsResult(reqCode, perms, grantRes);


        if(permsDenied()){
            //recreate activity if user is denied activity
            ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        }

        else{
            onResume();
        }
    }

    //check if we have to request permissions
    @Override
    protected void onResume(){
        musicFilesPermanent = getAudio(this);
        super.onResume();
        if(permsDenied()) {
            requestPermissions(PERMS, REQUEST_PERMS);
            return;
        }
    }
}

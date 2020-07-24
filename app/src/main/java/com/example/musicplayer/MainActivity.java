package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.ActivityManager;
import android.os.Build.VERSION;

import com.google.android.material.tabs.TabLayout;

import java.lang.Object;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize view pager
        initVP();
    }

    private static final int REQUEST_PERMS = 0; //request code
    private static final int PERMS_COUNT = 1;
    //permission to read files like music files from devices storage
    private static final String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE};


    private void initVP(){
        VPAdapter vpa = new VPAdapter(getSupportFragmentManager());
        ViewPager vp = findViewById(R.id.viewpager);
        TabLayout tl = findViewById(R.id.tab_layout);


        //adding albums and songs
        vpa.addFrags(new SongFragment(), "Songs");
        vpa.addFrags(new AlbumFragment(), "Albums");
        vpa.addFrags(new ArtistFragment(), "Artists");

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
    //check if permissions are denied
    private boolean permsDenied(){
        for(int i = 0; i< PERMS_COUNT ; i++){
            if(checkSelfPermission(PERMS[i]) != PackageManager.PERMISSION_GRANTED)
                return true;

        }
        return false;
    }

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
        super.onResume();
        if(permsDenied()){
            requestPermissions(PERMS, REQUEST_PERMS);
            return;
        }
    }
}

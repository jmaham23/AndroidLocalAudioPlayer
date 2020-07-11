package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final int REQUEST_PERMS = 0;
    private static final int PERMS_COUNT = 1;
    //permission to read files like music files from devices storage
    private static final String[] PERMS = {Manifest.permission.READ_EXTERNAL_STORAGE};


    //check if permissions are denied
    private boolean permsDenied(){
        for(int i = 0; i< PERMS_COUNT ; i++){
            if(checkSelfPermission(PERMS[i]) != PackageManager.PERMISSION_GRANTED)
                return true;

        }
        return false;
    }
}

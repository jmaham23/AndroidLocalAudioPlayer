package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.ActivityManager;
import android.os.Build.VERSION;
import java.lang.Object;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final int REQUEST_PERMS = 0; //request code
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

package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noMusic;
    ArrayList<Audio> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        noMusic = (TextView) findViewById(R.id.no_songs);

        if (checkPermission() == false)
        {
            requestPermission();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);

        while (cursor.moveToNext())
        {
            Audio songData = new Audio(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists())
                songsList.add(songData);
        }

        if ( songsList.size() == 0)
        {
            noMusic.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicList(songsList,getApplicationContext()));
        }

    }

    boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(MainActivity.this,"DOSYA OKUMA İZNİ GEREKLİ, LÜTFRN AYARLARDAN GEREKLİ İZİNLERİ VERİNİZ.",Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(recyclerView != null)
        {
            recyclerView.setAdapter(new MusicList(songsList,getApplicationContext()));
        }
    }
}
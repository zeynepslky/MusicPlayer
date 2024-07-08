package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titletv, currenttv, totalTimetv;
    SeekBar seekBar;
    ImageView pausePlay, nextBtn, previousBtn, musicIcon;
    ArrayList<Audio> songList;
    Audio currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    //int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titletv = (TextView) findViewById(R.id.song_title);
        currenttv = (TextView) findViewById(R.id.current_time);
        totalTimetv = (TextView) findViewById(R.id.total_time);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        pausePlay =(ImageView) findViewById(R.id.pause_play);
        nextBtn =(ImageView) findViewById(R.id.next);
        previousBtn = (ImageView) findViewById(R.id.previous);
        musicIcon = (ImageView) findViewById(R.id.music_icon);

        titletv.setSelected(true);

        songList = (ArrayList<Audio>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null)
                {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currenttv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                if(mediaPlayer.isPlaying())
                {
                    pausePlay.setImageResource(R.drawable.baseline_pause_24);
                    //musicIcon.setRotation(x++);
                }
                else
                {
                    pausePlay.setImageResource(R.drawable.baseline_arrow_drop_down_circle_24);
                    //musicIcon.setRotation(0);
                }
                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    void setResourcesWithMusic()
    {
        currentSong = songList.get(MyMediaPlayer.currentIndex);

        titletv.setText((currentSong.getTitle()));
        totalTimetv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());

        playMusic();
    }

    private void playMusic()
    {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    private void playNextSong()
    {
        if(MyMediaPlayer.currentIndex == songList.size() - 1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void playPreviousSong()
    {
        if(MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void pausePlay()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration)
    {
        long millis =Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis)%TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }

}
package com.eightywon.aqtrainer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp1;
    MediaPlayer mp2;
    MediaPlayer mp3;
    MediaPlayer mp4;
    ImageButton stage1Play;
    ImageButton stage2Play;
    ImageButton stage3Play;
    ImageButton stage4Play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stage1Play=(ImageButton) findViewById(R.id.btnStage1Play);
        stage2Play=(ImageButton) findViewById(R.id.btnStage2Play);
        stage3Play=(ImageButton) findViewById(R.id.btnStage3Play);
        stage4Play=(ImageButton) findViewById(R.id.btnStage4Play);

        mp1=new MediaPlayer();
        mp2=new MediaPlayer();
        mp3=new MediaPlayer();
        mp4=new MediaPlayer();

        try {
            AssetFileDescriptor afd1=getAssets().openFd("DescStage1.mp3");
            mp1.setDataSource(afd1.getFileDescriptor(),afd1.getStartOffset(),afd1.getLength());
            AssetFileDescriptor afd2=getAssets().openFd("DescStage2.mp3");
            mp2.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            AssetFileDescriptor afd3=getAssets().openFd("DescStage3.mp3");
            mp3.setDataSource(afd3.getFileDescriptor(),afd3.getStartOffset(),afd3.getLength());
            AssetFileDescriptor afd4=getAssets().openFd("DescStage4.mp3");
            mp4.setDataSource(afd4.getFileDescriptor(),afd4.getStartOffset(),afd4.getLength());
            mp1.prepare();
            mp2.prepare();
            mp3.prepare();
            mp4.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "nope", Toast.LENGTH_SHORT).show();;
        }
        stage1Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mp1.isPlaying()) {
                    mp1.pause();
                    stage1Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                } else {
                    mp1.start();
                    stage1Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
                }
            }
        });

        stage2Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mp.setLooping(true);
                if (mp2.isPlaying()) {
                    mp2.pause();
                    stage2Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                } else {
                    mp2.start();
                    stage2Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
                }
            }
        });

        stage3Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mp.setLooping(true);
                if (mp3.isPlaying()) {
                    mp3.pause();
                    stage3Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                } else {
                    mp3.start();
                    stage3Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
                }
            }
        });

        stage4Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mp.setLooping(true);
                if (mp4.isPlaying()) {
                    mp4.pause();
                    stage4Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                } else {
                    mp4.start();
                    stage4Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
                }
            }
        });

        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
            public void onCompletion(MediaPlayer p) {
               stage1Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
           }
        });

        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                stage2Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                stage3Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                stage4Play.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent=new Intent(this,DisplaySettings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

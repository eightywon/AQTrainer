package com.eightywon.aqtrainer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ViewPager aqtViewPager;

    public int previousPage;

    ImageButton testButton;

    MediaPlayer mp1;
    MediaPlayer mp2;
    MediaPlayer mp3;
    MediaPlayer mp4;

    public void playStage(View v) {
        testButton=(ImageButton) findViewById(v.getId());
        if (v.getId()==R.id.btnStage1Play) {
            if (mp1.isPlaying()) {
                mp1.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp1.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        } else if (v.getId()==R.id.btnStage2Play) {
            if (mp2.isPlaying()) {
                mp2.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp2.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        } else if (v.getId()==R.id.btnStage3Play) {
            if (mp3.isPlaying()) {
                mp3.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp3.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        } else if (v.getId()==R.id.btnStage4Play) {
            if (mp4.isPlaying()) {
                mp4.pause();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            } else {
                mp4.start();
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getPackageName()));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aqtViewPager=(ViewPager) findViewById(R.id.container);
        aqtViewPager.setAdapter(new AQTPagerAdapter(
                getSupportFragmentManager()));

        aqtViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (previousPage) {
                    case 0:
                        if (mp1.isPlaying()) {
                            Toast.makeText(MainActivity.this, "here", Toast.LENGTH_SHORT).show();
                            mp1.pause();
                            mp1.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage1Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                    case 1:
                        if (mp2.isPlaying()) {
                            mp2.pause();
                            mp2.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage2Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                    case 2:
                        if (mp3.isPlaying()) {
                            mp3.pause();
                            mp3.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage3Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                    case 3:
                        if (mp4.isPlaying()) {
                            mp4.pause();
                            mp4.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage4Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                }
                previousPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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

        mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage1Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage2Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage3Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });

        mp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton=(ImageButton) findViewById(R.id.btnStage4Play);
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
            }
        });
    }

    public class AQTPagerAdapter extends FragmentPagerAdapter {

        public AQTPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new StageOneFragment();
            } else if (position == 1) {
                return new StageTwoFragment();
            } else if (position == 2) {
                return new StageThreeFragment();
            } else {
                return new StageFourFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Stage 1";
            } else if (position == 1) {
                return "Stage 2";
            } else if (position == 2) {
                return "Stage 3";
            } else {
                return "Stage 4";
            }
        }
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

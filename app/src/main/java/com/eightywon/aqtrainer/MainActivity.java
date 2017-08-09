package com.eightywon.aqtrainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    final static int STEP_BEGIN=0;
    final static int STEP_STAGE_DESCRIPTION=1;
    final static int STEP_PREP_START=2;
    final static int STEP_PREP_IN_PROGRESS=3;
    final static int STEP_PREP_END=4;
    final static int STEP_LOAD=5;
    final static int STEP_FIRE_START=6;
    final static int STEP_FIRE_IN_PROGRESS=7;
    final static int STEP_FIRE_END=8;
    final static int STEP_DONE=99;

    /*
    final static String STEP_FIRE_END_MP3="cease.mp3";
    final static String STEP_STAGE_DESC_1_MP3="descstage1.mp3";
    final static String STEP_STAGE_DESC_2_MP3="descstage2.mp3";
    final static String STEP_STAGE_DESC_3_MP3="descstage3.mp3";
    final static String STEP_STAGE_DESC_4_MP3="descstage4.mp3";
    final static String STEP_STAGE_FIRE_START_MP3="fire.mp3";
    final static String STEP_STAGE_LOAD_MP3="load10.mp3";
    final static String STEP_STAGE_PREP_START_MP3="prepbegin.mp3";
    final static String STEP_STAGE_PREP_IN_PROGRESS_MP3="s30.ogg";
    final static String STEP_STAGE_PREP_END_MP3="prepend.mp3";
    final static String S1_MP3="s1.ogg";
    final static String S2_MP3="s1.ogg";
    final static String S3_MP3="s3.ogg";
    final static String S5_MP3="s5.ogg";
    final static String S10_MP3="s10.ogg";
    final static String S15_MP3="s15.ogg";
    final static String S30_MP3="s30.ogg";
    final static String S45_MP3="s45.ogg";
    final static String S55_MP3="s55.ogg";
    final static String S60_MP3="s60.ogg";
    final static String S65_MP3="s65.ogg";
    final static String S120_MP3="s120.ogg";
    final static String S300_MP3="s300.ogg";
    */

    static int audioFile;

    //static String STEP_BREAK_MP3="s2.ogg";

    public static int nextStep;

    ViewPager aqtViewPager;

    public int previousPage;
    public ImageButton testButton;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instance=this;

        aqtViewPager=(ViewPager) findViewById(R.id.container);
        aqtViewPager.setAdapter(new AQTPagerAdapter(
                getSupportFragmentManager()));

        aqtViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                switch (previousPage) {
                    case 0:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            testButton=(ImageButton) findViewById(R.id.btnStage1Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                    case 1:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            testButton=(ImageButton) findViewById(R.id.btnStage2Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                    case 2:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            testButton=(ImageButton) findViewById(R.id.btnStage3Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                    case 3:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            testButton=(ImageButton) findViewById(R.id.btnStage4Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                }
                previousPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    private class AQTPagerAdapter extends FragmentPagerAdapter {

        private AQTPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
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

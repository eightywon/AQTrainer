package com.eightywon.aqtrainer;

import android.content.Intent;
import android.media.MediaPlayer;
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
    final static int STEP_STAGE_DESCRIPTION=0;
    final static int STEP_PREP_START=1;
    final static int STEP_PREP_IN_PROGRESS=2;
    final static int STEP_PREP_END=3;
    final static int STEP_LOAD=4;
    final static int STEP_FIRE_START=5;
    final static int STEP_FIRE_IN_PROGRESS=6;
    final static int STEP_FIRE_END=7;
    final static int STEP_DONE=99;

    public static MediaPlayer mpStageDescription;
    public static MediaPlayer mpPrepStart;
    public static MediaPlayer mpPrepInProgress;
    public static MediaPlayer mpPrepEnd;
    public static MediaPlayer mpLoad;
    public static MediaPlayer mpFireStart;
    public static MediaPlayer mpFireInProgress;
    public static MediaPlayer mpFireEnd;
    public static MediaPlayer mpS5;
    public static MediaPlayer mpS3;
    public static MediaPlayer mpStepBreak;

    public static int nextStep;

    ViewPager aqtViewPager;

    public int previousPage;
    public ImageButton testButton;

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
                        if (StageOneFragment.isPlaying) {
                            stopPlaying(nextStep);
                            StageOneFragment.isPlaying=false;
                            testButton=(ImageButton) findViewById(R.id.btnStage1Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                    case 1:
                        if (StageTwoFragment.mpStage2!=null) {
                            if (StageTwoFragment.mpStage2.isPlaying()) {
                                StageTwoFragment.mpStage2.pause();
                            }
                            StageTwoFragment.mpStage2.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage2Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                    case 2:
                        if (StageThreeFragment.mpStage3!=null) {
                            if (StageThreeFragment.mpStage3.isPlaying()) {
                                StageThreeFragment.mpStage3.pause();
                            }
                            StageThreeFragment.mpStage3.seekTo(0);
                            testButton=(ImageButton) findViewById(R.id.btnStage3Play);
                            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getPackageName()));
                        }
                        break;
                    case 3:
                        if (StageFourFragment.mpStage4!=null) {
                            if (StageFourFragment.mpStage4.isPlaying()) {
                                StageFourFragment.mpStage4.pause();
                            }
                            StageFourFragment.mpStage4.seekTo(0);
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

    public static void stopPlaying(int step) {
        if (mpStepBreak.isPlaying()) {
            MainActivity.mpStepBreak.pause();
            MainActivity.mpStepBreak.seekTo(0);
        } else {
            switch (step) {
                case STEP_STAGE_DESCRIPTION:
                    mpStageDescription.pause();
                    mpStageDescription.seekTo(0);
                    break;
                case STEP_PREP_START:
                    mpPrepStart.pause();
                    mpPrepStart.seekTo(0);
                    break;
                case STEP_PREP_IN_PROGRESS:
                    mpPrepInProgress.pause();
                    mpPrepInProgress.seekTo(0);
                    break;
                case STEP_PREP_END:
                    mpPrepEnd.pause();
                    mpPrepEnd.seekTo(0);
                    break;
                case STEP_LOAD:
                    mpLoad.pause();
                    mpLoad.seekTo(0);
                    break;
                case STEP_FIRE_START:
                    mpFireStart.pause();
                    mpFireStart.seekTo(0);
                    break;
                case STEP_FIRE_IN_PROGRESS:
                    mpFireInProgress.pause();
                    mpFireInProgress.seekTo(0);
                    break;
                case STEP_FIRE_END:
                    mpFireEnd.pause();
                    mpFireEnd.seekTo(0);
                    break;
            }
        }
    }
}

package com.eightywon.aqtrainer;

import android.app.ActivityManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    final static int STEP_BEGIN=0;
    final static int STEP_STAGE_DESCRIPTION=1;
    final static int STEP_PREP_START=2;
    final static int STEP_PREP_IN_PROGRESS=3;
    final static int STEP_PREP_END=4;
    final static int STEP_SAFTIES_ON_STAND=5;
    final static int STEP_PAUSE_1=6;
    final static int STEP_LOAD=7;
    final static int STEP_PAUSE_2=8;
    final static int STEP_FIRE_START=9;
    final static int STEP_FIRE_IN_PROGRESS=10;
    final static int STEP_FIRE_END=11;
    final static int STEP_DONE=12;

    ViewPager aqtViewPager;
    public int previousPage;
    Button stageButton;

    public static TextToSpeech textToSpeech;
    static int[] sources=new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        setTaskDescription(new ActivityManager.TaskDescription("Appleseed Qualification Trainer",BitmapFactory.decodeResource(getResources(), R.drawable.icon_round_96),ContextCompat.getColor(this, R.color.colorNavBar)));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aqtViewPager=(ViewPager) findViewById(R.id.container);
        aqtViewPager.setAdapter(new AQTPagerAdapter(
                getSupportFragmentManager()));

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        aqtViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                View view=findViewById(previousPage+1);
                stageButton=(Button) view.findViewById(R.id.btnStagePlay);
                switch (previousPage) {
                    case 0:
                        if (isPlaying) {
                            //stageButton=(Button) StageOneFragment.fragView.findViewById(R.id.btnStagePlay);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                            MediaPlayerSingleton.getInstance().stopPlaying(getContext());
                        }
                        break;
                    case 1:
                        if (isPlaying) {
                            //stageButton=(Button) StageTwoFragment.fragView.findViewById(R.id.btnStagePlay);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                            MediaPlayerSingleton.getInstance().stopPlaying(getContext());
                        }
                        break;
                    case 2:
                        if (isPlaying) {
                            //stageButton=(Button) StageThreeFragment.fragView.findViewById(R.id.btnStagePlay);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                            MediaPlayerSingleton.getInstance().stopPlaying(getContext());
                        }
                        break;
                    case 3:
                        if (isPlaying) {
                            //stageButton=(Button) StageFourFragment.fragView.findViewById(R.id.btnStagePlay);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                            MediaPlayerSingleton.getInstance().stopPlaying(getContext());
                        }
                        break;
                }
                previousPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        textToSpeech = new TextToSpeech(MainActivity.this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        aqtViewPager.requestLayout();
    }

    public Context getContext() {
        //return instance.getApplicationContext();
        return getApplication().getApplicationContext();
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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
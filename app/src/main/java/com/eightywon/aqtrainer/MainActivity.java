package com.eightywon.aqtrainer;

import android.app.ActivityManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    static int lastSec=0;
    public static boolean firstTime=false;
    public static Handler hCountDownPrepStage=new Handler();
    public static Handler hCountDownFireStage=new Handler();
    public static int countDownFireStageInterval;

    public static Handler hCountDownDescStage=new Handler();

    static int[] sources=new int[100];
    private static MainActivity instance;

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
        instance=this;

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
                switch (previousPage) {
                    case 0:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            stageButton=(Button) findViewById(R.id.btnStage1Play);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                        }
                        break;
                    case 1:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            stageButton=(Button) findViewById(R.id.btnStage2Play);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                        }
                        break;
                    case 2:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            stageButton=(Button) findViewById(R.id.btnStage3Play);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
                        }
                        break;
                    case 3:
                        if (isPlaying) {
                            MediaPlayerSingleton.stopPlaying(getContext());
                            stageButton=(Button) findViewById(R.id.btnStage4Play);
                            stageButton.setText(R.string.btnStartStage);
                            stageButton.setBackgroundResource(R.drawable.button);
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

    public static boolean getPlayStageDescription() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        return prefs.getBoolean("chkpPlayStageDesc",false);
    }

    public static boolean getPlayPrepAnnouncements() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        return prefs.getBoolean("chkpPlayPrep",false);
    }

    public static int getPrepTime() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String prepTime=prefs.getString("lpSettingsPrepTime","");
        return Integer.parseInt(prepTime);
    }

    public static boolean getAnnounceStageTime() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        return prefs.getBoolean("chkpStageTimerAnnounce",false);
    }

    public static boolean getRedAlertMode() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        return prefs.getBoolean("chkpRedAlertMode",false);
    }

    public static Runnable countDownPrepStage = new Runnable() {
        @Override
        public void run() {
            TextView txtStageDescTimer = null;
            switch (MediaPlayerSingleton.getStage()) {
                case 1:
                    txtStageDescTimer=StageOneFragment.txtStageDescTimer;
                    break;
                case 2:
                    txtStageDescTimer=StageTwoFragment.txtStageDescTimer;
                    break;
                case 3:
                    txtStageDescTimer=StageThreeFragment.txtStageDescTimer;
                    break;
                case 4:
                    txtStageDescTimer=StageFourFragment.txtStageDescTimer;
                    break;
            }
            MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
            int remaining=mediaPlayer.getRemaining();

            int secs=0;
            int mins=0;
            if (remaining>=0) {
                if (remaining>=60) {
                    mins=remaining/60;
                    secs=remaining%60;
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:%d",mins,secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:0%d",mins,secs));
                            }
                        }
                    }
                } else {
                    secs=remaining%60;
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:%d",secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:0%d",secs));
                            }
                        }
                    }
                }
            }
            lastSec=secs;
            firstTime=false;
            hCountDownPrepStage.postDelayed(countDownPrepStage,200);
        }
    };

    public static Runnable countDownFireStage = new Runnable() {
        @Override
        public void run() {
            if (MainActivity.getAnnounceStageTime()) {
                String announceInterval;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
                announceInterval = prefs.getString("lpSettingsAnnounceInterval", "");
                MainActivity.countDownFireStageInterval = Integer.parseInt(announceInterval);
            }

            TextView txtStageDescTimer = null;
            switch (MediaPlayerSingleton.getStage()) {
                case 1:
                    txtStageDescTimer=StageOneFragment.txtStageDescTimer;
                    break;
                case 2:
                    txtStageDescTimer=StageTwoFragment.txtStageDescTimer;
                    break;
                case 3:
                    txtStageDescTimer=StageThreeFragment.txtStageDescTimer;
                    break;
                case 4:
                    txtStageDescTimer=StageFourFragment.txtStageDescTimer;
                    break;
            }

            MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
            int remaining=mediaPlayer.getRemaining();

            boolean redAlertMode=MainActivity.getRedAlertMode();
            int secs=0;
            int mins=0;
            String howLong;
            if (remaining>=0) {
                if (remaining>=60) {
                    mins=remaining/60;
                    secs=remaining%60;
                    howLong=String.valueOf(mins)+" minutes ";
                    if (secs>0) howLong+=String.valueOf(secs)+" seconds.";
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:%d",mins,secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"%d:0%d",mins,secs));
                            }
                        }
                    }
                } else {
                    secs=remaining%60;
                    if (remaining<=10 && redAlertMode) {
                        howLong=String.valueOf(secs);
                    } else {
                        howLong=String.valueOf(secs)+" seconds.";
                    }
                    if (secs!=lastSec || firstTime) {
                        if (txtStageDescTimer!=null) {
                            if (secs>=10) {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:%d",secs));
                            } else {
                                txtStageDescTimer.setText(String.format(Locale.US,"0:0%d",secs));
                            }
                        }
                    }
                }
                if ((remaining<=10 && redAlertMode) || (MainActivity.getAnnounceStageTime() && remaining%countDownFireStageInterval==0) || firstTime) {
                    if ((secs!=lastSec || firstTime) && remaining>0 && (MainActivity.getAnnounceStageTime() || (redAlertMode && remaining<=10))) {
                        textToSpeech.speak(howLong, TextToSpeech.QUEUE_FLUSH, null, "");
                    }
                }
            }
            lastSec=secs;
            firstTime=false;
            hCountDownFireStage.postDelayed(countDownFireStage, 200);
        }
    };

    public static Runnable countDownDescStage = new Runnable() {
        @Override
        public void run() {
            ImageView imageView1=StageFourFragment.target1;
            ImageView imageView2=StageFourFragment.target2;
            ImageView imageView3=StageFourFragment.target3;
            ImageView imageView4=StageFourFragment.target4;
            ImageView imageView5=StageFourFragment.target1Highlight;
            ImageView imageView6=StageFourFragment.target2Highlight;
            ImageView imageView7=StageFourFragment.target3Highlight;
            ImageView imageView8=StageFourFragment.target4Highlight;

            switch (MediaPlayerSingleton.getStage()) {
                case 1:
                    imageView1=StageOneFragment.target1;
                    imageView2=StageOneFragment.target1Highlight;
                    break;
                case 2:
                    imageView1=StageTwoFragment.target1;
                    imageView2=StageTwoFragment.target2;
                    imageView3=StageTwoFragment.target1Highlight;
                    imageView4=StageTwoFragment.target2Highlight;
                    break;
                case 3:
                    imageView1=StageThreeFragment.target1;
                    imageView2=StageThreeFragment.target2;
                    imageView3=StageThreeFragment.target3;

                    imageView4=StageThreeFragment.target1Highlight;
                    imageView5=StageThreeFragment.target2Highlight;
                    imageView6=StageThreeFragment.target3Highlight;
                    break;
                case 4:
                    imageView1=StageFourFragment.target1;
                    imageView2=StageFourFragment.target2;
                    imageView3=StageFourFragment.target3;
                    imageView4=StageFourFragment.target4;

                    imageView5=StageFourFragment.target1Highlight;
                    imageView6=StageFourFragment.target2Highlight;
                    imageView7=StageFourFragment.target3Highlight;
                    imageView8=StageFourFragment.target4Highlight;
                    break;
            }

            MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
            int remaining=mediaPlayer.getRemaining();

            switch (MediaPlayerSingleton.getStage()) {
                case 1:
                    if (remaining==10) {
                        StageOneFragment.shot1.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==9) {
                        StageOneFragment.shot2.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==8) {
                        StageOneFragment.shot3.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==7) {
                        StageOneFragment.shot4.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==6) {
                        StageOneFragment.shot5.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==5) {
                        imageView2.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                        imageView1.setPadding(1, 1, 1, 1);
                        StageOneFragment.shot6.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==4) {
                        StageOneFragment.shot7.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==3) {
                        StageOneFragment.shot8.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==2) {
                        imageView1.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                        imageView1.setPadding(0, 0, 0, 0);
                        StageOneFragment.shot9.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==1) {
                        StageOneFragment.shot10.setVisibility(View.VISIBLE);
                        break;
                    }
                    break;
                case 2:
                    if (remaining==14) {
                        imageView3.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                    } else if (remaining==13) {
                        StageTwoFragment.shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==12) {
                        StageTwoFragment.shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==11) {
                        StageTwoFragment.shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        StageTwoFragment.shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        StageTwoFragment.shot5.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        imageView1.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.INVISIBLE);
                        imageView4.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                    } else if (remaining==7) {
                        StageTwoFragment.shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==6) {
                        imageView4.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                        StageTwoFragment.shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        StageTwoFragment.shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        StageTwoFragment.shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        StageTwoFragment.shot10.setVisibility(View.VISIBLE);
                    } else if (remaining==2) {
                        imageView2.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 3:
                    if (remaining==18) {
                        imageView4.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                    } else if (remaining==12) {
                        imageView1.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.INVISIBLE);
                    } else if (remaining==17) {
                        StageThreeFragment.shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==16) {
                        StageThreeFragment.shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==13) {
                        StageThreeFragment.shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        imageView5.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                        StageThreeFragment.shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        StageThreeFragment.shot5.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        StageThreeFragment.shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==7) {
                        imageView2.setVisibility(View.VISIBLE);
                        imageView5.setVisibility(View.INVISIBLE);
                    } else if (remaining==6) {
                        imageView6.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.INVISIBLE);
                        StageThreeFragment.shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        StageThreeFragment.shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        StageThreeFragment.shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        StageThreeFragment.shot10.setVisibility(View.VISIBLE);
                    } else if (remaining==2) {
                        imageView3.setVisibility(View.VISIBLE);
                        imageView6.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 4:
                    if (remaining==13) {
                        imageView5.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.INVISIBLE);
                        imageView6.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                    } else if (remaining==12) {
                        StageFourFragment.shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==11) {
                        StageFourFragment.shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        StageFourFragment.shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        StageFourFragment.shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        StageFourFragment.shot5.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.VISIBLE);
                        imageView5.setVisibility(View.INVISIBLE);
                        imageView2.setVisibility(View.VISIBLE);
                        imageView6.setVisibility(View.INVISIBLE);
                        imageView7.setVisibility(View.VISIBLE);
                        imageView3.setVisibility(View.INVISIBLE);
                        imageView8.setVisibility(View.VISIBLE);
                        imageView4.setVisibility(View.INVISIBLE);
                    } else if (remaining==7) {
                        StageFourFragment.shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==6) {
                        StageFourFragment.shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        StageFourFragment.shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        StageFourFragment.shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        StageFourFragment.shot10.setVisibility(View.VISIBLE);
                    } else if (remaining==2) {
                        imageView3.setVisibility(View.VISIBLE);
                        imageView7.setVisibility(View.INVISIBLE);
                        imageView4.setVisibility(View.VISIBLE);
                        imageView8.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
            hCountDownDescStage.postDelayed(countDownDescStage,200);
        }
    };

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
package com.eightywon.aqtrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    public ImageButton testButton;

    public static TextToSpeech textToSpeech;
    //textToSpeech = new TextToSpeech(getActivity(),this);

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
        textToSpeech = new TextToSpeech(MainActivity.this,this);
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
        prepTime=prepTime.substring(0,prepTime.indexOf(" "));
        return Integer.parseInt(prepTime);
    }

    public static boolean getAnnounceStageTime() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        return prefs.getBoolean("chkpStageTimerAnnounce",true);
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
                    if (secs!=lastSec) {
                        if (txtStageDescTimer!=null) {
                            txtStageDescTimer.setText(String.format(Locale.US,"%dm %ds",mins,secs));
                        }
                    }
                } else {
                    secs=remaining%60;
                    if (secs!=lastSec) {
                        if (txtStageDescTimer!=null) {
                            txtStageDescTimer.setText(String.format(Locale.US,"%ds",secs));
                        }
                    }
                }
            }
            lastSec=secs;
            hCountDownPrepStage.postDelayed(countDownPrepStage,200);
        }
    };

    public static Runnable countDownFireStage = new Runnable() {
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
                            txtStageDescTimer.setText(String.format(Locale.US,"%dm %ds",mins,secs));
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
                            txtStageDescTimer.setText(String.format(Locale.US,"%ds",secs));
                        }
                    }
                }
                if ((remaining<=10 && redAlertMode) || (remaining%countDownFireStageInterval==0) || firstTime) {
                    if ((secs!=lastSec || firstTime) && remaining>0) {
                        textToSpeech.speak(howLong, TextToSpeech.QUEUE_FLUSH, null, "");
                    }
                }
            }
            lastSec=secs;
            firstTime=false;
            hCountDownFireStage.postDelayed(countDownFireStage,200);
        }
    };

    public static Runnable countDownDescStage = new Runnable() {
        @Override
        public void run() {

            ImageView imageView1=StageFourFragment.imageView1;
            ImageView imageView2=StageFourFragment.imageView2;
            ImageView imageView3=StageFourFragment.imageView3;
            ImageView imageView4=StageFourFragment.imageView4;

            switch (MediaPlayerSingleton.getStage()) {
                case 1:
                    imageView1=StageOneFragment.imageView;
                    break;
                case 2:
                    imageView1=StageTwoFragment.imageView1;
                    imageView2=StageTwoFragment.imageView2;
                    break;
                case 3:
                    imageView1=StageThreeFragment.imageView1;
                    imageView2=StageThreeFragment.imageView2;
                    imageView3=StageThreeFragment.imageView3;
                    break;
                case 4:
                    imageView1=StageFourFragment.imageView1;
                    imageView2=StageFourFragment.imageView2;
                    imageView3=StageFourFragment.imageView3;
                    imageView4=StageFourFragment.imageView4;
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
                        imageView1.setBackgroundResource(R.color.colorRed);
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
                        imageView1.setBackgroundResource(0);
                        imageView1.setPadding(0, 0, 0, 0);
                        StageOneFragment.shot9.setVisibility(View.VISIBLE);
                        break;
                    } else if (remaining==1) {
                        StageOneFragment.shot10.setVisibility(View.VISIBLE);
                        break;
                    }
                    break;
                case 2:
                    if (remaining==13) {
                        imageView1.setBackgroundResource(R.color.colorRed);
                        imageView1.setPadding(1, 1, 1, 1);
                        StageTwoFragment.leftShot1.setVisibility(View.VISIBLE);
                    } else if (remaining==12) {
                        StageTwoFragment.leftShot2.setVisibility(View.VISIBLE);
                    } else if (remaining==11) {
                        StageTwoFragment.leftShot3.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        StageTwoFragment.leftShot4.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        StageTwoFragment.leftShot5.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        imageView1.setBackgroundResource(0);
                        imageView1.setPadding(0,0,0,0);
                    } else if (remaining==7) {
                        imageView2.setBackgroundResource(R.color.colorRed);
                        imageView2.setPadding(1,1,1,1);
                        StageTwoFragment.rightShot1.setVisibility(View.VISIBLE);
                    } else if (remaining==6) {
                        StageTwoFragment.rightShot2.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        StageTwoFragment.rightShot3.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        StageTwoFragment.rightShot4.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        StageTwoFragment.rightShot5.setVisibility(View.VISIBLE);
                        imageView2.setBackgroundResource(0);
                        imageView2.setPadding(0,0,0,0);
                    }
                    break;
                case 3:
                    if (remaining==18) {
                        imageView1.setBackgroundResource(R.color.colorRed);
                        imageView1.setPadding(1, 1, 1, 1);
                    } else if (remaining==12) {
                        imageView1.setBackgroundResource(0);
                        imageView1.setPadding(0, 0, 0, 0);
                    } else if (remaining==17) {
                        StageThreeFragment.shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==16) {
                        StageThreeFragment.shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==13) {
                        StageThreeFragment.shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        imageView2.setBackgroundResource(R.color.colorRed);
                        imageView2.setPadding(1, 1, 1, 1);
                        StageThreeFragment.shot4.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        StageThreeFragment.shot5.setVisibility(View.VISIBLE);
                    } else if (remaining==8) {
                        StageThreeFragment.shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==7) {
                        imageView2.setBackgroundResource(0);
                        imageView2.setPadding(0, 0, 0, 0);
                    } else if (remaining==6) {
                        imageView3.setBackgroundResource(R.color.colorRed);
                        imageView3.setPadding(1, 1, 1, 1);
                        StageThreeFragment.shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        StageThreeFragment.shot8.setVisibility(View.VISIBLE);
                    } else if (remaining==4) {
                        StageThreeFragment.shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        imageView3.setBackgroundResource(0);
                        imageView3.setPadding(0, 0, 0, 0);
                        StageThreeFragment.shot10.setVisibility(View.VISIBLE);
                    }
                    break;
                case 4:
                    if (remaining==12) {
                        imageView1.setBackgroundResource(R.color.colorRed);
                        imageView1.setPadding(1, 1, 1, 1);
                        imageView2.setBackgroundResource(R.color.colorRed);
                        imageView2.setPadding(1, 1, 1, 1);
                        StageFourFragment.shot1.setVisibility(View.VISIBLE);
                    } else if (remaining==11) {
                        StageFourFragment.shot2.setVisibility(View.VISIBLE);
                    } else if (remaining==10) {
                        StageFourFragment.shot3.setVisibility(View.VISIBLE);
                    } else if (remaining==9) {
                        StageFourFragment.shot4.setVisibility(View.VISIBLE);
                        imageView1.setBackgroundResource(0);
                        imageView1.setPadding(0, 0, 0, 0);
                        imageView2.setBackgroundResource(0);
                        imageView2.setPadding(0, 0, 0, 0);
                    } else if (remaining==8) {
                        imageView3.setBackgroundResource(R.color.colorRed);
                        imageView3.setPadding(1, 1, 1, 1);
                        imageView4.setBackgroundResource(R.color.colorRed);
                        imageView4.setPadding(1, 1, 1, 1);
                        StageFourFragment.shot5.setVisibility(View.VISIBLE);
                    } else if (remaining==7) {
                        StageFourFragment.shot6.setVisibility(View.VISIBLE);
                    } else if (remaining==6) {
                        StageFourFragment.shot7.setVisibility(View.VISIBLE);
                    } else if (remaining==5) {
                        StageFourFragment.shot8.setVisibility(View.VISIBLE);
                        imageView3.setBackgroundResource(0);
                        imageView3.setPadding(0, 0, 0, 0);
                        imageView4.setBackgroundResource(0);
                        imageView4.setPadding(0, 0, 0, 0);
                    } else if (remaining==4) {
                        StageFourFragment.shot9.setVisibility(View.VISIBLE);
                    } else if (remaining==3) {
                        StageFourFragment.shot10.setVisibility(View.VISIBLE);
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
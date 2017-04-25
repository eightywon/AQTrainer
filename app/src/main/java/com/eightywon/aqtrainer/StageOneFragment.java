package com.eightywon.aqtrainer;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import java.io.IOException;
import java.util.Locale;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class StageOneFragment extends Fragment implements TextToSpeech.OnInitListener {

    /**
    final static int STEP_STAGE_DESCRIPTION=0;
    final static int STEP_PREP_START=1;
    final static int STEP_PREP_IN_PROGRESS=2;
    final static int STEP_PREP_END=3;
    final static int STEP_LOAD=4;
    final static int STEP_FIRE_START=5;
    final static int STEP_FIRE_IN_PROGRESS=6;
    final static int STEP_FIRE_END=7;
    final static int STEP_DONE=99;
    **/

    static String STEP_BREAK_MP3="S2.ogg";
    static String FIRE_MP3="S30.ogg";

    //int nextStep;
    boolean playStageDesc;
    boolean playPrep;

    /**
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
    **/

    TextToSpeech textToSpeech;

    public ImageButton testButton;

    public static boolean isPlaying;

    int countDownFireStageInterval;
    Switch swPlayStageDesc;
    Switch swPlayPrep;

    Handler hCountDownFireStage;

    TextView txtStageDescTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);


        MainActivity.mpStageDescription=new MediaPlayer();
        MainActivity.mpPrepStart=new MediaPlayer();
        MainActivity.mpPrepInProgress=new MediaPlayer();
        MainActivity.mpPrepEnd=new MediaPlayer();
        MainActivity.mpLoad=new MediaPlayer();
        MainActivity.mpFireStart=new MediaPlayer();
        MainActivity.mpFireInProgress=new MediaPlayer();
        MainActivity.mpFireEnd=new MediaPlayer();

        MainActivity.mpStepBreak=new MediaPlayer();
        MainActivity.mpS5=new MediaPlayer();
        MainActivity.mpS3=new MediaPlayer();

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage1Play);
        swPlayStageDesc=(Switch) rootView.findViewById(R.id.swStageDesc);
        swPlayPrep=(Switch) rootView.findViewById(R.id.swPrepPeriod);

        isPlaying=false;
        playStageDesc=swPlayStageDesc.isChecked();
        playPrep=swPlayPrep.isChecked();

        textToSpeech = new TextToSpeech(getActivity(),this);

        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    isPlaying=true;
                    if (playStageDesc) {
                        MainActivity.nextStep=MainActivity.STEP_STAGE_DESCRIPTION;
                    } else {
                        if (playPrep) {
                            MainActivity.nextStep=MainActivity.STEP_PREP_START;
                        } else {
                            MainActivity.nextStep=MainActivity.STEP_LOAD;
                        }
                    }
                    playNext(MainActivity.nextStep);
                } else {
                    MainActivity.stopPlaying(MainActivity.nextStep);
                    isPlaying=false;
                    hCountDownFireStage.removeCallbacks(countDownFireStage);
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                }
            }
        });

        swPlayStageDesc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                playStageDesc=isChecked;
            }
        });

        swPlayPrep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                playPrep=isChecked;
            }
        });

        try {
            AssetFileDescriptor afd1=getActivity().getAssets().openFd("DescStage1.mp3");
            AssetFileDescriptor afd2=getActivity().getAssets().openFd("PrepBegin.mp3");
            AssetFileDescriptor afd3=getActivity().getAssets().openFd("PrepEnd.mp3");
            AssetFileDescriptor afd4=getActivity().getAssets().openFd("Load10.mp3");
            AssetFileDescriptor afd5=getActivity().getAssets().openFd("Fire.mp3");
            AssetFileDescriptor afd10=getActivity().getAssets().openFd("Cease.mp3");
            AssetFileDescriptor afd7=getActivity().getAssets().openFd("S3.ogg");
            AssetFileDescriptor afd8=getActivity().getAssets().openFd("S10.ogg");

            AssetFileDescriptor afdStepBreakMP3=getActivity().getAssets().openFd(STEP_BREAK_MP3);
            MainActivity.mpStepBreak.setDataSource(afdStepBreakMP3.getFileDescriptor(),afdStepBreakMP3.getStartOffset(),afdStepBreakMP3.getLength());
            MainActivity.mpStepBreak.prepare();

            MainActivity.mpPrepInProgress.setDataSource(afd8.getFileDescriptor(),afd8.getStartOffset(),afd8.getLength());
            MainActivity.mpPrepInProgress.prepare();

            MainActivity.mpStageDescription.setDataSource(afd1.getFileDescriptor(),afd1.getStartOffset(),afd1.getLength());
            MainActivity.mpStageDescription.prepare();

            MainActivity.mpS3.setDataSource(afd7.getFileDescriptor(),afd7.getStartOffset(),afd7.getLength());
            MainActivity.mpS3.prepare();

            MainActivity.mpPrepStart.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            MainActivity.mpPrepStart.prepare();

            MainActivity.mpPrepEnd.setDataSource(afd3.getFileDescriptor(),afd3.getStartOffset(),afd3.getLength());
            MainActivity.mpPrepEnd.prepare();

            MainActivity.mpLoad.setDataSource(afd4.getFileDescriptor(),afd4.getStartOffset(),afd4.getLength());
            MainActivity.mpLoad.prepare();

            MainActivity.mpFireStart.setDataSource(afd5.getFileDescriptor(),afd5.getStartOffset(),afd5.getLength());
            MainActivity.mpFireStart.prepare();

            AssetFileDescriptor afdFireInProgress=getActivity().getAssets().openFd(FIRE_MP3);
            MainActivity.mpFireInProgress.setDataSource(afdFireInProgress.getFileDescriptor(),afdFireInProgress.getStartOffset(),afdFireInProgress.getLength());
            MainActivity.mpFireInProgress.prepare();

            MainActivity.mpFireEnd.setDataSource(afd10.getFileDescriptor(),afd10.getStartOffset(),afd10.getLength());
            MainActivity.mpFireEnd.prepare();

        } catch (IOException e) {
            Toast.makeText(getActivity(), "Oops", Toast.LENGTH_SHORT).show();
        }

        txtStageDescTimer.setText(String.valueOf(MainActivity.mpStageDescription.getDuration()/1000));

        MainActivity.mpStageDescription.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpStageDescription.pause();
                MainActivity.mpStageDescription.seekTo(0);
                if (playPrep) {
                    MainActivity.nextStep=MainActivity.STEP_PREP_START;
                } else {
                    MainActivity.nextStep=MainActivity.STEP_LOAD;
                }
                MainActivity.mpStepBreak.seekTo(0);
                MainActivity.mpStepBreak.start();
            }
        });

        MainActivity.mpPrepStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpPrepStart.pause();
                MainActivity.mpPrepStart.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_PREP_IN_PROGRESS;
                playNext(MainActivity.nextStep);
            }
        });

        MainActivity.mpPrepInProgress.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpPrepInProgress.pause();
                MainActivity.mpPrepInProgress.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_PREP_END;
                playNext(MainActivity.nextStep);
            }
        });

        MainActivity.mpPrepEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpPrepEnd.pause();
                MainActivity.mpPrepEnd.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_LOAD;
                MainActivity.mpStepBreak.seekTo(0);
                MainActivity.mpStepBreak.start();
            }
        });

        MainActivity.mpLoad.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpLoad.pause();
                MainActivity.mpLoad.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_FIRE_START;
                MainActivity.mpStepBreak.seekTo(0);
                MainActivity.mpStepBreak.start();
            }
        });

        MainActivity.mpFireStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpFireStart.pause();
                MainActivity.mpFireStart.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_FIRE_IN_PROGRESS;
                playNext(MainActivity.nextStep);
            }
        });

        MainActivity.mpFireInProgress.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                hCountDownFireStage.removeCallbacks(countDownFireStage);
                MainActivity.mpFireInProgress.pause();
                MainActivity.mpFireInProgress.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_FIRE_END;
                playNext(MainActivity.nextStep);
            }
        });

        MainActivity.mpFireEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpFireEnd.pause();
                MainActivity.mpFireEnd.seekTo(0);
                MainActivity.nextStep=MainActivity.STEP_DONE;
                playNext(MainActivity.nextStep);
            }
        });

        MainActivity.mpStepBreak.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                MainActivity.mpStepBreak.pause();
                MainActivity.mpStepBreak.seekTo(0);
                playNext(MainActivity.nextStep);
            }
        });

        hCountDownFireStage=new Handler();

        return rootView;
    }

    public void playNext (int nextStep) {
        String announceInterval;
        switch (nextStep) {
            case MainActivity.STEP_STAGE_DESCRIPTION:
                MainActivity.mpStageDescription.seekTo(0);
                MainActivity.mpStageDescription.start();
                break;
            case MainActivity.STEP_PREP_START:
                MainActivity.mpPrepStart.seekTo(0);
                MainActivity.mpPrepStart.start();
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                MainActivity.mpPrepInProgress.seekTo(0);
                MainActivity.mpPrepInProgress.start();
                break;
            case MainActivity.STEP_PREP_END:
                MainActivity.mpPrepEnd.seekTo(0);
                MainActivity.mpPrepEnd.start();
                break;
            case MainActivity.STEP_LOAD:
                MainActivity.mpLoad.seekTo(0);
                MainActivity.mpLoad.start();
                break;
            case MainActivity.STEP_FIRE_START:
                MainActivity.mpFireStart.seekTo(0);
                MainActivity.mpFireStart.start();
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                MainActivity.mpFireInProgress.seekTo(0);
                MainActivity.mpFireInProgress.start();
                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                countDownFireStageInterval=Integer.parseInt(announceInterval);
                hCountDownFireStage.post(countDownFireStage);
                break;
            case MainActivity.STEP_FIRE_END:
                MainActivity.mpFireEnd.seekTo(0);
                MainActivity.mpFireEnd.start();
                break;
            case MainActivity.STEP_DONE:
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                isPlaying=false;
        }
        if (isPlaying) {
            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
        }
    }

    private Runnable countDownFireStage = new Runnable() {
        @Override
        public void run() {
            int remaining=Math.round((MainActivity.mpFireInProgress.getDuration()-MainActivity.mpFireInProgress.getCurrentPosition())/1000);
            if (remaining>1) {
                textToSpeech.speak(String.valueOf(remaining),TextToSpeech.QUEUE_FLUSH,null,"");
            }
            hCountDownFireStage.postDelayed(countDownFireStage,countDownFireStageInterval*1000);
        }
    };

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
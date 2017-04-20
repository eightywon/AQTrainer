package com.eightywon.aqtrainer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StageOneFragment extends Fragment {
    public TextView tv;
    //public SeekBar sb;
    public static MediaPlayer mpStage1;
    public static MediaPlayer mpPrepStart;
    public static MediaPlayer mpPrepEnd;
    public ImageButton testButton;
    final int MINUTE = 60*1000;
    final int SECOND = 1000;
    int durationInMilli;
    int curInMilli;
    int durationMin;
    int durationSec;
    int currentMin;
    int currentSec;
    //Handler seekHandler;
    Handler prepDelay;
    public TextView txtCountDown;

    public Switch swStageDesc;
    public Switch swPrepPeriod;
    public final static int PREPDELAY=10*1000;
    //int delayCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        swStageDesc=(Switch) rootView.findViewById(R.id.swStageDesc);
        swStageDesc.setChecked(true);
        swPrepPeriod=(Switch) rootView.findViewById(R.id.swPrepPeriod);
        swPrepPeriod.setChecked(true);

        txtCountDown=(TextView) rootView.findViewById(R.id.txtCountDown);

        //sb=(SeekBar) rootView.findViewById(R.id.seekBarStage1);
        tv=(TextView) rootView.findViewById(R.id.txtSeekStatus);
        mpStage1=new MediaPlayer();
        mpPrepStart=new MediaPlayer();
        mpPrepEnd=new MediaPlayer();

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage1Play);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpStage1.isPlaying()) {
                    mpStage1.pause();
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                } else {
                    mpStage1.start();
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getActivity().getPackageName()));
                }
            }
        });
        try {
            AssetFileDescriptor afd=getActivity().getAssets().openFd("DescStage1.mp3");
            AssetFileDescriptor afd2=getActivity().getAssets().openFd("PrepBegin.mp3");
            AssetFileDescriptor afd3=getActivity().getAssets().openFd("PrepEnd.mp3");
            mpStage1.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mpPrepStart.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            mpPrepEnd.setDataSource(afd3.getFileDescriptor(),afd3.getStartOffset(),afd3.getLength());
            mpStage1.prepare();
            mpPrepStart.prepare();
            mpPrepEnd.prepare();
            mpStage1.setNextMediaPlayer(mpPrepStart);
            mpPrepStart.setNextMediaPlayer(mpPrepEnd);
        } catch (IOException e) {
        }
        //sb.setMax(mpStage1.getDuration());
        durationInMilli=mpStage1.getDuration();
        durationMin=(durationInMilli/MINUTE);
        durationSec=(durationInMilli%MINUTE)/SECOND;
        curInMilli=mpStage1.getCurrentPosition();
        currentMin=(curInMilli/MINUTE);
        currentSec=(curInMilli%MINUTE)/SECOND;

        txtCountDown.setText(durationInMilli/SECOND);

        tv.setText(String.format("%01d:%02d",currentMin,currentSec)+" of "+String.format("%01d:%02d",durationMin,durationSec));
        mpStage1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                    //testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                    mpStage1.seekTo(0);
                    //sb.setMax(mpPrepStart.getDuration());
            }
        });
        mpPrepStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                mpPrepStart.seekTo(0);
                //sb.setMax(mpPrepEnd.getDuration());
                mpPrepEnd.pause();
                prepDelay.postDelayed(pauseForPrep, PREPDELAY);
            }
        });
        mpPrepEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                p.seekTo(0);
                //sb.setProgress(0);
                tv.setText("00:00 of "+String.format("%01d:%02d",durationMin,durationSec));
            }
        });

        /**
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mpStage1.isPlaying()) {
                    durationInMilli = mpStage1.getDuration();
                    curInMilli=mpStage1.getCurrentPosition();
                } else if (mpPrepStart.isPlaying()) {
                    durationInMilli = mpPrepStart.getDuration();
                    curInMilli=mpPrepStart.getCurrentPosition();
                } else if (mpPrepEnd.isPlaying()) {
                    durationInMilli = mpPrepEnd.getDuration();
                    curInMilli=mpPrepEnd.getCurrentPosition();
                }
                durationMin=(durationInMilli/MINUTE);
                durationSec=(durationInMilli%MINUTE)/SECOND;
                currentMin=(curInMilli/MINUTE);
                currentSec=(curInMilli%MINUTE)/SECOND;
                tv.setText(String.format("%01d:%02d",currentMin,currentSec)+" of "+String.format("%01d:%02d",durationMin,durationSec));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        **/

        //seekHandler=new Handler();
        //seekHandler.post(updateSeekBarTime);

        prepDelay=new Handler();

        return rootView;
    }

    /**
    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            if (mpStage1.isPlaying()) {
                sb.setMax(mpStage1.getDuration());
                sb.setProgress(mpStage1.getCurrentPosition());
            } else if (mpPrepStart.isPlaying()) {
                sb.setMax(mpPrepStart.getDuration());
                sb.setProgress(mpPrepStart.getCurrentPosition());
            } else if (mpPrepEnd.isPlaying()) {
                sb.setMax(mpPrepEnd.getDuration());
                sb.setProgress(mpPrepEnd.getCurrentPosition());
            }
            seekHandler.postDelayed(updateSeekBarTime, 50);
        }
    };
    **/

    //handler to change seekBarTime
    private Runnable pauseForPrep = new Runnable() {
        public void run() {
            mpPrepEnd.start();
        }
    };
}
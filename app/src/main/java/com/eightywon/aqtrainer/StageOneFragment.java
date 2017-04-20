package com.eightywon.aqtrainer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class StageOneFragment extends Fragment {
    public TextView tv;
    public SeekBar sb;
    public static MediaPlayer mpStage1;
    public ImageButton testButton;
    final int MINUTE = 60*1000;
    final int SECOND = 1000;
    int durationInMilli;
    int curInMilli;
    int durationMin;
    int durationSec;
    int currentMin;
    int currentSec;
    Handler seekHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        sb=(SeekBar) rootView.findViewById(R.id.seekBarStage1);
        tv=(TextView) rootView.findViewById(R.id.txtSeekStatus);
        mpStage1=new MediaPlayer();
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
            mpStage1.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mpStage1.prepare();
        } catch (IOException e) {
        }
        sb.setMax(mpStage1.getDuration());
        durationInMilli=mpStage1.getDuration();
        durationMin=(durationInMilli/MINUTE);
        durationSec=(durationInMilli%MINUTE)/SECOND;
        curInMilli=mpStage1.getCurrentPosition();
        currentMin=(curInMilli/MINUTE);
        currentSec=(curInMilli%MINUTE)/SECOND;

        tv.setText(String.format("%01d:%02d",currentMin,currentSec)+" of "+String.format("%01d:%02d",durationMin,durationSec));
        mpStage1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                mpStage1.seekTo(0);
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationInMilli=mpStage1.getDuration();
                durationMin=(durationInMilli/MINUTE);
                durationSec=(durationInMilli%MINUTE)/SECOND;
                curInMilli=mpStage1.getCurrentPosition();
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

        seekHandler=new Handler();
        seekHandler.post(updateSeekBarTime);
        return rootView;
    }
    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            sb.setMax(mpStage1.getDuration());
            sb.setProgress(mpStage1.getCurrentPosition());
            seekHandler.postDelayed(updateSeekBarTime, 50);
        }
    };
}
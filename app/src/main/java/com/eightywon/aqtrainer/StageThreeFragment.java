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

public class StageThreeFragment extends Fragment {
    public TextView tv;
    public SeekBar sb;
    public static MediaPlayer mpStage3;
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
        View rootView = inflater.inflate(R.layout.frag_stage_3, container,
                false);
        sb=(SeekBar) rootView.findViewById(R.id.seekBarStage3);
        tv=(TextView) rootView.findViewById(R.id.txtSeekStatus3);

        mpStage3=new MediaPlayer();
        testButton=(ImageButton) rootView.findViewById(R.id.btnStage3Play);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mpStage3.isPlaying()) {
                    mpStage3.pause();
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                } else {
                    mpStage3.start();
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_pause","drawable",getActivity().getPackageName()));
                }
            }
        });
        try {
            AssetFileDescriptor afd=getActivity().getAssets().openFd("DescStage3.mp3");
            mpStage3.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mpStage3.prepare();
        } catch (IOException e) {
        }
        sb.setMax(mpStage3.getDuration());
        durationInMilli=mpStage3.getDuration();
        durationMin=(durationInMilli/MINUTE);
        durationSec=(durationInMilli%MINUTE)/SECOND;
        curInMilli=mpStage3.getCurrentPosition();
        currentMin=(curInMilli/MINUTE);
        currentSec=(curInMilli%MINUTE)/SECOND;

        tv.setText(String.format("%01d:%02d",currentMin,currentSec)+" of "+String.format("%01d:%02d",durationMin,durationSec));
        mpStage3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                mpStage3.seekTo(0);
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationInMilli=mpStage3.getDuration();
                durationMin=(durationInMilli/MINUTE);
                durationSec=(durationInMilli%MINUTE)/SECOND;
                curInMilli=mpStage3.getCurrentPosition();
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
            sb.setMax(mpStage3.getDuration());
            sb.setProgress(mpStage3.getCurrentPosition());
            seekHandler.postDelayed(updateSeekBarTime, 50);
        }
    };
}
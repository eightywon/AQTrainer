package com.eightywon.aqtrainer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import android.os.Handler;

public class StageOneFragment extends Fragment {
    public static MediaPlayer mpStageDescription;
    public static MediaPlayer mpPrepStart;
    public static MediaPlayer mpPrepEnd;
    public static MediaPlayer mpLoad;
    public static MediaPlayer mpFire;

    public ImageButton testButton;

    public static boolean isPlaying;

    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txt5;

    Handler hPrepPause;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        mpStageDescription=new MediaPlayer();
        mpPrepStart=new MediaPlayer();
        mpPrepEnd=new MediaPlayer();
        mpLoad=new MediaPlayer();
        mpFire=new MediaPlayer();

        txt1=(TextView) rootView.findViewById(R.id.txt1);
        txt2=(TextView) rootView.findViewById(R.id.txt2);
        txt3=(TextView) rootView.findViewById(R.id.txt3);
        txt4=(TextView) rootView.findViewById(R.id.txt4);
        txt5=(TextView) rootView.findViewById(R.id.txt5);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage1Play);

        isPlaying=false;
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    isPlaying=true;
                    mpStageDescription.start();
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                } else {
                    if (mpStageDescription.isPlaying()) {
                        mpStageDescription.pause();
                        mpStageDescription.seekTo(0);
                    }
                    if (mpPrepStart.isPlaying()) {
                        mpPrepStart.pause();
                        mpPrepStart.seekTo(0);
                    }
                    if (mpPrepEnd.isPlaying()) {
                        mpPrepEnd.pause();
                        mpPrepEnd.seekTo(0);
                    }
                    if (mpLoad.isPlaying()) {
                        mpLoad.pause();
                        mpLoad.seekTo(0);
                    }
                    if (mpFire.isPlaying()) {
                        mpFire.pause();
                        mpFire.seekTo(0);
                    }
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                    isPlaying=false;
                }
                /**
                txt1.setText(String.valueOf(mpStageDescription.isPlaying()));
                txt2.setText(String.valueOf(mpPrepStart.isPlaying()));
                txt3.setText(String.valueOf(mpPrepEnd.isPlaying()));
                txt4.setText(String.valueOf(mpLoad.isPlaying()));
                txt5.setText(String.valueOf(mpFire.isPlaying()));
                **/
            }
        });

        try {
            AssetFileDescriptor afd1=getActivity().getAssets().openFd("DescStage1.mp3");
            AssetFileDescriptor afd2=getActivity().getAssets().openFd("PrepBegin.mp3");
            AssetFileDescriptor afd3=getActivity().getAssets().openFd("PrepEnd.mp3");
            AssetFileDescriptor afd4=getActivity().getAssets().openFd("Load10.mp3");
            AssetFileDescriptor afd5=getActivity().getAssets().openFd("Fire.mp3");

            mpStageDescription.setDataSource(afd1.getFileDescriptor(),afd1.getStartOffset(),afd1.getLength());
            mpStageDescription.prepare();

            mpPrepStart.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            mpPrepStart.prepare();
            mpStageDescription.setNextMediaPlayer(mpPrepStart);

            mpPrepEnd.setDataSource(afd3.getFileDescriptor(),afd3.getStartOffset(),afd3.getLength());
            mpPrepEnd.prepare();
            mpPrepStart.setNextMediaPlayer(mpPrepEnd);

            mpLoad.setDataSource(afd4.getFileDescriptor(),afd4.getStartOffset(),afd4.getLength());
            mpLoad.prepare();
            mpPrepEnd.setNextMediaPlayer(mpLoad);

            mpFire.setDataSource(afd5.getFileDescriptor(),afd5.getStartOffset(),afd5.getLength());
            mpFire.prepare();
            mpLoad.setNextMediaPlayer(mpFire);

        } catch (IOException e) {
        }

        mpStageDescription.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                //testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                mpStageDescription.stop();
            }
        });

        mpPrepStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                //sb.setMax(mpPrepEnd.getDuration());
                mpPrepEnd.pause();
                hPrepPause.postDelayed(prepPause,10000);
            }
        });

        mpFire.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
            }
        });

        hPrepPause=new Handler();
        return rootView;
    }

    private Runnable prepPause = new Runnable() {
        public void run() {
            mpPrepEnd.start();
        }
    };

}
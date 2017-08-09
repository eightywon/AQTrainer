package com.eightywon.aqtrainer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    boolean playStageDesc;
    boolean playPrep;

    TextToSpeech textToSpeech;

    public ImageButton testButton;

    static int countDownFireStageInterval;
    Switch swPlayStageDesc;
    Switch swPlayPrep;

    Handler hCountDownFireStage;

    TextView txtStageDescTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage1Play);
        swPlayStageDesc=(Switch) rootView.findViewById(R.id.swStageDesc);
        swPlayPrep=(Switch) rootView.findViewById(R.id.swPrepPeriod);

        playStageDesc=swPlayStageDesc.isChecked();
        playPrep=swPlayPrep.isChecked();

        textToSpeech = new TextToSpeech(getActivity(),this);

        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "HERE", Toast.LENGTH_SHORT).show();
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    MediaPlayerSingleton.togglePlayingState();
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                    if (playStageDesc) {
                        MainActivity.nextStep=MainActivity.STEP_STAGE_DESCRIPTION;
                    } else {
                        if (playPrep) {
                            MainActivity.nextStep=MainActivity.STEP_PREP_START;
                        } else {
                            MainActivity.nextStep=MainActivity.STEP_LOAD;
                        }
                    }
                    MediaPlayerSingleton.playNext(MainActivity.nextStep);
                } else {
                    MediaPlayerSingleton.stopPlaying();
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

        MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getActivity().getApplicationContext(), "FIRED", Toast.LENGTH_SHORT).show();
            }
        });




        /*
        mediaPlayer.setOnCompletionListener(new MediaPlayerSingleton.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer p) {
                Toast.makeText(getActivity().getApplicationContext(), "HERE", Toast.LENGTH_SHORT).show();
                Log.d("DEBUGGING","Next Step: "+MainActivity.nextStep);
                p.pause();
                p.seekTo(0);
                p.stop();
                p.reset();
                MediaPlayerSingleton.getNext(MainActivity.nextStep);
                Log.d("DEBUGGING","Next Step: "+MainActivity.nextStep);
                if (MainActivity.nextStep!=MainActivity.STEP_DONE) {
                    MediaPlayerSingleton.playNext(MainActivity.nextStep);
                    if (MediaPlayerSingleton.getPlayingState()) {
                        testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop", "drawable", getActivity().getPackageName()));
                    }
                } else {
                    Log.d("DEBUGGING","Step: "+MainActivity.nextStep+", here we are!");
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                }
            }
        });
        */

        hCountDownFireStage=new Handler();

        return rootView;
    }

    private Runnable countDownFireStage = new Runnable() {
        @Override
        public void run() {
            MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
            int remaining=Math.round((mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())/1000);
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
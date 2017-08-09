package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import java.util.Locale;
import android.os.Handler;
import android.widget.TextView;

public class StageFourFragment extends Fragment implements TextToSpeech.OnInitListener {

    boolean playStageDesc;
    boolean playPrep;

    static TextToSpeech textToSpeech;

    public ImageButton testButton;

    public static int countDownFireStageInterval;
    Switch swPlayStageDesc;
    Switch swPlayPrep;

    public static Handler hCountDownFireStage;

    TextView txtStageDescTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_4, container,
                false);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage4Play);
        swPlayStageDesc=(Switch) rootView.findViewById(R.id.swStageDesc);
        swPlayPrep=(Switch) rootView.findViewById(R.id.swPrepPeriod);

        playStageDesc=swPlayStageDesc.isChecked();
        playPrep=swPlayPrep.isChecked();

        textToSpeech = new TextToSpeech(getActivity(),this);

        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                    MediaPlayerSingleton.setStage(4);
                    MediaPlayerSingleton.setActivity(getActivity());
                    MediaPlayerSingleton.getInstance().play(getContext(),MediaPlayerSingleton.playNext(MediaPlayerSingleton.getCurrentStep(),playStageDesc,playPrep),false);
                } else {
                    MediaPlayerSingleton.stopPlaying(getContext());
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


        hCountDownFireStage=new Handler();

        return rootView;
    }

    public static Runnable countDownFireStage = new Runnable() {
        @Override
        public void run() {
            MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
            int remaining=mediaPlayer.getRemaining();
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
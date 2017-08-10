package com.eightywon.aqtrainer;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import java.util.Locale;
import android.os.Handler;
import android.widget.TextView;

public class StageThreeFragment extends Fragment implements TextToSpeech.OnInitListener {

    static TextToSpeech textToSpeech;

    public ImageButton testButton;

    public static int countDownFireStageInterval;

    public static Handler hCountDownFireStage;

    static TextView txtStageDescTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_3, container,
                false);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage3Play);

        textToSpeech = new TextToSpeech(getActivity(),this);

        txtStageDescTimer=(TextView) rootView.findViewById(R.id.txtStageDescTimer);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPlaying=MediaPlayerSingleton.getPlayingState();
                if (!isPlaying) {
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
                    MediaPlayerSingleton.setStage(3);
                    MediaPlayerSingleton.setActivity(getActivity());
                    MediaPlayerSingleton.getInstance().play(getContext(),0,false);
                } else {
                    MediaPlayerSingleton.stopPlaying(getContext());
                    hCountDownFireStage.removeCallbacks(countDownFireStage);
                    testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                }
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

            boolean redAlertMode=MainActivity.getRedAlertMode();
            int secs=0;
            int mins=0;
            String howLong="";
            if (remaining>=1) {
                if (remaining>=60) {
                    mins=remaining/60;
                    secs=remaining%60;
                    howLong=String.valueOf(mins)+" minutes ";
                    if (secs>0) howLong+=String.valueOf(secs)+" seconds.";
                    StageThreeFragment.txtStageDescTimer.setText(mins+"m "+secs+"s");
                } else {
                    secs=remaining%60;
                    if (remaining<=10 && redAlertMode) {
                        howLong=String.valueOf(secs);
                    } else {
                        howLong=String.valueOf(secs)+" seconds.";
                    }
                    StageThreeFragment.txtStageDescTimer.setText(secs+"s");
                }
                if ((remaining<=10 && redAlertMode) || (remaining%countDownFireStageInterval==0)) {
                    textToSpeech.speak(howLong, TextToSpeech.QUEUE_FLUSH, null, "");
                }
            }

            hCountDownFireStage.postDelayed(countDownFireStage,1000);
        }
    };

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
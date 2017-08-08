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

public class StageFourFragment extends Fragment implements TextToSpeech.OnInitListener {

    boolean playStageDesc;
    boolean playPrep;

    TextToSpeech textToSpeech;

    public ImageButton testButton;

    int countDownFireStageInterval;
    Switch swPlayStageDesc;
    Switch swPlayPrep;

    Handler hCountDownFireStage;

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
                    MediaPlayerSingleton.togglePlayingState();
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
            public void onCompletion(MediaPlayer p) {
                p.pause();
                p.seekTo(0);
                p.stop();
                p.reset();
                getNext(MainActivity.nextStep);
                playNext(MainActivity.nextStep);
            }
        });

        hCountDownFireStage=new Handler();

        return rootView;
    }

    public void getNext (int lastStep) {
        switch (lastStep) {
            case MainActivity.STEP_STAGE_DESCRIPTION:
                MainActivity.nextStep=MainActivity.STEP_PREP_START;
                break;
            case MainActivity.STEP_PREP_START:
                MainActivity.nextStep=MainActivity.STEP_PREP_IN_PROGRESS;
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                MainActivity.nextStep=MainActivity.STEP_PREP_END;
                break;
            case MainActivity.STEP_PREP_END:
                MainActivity.nextStep=MainActivity.STEP_LOAD;
                break;
            case MainActivity.STEP_LOAD:
                MainActivity.nextStep=MainActivity.STEP_FIRE_START;
                break;
            case MainActivity.STEP_FIRE_START:
                MainActivity.nextStep=MainActivity.STEP_FIRE_IN_PROGRESS;
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                hCountDownFireStage.removeCallbacks(countDownFireStage);
                MainActivity.nextStep=MainActivity.STEP_FIRE_END;
                break;
            case MainActivity.STEP_FIRE_END:
                MainActivity.nextStep=MainActivity.STEP_DONE;
                MediaPlayerSingleton.togglePlayingState();
                break;
        }
    }

    public void playNext (int nextStep) {

        MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();

        String announceInterval;
        switch (nextStep) {
            case MainActivity.STEP_STAGE_DESCRIPTION:
                MainActivity.audioFile=MainActivity.STEP_STAGE_DESC_4_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_START:
                MainActivity.audioFile=MainActivity.STEP_STAGE_PREP_START_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                MainActivity.audioFile=MainActivity.S15_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_END:
                MainActivity.audioFile=MainActivity.STEP_STAGE_PREP_END_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_LOAD:
                MainActivity.audioFile=MainActivity.STEP_STAGE_LOAD_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_START:
                MainActivity.audioFile=MainActivity.STEP_STAGE_FIRE_START_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                MainActivity.audioFile=MainActivity.S30_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                countDownFireStageInterval=Integer.parseInt(announceInterval);
                hCountDownFireStage.post(countDownFireStage);
                break;
            case MainActivity.STEP_FIRE_END:
                MainActivity.audioFile=MainActivity.STEP_FIRE_END_MP3;
                openAudioFile(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_DONE:
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", getActivity().getPackageName()));
                MediaPlayerSingleton.togglePlayingState();
        }
        if (MediaPlayerSingleton.getPlayingState()) {
            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
        }
    }

    public void openAudioFile(MediaPlayer mp) {
        AssetFileDescriptor afd = null;
        try {
            afd = getActivity().getAssets().openFd(MainActivity.audioFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assert afd != null;
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
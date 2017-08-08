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

    static String STEP_BREAK_MP3="S2.ogg";
    static String FIRE_MP3="S30.ogg";

    //int nextStep;
    boolean playStageDesc;
    boolean playPrep;

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
                isPlaying=false;
                break;
        }
    }

    public void playNext (int nextStep) {

        MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();

        String announceInterval;
        AssetFileDescriptor afd = null;
        switch (nextStep) {
            case MainActivity.STEP_STAGE_DESCRIPTION:

                try {
                    afd = getActivity().getAssets().openFd("DescStage1.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_START:
                try {
                    afd = getActivity().getAssets().openFd("PrepBegin.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                try {
                    afd = getActivity().getAssets().openFd("S10.ogg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_END:
                try {
                    afd = getActivity().getAssets().openFd("PrepEnd.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_LOAD:
                try {
                    afd = getActivity().getAssets().openFd("Load10.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_START:
                try {
                    afd = getActivity().getAssets().openFd("Fire.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                try {
                    afd = getActivity().getAssets().openFd(FIRE_MP3);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                countDownFireStageInterval=Integer.parseInt(announceInterval);
                hCountDownFireStage.post(countDownFireStage);
                break;
            case MainActivity.STEP_FIRE_END:
                try {
                    afd = getActivity().getAssets().openFd("Cease.mp3");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    assert afd != null;
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_DONE:
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", getActivity().getPackageName()));
                isPlaying = false;
        }
        if (isPlaying) {
            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
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
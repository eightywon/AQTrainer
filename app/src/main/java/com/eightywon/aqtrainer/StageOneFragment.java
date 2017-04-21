package com.eightywon.aqtrainer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import android.os.Handler;
import android.widget.Toast;

public class StageOneFragment extends Fragment {
    final static int STEP_STAGE_DESCRIPTION=0;
    final static int STEP_PREP_START=1;
    final static int STEP_PREP_IN_PROGRESS=2;
    final static int STEP_PREP_END=3;
    final static int STEP_LOAD=4;
    final static int STEP_FIRE_START=5;
    final static int STEP_FIRE_IN_PROGRESS=6;
    final static int STEP_FIRE_END=7;
    final static int STEP_STOP=98;
    final static int STEP_DONE=99;
    int nextStep;
    boolean playStageDesc;
    boolean playPrep;

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

    public ImageButton testButton;

    public static boolean isPlaying;

    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txt5;

    Handler hPrepPause;

    Switch swPlayStageDesc;
    Switch swPlayPrep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_stage_1, container,
                false);

        mpStageDescription=new MediaPlayer();
        mpPrepStart=new MediaPlayer();
        mpPrepInProgress=new MediaPlayer();
        mpPrepEnd=new MediaPlayer();
        mpLoad=new MediaPlayer();
        mpFireStart=new MediaPlayer();
        mpFireInProgress=new MediaPlayer();
        mpFireEnd=new MediaPlayer();

        mpS5=new MediaPlayer();
        mpS3=new MediaPlayer();

        txt1=(TextView) rootView.findViewById(R.id.txt1);
        txt2=(TextView) rootView.findViewById(R.id.txt2);
        txt3=(TextView) rootView.findViewById(R.id.txt3);
        txt4=(TextView) rootView.findViewById(R.id.txt4);
        txt5=(TextView) rootView.findViewById(R.id.txt5);

        testButton=(ImageButton) rootView.findViewById(R.id.btnStage1Play);
        swPlayStageDesc=(Switch) rootView.findViewById(R.id.swStageDesc);
        swPlayPrep=(Switch) rootView.findViewById(R.id.swPrepPeriod);

        isPlaying=false;
        playStageDesc=swPlayStageDesc.isChecked();
        playPrep=swPlayPrep.isChecked();

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    isPlaying=true;
                    if (playStageDesc) {
                        nextStep=STEP_STAGE_DESCRIPTION;
                    } else {
                        if (playPrep) {
                            nextStep=STEP_PREP_START;
                        } else {
                            nextStep=STEP_LOAD;
                        }
                    }
                    playNext(nextStep);
                } else {
                    Toast.makeText(getActivity(), String.valueOf(nextStep), Toast.LENGTH_SHORT).show();
                    switch (nextStep) {
                        case STEP_STAGE_DESCRIPTION:
                            mpStageDescription.pause();
                            mpStageDescription.seekTo(0);
                            break;
                        case STEP_PREP_START:
                            mpPrepStart.pause();
                            mpPrepStart.seekTo(0);
                            break;
                        case STEP_PREP_IN_PROGRESS:
                            mpPrepInProgress.pause();
                            mpPrepInProgress.seekTo(0);
                            break;
                        case STEP_PREP_END:
                            mpPrepEnd.pause();
                            mpPrepEnd.seekTo(0);
                            break;
                        case STEP_LOAD:
                            mpLoad.pause();
                            mpLoad.seekTo(0);
                            break;
                        case STEP_FIRE_START:
                            mpFireStart.pause();
                            mpFireStart.seekTo(0);
                            break;
                        case STEP_FIRE_IN_PROGRESS:
                            mpFireInProgress.pause();
                            mpFireInProgress.seekTo(0);
                            break;
                        case STEP_FIRE_END:
                            mpFireEnd.pause();
                            mpFireEnd.seekTo(0);
                            break;
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

            AssetFileDescriptor afd6=getActivity().getAssets().openFd("S5.ogg");
            AssetFileDescriptor afd7=getActivity().getAssets().openFd("S3.ogg");

            AssetFileDescriptor afd8=getActivity().getAssets().openFd("S10.ogg");
            AssetFileDescriptor afd9=getActivity().getAssets().openFd("Fire.mp3");
            AssetFileDescriptor afd10=getActivity().getAssets().openFd("Cease.mp3");

            mpPrepInProgress.setDataSource(afd8.getFileDescriptor(),afd8.getStartOffset(),afd8.getLength());
            mpPrepInProgress.prepare();

            mpStageDescription.setDataSource(afd1.getFileDescriptor(),afd1.getStartOffset(),afd1.getLength());
            mpStageDescription.prepare();

            mpS3.setDataSource(afd7.getFileDescriptor(),afd7.getStartOffset(),afd7.getLength());
            mpS3.prepare();
            //mpStageDescription.setNextMediaPlayer(mpS3);

            mpPrepStart.setDataSource(afd2.getFileDescriptor(),afd2.getStartOffset(),afd2.getLength());
            mpPrepStart.prepare();
            //mpS5.setNextMediaPlayer(mpPrepStart);

            mpPrepEnd.setDataSource(afd3.getFileDescriptor(),afd3.getStartOffset(),afd3.getLength());
            mpPrepEnd.prepare();
            //mpPrepStart.setNextMediaPlayer(mpPrepEnd);

            mpLoad.setDataSource(afd4.getFileDescriptor(),afd4.getStartOffset(),afd4.getLength());
            mpLoad.prepare();
            //mpPrepEnd.setNextMediaPlayer(mpLoad);

            mpFireStart.setDataSource(afd5.getFileDescriptor(),afd5.getStartOffset(),afd5.getLength());
            mpFireStart.prepare();
            //mpLoad.setNextMediaPlayer(mpFire);

            mpFireInProgress.setDataSource(afd8.getFileDescriptor(),afd8.getStartOffset(),afd8.getLength());
            mpFireInProgress.prepare();

            mpFireEnd.setDataSource(afd10.getFileDescriptor(),afd10.getStartOffset(),afd10.getLength());
            mpFireEnd.prepare();

        } catch (IOException e) {
        }

        mpStageDescription.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                mpStageDescription.pause();
                mpStageDescription.seekTo(0);
                if (playPrep) {
                    nextStep=STEP_PREP_START;
                } else {
                    nextStep=STEP_LOAD;
                }
                playNext(nextStep);
            }
        });

        mpPrepStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                //sb.setMax(mpPrepEnd.getDuration());
                mpPrepStart.pause();
                mpPrepStart.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_PREP_IN_PROGRESS;
                playNext(nextStep);
            }
        });

        mpPrepInProgress.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                //sb.setMax(mpPrepEnd.getDuration());
                mpPrepInProgress.pause();
                mpPrepInProgress.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_PREP_END;
                playNext(nextStep);
            }
        });

        mpPrepEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                //sb.setMax(mpPrepEnd.getDuration());
                mpPrepEnd.pause();
                mpPrepEnd.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_LOAD;
                playNext(nextStep);
            }
        });

        mpLoad.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                //sb.setMax(mpPrepEnd.getDuration());
                mpLoad.pause();
                mpLoad.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_FIRE_START;
                playNext(nextStep);
            }
        });

        mpFireStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                mpFireStart.pause();
                mpFireStart.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_FIRE_IN_PROGRESS;
                playNext(nextStep);
            }
        });

        mpFireInProgress.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                mpFireInProgress.pause();
                mpFireInProgress.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_FIRE_END;
                playNext(nextStep);
            }
        });

        mpFireEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer p) {
                mpFireEnd.pause();
                mpFireEnd.seekTo(0);
                //hPrepPause.postDelayed(prepPause,10000);
                nextStep=STEP_DONE;
                playNext(nextStep);
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

    public void playNext (int nextStep) {
        switch (nextStep) {
            case STEP_STAGE_DESCRIPTION:
                mpStageDescription.seekTo(0);
                mpStageDescription.start();
                break;
            case STEP_PREP_START:
                mpPrepStart.seekTo(0);
                mpPrepStart.start();
                break;
            case STEP_PREP_IN_PROGRESS:
                mpPrepInProgress.seekTo(0);
                mpPrepInProgress.start();
                break;
            case STEP_PREP_END:
                mpPrepEnd.seekTo(0);
                mpPrepEnd.start();
                break;
            case STEP_LOAD:
                mpLoad.seekTo(0);
                mpLoad.start();
                break;
            case STEP_FIRE_START:
                mpFireStart.seekTo(0);
                mpFireStart.start();
                break;
            case STEP_FIRE_IN_PROGRESS:
                mpFireInProgress.seekTo(0);
                mpFireInProgress.start();
                break;
            case STEP_FIRE_END:
                mpFireEnd.seekTo(0);
                mpFireEnd.start();
                break;
            case STEP_DONE:
                testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                isPlaying=false;
        }
        if (isPlaying) {
            testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop","drawable",getActivity().getPackageName()));
        }
    }
}
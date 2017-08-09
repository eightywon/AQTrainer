package com.eightywon.aqtrainer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;

class MediaPlayerSingleton {
    private static MediaPlayerSingleton mediaPlayerSingleton;
    private static int currentStep;
    private static MediaPlayer mediaPlayer;
    private static int stage;
    private static Activity act;

    static MediaPlayerSingleton getInstance() {
        if (mediaPlayerSingleton==null) {
            mediaPlayerSingleton=new MediaPlayerSingleton();
        }
        return mediaPlayerSingleton;
    }

    private static ImageButton testButton;

    int getRemaining() {
        return Math.round((mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())/1000);
    }

    void play(Context context, int resId, boolean stop) {
        play(context, resId, null, null, stop);
    }

    public void play(Context context, int resId, MediaPlayer.OnCompletionListener completionListener) {
        play(context, resId, completionListener, null, false);
    }

    public void play(Context context, int resId, MediaPlayer.OnErrorListener errorListener) {
        play(context, resId, null, errorListener, false);
    }

    private void play(final Context context, int resId, MediaPlayer.OnCompletionListener completionListener, MediaPlayer.OnErrorListener errorListener, boolean stop) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                currentStep=MainActivity.STEP_BEGIN;
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (!stop) {
            Log.d("DEBUGGING: ", "resId: "+String.valueOf(resId)+", currentStep: "+currentStep);
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), resId);
            mediaPlayer.setOnErrorListener(errorListener);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    switch (stage) {
                        case 1:
                            testButton=(ImageButton) act.findViewById(R.id.btnStage1Play);
                            break;
                        case 2:
                            testButton=(ImageButton) act.findViewById(R.id.btnStage2Play);
                            break;
                        case 3:
                            testButton=(ImageButton) act.findViewById(R.id.btnStage3Play);
                            break;
                        case 4:
                            testButton=(ImageButton) act.findViewById(R.id.btnStage4Play);
                            break;
                    }

                    if (currentStep!=MainActivity.STEP_FIRE_END && currentStep!=MainActivity.STEP_DONE) {
                        MediaPlayerSingleton.getInstance().play(context,MediaPlayerSingleton.playNext(currentStep,false,false),false);
                        if (MediaPlayerSingleton.getPlayingState()) {
                            testButton.setImageResource(act.getResources().getIdentifier("@android:drawable/ic_media_stop", "drawable", act.getPackageName()));
                        }
                    } else {
                        stopPlaying(context);
                        currentStep=MainActivity.STEP_BEGIN;
                        testButton.setImageResource(act.getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", act.getPackageName()));
                    }
                }
            });
        }
    }

    static void stopPlaying(final Context context) {
        MediaPlayerSingleton.getInstance().play(context,MediaPlayerSingleton.playNext(currentStep,false,false),true);
        StageOneFragment.hCountDownFireStage.removeCallbacks(StageOneFragment.countDownFireStage);
    }

    static boolean getPlayingState() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    static void setStage(int s) {
        stage=s;
    }

    static void setActivity(Activity a) {
        act=a;
    }

    static int getCurrentStep() {
        return currentStep;
    }

    static int playNext(int step, boolean playDesc, boolean playPrep) {
        String announceInterval;
        int source = 0;
        switch (step) {
            case MainActivity.STEP_BEGIN:
                if (playDesc) {
                    switch (stage) {
                        case 1:
                            source=R.raw.descstage1;
                            break;
                        case 2:
                            source=R.raw.descstage2;
                            break;
                        case 3:
                            source=R.raw.descstage3;
                            break;
                        case 4:
                            source=R.raw.descstage4;
                            break;
                    }
                    currentStep=MainActivity.STEP_STAGE_DESCRIPTION;
                    break;
                } else if (playPrep) {
                    source=R.raw.prepbegin;
                    currentStep=MainActivity.STEP_PREP_START;
                    break;
                } else {
                    source=R.raw.load10;
                    currentStep = MainActivity.STEP_LOAD;
                    break;
                }
            case MainActivity.STEP_STAGE_DESCRIPTION:
                if (playPrep){
                    source=R.raw.prepbegin;
                    currentStep=MainActivity.STEP_PREP_START;
                    break;
                } else {
                    source=R.raw.load10;
                    currentStep=MainActivity.STEP_LOAD;
                    break;
                }
            case MainActivity.STEP_PREP_START:
                source=R.raw.s15;
                currentStep=MainActivity.STEP_PREP_IN_PROGRESS;
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                 source=R.raw.prepend;
                currentStep=MainActivity.STEP_PREP_END;
                break;
            case MainActivity.STEP_PREP_END:
                source=R.raw.load10;
                currentStep=MainActivity.STEP_LOAD;
                break;
            case MainActivity.STEP_LOAD:
                source=R.raw.fire;
                currentStep=MainActivity.STEP_FIRE_START;
                break;
            case MainActivity.STEP_FIRE_START:
                switch (stage) {
                    case 1:
                        source=R.raw.s5;
                        break;
                    case 2:
                        source=R.raw.s55;
                        break;
                    case 3:
                        source=R.raw.s65;
                        break;
                    case 4:
                        source=R.raw.s300;
                        break;
                }
                currentStep=MainActivity.STEP_FIRE_IN_PROGRESS;
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                StageOneFragment.countDownFireStageInterval=Integer.parseInt(announceInterval);
                StageOneFragment.hCountDownFireStage.post(StageOneFragment.countDownFireStage);
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                source=R.raw.cease;
                currentStep=MainActivity.STEP_FIRE_END;
                break;
            case MainActivity.STEP_FIRE_END:
                currentStep=MainActivity.STEP_DONE;
                break;
            case MainActivity.STEP_DONE:
                break;
        }
        return source;
    }
}

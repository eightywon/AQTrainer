package com.eightywon.aqtrainer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;

class MediaPlayerSingleton {
    private static MediaPlayerSingleton mediaPlayerSingleton;
    private static int currentStep;
    private static MediaPlayer mediaPlayer;
    private static int stage;
    private static Activity act;
    private static ImageButton testButton;

    static MediaPlayerSingleton getInstance() {
        if (mediaPlayerSingleton==null) {
            mediaPlayerSingleton=new MediaPlayerSingleton();
        }
        return mediaPlayerSingleton;
    }

    int getRemaining() {
        //return (int) Math.round((((mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())+200)/1000));
        return (int) Math.round(((float)(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())/(float) 1000));
    }

    void play(Context context, int resId, boolean stop) {

        int i=0;
        while (i<MainActivity.sources.length) {
            MainActivity.sources[i]=0;
            i++;
        }

        if (MainActivity.getPlayStageDescription()) {
            switch (stage) {
                case 1:
                    MainActivity.sources[MainActivity.STEP_STAGE_DESCRIPTION]=R.raw.descstage1;
                    break;
                case 2:
                    MainActivity.sources[MainActivity.STEP_STAGE_DESCRIPTION]=R.raw.descstage2;
                    break;
                case 3:
                    MainActivity.sources[MainActivity.STEP_STAGE_DESCRIPTION]=R.raw.descstage3;
                    break;
                case 4:
                    MainActivity.sources[MainActivity.STEP_STAGE_DESCRIPTION]=R.raw.descstage4;
                    break;
            }
        }

        if (MainActivity.getPlayPrepAnnouncements()) {

            //set begin
            MainActivity.sources[MainActivity.STEP_PREP_START]=R.raw.prepbegin;

            //set in process
            switch (MainActivity.getPrepTime()) {
                case 30:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s30;
                    break;
                case 60:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s60;
                    break;
                case 90:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s90;
                    break;
                case 120:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s120;
                    break;
            }

            MainActivity.sources[MainActivity.STEP_PREP_END]=R.raw.prepend;
        }

        if (stage==2 || stage==3) {
            MainActivity.sources[MainActivity.STEP_SAFTIES_ON_STAND]=R.raw.safetiesonstand;
        }

        MainActivity.sources[MainActivity.STEP_LOAD]=R.raw.load10;
        MainActivity.sources[MainActivity.STEP_FIRE_START]=R.raw.fire;

        switch (stage) {
            case 1:
                MainActivity.sources[MainActivity.STEP_FIRE_IN_PROGRESS]=R.raw.s30;
                break;
            case 2:
                MainActivity.sources[MainActivity.STEP_FIRE_IN_PROGRESS]=R.raw.s55;
                break;
            case 3:
                MainActivity.sources[MainActivity.STEP_FIRE_IN_PROGRESS]=R.raw.s65;
                break;
            case 4:
                MainActivity.sources[MainActivity.STEP_FIRE_IN_PROGRESS]=R.raw.s300;
                break;
        }

        MainActivity.sources[MainActivity.STEP_FIRE_END]=R.raw.cease;


        resId=getNextResId();
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
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (!stop) {
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), resId);
            mediaPlayer.setOnErrorListener(errorListener);
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentStep++;
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

                    if (currentStep!=MainActivity.STEP_DONE) {
                        MediaPlayerSingleton.getInstance().play(context,getNextResId(),null,null,false);
                        if (MediaPlayerSingleton.getPlayingState()) {
                            testButton.setImageResource(act.getResources().getIdentifier("@android:drawable/ic_media_stop", "drawable", act.getPackageName()));
                        }
                    } else {
                        stopPlaying(context);
                        currentStep=0;
                        testButton.setImageResource(act.getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", act.getPackageName()));
                    }
                }
            });
        }
    }

    static void stopPlaying(final Context context) {
        switch (stage) {
            case 1:
                StageOneFragment.hCountDownFireStage.removeCallbacks(StageOneFragment.countDownFireStage);
                StageOneFragment.txtStageDescTimer.setText("0m 0s");
                break;
            case 2:
                StageTwoFragment.hCountDownFireStage.removeCallbacks(StageTwoFragment.countDownFireStage);
                StageTwoFragment.txtStageDescTimer.setText("0m 0s");
                break;
            case 3:
                StageThreeFragment.hCountDownFireStage.removeCallbacks(StageThreeFragment.countDownFireStage);
                StageThreeFragment.txtStageDescTimer.setText("0m 0s");
                break;
            case 4:
                StageFourFragment.hCountDownFireStage.removeCallbacks(StageFourFragment.countDownFireStage);
                StageFourFragment.txtStageDescTimer.setText("0m 0s");
                break;

        }

        MediaPlayerSingleton.getInstance().play(context,0,null,null,true);
        currentStep=0;
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

    static int getNextResId() {
        while (MainActivity.sources[currentStep]==0) {
            currentStep++;
        }

        if (currentStep==MainActivity.STEP_FIRE_IN_PROGRESS) {
            if (MainActivity.getAnnounceStageTime()) {
                String announceInterval;
                SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                switch (stage) {
                    case 1:
                        StageOneFragment.countDownFireStageInterval=Integer.parseInt(announceInterval);
                        StageOneFragment.hCountDownFireStage.post(StageOneFragment.countDownFireStage);
                        break;
                    case 2:
                        StageTwoFragment.countDownFireStageInterval=Integer.parseInt(announceInterval);
                        StageTwoFragment.hCountDownFireStage.post(StageTwoFragment.countDownFireStage);
                        break;
                    case 3:
                        StageThreeFragment.countDownFireStageInterval=Integer.parseInt(announceInterval);
                        StageThreeFragment.hCountDownFireStage.post(StageThreeFragment.countDownFireStage);
                        break;
                    case 4:
                        StageFourFragment.countDownFireStageInterval=Integer.parseInt(announceInterval);
                        StageFourFragment.hCountDownFireStage.post(StageFourFragment.countDownFireStage);
                        break;
                }
            }
        } else if (currentStep==MainActivity.STEP_FIRE_END) {
            switch (stage) {
                case 1:
                    StageOneFragment.hCountDownFireStage.removeCallbacks(StageOneFragment.countDownFireStage);
                    StageOneFragment.txtStageDescTimer.setText("0m 0s");
                    break;
                case 2:
                    StageTwoFragment.hCountDownFireStage.removeCallbacks(StageTwoFragment.countDownFireStage);
                    StageTwoFragment.txtStageDescTimer.setText("0m 0s");
                    break;
                case 3:
                    StageThreeFragment.hCountDownFireStage.removeCallbacks(StageThreeFragment.countDownFireStage);
                    StageThreeFragment.txtStageDescTimer.setText("0m 0s");
                    break;
                case 4:
                    StageFourFragment.hCountDownFireStage.removeCallbacks(StageFourFragment.countDownFireStage);
                    StageFourFragment.txtStageDescTimer.setText("0m 0s");
                    break;

            }
        }
        return MainActivity.sources[currentStep];
    }
}

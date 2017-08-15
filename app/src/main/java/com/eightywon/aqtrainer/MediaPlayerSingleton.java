package com.eightywon.aqtrainer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
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
        return Math.round(((float)(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())/(float) 1000));
    }

    void play(Context context, int resId, boolean stop) {

        int i=MainActivity.STEP_BEGIN;
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

        if (MainActivity.getPlayPrepAnnouncements() || MainActivity.getPlayStageDescription() || stage==2 || stage==3) {
            MainActivity.sources[MainActivity.STEP_PAUSE_1]=R.raw.s2;
        }

        MainActivity.sources[MainActivity.STEP_LOAD]=R.raw.load10;
        MainActivity.sources[MainActivity.STEP_PAUSE_2]=R.raw.s5;
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
                        currentStep=MainActivity.STEP_BEGIN;
                        testButton.setImageResource(act.getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", act.getPackageName()));
                    }
                }
            });
        }
    }

    static void stopPlaying(final Context context) {
        MainActivity.hCountDownFireStage.removeCallbacks(MainActivity.countDownFireStage);
        MainActivity.hCountDownPrepStage.removeCallbacks(MainActivity.countDownPrepStage);
        MainActivity.hCountDownDescStage.removeCallbacks(MainActivity.countDownDescStage);

        switch (stage) {
            case 1:
                StageOneFragment.shot1.setVisibility(View.INVISIBLE);
                StageOneFragment.shot2.setVisibility(View.INVISIBLE);
                StageOneFragment.shot3.setVisibility(View.INVISIBLE);
                StageOneFragment.shot4.setVisibility(View.INVISIBLE);
                StageOneFragment.shot5.setVisibility(View.INVISIBLE);
                StageOneFragment.shot6.setVisibility(View.INVISIBLE);
                StageOneFragment.shot7.setVisibility(View.INVISIBLE);
                StageOneFragment.shot8.setVisibility(View.INVISIBLE);
                StageOneFragment.shot9.setVisibility(View.INVISIBLE);
                StageOneFragment.shot10.setVisibility(View.INVISIBLE);
                StageOneFragment.imageView.setBackgroundResource(0);
                StageOneFragment.imageView.setPadding(0,0,0,0);
                break;
            case 2:
                StageTwoFragment.shot1.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot2.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot3.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot4.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot5.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot6.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot7.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot8.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot9.setVisibility(View.INVISIBLE);
                StageTwoFragment.shot10.setVisibility(View.INVISIBLE);
                StageTwoFragment.target1.setVisibility(View.VISIBLE);
                StageTwoFragment.target2.setVisibility(View.VISIBLE);
                StageTwoFragment.target1Highlight.setVisibility(View.INVISIBLE);
                StageTwoFragment.target2Highlight.setVisibility(View.INVISIBLE);
                //StageTwoFragment.imageView1.setBackgroundResource(0);
                //StageTwoFragment.imageView1.setPadding(0,0,0,0);
                //StageTwoFragment.imageView2.setBackgroundResource(0);
                //StageTwoFragment.imageView2.setPadding(0,0,0,0);
                break;
            case 3:
                StageThreeFragment.shot1.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot2.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot3.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot4.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot5.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot6.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot7.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot8.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot9.setVisibility(View.INVISIBLE);
                StageThreeFragment.shot10.setVisibility(View.INVISIBLE);
                StageThreeFragment.target1.setBackgroundResource(0);
                StageThreeFragment.target1.setPadding(0,0,0,0);
                StageThreeFragment.target2.setBackgroundResource(0);
                StageThreeFragment.target2.setPadding(0,0,0,0);
                StageThreeFragment.target3.setBackgroundResource(0);
                StageThreeFragment.target3.setPadding(0,0,0,0);
                break;
            case 4:
                StageFourFragment.shot1.setVisibility(View.INVISIBLE);
                StageFourFragment.shot2.setVisibility(View.INVISIBLE);
                StageFourFragment.shot3.setVisibility(View.INVISIBLE);
                StageFourFragment.shot4.setVisibility(View.INVISIBLE);
                StageFourFragment.shot5.setVisibility(View.INVISIBLE);
                StageFourFragment.shot6.setVisibility(View.INVISIBLE);
                StageFourFragment.shot7.setVisibility(View.INVISIBLE);
                StageFourFragment.shot8.setVisibility(View.INVISIBLE);
                StageFourFragment.shot9.setVisibility(View.INVISIBLE);
                StageFourFragment.shot10.setVisibility(View.INVISIBLE);
                StageFourFragment.imageView1.setBackgroundResource(0);
                StageFourFragment.imageView1.setPadding(0,0,0,0);
                StageFourFragment.imageView2.setBackgroundResource(0);
                StageFourFragment.imageView2.setPadding(0,0,0,0);
                StageFourFragment.imageView3.setBackgroundResource(0);
                StageFourFragment.imageView3.setPadding(0,0,0,0);
                StageFourFragment.imageView4.setBackgroundResource(0);
                StageFourFragment.imageView4.setPadding(0,0,0,0);
                break;
        }

        switch (stage) {
            case 1:
                StageOneFragment.txtStageDescTimer.setText("");
                StageOneFragment.txtStepDesc.setText("");
                break;
            case 2:
                StageTwoFragment.txtStageDescTimer.setText("");
                StageTwoFragment.txtStepDesc.setText("");
                break;
            case 3:
                StageThreeFragment.txtStageDescTimer.setText("");
                StageThreeFragment.txtStepDesc.setText("");
                break;
            case 4:
                StageFourFragment.txtStageDescTimer.setText("");
                StageFourFragment.txtStepDesc.setText("");
                break;

        }

        MediaPlayerSingleton.getInstance().play(context,0,null,null,true);
        currentStep=MainActivity.STEP_BEGIN;
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

    static int getStage() {return stage;}

    static void setActivity(Activity a) {
        act=a;
    }

    private static int getNextResId() {
        while (MainActivity.sources[currentStep]==0) {
            currentStep++;
        }

        if (currentStep==MainActivity.STEP_STAGE_DESCRIPTION) {
            MainActivity.hCountDownDescStage.post(MainActivity.countDownDescStage);
        } else {
            MainActivity.hCountDownDescStage.removeCallbacks(MainActivity.countDownDescStage);

            switch (stage) {
                case 1:
                    StageOneFragment.shot1.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot2.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot3.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot4.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot5.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot6.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot7.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot8.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot9.setVisibility(View.INVISIBLE);
                    StageOneFragment.shot10.setVisibility(View.INVISIBLE);
                    StageOneFragment.imageView.setBackgroundResource(0);
                    StageOneFragment.imageView.setPadding(0,0,0,0);
                    break;
                case 2:
                    StageTwoFragment.shot1.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot2.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot3.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot4.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot5.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot6.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot7.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot8.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot9.setVisibility(View.INVISIBLE);
                    StageTwoFragment.shot10.setVisibility(View.INVISIBLE);
                    StageTwoFragment.target1.setVisibility(View.VISIBLE);
                    StageTwoFragment.target2.setVisibility(View.VISIBLE);
                    StageTwoFragment.target1Highlight.setVisibility(View.INVISIBLE);
                    StageTwoFragment.target2Highlight.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    StageThreeFragment.shot1.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot2.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot3.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot4.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot5.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot6.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot7.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot8.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot9.setVisibility(View.INVISIBLE);
                    StageThreeFragment.shot10.setVisibility(View.INVISIBLE);
                    StageThreeFragment.target1.setBackgroundResource(0);
                    StageThreeFragment.target1.setPadding(0,0,0,0);
                    StageThreeFragment.target2.setBackgroundResource(0);
                    StageThreeFragment.target2.setPadding(0,0,0,0);
                    StageThreeFragment.target3.setBackgroundResource(0);
                    StageThreeFragment.target3.setPadding(0,0,0,0);
                    break;
                case 4:
                    StageFourFragment.shot1.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot2.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot3.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot4.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot5.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot6.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot7.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot8.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot9.setVisibility(View.INVISIBLE);
                    StageFourFragment.shot10.setVisibility(View.INVISIBLE);
                    StageFourFragment.imageView1.setBackgroundResource(0);
                    StageFourFragment.imageView1.setPadding(0,0,0,0);
                    StageFourFragment.imageView2.setBackgroundResource(0);
                    StageFourFragment.imageView2.setPadding(0,0,0,0);
                    StageFourFragment.imageView3.setBackgroundResource(0);
                    StageFourFragment.imageView3.setPadding(0,0,0,0);
                    StageFourFragment.imageView4.setBackgroundResource(0);
                    StageFourFragment.imageView4.setPadding(0,0,0,0);
                    break;
            }
        }

        if (currentStep==MainActivity.STEP_PREP_IN_PROGRESS) {
            MainActivity.firstTime=true;
            MainActivity.lastSec=0;
            MainActivity.hCountDownPrepStage.post(MainActivity.countDownPrepStage);
            switch (stage) {
                case 1:
                    StageOneFragment.txtStepDesc.setText(R.string.StepDescPreparing);
                    break;
                case 2:
                    StageTwoFragment.txtStepDesc.setText(R.string.StepDescPreparing);
                    break;
                case 3:
                    StageThreeFragment.txtStepDesc.setText(R.string.StepDescPreparing);
                    break;
                case 4:
                    StageFourFragment.txtStepDesc.setText(R.string.StepDescPreparing);
                    break;
            }
        } else if (currentStep==MainActivity.STEP_PREP_END) {
            MainActivity.hCountDownPrepStage.removeCallbacks(MainActivity.countDownPrepStage);
            switch (stage) {
                case 1:
                    StageOneFragment.txtStepDesc.setText("");
                    StageOneFragment.txtStageDescTimer.setText("");
                    break;
                case 2:
                    StageTwoFragment.txtStepDesc.setText("");
                    StageTwoFragment.txtStageDescTimer.setText("");
                    break;
                case 3:
                    StageThreeFragment.txtStepDesc.setText("");
                    StageThreeFragment.txtStageDescTimer.setText("");
                    break;
                case 4:
                    StageFourFragment.txtStepDesc.setText("");
                    StageFourFragment.txtStageDescTimer.setText("");
                    break;
            }
        }
        if (currentStep==MainActivity.STEP_FIRE_IN_PROGRESS) {
            MainActivity.firstTime=true;
            MainActivity.lastSec=0;
            MainActivity.hCountDownFireStage.post(MainActivity.countDownFireStage);
            switch (stage) {
                case 1:
                    StageOneFragment.txtStepDesc.setText(R.string.StepDescFireInProgress);
                    break;
                case 2:
                    StageTwoFragment.txtStepDesc.setText(R.string.StepDescFireInProgress);
                    break;
                case 3:
                    StageThreeFragment.txtStepDesc.setText(R.string.StepDescFireInProgress);
                    break;
                case 4:
                    StageFourFragment.txtStepDesc.setText(R.string.StepDescFireInProgress);
                    break;
            }
        } else if (currentStep==MainActivity.STEP_FIRE_END) {
            MainActivity.hCountDownFireStage.removeCallbacks(MainActivity.countDownFireStage);
            switch (stage) {
                case 1:
                    StageOneFragment.txtStageDescTimer.setText(R.string.StepDescCeaseFire);
                    break;
                case 2:
                    StageTwoFragment.txtStageDescTimer.setText(R.string.StepDescCeaseFire);
                    break;
                case 3:
                    StageThreeFragment.txtStageDescTimer.setText(R.string.StepDescCeaseFire);
                    break;
                case 4:
                    StageFourFragment.txtStageDescTimer.setText(R.string.StepDescCeaseFire);
                    break;

            }
        } else if (currentStep==MainActivity.STEP_DONE){
            switch (stage) {
                case 1:
                    StageOneFragment.txtStageDescTimer.setText("");
                    StageOneFragment.txtStepDesc.setText("");
                    break;
                case 2:
                    StageTwoFragment.txtStageDescTimer.setText("");
                    StageTwoFragment.txtStepDesc.setText("");
                    break;
                case 3:
                    StageThreeFragment.txtStageDescTimer.setText("");
                    StageThreeFragment.txtStepDesc.setText("");
                    break;
                case 4:
                    StageFourFragment.txtStageDescTimer.setText("");
                    StageFourFragment.txtStepDesc.setText("");
                    break;

            }
        }

        return MainActivity.sources[currentStep];
    }
}

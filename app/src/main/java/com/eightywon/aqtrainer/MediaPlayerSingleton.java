package com.eightywon.aqtrainer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class MediaPlayerSingleton {
    private static MediaPlayerSingleton mediaPlayerSingleton;
    private static int currentStep;
    private static MediaPlayer mediaPlayer;
    private static int stage;
    private Button stageButton;
    private Activity mainAct;
    private int previousPage;

    private Utils utils;

    static MediaPlayerSingleton getInstance() {
        if (mediaPlayerSingleton==null) {
            mediaPlayerSingleton=new MediaPlayerSingleton(instance);
        }
        return mediaPlayerSingleton;
    }

    int getRemaining() {
        return Math.round(((float)(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())/(float) 1000));
    }

    int getTotalLength() {
        return Math.round(mediaPlayer.getDuration()/(float) 1000);
    }

    void play(Context context, boolean stop, Activity activity, int page) {

        utils=new Utils(activity, page, context);

        mainAct=activity;
        previousPage=page;

        int i=MainActivity.STEP_BEGIN;
        while (i<MainActivity.sources.length) {
            MainActivity.sources[i]=0;
            i++;
        }

        if (utils.getPlayStageDescription(activity)) {
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

        if (utils.getPlayPrepAnnouncements(activity)) {

            //set begin
            MainActivity.sources[MainActivity.STEP_PREP_START]=R.raw.prepbegin;

            //set in process
            switch (utils.getPrepTime(activity)) {
                case 5:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s5;
                    break;
                case 15:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s15;
                    break;
                case 30:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s30;
                    break;
                case 45:
                    MainActivity.sources[MainActivity.STEP_PREP_IN_PROGRESS]=R.raw.s45;
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

        if (utils.getPlayPrepAnnouncements(activity) || utils.getPlayStageDescription(activity) || stage==2 || stage==3) {
            MainActivity.sources[MainActivity.STEP_PAUSE_1]=R.raw.s2;
        }

        if (stage!=2 && stage!=3) {
            MainActivity.sources[MainActivity.STEP_LOAD] = R.raw.load10;
        }

        MainActivity.sources[MainActivity.STEP_PAUSE_2]=R.raw.s5;
        MainActivity.sources[MainActivity.STEP_FIRE_START]=R.raw.fire;

        switch (stage) {
            case 1:
                MainActivity.sources[MainActivity.STEP_FIRE_IN_PROGRESS]=R.raw.s120;
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

        int resId = getNextResId();
        play(context, resId, null, stop, activity, page);
    }

    private void play(final Context context, int resId, MediaPlayer.OnErrorListener errorListener, boolean stop,
                      final Activity activity, final int previousPage) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
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
                    View view=activity.findViewById(previousPage);
                    stageButton=(Button) view.findViewById(R.id.btnStagePlay);
                    if (currentStep!=MainActivity.STEP_DONE) {
                        play(context,getNextResId(),null,false, activity, previousPage);
                        if (MediaPlayerSingleton.getPlayingState()) {
                            stageButton.setText(R.string.btnStopStage);
                            stageButton.setBackgroundResource(R.drawable.button_clicked);
                        }
                    } else {
                        stopPlaying(context);
                        currentStep=MainActivity.STEP_BEGIN;
                        stageButton.setText(R.string.btnStartStage);
                        stageButton.setBackgroundResource(R.drawable.button);
                    }
                }
            });
        }
    }

    void stopPlaying(Context context) {
        utils.hCountDownFireStage.removeCallbacks(utils.countDownFireStage);
        utils.hCountDownPrepStage.removeCallbacks(utils.countDownPrepStage);
        utils.hCountDownDescStage.removeCallbacks(utils.countDownDescStage);
        utils=null;

        View view=mainAct.findViewById(previousPage);
        TextView txtStageDescTimer=(TextView) view.findViewById(R.id.txtStageDescTimer);
        TextView txtStepDesc=(TextView) view.findViewById(R.id.txtStepDesc);

        ImageView shot1=(ImageView) view.findViewById(R.id.shot1);
        ImageView shot2=(ImageView) view.findViewById(R.id.shot2);
        ImageView shot3=(ImageView) view.findViewById(R.id.shot3);
        ImageView shot4=(ImageView) view.findViewById(R.id.shot4);
        ImageView shot5=(ImageView) view.findViewById(R.id.shot5);
        ImageView shot6=(ImageView) view.findViewById(R.id.shot6);
        ImageView shot7=(ImageView) view.findViewById(R.id.shot7);
        ImageView shot8=(ImageView) view.findViewById(R.id.shot8);
        ImageView shot9=(ImageView) view.findViewById(R.id.shot9);
        ImageView shot10=(ImageView) view.findViewById(R.id.shot10);

        ImageView target1=(ImageView) view.findViewById(R.id.target1);
        ImageView target2=(ImageView) view.findViewById(R.id.target2);
        ImageView target3=(ImageView) view.findViewById(R.id.target3);
        ImageView target4=(ImageView) view.findViewById(R.id.target4);
        ImageView target1Highlight=(ImageView) view.findViewById(R.id.target1Highlight);
        ImageView target2Highlight=(ImageView) view.findViewById(R.id.target2Highlight);
        ImageView target3Highlight=(ImageView) view.findViewById(R.id.target3Highlight);
        ImageView target4Highlight=(ImageView) view.findViewById(R.id.target4Highlight);

        shot1.setVisibility(View.INVISIBLE);
        shot2.setVisibility(View.INVISIBLE);
        shot3.setVisibility(View.INVISIBLE);
        shot4.setVisibility(View.INVISIBLE);
        shot5.setVisibility(View.INVISIBLE);
        shot6.setVisibility(View.INVISIBLE);
        shot7.setVisibility(View.INVISIBLE);
        shot8.setVisibility(View.INVISIBLE);
        shot9.setVisibility(View.INVISIBLE);
        shot10.setVisibility(View.INVISIBLE);

        txtStepDesc.setText("");
        txtStageDescTimer.setText("");

        switch (stage) {
            case 1:
                target1.setVisibility(View.VISIBLE);
                target1Highlight.setVisibility(View.INVISIBLE);
                break;
            case 2:
                target1.setVisibility(View.VISIBLE);
                target2.setVisibility(View.VISIBLE);
                target1Highlight.setVisibility(View.INVISIBLE);
                target2Highlight.setVisibility(View.INVISIBLE);
                break;
            case 3:
                target1.setVisibility(View.VISIBLE);
                target2.setVisibility(View.VISIBLE);
                target3.setVisibility(View.VISIBLE);
                target1Highlight.setVisibility(View.INVISIBLE);
                target2Highlight.setVisibility(View.INVISIBLE);
                target3Highlight.setVisibility(View.INVISIBLE);
                break;
            case 4:
                target1.setVisibility(View.VISIBLE);
                target2.setVisibility(View.VISIBLE);
                target3.setVisibility(View.VISIBLE);
                target4.setVisibility(View.VISIBLE);
                target1Highlight.setVisibility(View.INVISIBLE);
                target2Highlight.setVisibility(View.INVISIBLE);
                target3Highlight.setVisibility(View.INVISIBLE);
                target4Highlight.setVisibility(View.INVISIBLE);
                break;
        }
        play(context,0,null,true, null, 0);
        currentStep=MainActivity.STEP_BEGIN;
    }

    static boolean getPlayingState() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    static void setStage(int s) {
        stage=s;
    }

    static int getStage() {return stage;}

    private int getNextResId() {
        while (MainActivity.sources[currentStep]==0) {
            currentStep++;
        }

        View view=mainAct.findViewById(previousPage);
        TextView txtStageDescTimer=(TextView) view.findViewById(R.id.txtStageDescTimer);
        TextView txtStepDesc=(TextView) view.findViewById(R.id.txtStepDesc);

        ImageView shot1=(ImageView) view.findViewById(R.id.shot1);
        ImageView shot2=(ImageView) view.findViewById(R.id.shot2);
        ImageView shot3=(ImageView) view.findViewById(R.id.shot3);
        ImageView shot4=(ImageView) view.findViewById(R.id.shot4);
        ImageView shot5=(ImageView) view.findViewById(R.id.shot5);
        ImageView shot6=(ImageView) view.findViewById(R.id.shot6);
        ImageView shot7=(ImageView) view.findViewById(R.id.shot7);
        ImageView shot8=(ImageView) view.findViewById(R.id.shot8);
        ImageView shot9=(ImageView) view.findViewById(R.id.shot9);
        ImageView shot10=(ImageView) view.findViewById(R.id.shot10);

        ImageView target1=(ImageView) view.findViewById(R.id.target1);
        ImageView target2=(ImageView) view.findViewById(R.id.target2);
        ImageView target3=(ImageView) view.findViewById(R.id.target3);
        ImageView target4=(ImageView) view.findViewById(R.id.target4);
        ImageView target1Highlight=(ImageView) view.findViewById(R.id.target1Highlight);
        ImageView target2Highlight=(ImageView) view.findViewById(R.id.target2Highlight);
        ImageView target3Highlight=(ImageView) view.findViewById(R.id.target3Highlight);
        ImageView target4Highlight=(ImageView) view.findViewById(R.id.target4Highlight);

        shot1.setVisibility(View.INVISIBLE);
        shot2.setVisibility(View.INVISIBLE);
        shot3.setVisibility(View.INVISIBLE);
        shot4.setVisibility(View.INVISIBLE);
        shot5.setVisibility(View.INVISIBLE);
        shot6.setVisibility(View.INVISIBLE);
        shot7.setVisibility(View.INVISIBLE);
        shot8.setVisibility(View.INVISIBLE);
        shot9.setVisibility(View.INVISIBLE);
        shot10.setVisibility(View.INVISIBLE);

        if (currentStep==MainActivity.STEP_STAGE_DESCRIPTION) {
            utils.hCountDownDescStage.post(utils.countDownDescStage);
        } else {
            utils.hCountDownDescStage.removeCallbacks(utils.countDownDescStage);

            switch (stage) {
                case 1:
                    target1.setVisibility(View.VISIBLE);
                    target1Highlight.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    target1.setVisibility(View.VISIBLE);
                    target2.setVisibility(View.VISIBLE);
                    target1Highlight.setVisibility(View.INVISIBLE);
                    target2Highlight.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    target1.setVisibility(View.VISIBLE);
                    target2.setVisibility(View.VISIBLE);
                    target3.setVisibility(View.VISIBLE);
                    target1Highlight.setVisibility(View.INVISIBLE);
                    target2Highlight.setVisibility(View.INVISIBLE);
                    target3Highlight.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    target1.setVisibility(View.VISIBLE);
                    target2.setVisibility(View.VISIBLE);
                    target3.setVisibility(View.VISIBLE);
                    target4.setVisibility(View.VISIBLE);
                    target1Highlight.setVisibility(View.INVISIBLE);
                    target2Highlight.setVisibility(View.INVISIBLE);
                    target3Highlight.setVisibility(View.INVISIBLE);
                    target4Highlight.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        if (currentStep==MainActivity.STEP_PREP_IN_PROGRESS) {
            utils.firstTime=true;
            utils.lastSec=0;
            utils.hCountDownPrepStage.post(utils.countDownPrepStage);
            txtStepDesc.setText(R.string.StepDescPreparing);
        } else if (currentStep==MainActivity.STEP_PREP_END) {
            utils.hCountDownPrepStage.removeCallbacks(utils.countDownPrepStage);
            txtStageDescTimer.setText("");
            txtStepDesc.setText("");
        }
        if (currentStep==MainActivity.STEP_FIRE_IN_PROGRESS) {
            utils.firstTime=true;
            utils.lastSec=0;
            utils.hCountDownFireStage.post(utils.countDownFireStage);
            txtStepDesc.setText(R.string.StepDescFireInProgress);
        } else if (currentStep==MainActivity.STEP_FIRE_END) {
            utils.hCountDownFireStage.removeCallbacks(utils.countDownFireStage);
            txtStageDescTimer.setText(R.string.StepDescCeaseFire);
        } else if (currentStep==MainActivity.STEP_DONE){
            txtStageDescTimer.setText("");
            txtStepDesc.setText("");
        }
        return MainActivity.sources[currentStep];
    }

    private static Context instance;

    private MediaPlayerSingleton(Context context){
        instance = context;
    }

    public static void destroyMediaPlayerSingleton(){
        mediaPlayerSingleton=null;
    }
}

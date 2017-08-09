package com.eightywon.aqtrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import java.io.IOException;

//class MediaPlayerSingleton extends MediaPlayer {
class MediaPlayerSingleton {
    private static MediaPlayerSingleton mediaPlayerSingleton;
    private static boolean isPlaying;

    public static MediaPlayerSingleton getInstance() {
        if (mediaPlayerSingleton==null) {
            mediaPlayerSingleton=new MediaPlayerSingleton();
        }
        return mediaPlayerSingleton;
    }

    private MediaPlayer mediaPlayer;

    public int getRemaining() {
        return Math.round((mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition())/1000);
    }

    public void play(Context context, int resId, boolean stop) {
        play(context, resId, null, null, stop);
    }

    public void play(Context context, int resId, MediaPlayer.OnCompletionListener completionListener) {
        play(context, resId, completionListener, null, false);
    }

    public void play(Context context, int resId, MediaPlayer.OnErrorListener errorListener) {
        play(context, resId, null, errorListener, false);
    }

    public void play(final Context context, int resId, MediaPlayer.OnCompletionListener completionListener, MediaPlayer.OnErrorListener errorListener, boolean stop) {
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
                    MediaPlayerSingleton.getNext(MainActivity.nextStep);
                    if (MainActivity.nextStep!=MainActivity.STEP_DONE) {
                        MediaPlayerSingleton.playNext(MainActivity.nextStep);
                        MediaPlayerSingleton.getInstance().play(context,MainActivity.audioFile,false);
                        if (MediaPlayerSingleton.getPlayingState()) {
                            //testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_stop", "drawable", getActivity().getPackageName()));
                        }
                    } else {
                        //testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play","drawable",getActivity().getPackageName()));
                    }
                }
            });
        }
    }

    static void stopPlaying(final Context context) {
        MediaPlayerSingleton.getInstance().play(context,MainActivity.audioFile,true);
        StageOneFragment.hCountDownFireStage.removeCallbacks(StageOneFragment.countDownFireStage);
        togglePlayingState();
    }

    static boolean getPlayingState() {
        return isPlaying;
    }

    static void togglePlayingState() {
        isPlaying = !isPlaying;
    }

    static void playNext(int currentStep) {

        MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();

        String announceInterval;
        switch (currentStep) {
            case MainActivity.STEP_BEGIN:
                //MainActivity.audioFile=MainActivity.STEP_STAGE_DESC_1_MP3;
                MainActivity.audioFile=R.raw.descstage1;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_STAGE_DESCRIPTION:
                //MainActivity.audioFile=MainActivity.STEP_STAGE_DESC_1_MP3;
                MainActivity.audioFile=R.raw.descstage1;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_START:
                //MainActivity.audioFile=MainActivity.STEP_STAGE_PREP_START_MP3;
                MainActivity.audioFile=R.raw.prepbegin;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                //MainActivity.audioFile=MainActivity.S15_MP3;
                MainActivity.audioFile=R.raw.s15;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_END:
                //MainActivity.audioFile=MainActivity.STEP_STAGE_PREP_END_MP3;
                MainActivity.audioFile=R.raw.prepend;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_LOAD:
                //MainActivity.audioFile=MainActivity.STEP_STAGE_LOAD_MP3;
                MainActivity.audioFile=R.raw.load10;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_START:
                //MainActivity.audioFile=MainActivity.STEP_STAGE_FIRE_START_MP3;
                MainActivity.audioFile=R.raw.fire;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                //MainActivity.audioFile=MainActivity.S10_MP3;
                MainActivity.audioFile=R.raw.s15;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                StageOneFragment.countDownFireStageInterval=Integer.parseInt(announceInterval);
                StageOneFragment.hCountDownFireStage.post(StageOneFragment.countDownFireStage);
                break;
            case MainActivity.STEP_FIRE_END:
                //MainActivity.audioFile=MainActivity.STEP_FIRE_END_MP3;
                MainActivity.audioFile=R.raw.cease;
                //setSource(mediaPlayer);
                //mediaPlayer.seekTo(0);
                //mediaPlayer.start();
                break;
            case MainActivity.STEP_DONE:
                //testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", getActivity().getPackageName()));
                MediaPlayerSingleton.togglePlayingState();
                break;
        }
    }

    /*
    private static void setSource(MediaPlayerSingleton mp) {
        AssetFileDescriptor afd = null;
        Context context=MainActivity.getContext();
        try {
            afd=context.getAssets().openFd(MainActivity.audioFile);
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
    */

    static void getNext(int lastStep) {
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
                StageOneFragment.hCountDownFireStage.removeCallbacks(StageOneFragment.countDownFireStage);
                MainActivity.nextStep=MainActivity.STEP_FIRE_END;
                break;
            case MainActivity.STEP_FIRE_END:
                MainActivity.nextStep=MainActivity.STEP_DONE;
                //MediaPlayerSingleton.togglePlayingState();
                break;
        }
    }
}

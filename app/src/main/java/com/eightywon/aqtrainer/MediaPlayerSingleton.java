package com.eightywon.aqtrainer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

class MediaPlayerSingleton extends MediaPlayer {
    private static MediaPlayerSingleton mediaPlayerSingleton;
    private static boolean isPlaying;

    static MediaPlayerSingleton getInstance() {
        if (mediaPlayerSingleton==null) {
            mediaPlayerSingleton=new MediaPlayerSingleton();
        }
        return mediaPlayerSingleton;
    }

    static void stopPlaying() {
        MediaPlayerSingleton mediaPlayer=MediaPlayerSingleton.getInstance();
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        mediaPlayer.stop();
        mediaPlayer.reset();
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
        Log.d("DEBUGGING","Playing Next: "+MainActivity.nextStep);
        Toast.makeText(MainActivity.getContext(), "playNext!", Toast.LENGTH_SHORT).show();
        switch (currentStep) {
            case MainActivity.STEP_BEGIN:
                MainActivity.audioFile=MainActivity.STEP_STAGE_DESC_1_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_STAGE_DESCRIPTION:
                MainActivity.audioFile=MainActivity.STEP_STAGE_DESC_1_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_START:
                MainActivity.audioFile=MainActivity.STEP_STAGE_PREP_START_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_IN_PROGRESS:
                MainActivity.audioFile=MainActivity.S15_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_PREP_END:
                MainActivity.audioFile=MainActivity.STEP_STAGE_PREP_END_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_LOAD:
                MainActivity.audioFile=MainActivity.STEP_STAGE_LOAD_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_START:
                MainActivity.audioFile=MainActivity.STEP_STAGE_FIRE_START_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_FIRE_IN_PROGRESS:
                MainActivity.audioFile=MainActivity.S10_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
                announceInterval=prefs.getString("lpSettingsAnnounceInterval","");
                announceInterval=announceInterval.substring(0,announceInterval.indexOf(" "));
                //countDownFireStageInterval=Integer.parseInt(announceInterval);
                //hCountDownFireStage.post(countDownFireStage);
                break;
            case MainActivity.STEP_FIRE_END:
                MainActivity.audioFile=MainActivity.STEP_FIRE_END_MP3;
                setSource(mediaPlayer);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case MainActivity.STEP_DONE:
                //testButton.setImageResource(getResources().getIdentifier("@android:drawable/ic_media_play", "drawable", getActivity().getPackageName()));
                MediaPlayerSingleton.togglePlayingState();
                break;
        }
    }

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
                //hCountDownFireStage.removeCallbacks(countDownFireStage);
                MainActivity.nextStep=MainActivity.STEP_FIRE_END;
                break;
            case MainActivity.STEP_FIRE_END:
                MainActivity.nextStep=MainActivity.STEP_DONE;
                //MediaPlayerSingleton.togglePlayingState();
                break;
        }
    }
}

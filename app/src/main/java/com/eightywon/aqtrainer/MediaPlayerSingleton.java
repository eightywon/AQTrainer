package com.eightywon.aqtrainer;

import android.media.MediaPlayer;

import java.io.IOException;

class MediaPlayerSingleton extends MediaPlayer {
    private static MediaPlayerSingleton mediaPlayerSingleton;
    private static boolean isPlaying;

    static MediaPlayerSingleton getInstance() {

            if (mediaPlayerSingleton==null) {
                mediaPlayerSingleton = new MediaPlayerSingleton();
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
}

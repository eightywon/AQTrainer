package com.eightywon.aqtrainer;

import android.media.MediaPlayer;

import java.io.IOException;

class MediaPlayerSingleton extends MediaPlayer {
    private static MediaPlayerSingleton mediaPlayerSingleton;

    static MediaPlayerSingleton getInstance() {

            if (mediaPlayerSingleton==null) {
                mediaPlayerSingleton = new MediaPlayerSingleton();
            }

        return mediaPlayerSingleton;
    }
}

package com.pengembangsebelah.stmmappxo.utils;

import android.media.MediaPlayer;

public interface AudioTask {
    interface Listener{
        void FailedPlay();
        void CanPlay(MediaPlayer mediaPlayer);
    }
}

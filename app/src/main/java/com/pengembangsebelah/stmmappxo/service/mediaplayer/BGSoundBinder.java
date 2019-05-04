package com.pengembangsebelah.stmmappxo.service.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Binder;
import android.util.Log;

import com.pengembangsebelah.stmmappxo.utils.AudioTask;
import com.pengembangsebelah.stmmappxo.utils.AudioTaskLoader;

public class BGSoundBinder extends Binder implements AudioTask.Listener {
    String TAG = "MySoundService";

    AudioTask.Listener listenerAudio;
    MediaPlayer mediaPlayer=null;
    boolean streaming;
    String audioUrl;

    Context context;

    boolean prepared=false;

    public boolean IsPlaying(){
        if(mediaPlayer!=null){
            return mediaPlayer.isPlaying();
        }else {
            return false;
        }
    }

    public void PlayAudio(AudioTask.Listener listenerAudio){
        this.listenerAudio=listenerAudio;
        if(prepared){
            mediaPlayer.start();
            listenerAudio.CanPlay(mediaPlayer);
        }else {
            Log.d(TAG, "PlayAudio: load");
            if(streaming){
                Log.d(TAG, "PlayAudio: Streaming");
                new AudioTaskLoader(audioUrl,this).execute(mediaPlayer);
            }else {
                new AudioTaskLoader(context,audioUrl,this).execute(mediaPlayer);
            }
        }
    }
    public void PlayAudio(){
        if(prepared){
            mediaPlayer.start();
            listenerAudio.CanPlay(mediaPlayer);
        }else {
            Log.d(TAG, "PlayAudio: load");
            if(streaming){
                Log.d(TAG, "PlayAudio: Streaming");
                new AudioTaskLoader(audioUrl,this).execute(mediaPlayer);
            }else {
                new AudioTaskLoader(context,audioUrl,this).execute(mediaPlayer);
            }
        }
    }
    public void StopAudio(){
        if(mediaPlayer!=null){
            if(IsPlaying()) {
                mediaPlayer.stop();
            }
            prepared=false;
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    public void SetAudioFileURL(String audiourl, boolean isStreaming, Context context){
        this.audioUrl=audiourl;
        this.streaming=isStreaming;
        this.context=context;
    }


    @Override
    public void FailedPlay() {
        listenerAudio.FailedPlay();
    }

    @Override
    public void CanPlay(MediaPlayer mediaPlayer) {
        listenerAudio.CanPlay(mediaPlayer);
        prepared = true;
        this.mediaPlayer=mediaPlayer;
    }

}

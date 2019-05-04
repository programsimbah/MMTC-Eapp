package com.pengembangsebelah.stmmappxo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class AudioTaskLoader extends AsyncTask<MediaPlayer,Void,MediaPlayer>{
    AudioTask.Listener listener;
    @SuppressLint("StaticFieldLeak")
    Context context;
    String uri="";
    String url="";

    public AudioTaskLoader(String url,AudioTask.Listener audioTaskListener) {
        super();
        this.listener=audioTaskListener;
        this.url=url;
    }

    public AudioTaskLoader(Context context,String uri, AudioTask.Listener audioTaskListener) {
        super();
        this.context=context;
        this.uri=uri;
        this.listener=audioTaskListener;
    }

    @Override
    protected MediaPlayer doInBackground(MediaPlayer... mediaPlayers) {
        try {
            mediaPlayers[0]= new MediaPlayer();
            if (TextUtils.isEmpty(uri)) {
                mediaPlayers[0].setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayers[0].setDataSource(url);
            } else {
                mediaPlayers[0].setDataSource(context, Uri.parse(uri));
            }
            mediaPlayers[0].prepare();
            return mediaPlayers[0];
        }catch (Exception e) {
            Log.d("AudioTask", "doInBackground err: "+e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(MediaPlayer mediaPlayer) {
        super.onPostExecute(mediaPlayer);
        if(mediaPlayer!=null){
            mediaPlayer.start();
            listener.CanPlay(mediaPlayer);
        }else {
            listener.FailedPlay();
        }
    }
}

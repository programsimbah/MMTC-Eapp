package com.pengembangsebelah.stmmappxo.core.mediapapa;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;
import com.pengembangsebelah.stmmappxo.R;

public class RadioService extends Service implements Player.EventListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "com.xstudio.stmm.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.xstudio.stmm.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.xstudio.stmm.ACTION_STOP";
    public static final String ACTION_NEXT = "com.xstudio.stmm.ACTION_NEXT";
    public static final String ACTION_PREVIEW = "com.xstudio.stmm.ACTION_PREV";
    public static final String ACTION_REPEAT = "com.xstudio.stmm.ACTION_REPEAT";

    private final IBinder iBinder = new LocalBinder();

    private Handler handler;
    private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private boolean onGoingCall = false;
    private TelephonyManager telephonyManager;

    private WifiManager.WifiLock wifiLock;

    private AudioManager audioManager;

    private MediaNotificationManager notificationManager;

    private String status;

    private String strAppName;
    private String strLiveBroadcast;
    private String streamUrl;

    public class LocalBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            pause();
        }
    };

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if(state == TelephonyManager.CALL_STATE_OFFHOOK
                    || state == TelephonyManager.CALL_STATE_RINGING){

                if(!isPlaying()) return;

                onGoingCall = true;
                stop();

            } else if (state == TelephonyManager.CALL_STATE_IDLE){

                if(!onGoingCall) return;

                onGoingCall = false;
                resume();
            }
        }
    };

    private MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();

            pause();
        }

        @Override
        public void onStop() {
            super.onStop();

            stop();

            notificationManager.cancelNotify();
        }

        @Override
        public void onPlay() {
            super.onPlay();

            resume();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        strAppName = getResources().getString(R.string.app_name);
        strLiveBroadcast = getResources().getString(R.string.live_broadcast);

        onGoingCall = false;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mcScPAmpLock");

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        handler = new Handler();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        exoPlayer.addListener(this);

        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        status = PlaybackStatus.IDLE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        if(TextUtils.isEmpty(action))
            return START_NOT_STICKY;

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

            stop();

            return START_NOT_STICKY;
        }

        if(action.equalsIgnoreCase(ACTION_PLAY)){

            transportControls.play();

        } else if(action.equalsIgnoreCase(ACTION_PAUSE)) {

            transportControls.pause();

        } else if(action.equalsIgnoreCase(ACTION_STOP)){

            transportControls.stop();

        } else if(action.equalsIgnoreCase(ACTION_REPEAT)){
            transportControls.setRepeatMode(0);
        }

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        if(status.equals(PlaybackStatus.IDLE))
            stopSelf();

        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(final Intent intent) {

    }

    @Override
    public void onDestroy() {

        pause();

        exoPlayer.release();
        exoPlayer.removeListener(this);

        if(telephonyManager != null)
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

        try {
            notificationManager.cancelNotify();
            mediaSession.release();
        }catch (NullPointerException ignored){

        }




        unregisterReceiver(becomingNoisyReceiver);

        super.onDestroy();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                exoPlayer.setVolume(0.8f);

                resume();

                break;

            case AudioManager.AUDIOFOCUS_LOSS:

                stop();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                if (isPlaying()) pause();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                if (isPlaying())
                    exoPlayer.setVolume(0.1f);

                break;
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case Player.STATE_BUFFERING:
                status = PlaybackStatus.LOADING;
                break;
            case Player.STATE_ENDED:
                status = PlaybackStatus.STOPPED;
                break;
            case Player.STATE_IDLE:
                status = PlaybackStatus.IDLE;
                break;
            case Player.STATE_READY:
                status = playWhenReady ? PlaybackStatus.PLAYING : PlaybackStatus.PAUSED;
                break;
            default:
                status = PlaybackStatus.IDLE;
                break;
        }

        if(!status.equals(PlaybackStatus.IDLE))
            notificationManager.startNotify(status);
        if(!status.equals(PlaybackStatus.STOPPED))
            notificationManager.startNotify(status);

        EventBus.getDefault().post(status);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        Log.d("FFFF", "onRepeatModeChanged: ss");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d("FFFF", "onRepeatModeChanged:4 ");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d("FFFF", "onRepeatModeChanged: 33");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        EventBus.getDefault().post(PlaybackStatus.ERROR);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        Log.d("FFFF", "onRepeatModeChanged: ");
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.d("FFFF", "onRepeatModeChanged: 33");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.d("FFFF", "onRepeatModeChanged: dd");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.d("FFFF", "onRepeatModeChanged: ew");
    }

    @Override
    public void onSeekProcessed() {
        Log.d("FFFF", "onRepeatModeChanged: wqew");
    }

    public void play(String streamUrl) {

        this.streamUrl = streamUrl;

        if (wifiLock != null && !wifiLock.isHeld()) {

            wifiLock.acquire();

        }

//        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(getUserAgent());

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, getUserAgent(), BANDWIDTH_METER);

        ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(streamUrl));

        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);
    }

    public void resume() {

        if(streamUrl != null)
            play(streamUrl);
    }

    public void pause() {

        exoPlayer.setPlayWhenReady(false);

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void stop() {

        exoPlayer.stop();

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void playOrPause(String url, String title, String subtitle, Bitmap albumArt){

        notificationManager = new MediaNotificationManager(this,title,subtitle,albumArt);

        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "...")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, strAppName )
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, strLiveBroadcast)
                .build());
        mediaSession.setCallback(mediasSessionCallback);


        paaa(url,subtitle);
    }
    public void playOrPause(String url,String title,String subtitle){

        notificationManager = new MediaNotificationManager(this,title,subtitle);

        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "...")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, strAppName )
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, strLiveBroadcast)
                .build());
        mediaSession.setCallback(mediasSessionCallback);


        paaa(url,subtitle);
    }
    void paaa(String url,String subtitu){
        if(streamUrl != null && streamUrl.equals(url)){

            if(!isPlaying()){

                play(streamUrl);
                Toast.makeText(this, "playing "+subtitu, Toast.LENGTH_SHORT).show();

            } else {
                pause();
                //Toast.makeText(this, "stop playing "+subtitu, Toast.LENGTH_SHORT).show();
            }

        } else {

            if(isPlaying()){

                pause();
                //Toast.makeText(this, "stop playing "+subtitu, Toast.LENGTH_SHORT).show();

            }

            play(url);
            Toast.makeText(this, "playing "+subtitu, Toast.LENGTH_SHORT).show();
        }
    }
    public String getStatus(){

        return status;
    }

    public MediaSessionCompat getMediaSession(){

        return mediaSession;
    }

    public boolean isPlaying(){

        return this.status.equals(PlaybackStatus.PLAYING);
    }

    private void wifiLockRelease(){

        if (wifiLock != null && wifiLock.isHeld()) {

            wifiLock.release();
        }
    }

    private String getUserAgent(){

        return Util.getUserAgent(this, getClass().getSimpleName());
    }
}

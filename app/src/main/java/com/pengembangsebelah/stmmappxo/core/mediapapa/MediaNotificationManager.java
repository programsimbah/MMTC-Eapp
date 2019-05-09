package com.pengembangsebelah.stmmappxo.core.mediapapa;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pengembangsebelah.stmmappxo.MainActivity;
import com.pengembangsebelah.stmmappxo.R;

public class MediaNotificationManager {
    public static final int NOTIFICATION_ID = 555;
    private final String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID";
    private final String PRIMARY_CHANNEL_NAME = "PRIMARY";

    private RadioService service;

    private String strAppName, strLiveBroadcast;

    Bitmap icone;

    private Resources resources;

    private NotificationManagerCompat notificationManager;

    public MediaNotificationManager(RadioService service,String strAppName, String strLiveBroadcast) {

        this.service = service;
        this.resources = service.getResources();

        this.strAppName = strAppName;
        this.strLiveBroadcast = strLiveBroadcast;
        this.icone = BitmapFactory.decodeResource(resources, R.drawable.icon);

        notificationManager = NotificationManagerCompat.from(service);
    }

    public MediaNotificationManager(RadioService service,String strAppName, String strLiveBroadcast, Bitmap icone) {

        this.service = service;
        this.resources = service.getResources();

        this.strAppName = strAppName;
        this.strLiveBroadcast = strLiveBroadcast;
        if(icone!=null) {
            this.icone = icone;
        }else {
            this.icone = BitmapFactory.decodeResource(resources, R.drawable.icon);
        }

        notificationManager = NotificationManagerCompat.from(service);
    }


    public void startNotify(String playbackStatus) {

        int icon = R.drawable.ic_stop_white_24dp;

        Intent playbackAction = new Intent(service, RadioService.class);
        playbackAction.setAction(RadioService.ACTION_PAUSE);
        PendingIntent action = PendingIntent.getService(service, 1, playbackAction, 0);

        if(playbackStatus.equals(PlaybackStatus.PAUSED)){

            icon = R.drawable.ic_play_arrow_white_24dp;
            playbackAction.setAction(RadioService.ACTION_PLAY);
            action = PendingIntent.getService(service, 2, playbackAction, 0);

        }
        Intent nextAction = new Intent(service, RadioService.class);
        nextAction.setAction(RadioService.ACTION_NEXT);
        PendingIntent actionn = PendingIntent.getService(service, 4, nextAction, 0);

        Intent prevAction = new Intent(service, RadioService.class);
        prevAction.setAction(RadioService.ACTION_PREVIEW);
        PendingIntent actionp = PendingIntent.getService(service, 5, prevAction, 0);

        Intent repeatAction = new Intent(service, RadioService.class);
        repeatAction.setAction(RadioService.ACTION_REPEAT);
        PendingIntent actionR = PendingIntent.getService(service, 6, repeatAction, 0);

        Intent stopIntent = new Intent(service, RadioService.class);
        stopIntent.setAction(RadioService.ACTION_STOP);
        PendingIntent stopAction = PendingIntent.getService(service, 3, stopIntent, 0);

        Intent intent = new Intent(service, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, 0);

        notificationManager.cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setAutoCancel(false)
                .setContentTitle(strLiveBroadcast)
                .setContentText(strAppName)
                .setLargeIcon(icone)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                .setSmallIcon(R.mipmap.ic_launcher)

//                .addAction(R.drawable.exo_controls_repeat_all, "repeat", actionR)
//                .addAction(R.drawable.exo_icon_previous, "prev", actionp)
                .addAction(icon, "pause", action)
//                .addAction(R.drawable.exo_icon_next, "next", actionn)
                .addAction(R.drawable.ic_clear_black_24dp, "stop", stopAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(service.getMediaSession().getSessionToken())
                        .setShowActionsInCompactView(0, 1)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(stopAction));

        service.startForeground(NOTIFICATION_ID, builder.build());
    }

    public void cancelNotify() {

        service.stopForeground(true);
    }
}

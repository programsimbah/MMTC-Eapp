package com.pengembangsebelah.stmmappxo.service.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class BGSoundService extends Service {

    BGSoundBinder bgSoundBinder=new BGSoundBinder();

    public BGSoundService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bgSoundBinder;
    }
}

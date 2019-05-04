package com.pengembangsebelah.stmmappxo.core.mediapapa;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.pengembangsebelah.stmmappxo.model.ProgramModel;
import com.pengembangsebelah.stmmappxo.model.ScheduleProgram;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class RadioManager {

    private static RadioManager instance = null;

    private static RadioService service;

    private Context context;

    private boolean serviceBound;

    private RadioManager(Context context) {
        this.context = context;
        serviceBound = false;
    }

    public static RadioManager with(Context context) {

        if (instance == null)
            instance = new RadioManager(context);

        return instance;
    }

    public static RadioService getService(){
        return service;
    }

//    public void playOrPause(String streamUrl){
//
//        service.playOrPause(streamUrl,title,subtitle);
//    }
    public void playOrPause(String streamUrl,String title,String subtitle){

        service.playOrPause(streamUrl,title,subtitle);
    }
    public void playOrPause(String streamUrl, String title, String subtitle, Bitmap albumart){

        service.playOrPause(streamUrl,title,subtitle,albumart);
    }

    String GetDay(Calendar calendar){
        String roti = String.format("%01d",calendar.get(Calendar.DAY_OF_WEEK));
        if(roti.equals("1")){
            return "6";
        }else if(Integer.valueOf(roti)>=2){
            Integer love = Integer.valueOf(roti)-2;
            return String.valueOf(love);
        }else {
            return roti;
        }
    }
    String GetTime(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+07:00");
        Calendar calendar = Calendar.getInstance(timeZone);
        String time =
                        GetDay(calendar)+
                        String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY))+
                        String.format("%02d",calendar.get(Calendar.MINUTE))+
                        String.format("%02d",calendar.get(Calendar.SECOND))+
                        String.format("%03d",calendar.get(Calendar.MILLISECOND));
        Log.d("MASIHADA", "GetTime: "+GetDay(calendar));
        return time;
    }

    public void playOrPause(String streamUrl, List<ScheduleProgram> scheduleProgramse){
        List<ProgramModel> prodal= scheduleProgramse.get(Integer.valueOf(GetTime().substring(0,1))).programModels;
        String jada = null;
        List<ProgramModel> pogaramfix= new ArrayList<>();
        for (int i = 0 ;i<prodal.size();i++){
            String end = prodal.get(i).endO;
            String start = prodal.get(i).startO;
            int totaS = (Integer.valueOf(start.substring( 0,2))*60)+Integer.valueOf(start.substring( 3,5));
            int totaE = (Integer.valueOf(end.substring(0,2))*60)+Integer.valueOf(end.substring( 3,5));

            int suneo = (Integer.valueOf(GetTime().substring(1,3))*60)+Integer.valueOf(GetTime().substring(3,5));

            if(totaS<suneo&&totaE>suneo){
                pogaramfix.add(prodal.get(i));
            }
        }
        if(pogaramfix.size()>0){
            service.playOrPause(streamUrl, "MMTC RADIO JOGJA",pogaramfix.get(0).program);
        }else {
            Toast.makeText(service, "off air", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isPlaying() {

        return service.isPlaying();
    }

    public void bind() {

        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if(service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {

        context.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {

            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            serviceBound = false;
        }
    };

}
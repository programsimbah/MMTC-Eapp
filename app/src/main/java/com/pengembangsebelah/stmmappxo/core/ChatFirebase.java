package com.pengembangsebelah.stmmappxo.core;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pengembangsebelah.stmmappxo.model.ChatData;
import com.pengembangsebelah.stmmappxo.model.Kontent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ChatFirebase {
    public interface ListenerGet{
        void GetFailed(String message);
        void GetSuccesed(List<ChatData> chatData);
    }
    public interface ListenerSet{
        void SetFailed(String message);
        void SetSuccesed();
    }

    //area jupuk
    ChatFirebase.ListenerGet listenerGet;
    public void GetData(ChatFirebase.ListenerGet listenerGet){
        this.listenerGet=listenerGet;
        GetDataFirebase();
    }
    public void GetDataFirebase(){
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_CHAT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                List<ChatData> schedulePrograms=new ArrayList<>();
                while (dataSnapshotIterator.hasNext()){
                    DataSnapshot dataSnapshot1=dataSnapshotIterator.next();
                    ChatData itm=dataSnapshot1.getValue(ChatData.class);
                    schedulePrograms.add(itm);
                }
                int maxI=500;
                if(schedulePrograms.size()>maxI) {
                    int lo=schedulePrograms.size()-maxI;
                    Log.d("SASAA", "onDataChange: "+lo+" "+schedulePrograms.size());
                    for (int r = 0; r < lo; r++) {
                        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_CHAT).child(schedulePrograms.get(r).date).removeValue();
                    }
                }

                Collections.sort(schedulePrograms, new Comparator<ChatData>() {
                    @Override
                    public int compare(ChatData o1, ChatData o2) {
                        return o2.date.compareTo(o1.date);
                    }
                });
                listenerGet.GetSuccesed(schedulePrograms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerGet.GetFailed(databaseError.getMessage());
            }
        });
    }

}

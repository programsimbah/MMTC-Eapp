package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.model.ProgramModel;
import com.pengembangsebelah.stmmappxo.model.ScheduleProgram;
import com.pengembangsebelah.stmmappxo.ui.adapter.ScheduleAdapterRecycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadioSchduleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public RadioSchduleFragment() {
    }

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    static List<ScheduleProgram> schedule;

    public static RadioSchduleFragment newInstance(int sectionNumber) {
        RadioSchduleFragment fragment = new RadioSchduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radio_schedule, container, false);
        init(rootView);
        //Update();
        if(schedule==null) {
            GetData();
        }else {
            Valid(schedule);
        }
        return rootView;
    }
    void Valid(List<ScheduleProgram> schedulePrograms){
        ScheduleAdapterRecycle scheduleAdapterRecycle = new ScheduleAdapterRecycle(schedulePrograms,getActivity());
        recyclerView.setAdapter(scheduleAdapterRecycle);
        scheduleAdapterRecycle.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        schedule = schedulePrograms;
    }

    void GetData(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_SCH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                List<ScheduleProgram> schedulePrograms=new ArrayList<>();
                while (dataSnapshotIterator.hasNext()){
                    DataSnapshot dataSnapshot1=dataSnapshotIterator.next();
                    ScheduleProgram itm=dataSnapshot1.getValue(ScheduleProgram.class);
                    schedulePrograms.add(itm);
                }

                Valid(schedulePrograms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    void Update(){
        List<ScheduleProgram> schedulePrograms = new ArrayList<>();
        for (int a=0;a<7;a++){
            String day="";
            List<ProgramModel> programModels = new ArrayList<>();
            if(a==0||a==1||a==2||a==3||a==4){
                if(a==0){
                    day="Senin";
                }else if(a==1){
                    day="Selasa";
                }else if(a==2){
                    day="Rabu";
                }else if(a==3){
                    day="Kamis";
                }else if(a==4){
                    day="Jumat";
                }
                for (int z=0;z<6;z++) {
                    String program;
                    String start;
                    String end;
                    if(z==0){
                        program="Morning Update";
                        start="06:00";
                        end="08:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==1){
                        program="Your Music Soul";
                        start="08:00";
                        end="12:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==2){
                        program="Jeda Siang";
                        start="12:00";
                        end="15:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==3){
                        program="Evening Break";
                        start="15:00";
                        end="17:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==4){
                        program="Inspirasi Senja";
                        start="17:00";
                        end="18:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==5){
                        program="Lampu Kota";
                        start="18:00";
                        end="21:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }
                }
            }else if(a==5){
                day="Sabtu";
                for (int z=0;z<9;z++) {
                    String program;
                    String start;
                    String end;
                    if(z==0){
                        program="Radios Magazine";
                        start="06:00";
                        end="08:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==1){
                        program="Movie Time";
                        start="08:00";
                        end="10:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==2){
                        program="Brodcastive";
                        start="10:00";
                        end="12:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==3){
                        program="Ngejazz Blues";
                        start="12:00";
                        end="14:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==4){
                        program="Namikaze";
                        start="14:00";
                        end="16:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==5){
                        program="Talk Aktive";
                        start="16:00";
                        end="18:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==6){
                        program="TopChart";
                        start="18:00";
                        end="19:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==7){
                        program="TopChart";
                        start="18:00";
                        end="19:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==8){
                        program="Ngobring";
                        start="19:00";
                        end="21:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }
                }

            }else if(a==6){
                day="Minggu";
                for (int z=0;z<8;z++) {
                    String program;
                    String start;
                    String end;
                    if(z==0){
                        program="Loker Olahraga";
                        start="06:00";
                        end="08:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==1){
                        program="Genita";
                        start="08:00";
                        end="10:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==2){
                        program="Rantang";
                        start="10:00";
                        end="12:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==3){
                        program="Raska";
                        start="12:00";
                        end="14:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==4){
                        program="K Now";
                        start="14:00";
                        end="16:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==5){
                        program="Chat Zhone";
                        start="16:00";
                        end="18:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==6){
                        program="Top Chart";
                        start="18:00";
                        end="19:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }else if(z==7){
                        program="All About Rock";
                        start="19:00";
                        end="21:00";
                        programModels.add(new ProgramModel(program,start,end));
                    }
                }
            }
            // eend
            schedulePrograms.add(new ScheduleProgram(day,programModels));
        }

        Log.d("SARIAWan", "Update: "+schedulePrograms.size()+"\n"+schedulePrograms.get(0).day);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_SCH).setValue(schedulePrograms).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    void init(View view){
        swipeRefreshLayout = view.findViewById(R.id.swipe_schedule_radio);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = view.findViewById(R.id.recyleview_schedule_radio);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onRefresh() {
        GetData();
    }
}

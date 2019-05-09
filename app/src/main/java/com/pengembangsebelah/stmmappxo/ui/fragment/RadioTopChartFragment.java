package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pengembangsebelah.stmmappxo.model.TopChartData;
import com.pengembangsebelah.stmmappxo.ui.adapter.TopChartAdapterRecycle;
import com.pengembangsebelah.stmmappxo.utils.JsonParse;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.model.TopAudioData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadioTopChartFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, JsonParse.Listener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    static List<TopChartData> topChartData=null;


    public RadioTopChartFragment() {
    }

    public static RadioTopChartFragment newInstance(int sectionNumber) {
        RadioTopChartFragment fragment = new RadioTopChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radio_topchart, container, false);
        init(rootView);
        return rootView;
    }

    void init(View view){
        swipeRefreshLayout = view.findViewById(R.id.swift_radio_topchart);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView=view.findViewById(R.id.recycle_view_radio_topchart);
        if(topChartData==null) {
            GetData();
        }else {
            Buat(topChartData);
        }
    }

    void Buat(List<TopChartData> topChartData){

        this.swipeRefreshLayout.setRefreshing(false);

        TopChartAdapterRecycle adapterRecycle = new TopChartAdapterRecycle(topChartData,getActivity());
        this.recyclerView.setAdapter(adapterRecycle);
        adapterRecycle.notifyDataSetChanged();
    }

    List<TopChartData> DebugtopChartData;
    void ExcuteChart(List<TopChartData> topChartData){
        totalCount=topChartData.size();
        DebugtopChartData=topChartData;
        for (int i=0;i<topChartData.size();i++){
            JsonParse jsonParse = new JsonParse(i);
            jsonParse.SetUrl(topChartData.get(i).url);
            jsonParse.SetListenter(this);
            jsonParse.execute();
        }
    }

    void GetData(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_TOP_CHART).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // TopChartData topChartData = dataSnapshot.getValue(TopChartData.class);

//                Item item=dataSnapshot.getValue(Item.class);

                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                List<TopChartData> itemGets = new ArrayList<>();
                while (dataSnapshotIterator.hasNext()) {
                    DataSnapshot dataSnapshot1 = dataSnapshotIterator.next();
                    TopChartData item1 = dataSnapshot1.getValue(TopChartData.class);
                    itemGets.add(item1);
                }

                ExcuteChart(itemGets);
//                Log.d("FEED FIREBASE", "onDataChange: "+dataSnapshot.getValue()+"\n"+item.title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void DataCovered(){
        if(DebugtopChartData!=null) {
            this.topChartData = DebugtopChartData;
            Buat(topChartData);
        }
        countFinish=0;
    }

    @Override
    public void onRefresh() {
        GetData();
    }

    int totalCount=0;
    int countFinish=0;
    @Override
    public void OnFinish(String data, JSONArray object, int code) {
        List<TopAudioData> topAudioDataList;
        topAudioDataList=new ArrayList<>();
        if(object!=null) {
            for (int i = 0; i < object.length(); i++) {

                String _title;
                String _subtitle;
                String _audio;
                String _cover;
                try {
                    JSONObject jsonObject = (JSONObject) object.get(i);
                    Log.d("RADIO", "OnFinish: " + jsonObject.getString("title"));
                    _title = jsonObject.getString("title");
                    _audio = jsonObject.getString("audio");
                    _subtitle = jsonObject.getString("subtitle");
                    _cover = jsonObject.getString("cover");
                    TopAudioData topAudioDT = new TopAudioData(_title, _subtitle, _audio, _cover);
                    topAudioDataList.add(topAudioDT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        DebugtopChartData.get(code).topAudioData = topAudioDataList;
        countFinish+=1;
        if(countFinish==totalCount){
            DataCovered();
        }
    }
}

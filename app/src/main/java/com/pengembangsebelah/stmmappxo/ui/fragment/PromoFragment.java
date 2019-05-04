package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.model.PromoItem;
import com.pengembangsebelah.stmmappxo.ui.adapter.PromoAdapterRecyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PromoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_SECTION_NUMBER = "section_number";

    static List<PromoItem> promDatae=new ArrayList<>();

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public PromoFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PromoFragment newInstance(int sectionNumber) {
        PromoFragment fragment = new PromoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prom, container, false);
        init(rootView);
        return rootView;
    }

    void init(View view){
        swipeRefreshLayout= view.findViewById(R.id.swift_promo_ac);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView=view.findViewById(R.id.recycle_promo_ac);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        GetData();
        //Update();
    }

    void Valid(List<PromoItem> data){
        swipeRefreshLayout.setRefreshing(false);
        promDatae=data;

        PromoAdapterRecyle adapterRecyle=new PromoAdapterRecyle(promDatae,getActivity());
        recyclerView.setAdapter(adapterRecyle);
        adapterRecyle.notifyDataSetChanged();
    }

    void GetData(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_MO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                List<PromoItem> schedulePrograms=new ArrayList<>();
                while (dataSnapshotIterator.hasNext()){
                    DataSnapshot dataSnapshot1=dataSnapshotIterator.next();
                    PromoItem itm=dataSnapshot1.getValue(PromoItem.class);
                    schedulePrograms.add(itm);
                }
                Collections.shuffle(schedulePrograms);
                Valid(schedulePrograms);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.promo_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        GetData();
    }
}

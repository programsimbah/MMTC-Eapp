package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.model.PromotionData;
import com.pengembangsebelah.stmmappxo.ui.adapter.HomeAdapterRecyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public HomeFragment() {
    }

    static List<PromotionData> promData=new ArrayList<>();

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        init(rootView);
        return rootView;
    }

    void init(View view){
        swipeRefreshLayout= view.findViewById(R.id.swift_home_ac);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView=view.findViewById(R.id.recycle_home_ac);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        GetData();
        //Update();
    }

    void Valid(List<PromotionData> data){
        swipeRefreshLayout.setRefreshing(false);
        promData=data;

        HomeAdapterRecyle adapterRecyle=new HomeAdapterRecyle(promData,getActivity());
        recyclerView.setAdapter(adapterRecyle);
        adapterRecyle.notifyDataSetChanged();
    }

    void GetData(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_PROM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                List<PromotionData> schedulePrograms=new ArrayList<>();
                while (dataSnapshotIterator.hasNext()){
                    DataSnapshot dataSnapshot1=dataSnapshotIterator.next();
                    PromotionData itm=dataSnapshot1.getValue(PromotionData.class);
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

    void Update(){
        List<PromotionData> promotionDataList= new ArrayList<>();

        PromotionData promotionData0=new PromotionData("Danggers Ollie","There's a boy named Ollie. He loves playing skateboard. He wanted to get through any challenge so that he could become a professional skater and so some famous brands would glance at him.","https://play.google.com/store/apps/details?id=com.Danggers.Ollie","uy",0);
        PromotionData promotionData1=new PromotionData("Bubble Zoo Collect","Bubble Zoo Collect is educational game-puzzle that provide challenges to players select and play different kinds of animal characters that have been provided. The player can choose a character and then search for and collect the letters to form a karate played.","https://play.google.com/store/apps/details?id=com.MMTC.BubbleZooCollect","uy",0);
        PromotionData promotionData2=new PromotionData("Box Stack","Educational game that combines the gameplay towerblock and slide on the screen. players answer the question with a slide box and if the answer is correct box will fall and pile up until it reaches a certain height in each of each level","https://play.google.com/store/apps/details?id=com.boxstack.id","uy",0);
        PromotionData promotionData3=new PromotionData("The Wyndis Game","three match game with the theme of yogurt, which work together to promote products wyndis yogurt","https://play.google.com/store/apps/details?id=com.the.wyndis","uy",0);
        PromotionData promotionData4=new PromotionData("D'catch ( Durian Catch)","Game D'Catch is a casual game, this game is inspired because many sellers of durian, durian fruit which is rarely harvested while demand from consumers very much,","https://play.google.com/store/apps/details?id=com.titanium.dcatchgame","uy",0);
        PromotionData promotionData5=new PromotionData("Green Tank Garuda Defender","Game Green Tank Garuda Defender is a tank game yangdiciptakan eagle protector so that we can protect the eagle. Destroy all enemy tanks and protect Garuda.","https://play.google.com/store/apps/details?id=com.SleepingSmoke.GreenTankGarudaDefender","uy",0);
        PromotionData promotionData6=new PromotionData("Amazepong Bali 1.0","Amazepong made specifically for the Android platform, this game is a casual game genre with the addition of features from its predecessor.Amazepong Bali can be played by two people that","https://play.google.com/store/apps/details?id=com.HappyMommy.AmazePong","uy",0);
        PromotionData promotionData7=new PromotionData("Cosmo Sheep","Cosmo Sheep is an endless game run 2,5D which tells about the adventures of a sheep pengelanan Jalu space.","https://play.google.com/store/apps/details?id=com.asd.kelana","uy",0);
        PromotionData promotionData8=new PromotionData("Ultimate Soccer","The Ultimate Soccer game is a casual genre game designed to introduce the game football in the form of a two-dimensional Endless Run game with the avoidance of character movements","https://play.google.com/store/apps/details?id=com.GOOD.ULTIMATE","uy",0);
        PromotionData promotionData9=new PromotionData("Let's Go Recycle","Garbage is a common problem for people in the world. Every day people throw garbage, the which some of them can be recycled and some of them can be composted. This game Teaches us to understand the common categories of waste by sorting waste. Time to learn about recycling waste in-game Let's Go Recycle. Learning the four categories of waste sorting with fun contents in the game.","https://play.google.com/store/apps/details?id=com.facebook.letsgorecycle","uy",0);
        PromotionData promotionData10=new PromotionData("Geprek Express","This game is a culinary game, players must match orders with food sold at a predetermined time, players must be able to reach the target points before time runs out.","https://play.google.com/store/apps/details?id=com.AangCompany.GeprekExpress","uy",0);
        PromotionData promotionData11=new PromotionData("Plintheng Semar","Game Plintheng Semar is a 2D Platformer game Adventure","https://play.google.com/store/apps/details?id=com.fai.pe","uy",0);
        PromotionData promotionData12=new PromotionData("Yu Sri Adventure","An adventure game with side-scrolling gameplay that explores the tourist locations in Gunungkidul Indonesia. help Yu Sri adventure explore tourism in Gunungkidul by passing every hurdle that exists","https://play.google.com/store/apps/details?id=com.MS.Yu","uy",0);
        PromotionData promotionData13=new PromotionData("Mucu Defense","Mucu Defense is a Tower Defense game. Players can builds the towers and makes their own routes to defeat the monsters. The goal of this game is to defeat all existing monster before reaching the red portal. Letting many monsters get into the red portal will result in game over.","https://play.google.com/store/apps/details?id=com.GaniPrakoso.MucuDefense","uy",0);
        PromotionData promotionData14=new PromotionData("Laksa The Game","Laksa The Game is a time management game with a food theme, which is aimed at promoting the culinary and cultural tourism of Tangerang city","https://play.google.com/store/apps/details?id=com.Bernama.Laksa","uy",0);
        PromotionData promotionData15=new PromotionData("I'm Starving","I'm Starving is a casual puzzle game genre that tells an alien who is looking for snacks in Indonesia. Interest Players can go to his space flight in a way to roll, past obstacles and puzzles that exist, such as moving past the barbed spears, paving the way closed, and found the pattern right moves to get his space flight. If the player is hit by a spear character will die and the game will be automatically restarted from the beginning.","https://play.google.com/store/apps/details?id=com.practicestudios.imstarving","uy",0);
        PromotionData promotionData16=new PromotionData("Mammal Land","Mammal Land game presents 3 types of modes that can be played and increase children's knowledge about mammals around. By playing this game you can play quizzes, match cards, and even feed animals.","https://play.google.com/store/apps/details?id=com.MMTC.MammalLand","uy",0);
        PromotionData promotionData17=new PromotionData("Duel Ball","Duel Game Ball was adapted from a folk game Indonesia, marbles, games area using a small ball and a play area on the ground. Game 'Duel Ball' will create","https://play.google.com/store/apps/details?id=com.Titanium.DuelBall","uy",0);
        PromotionData promotionData18=new PromotionData("Playon","the Bejo must get diamonds and pass obstacles in an arena, from rice fields, mountains to the countryside. Bejo must run by following the rhythm or every turn.","https://play.google.com/store/apps/details?id=com.Dep.Dep","uy",0);
        PromotionData promotionData19=new PromotionData("Markobar The Game","You act as Seller Martabak in Stand MARKOBAR. You have to quickly serve orders with taste in accordance with the wishes of each customer","https://play.google.com/store/apps/details?id=com.infinitebeyond.markobarthegame","uy",0);

        promotionDataList.add(promotionData0);
        promotionDataList.add(promotionData1);
        promotionDataList.add(promotionData2);
        promotionDataList.add(promotionData3);
        promotionDataList.add(promotionData4);
        promotionDataList.add(promotionData5);
        promotionDataList.add(promotionData6);
        promotionDataList.add(promotionData7);
        promotionDataList.add(promotionData8);
        promotionDataList.add(promotionData9);
        promotionDataList.add(promotionData10);
        promotionDataList.add(promotionData11);
        promotionDataList.add(promotionData12);
        promotionDataList.add(promotionData13);
        promotionDataList.add(promotionData14);
        promotionDataList.add(promotionData15);
        promotionDataList.add(promotionData16);
        promotionDataList.add(promotionData17);
        promotionDataList.add(promotionData18);
        promotionDataList.add(promotionData19);



        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Kontent.ARG_RADIO_PROM).setValue(promotionDataList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void onRefresh() {
        GetData();
    }
}

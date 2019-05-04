package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pengembangsebelah.stmmappxo.MainActivity;
import com.pengembangsebelah.stmmappxo.core.ChatFirebase;
import com.pengembangsebelah.stmmappxo.model.ChatData;
import com.pengembangsebelah.stmmappxo.ui.adapter.ChatAdapterRecyle;
import com.pengembangsebelah.stmmappxo.utils.FloatingButtonMoveable;
import com.pengembangsebelah.stmmappxo.R;

import java.util.List;
import java.util.Objects;

public class RadioChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ChatFirebase.ListenerGet {
    private static final String ARG_SECTION_NUMBER = "section_number";
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    FloatingButtonMoveable sendi;

    public RadioChatFragment() {
    }

    public static RadioChatFragment newInstance(int sectionNumber) {
        RadioChatFragment fragment = new RadioChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radio_chat, container, false);
        init(rootView);
        return rootView;
    }

    static List<ChatData> chatData;

    void init(View view){
        swipeRefreshLayout = view.findViewById(R.id.swipe_chat_radio);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView=view.findViewById(R.id.recyleview_chat_radio);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        sendi = view.findViewById(R.id.sendi);
        sendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendiP();
            }
        });

        if(chatData==null) {
            GetData();
        }else {
            GetSuccesed(chatData);
        }
    }

    void GetData(){
        swipeRefreshLayout.setRefreshing(true);
        ChatFirebase chatFirebase=new ChatFirebase();
        chatFirebase.GetData(this);
    }

    @Override
    public void onRefresh() {
        GetData();
    }

    @Override
    public void GetFailed(String message) {
        Log.d("Chat Firebase Error", "GetFailed: "+message);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void GetSuccesed(List<ChatData> chatData) {
        ChatAdapterRecyle chatAdapterRecyle=new ChatAdapterRecyle(chatData,getActivity());
        recyclerView.setAdapter(chatAdapterRecyle);
        swipeRefreshLayout.setRefreshing(false);
    }

    void SendiP(){
        ((MainActivity) Objects.requireNonNull(getActivity())).Message();
    }
}

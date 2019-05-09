package com.pengembangsebelah.stmmappxo.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.leocardz.link.preview.library.SourceContent;
import com.pengembangsebelah.stmmappxo.model.Item;
import com.pengembangsebelah.stmmappxo.model.RSSObject;
import com.pengembangsebelah.stmmappxo.ui.activity.WebOpenActivity;
import com.pengembangsebelah.stmmappxo.utils.HTTPDataHandler;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.ui.adapter.NewsAdapterRecyle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadioNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_SECTION_NUMBER = "section_number";
    static RSSObject rssObject=null;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout;

    //feedUpu
    static List<Item> feedsItem;

    //dsds
    ImageView bannerLargeView;
    RelativeLayout bannerLarge;
    TextView titleBannerLarge;
    TextView descBannerLarge;

    public RadioNewsFragment() {
    }

    public static RadioNewsFragment newInstance(int sectionNumber) {
        RadioNewsFragment fragment = new RadioNewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radio_news, container, false);
        Init(rootView);
        return rootView;
    }

    void Init(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyleview_news);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        swipeRefreshLayout = view.findViewById(R.id.refresh_news);
        swipeRefreshLayout.setOnRefreshListener(this);

        linearLayout = view.findViewById(R.id.layout_radio_news);

        //adsdasd
        bannerLarge = view.findViewById(R.id.layout_banner_large);
        bannerLargeView=view.findViewById(R.id.banner_large);
        titleBannerLarge=view.findViewById(R.id.title_banner_large);
        descBannerLarge=view.findViewById(R.id.desc_banner_large);

        if(rssObject==null) {
            Refresh();
        }else {
            SetData(rssObject);
        }
    }

    void Refresh(){
        swipeRefreshLayout.setRefreshing(true);
        if(rssObject==null) {
            linearLayout.setVisibility(View.GONE);
        }
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child("app").child("testrss").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                swipeRefreshLayout.setRefreshing(false);
                if (!dataSnapshot.getValue().toString().isEmpty()) {
                    loadRSS(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    private void loadRSS(String rssLink) {
        AsyncTask<String,String,String> loadRSSAsync = new AsyncTask<String, String, String>() {


            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return  result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected void onPostExecute(String s) {
                rssObject = new Gson().fromJson(s,RSSObject.class);
                Log.d("HTTPRSS", "onPostExecute: j "+rssObject.getItems().get(0).thumbnail.toString());
                SetData(rssObject);
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(rssLink);
        loadRSSAsync.execute(url_get_data.toString());
    }

    @Override
    public void onRefresh() {
        Refresh();
    }

    void SetData(RSSObject rssOb){
        Lunao(rssOb);
    }
    void ChildSucces(final Item bannerItem, List<Item> items){
        titleBannerLarge.setText(bannerItem.title);
        descBannerLarge.setText(bannerItem.description);
        try {
            Glide.with(getActivity())
                    .load(bannerItem.thumbnail)
                    .into(bannerLargeView);
            bannerLarge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SourceContent sourceContent=new SourceContent();
                    sourceContent.setTitle(bannerItem.title);
                    sourceContent.setUrl(bannerItem.link);
                    sourceContent.setFinalUrl(bannerItem.link);
                    List<String> urisd = new ArrayList<>();
                    urisd.add(bannerItem.thumbnail);
                    sourceContent.setImages(urisd);
                    WebOpenActivity.StartActivity(getActivity(),sourceContent);
                    //Toast.makeText(getActivity(), "Open "+bannerItem.link, Toast.LENGTH_SHORT).show();
                }
            });

        }catch (NullPointerException e){

        }
        if(items!=null&&items.size()>0) {
            feedsItem = items;
            Log.d("ChildTest", "ChildSucces: "+feedsItem.get(0).title);
        }

        NewsAdapterRecyle adapterRecyle = new NewsAdapterRecyle(feedsItem,getActivity());
        recyclerView.setAdapter(adapterRecyle);
        adapterRecyle.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
        if(linearLayout.getVisibility()==View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
        }

    }

    static Item lama;
    void Lunao(RSSObject rssObject){
        lama = new Item(rssObject.getItems().get(0).title,rssObject.getItems().get(0).link,rssObject.getItems().get(0).thumbnail,rssObject.getItems().get(0).description);
        if(lama.thumbnail.equals("")){
            lama.thumbnail="http://via.placeholder.com/350x150";
        }
        FirebaseDatabase.getInstance().getReference().child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_FEED).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item item=dataSnapshot.getValue(Item.class);

                Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.child("items").getChildren().iterator();
                List<Item> itemGets = new ArrayList<>();
                while (dataSnapshotIterator.hasNext()){
                    DataSnapshot dataSnapshot1=dataSnapshotIterator.next();
                    Item item1=dataSnapshot1.getValue(Item.class);
                    itemGets.add(item1);
                }
                feedsItem=itemGets;
                Log.d("CHILD FIRE", "onDataChange: "+feedsItem.size());
               // baru[0] =item;
                if(item!=null) {
                    if (item.title.equals(lama.title) && item.link.equals(lama.link)) {
                        ChildSucces(item,feedsItem);
                        Log.d("FEED CEK EQUAL", "onDataChange: ");
                    } else {
                        lama=item;
                        itemGets.add(item);
                        UpDatany(item, itemGets);
                    }
                }else {
                    item=lama;
                    itemGets.add(item);
                    UpDatany(item, itemGets);
                }
                Log.d("FEED FIREBASE", "onDataChange: "+dataSnapshot.getValue()+"\n"+item.title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ChildSucces(null,null);
            }
        });



    }

    void UpDatany(final Item item, final List<Item> itemGets){
        feedsItem=itemGets;
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_FEED).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        databaseReference.child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_FEED).child("items").setValue(itemGets).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("FEED FIREBASE", "onComplete: add child" + item.title);
                ChildSucces(item,feedsItem);
            }
        });
    }
}

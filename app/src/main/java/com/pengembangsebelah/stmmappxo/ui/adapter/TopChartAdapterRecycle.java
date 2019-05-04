package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pengembangsebelah.stmmappxo.MainActivity;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.TopAudioData;
import com.pengembangsebelah.stmmappxo.model.TopChartData;

import java.util.List;

class TopChartAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    RecyclerView recyclerView;
    TextView title;

    public TopChartAdapterHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.title_item_horizontal_view);
        recyclerView=itemView.findViewById(R.id.recyleview_item_horizontal_view);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

public class TopChartAdapterRecycle extends RecyclerView.Adapter<TopChartAdapterHolder>
{
    List<TopChartData> topChartData;
    Context context;
    public TopChartAdapterRecycle(List<TopChartData> topChartData, Context context) {
        Log.d("KAMBING", "TopChartAdapterHolder: Masuk");
        this.topChartData=topChartData;
        this.context=context;
    }

    @NonNull
    @Override
    public TopChartAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_view,viewGroup,false);
        return new TopChartAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopChartAdapterHolder topChartAdapterHolder, int i) {
        topChartAdapterHolder.title.setText(topChartData.get(i).title);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        topChartAdapterHolder.recyclerView.setLayoutManager(layoutManager);

        AlbumAdapterRecycle adapterRecycle = new AlbumAdapterRecycle(topChartData.get(i).topAudioData ,context);
        topChartAdapterHolder.recyclerView.setAdapter(adapterRecycle);
//        adapterRecycle.setHasStableIds(true);

    }

    @Override
    public int getItemCount() {
        if(topChartData!=null) {
            return topChartData.size();
        }else return 0;
    }
}

class AlbumAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

    ImageView cover;
    TextView title;
    TextView number;
    AlbumOnClik albumOnClik;

    public AlbumAdapterHolder(@NonNull View itemView) {
        super(itemView);
        cover = itemView.findViewById(R.id.item_preview_album_thumbnail);
        title = itemView.findViewById(R.id.item_album_title);
        number = itemView.findViewById(R.id.item_album_numb);
        itemView.setOnClickListener(this);
    }

    public void SetOnClick(AlbumOnClik albumOnClik){
        this.albumOnClik=albumOnClik;
    }
    @Override
    public void onClick(View v) {
        albumOnClik.OnClick();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
class AlbumAdapterRecycle extends RecyclerView.Adapter<AlbumAdapterHolder> {
    List<TopAudioData> audioData;
    Context context;
    List<ImageView> imageViewList;

    static InterstitialAd interstitialAd;

    public AlbumAdapterRecycle(List<TopAudioData> audioData,Context context) {
        this.audioData=audioData;
        this.context=context;
    }

    @NonNull
    @Override
    public AlbumAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_audio,viewGroup,false);
        return new AlbumAdapterHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final AlbumAdapterHolder albumAdapterHolder, final int i) {
        int pos = i+1;
        albumAdapterHolder.number.setText(String.valueOf(pos));
        albumAdapterHolder.title.setText(audioData.get(i).title);
        final RequestOptions requestOptions=new RequestOptions().fitCenter().override(100,100);
        //Glide.with(context).load(audioData.get(i).cover).apply(requestOptions).into(albumAdapterHolder.cover);
        albumAdapterHolder.SetOnClick(new AlbumOnClik() {
            @Override
            public void OnClick() {
                Toast.makeText(context, "please wait loading data, refresh it", Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(context).load(audioData.get(i).cover).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("MISKAL", "onLoadFailed: ");
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("MISKAL", "onLoadReady: ");
                albumAdapterHolder.cover.setImageDrawable(resource);
                albumAdapterHolder.SetOnClick(new AlbumOnClik() {
                    @Override
                    public void OnClick() {
                        BitmapDrawable drawable=(BitmapDrawable) resource;
                        Bitmap bitmap = drawable.getBitmap();
                        MainActivity.radioManager.playOrPause(audioData.get(i).audio, audioData.get(i).subtitle, audioData.get(i).title, bitmap);

                        Bundle extra = new Bundle();
                        extra.putString("npa","1");

                        interstitialAd = new InterstitialAd(context);
                        interstitialAd.setAdUnitId(context.getString(R.string.interstitial_open_album));
                        interstitialAd.loadAd(new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extra).addTestDevice(context.getString(R.string.device_test)).build());
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdLoaded() {
                                final ProgressDialog progressDialog = new ProgressDialog(context);
                                progressDialog.setTitle("Appear ads");
                                progressDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();

                                new CountDownTimer(3000,1000){

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        progressDialog.setMessage(millisUntilFinished/1000+" s");
                                    }

                                    @Override
                                    public void onFinish() {
                                        interstitialAd.show();
                                        progressDialog.dismiss();
                                    }
                                }.start();
                                super.onAdLoaded();
                            }
                        });

                    }
                });
                return true;
            }
        }).into(albumAdapterHolder.cover);

    }

    @Override
    public int getItemCount() {
        if(audioData!=null) {
            return audioData.size();
        }else return 0;
    }
}

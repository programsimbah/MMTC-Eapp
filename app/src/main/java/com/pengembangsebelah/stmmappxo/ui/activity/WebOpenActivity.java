package com.pengembangsebelah.stmmappxo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.leocardz.link.preview.library.SourceContent;
import com.pengembangsebelah.stmmappxo.utils.Redirecter;
import com.pengembangsebelah.stmmappxo.R;

import java.util.Objects;

public class WebOpenActivity extends AppCompatActivity {

    static SourceContent sourceContent;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_open);

        Bundle extra = new Bundle();
        extra.putString("npa","1");

        adView = findViewById(R.id.ad_view_web_open);
        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extra).addTestDevice(getString(R.string.device_test)).build();
        adView.loadAd(adRequest);

        final ImageView imageView=findViewById(R.id.image_tolbar_view);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        //TextView textView = findViewById(R.id.web_article_tv);
        //WebView webView = findViewById(R.id.web_view_artikel);

        final Button button = findViewById(R.id.button_artikel_next);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            Glide.with(this).load(sourceContent.getImages().get(0)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    Palette.generateAsync(((BitmapDrawable) resource).getBitmap(), new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            if (palette != null) {
                                Palette.Swatch vib = palette.getVibrantSwatch();
                                try {
                                    int mutedCollor = Objects.requireNonNull(palette.getVibrantSwatch()).getRgb();
                                    if (vib != null) {
                                        collapsingToolbarLayout.setBackgroundColor(mutedCollor);
                                        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(mutedCollor));
                                        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedCollor));
//                                button.setBackgroundColor(mutedCollor);
                                    }
                                } catch (NullPointerException ignored) {

                                }
                            }
                        }
                    });
                    imageView.setImageDrawable(((BitmapDrawable) resource));
                    return true;
                }
            }).into(imageView);
        }catch (IndexOutOfBoundsException ignore){

        }
        if(sourceContent.getTitle()!=null) {
            toolbar.setTitle(sourceContent.getTitle());
        }else {
            toolbar.setTitle(getString(R.string.app_name));
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //textView.setText(sourceContent.getHtmlCode());

//        webView.loadData(sourceContent.getHtmlCode(),"text/html","UTF-8");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirecter redirecter = new Redirecter();
                startActivity(redirecter.GetIntentWebsite(WebOpenActivity.this,sourceContent.getUrl()));
            }
        });

        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                button.setEnabled(false);
                button.setText("plese wait "+millisUntilFinished/1000+" s");
            }

            @Override
            public void onFinish() {
                button.setEnabled(true);
                button.setText("Open Website");
            }
        }.start();

    }

    public static void StartActivity(Context conte, SourceContent sourceContents){
        Intent intent = new Intent(conte,WebOpenActivity.class);
        sourceContent=sourceContents;
        conte.startActivity(intent);
    }
}

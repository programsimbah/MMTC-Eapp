package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leocardz.link.preview.library.SourceContent;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.PromoItem;
import com.pengembangsebelah.stmmappxo.ui.activity.WebOpenActivity;
import com.pengembangsebelah.stmmappxo.utils.OnClickListenerCustom;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


class PromoAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    ImageView preview;
    TextView title;
    CardView cardView;
    OnClickListenerCustom.Item clik;

    public PromoAdapterHolder(@NonNull View itemView) {
        super(itemView);
        preview = itemView.findViewById(R.id.image_view_promo);
        title=itemView.findViewById(R.id.text_title_promo);
        cardView=itemView.findViewById(R.id.layoute_promo);
        itemView.setOnClickListener(this);
    }

    public void SetOnClick(OnClickListenerCustom.Item onclickListener){
        this.clik=onclickListener;
    }

    @Override
    public void onClick(View v) {
        clik.OnClick();
    }

    @Override
    public boolean onLongClick(View v) {
        clik.LongClick();
        return true;
    }
}

public class PromoAdapterRecyle extends RecyclerView.Adapter<PromoAdapterHolder>{

    List<PromoItem> items;
    Context context;
    public PromoAdapterRecyle(List<PromoItem> items, Context context) {
        this.items=items;
        this.context=context;
    }

    @NonNull
    @Override
    public PromoAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_promo,viewGroup,false);
        return new PromoAdapterHolder(view);
    }

    String yuyu(String s){
        int lt = 78;
        if(s.toCharArray().length>lt){
            return s.substring(0,lt)+"..";
        }else {
            return s;
        }


    }
    @Override
    public void onBindViewHolder(@NonNull final PromoAdapterHolder newsAdapterHolder, final int i) {
        newsAdapterHolder.title.setText(items.get(i).title);
        newsAdapterHolder.SetOnClick(new OnClickListenerCustom.Item() {
            @Override
            public void OnClick() {
                SourceContent sourceContent = new SourceContent();
                sourceContent.setTitle(items.get(i).title);
                sourceContent.setUrl(items.get(i).url);
                sourceContent.setFinalUrl(items.get(i).url);
                WebOpenActivity.StartActivity(context,sourceContent);
            }

            @Override
            public void LongClick() {

            }
        });
        Glide.with(context).load(items.get(i).imageLink).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                newsAdapterHolder.preview.setImageDrawable(resource);
                Palette.generateAsync(((BitmapDrawable) resource).getBitmap(), new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        if (palette != null) {
                            Palette.Swatch vib = palette.getVibrantSwatch();
                            try {
                                int mutedCollor = Objects.requireNonNull(palette.getVibrantSwatch()).getRgb();
                                if (vib != null) {
                                    newsAdapterHolder.cardView.setCardBackgroundColor(palette.getMutedColor(mutedCollor));
//                                            collapsingToolbarLayout.setBackgroundColor(mutedCollor);
//                                            collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(mutedCollor));
//                                            collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedCollor));
//                                button.setBackgroundColor(mutedCollor);
                                }
                            } catch (NullPointerException ignored) {

                            }
                        }
                    }
                });
                newsAdapterHolder.SetOnClick(new OnClickListenerCustom.Item() {
                    @Override
                    public void OnClick() {
                        SourceContent sourceContent = new SourceContent();
                        sourceContent.setTitle(items.get(i).title);
                        sourceContent.setUrl(items.get(i).url);
                        sourceContent.setFinalUrl(items.get(i).url);
                        sourceContent.setImages(Collections.singletonList(items.get(i).imageLink));

                        WebOpenActivity.StartActivity(context,sourceContent);
                    }

                    @Override
                    public void LongClick() {

                    }
                });
                return true;
            }
        }).into(newsAdapterHolder.preview);

    }

    @Override
    public int getItemCount() {
        if(items!=null)
        return items.size();
        else return 0;
    }
}
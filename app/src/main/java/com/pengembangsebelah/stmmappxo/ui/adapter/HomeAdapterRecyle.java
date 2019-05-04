package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.leocardz.link.preview.library.SourceContent;
import com.pengembangsebelah.stmmappxo.model.PromotionData;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.ui.activity.WebOpenActivity;
import com.pengembangsebelah.stmmappxo.utils.OnClickListenerCustom;

import java.util.ArrayList;
import java.util.List;


class HomeAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    ImageView preview;
    TextView title;
    TextView subtitle;
    Button open;
    OnClickListenerCustom.Item clik;

    public HomeAdapterHolder(@NonNull View itemView) {
        super(itemView);
        preview = itemView.findViewById(R.id.thumbnail_promotion);
        title=itemView.findViewById(R.id.title_promotion);
        subtitle=itemView.findViewById(R.id.title_shortdesc_promotion);
        open=itemView.findViewById(R.id.promotion_tre);
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

public class HomeAdapterRecyle extends RecyclerView.Adapter<HomeAdapterHolder>{

    List<PromotionData> items;
    Context context;
    public HomeAdapterRecyle(List<PromotionData> items, Context context) {
        this.items=items;
        this.context=context;
    }

    @NonNull
    @Override
    public HomeAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_promotion,viewGroup,false);
        return new HomeAdapterHolder(view);
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
    public void onBindViewHolder(@NonNull final HomeAdapterHolder newsAdapterHolder, final int i) {
        newsAdapterHolder.title.setText(items.get(i).title);
        newsAdapterHolder.subtitle.setText(yuyu(items.get(i).description));
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child(items.get(i).thumbnailRute).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                RequestOptions requestOptions=new RequestOptions().fitCenter().override(100,100);
                Glide.with(context).load(uri).apply(requestOptions).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        newsAdapterHolder.preview.setImageDrawable(resource);
                        final SourceContent sourceContent=new SourceContent();
                        sourceContent.setTitle(items.get(i).title);
                        sourceContent.setUrl(items.get(i).url);
                        sourceContent.setFinalUrl(items.get(i).url);
                        List<String> urisd = new ArrayList<>();
                        urisd.add(String.valueOf(uri));
                        sourceContent.setImages(urisd);
                        newsAdapterHolder.open.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                WebOpenActivity.StartActivity(context,sourceContent);
                            }
                        });
                        return true;
                    }
                }).into(newsAdapterHolder.preview);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public int getItemCount() {
        if(items!=null)
        return items.size();
        else return 0;
    }
}
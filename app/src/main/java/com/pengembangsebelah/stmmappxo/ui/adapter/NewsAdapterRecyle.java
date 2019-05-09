package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.leocardz.link.preview.library.SourceContent;
import com.pengembangsebelah.stmmappxo.model.Item;
import com.pengembangsebelah.stmmappxo.ui.activity.WebOpenActivity;
import com.pengembangsebelah.stmmappxo.utils.OnClickListenerCustom;
import com.pengembangsebelah.stmmappxo.R;

import java.util.ArrayList;
import java.util.List;


class NewsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    ImageView preview;
    TextView title;
    OnClickListenerCustom.Item clik;
    public NewsAdapterHolder(@NonNull View itemView) {
        super(itemView);
        preview = itemView.findViewById(R.id.item_preview_thumbnail);
        title=itemView.findViewById(R.id.item_news_title);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
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
public class NewsAdapterRecyle extends RecyclerView.Adapter<NewsAdapterHolder>{

    List<Item> items;
    Context context;
    public NewsAdapterRecyle(List<Item> items,Context context) {
        this.items=items;
        this.context=context;
        Log.d("CHILDS", "NewsAdapterRecyle: Masuk"+items.size());
    }

    @NonNull
    @Override
    public NewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news,viewGroup,false);
        return new NewsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsAdapterHolder newsAdapterHolder, final int i) {
        newsAdapterHolder.title.setText(items.get(i).title);
        RequestOptions requestOptions=new RequestOptions().fitCenter().override(100,100);

        Glide.with(context).load(items.get(i).thumbnail).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                final SourceContent sourceContent=new SourceContent();
                sourceContent.setTitle(items.get(i).title);
                sourceContent.setUrl(items.get(i).link);
                sourceContent.setFinalUrl(items.get(i).link);
                List<String> urisd = new ArrayList<>();
                urisd.add(items.get(i).thumbnail);
                sourceContent.setImages(urisd);
                newsAdapterHolder.SetOnClick(new OnClickListenerCustom.Item() {
                    @Override
                    public void OnClick() {
                        WebOpenActivity.StartActivity(context,sourceContent);
                    }

                    @Override
                    public void LongClick() {
                        //Toast.makeText(context, "On Long "+items.get(i).link, Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
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
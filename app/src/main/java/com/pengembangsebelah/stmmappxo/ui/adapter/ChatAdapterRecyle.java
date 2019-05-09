package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.pengembangsebelah.stmmappxo.model.ChatData;
import com.pengembangsebelah.stmmappxo.utils.TimeParse;
import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.ui.activity.WebOpenActivity;
import com.pengembangsebelah.stmmappxo.utils.OnClickListenerCustom;
import com.pengembangsebelah.stmmappxo.utils.Redirecter;
import com.leocardz.link.preview.library.TextCrawler;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


import static com.pengembangsebelah.stmmappxo.ui.adapter.ChatAdapterHolder.TAG;


class ChatAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public final static String TAG = "CHATADAPTER";
    ImageView preview;
    TextView title;
    TextView message;
    TextView time;
    CardView place;
    TextView title_P;
    TextView link_P;
    TextView desc_P;
    ImageView image_P;

    OnClickListenerCustom.Item clik;
    public ChatAdapterHolder(@NonNull View itemView) {
        super(itemView);
        preview = itemView.findViewById(R.id.profile_chat);
        title=itemView.findViewById(R.id.title_chat);
        message=itemView.findViewById(R.id.message);
        time=itemView.findViewById(R.id.time_chat);

        place=itemView.findViewById(R.id.link_preview_chat);
        title_P=itemView.findViewById(R.id.title_web_preview);
        link_P=itemView.findViewById(R.id.link_web_preview);
        desc_P=itemView.findViewById(R.id.desc_web_preview);
        image_P=itemView.findViewById(R.id.imagePost_preview);
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

public class ChatAdapterRecyle extends RecyclerView.Adapter<ChatAdapterHolder>{

    List<ChatData> items;
    Context context;
    public ChatAdapterRecyle(List<ChatData> chatData,Context context) {
        this.items=chatData;
        this.context=context;
    }

    @NonNull
    @Override
    public ChatAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("RUSSI", "onCreateViewHolder: "+i);
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat,viewGroup,false);
        return new ChatAdapterHolder(view);
    }

    String GetTime(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT+07:00");
        Calendar calendar = Calendar.getInstance(timeZone);
        String time =
                String.format("%04d",calendar.get(Calendar.YEAR))+
                String.format("%02d",calendar.get(Calendar.MONTH))+
                String.format("%02d",calendar.get(Calendar.DATE))+
                String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY))+
                String.format("%02d",calendar.get(Calendar.MINUTE))+
                String.format("%02d",calendar.get(Calendar.SECOND))+
                        String.format("%03d",calendar.get(Calendar.MILLISECOND));
        return time;
    }

    String Waktu(TimeParse now, TimeParse old){
        int detik = (now.getSecond()-old.getSecond());
        int detik2 = (now.getMinuite()-old.getMinuite())*60;
        int detik3 = (now.getHour()-old.getHour())*60*60;

        int counte = detik+detik2+detik3;
        Log.d("MANS", "Waktu: "+counte+"\n" +detik+
                "\n" +detik2+
                "\n" +detik3+
                "\n "+now.getHour()+" "+old.getHour()+" \n"+old.getDateCal()+" "+now.getDateCal() );
        if(now.getDateCal()<=old.getDateCal()) {
            if (counte < 1) {
                return "now";
            } else if (counte < 60) {
                return counte + " s";
            } else if (counte < 3600) {
                return counte / 60 + " m";
            } else if(counte <216000){
                return counte / 3600 + " h";
            } else {
                return counte/216000 + " d";
            }
        }else {
            return "yesterday";
        }
    }

    String AmbilLINK(String f){
        String text="http";
        try {
            String linknya=f.substring(f.indexOf(text),f.length());
            Log.d(TAG, "AmbilLINK: " + linknya );
            if(linknya!=null) {
                String[] linkfix = linknya.split(" ");
                Log.d(TAG, "AmbilLINK: " + linknya + " \n " + linkfix[0]);
                if (linkfix!=null) {
                    return linkfix[0];
                }else {
                    return "null";
                }
            }else {
                return "null";
            }
        }catch (StringIndexOutOfBoundsException e){
            return "null" ;
        }


    }


    @Override
    public void onBindViewHolder(@NonNull final ChatAdapterHolder chat, final int i) {
        chat.title.setText(items.get(i).name);
        chat.message.setText(items.get(i).message);

        TimeParse timeParseOld = new TimeParse(items.get(i).date);
        TimeParse timeParseNow = new TimeParse(GetTime());
        chat.time.setText(Waktu(timeParseNow,timeParseOld));

        if(items.get(i).name.contains("MMTC ")){
            final RequestOptions requestOptions=new RequestOptions().fitCenter().override(100,100);
            Glide.with(context).load(R.drawable.icon).apply(requestOptions).into(chat.preview);
        }else if(items.get(i).imagedisplay!=null){
            final RequestOptions requestOptions=new RequestOptions().fitCenter().override(100,100);
            Glide.with(context).load(items.get(i).imagedisplay).apply(requestOptions).into(chat.preview);
        }else {
            chat.preview.setVisibility(View.GONE);
        }

        chat.message.setMovementMethod(LinkMovementMethod.getInstance());

        final String ha = AmbilLINK(items.get(i).message);
        Log.d(TAG, "onBindViewHolder: "+ha);
        if(!ha.equals("null")) {
            chat.place.setVisibility(View.VISIBLE);
            TextCrawler textCrawler = new TextCrawler();
            LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {
                @Override
                public void onPre() {
                    chat.place.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "please wait we will load data", Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "onClick: MANANS");
//                            Redirecter redirecter = new Redirecter();
//                            context.startActivity(redirecter.GetIntentWebsite(context,ha));
                        }
                    });
                    chat.title_P.setText("load preview from "+ha);
                    chat.desc_P.setVisibility(View.GONE);
                    chat.link_P.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPos(final SourceContent sourceContent, boolean b) {
                        chat.place.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "onClick: MANANS \n"+sourceContent.getHtmlCode()+"\n ffsad \n"+sourceContent.getRaw());
                                Redirecter redirecter = new Redirecter();
//                                context.startActivity(redirecter.GetIntentWebsite(context,sourceContent.getFinalUrl()));
                                WebOpenActivity.StartActivity(context,sourceContent);
                            }
                        });

                        chat.title_P.setText(sourceContent.getTitle());
                        chat.link_P.setText(sourceContent.getFinalUrl());
                        chat.desc_P.setText(sourceContent.getDescription());
                        chat.desc_P.setVisibility(View.VISIBLE);
                        chat.link_P.setVisibility(View.VISIBLE);
                        try {
                            Glide.with(context).load(sourceContent.getImages().get(0)).into(chat.image_P);
                        }catch (IndexOutOfBoundsException | IllegalArgumentException ignored){

                        }
                    }
                };
                textCrawler.makePreview(linkPreviewCallback, ha);
        }else {
            chat.place.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(items!=null)
        return items.size();
        else return 0;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ChatAdapterHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d(TAG, "onViewDetachedFromWindow: Kang sunar detack");
    }
}
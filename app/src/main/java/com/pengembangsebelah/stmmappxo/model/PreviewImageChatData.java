package com.pengembangsebelah.stmmappxo.model;

import android.graphics.drawable.Drawable;

public class PreviewImageChatData {
    public String url;
    public Drawable drawable;
    public String title;
    public String link;
    public String desc;

    public PreviewImageChatData(){

    }
    public PreviewImageChatData(String url,String title,String link,String desc){
        this.url=url;
        this.title=title;
        this.link=link;
        this.desc=desc;
    }
    public PreviewImageChatData(String url,String title,String link,String desc,Drawable drawable){
        this.url=url;
        this.title=title;
        this.link=link;
        this.desc=desc;
        this.drawable=drawable;
    }
}

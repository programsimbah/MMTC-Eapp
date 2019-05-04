package com.pengembangsebelah.stmmappxo.model;

public class PromoItem {
    public String title;
    public String url;
    public String imageLink;

    public PromoItem(){

    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PromoItem(String title, String url, String imageLink){
        this.title=title;
        this.url=url;
        this.imageLink=imageLink;
    }
    public PromoItem(String title, String url){
        this.title=title;
        this.url=url;
    }
}

package com.pengembangsebelah.stmmappxo.model;

public class PromotionData {
    public String title;
    public String description;
    public String url;
    public String thumbnailRute;
    public int type;

    public PromotionData (){}
    public PromotionData(String title,String description,String url, String thumbnailRute, int type){
        this.title=title;
        this.description=description;
        this.url=url;
        this.thumbnailRute=thumbnailRute;
        this.type=type;

    }
}

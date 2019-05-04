package com.pengembangsebelah.stmmappxo.model;

import java.util.List;

public class TopChartData {
    public String title;
    public String url;
    public List<TopAudioData> topAudioData;

    public TopChartData(){

    }

    public TopChartData(String title,String url,List<TopAudioData> topAudioData){
        this.title=title;
        this.url=url;
        this.topAudioData=topAudioData;
    }
}

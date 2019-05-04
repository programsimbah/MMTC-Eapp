package com.pengembangsebelah.stmmappxo.utils;

public class TimeParse {
    String yu;
    public TimeParse(String me){
        this.yu=me;
    }
    public Integer getSecond(){
        return Integer.valueOf(yu.substring(12,14));
    }
    public Integer getMinuite(){
        return Integer.valueOf(yu.substring(10,12));
    }
    public Integer getHour(){
        return Integer.valueOf(yu.substring(8,10));
    }
    public Integer getYear(){
        return Integer.valueOf(yu.substring(0,4));
    }
    public Integer getMonth(){
        return Integer.valueOf(yu.substring(4,6));
    }
    public Integer getDay(){
        return Integer.valueOf(yu.substring(6,8));
    }
    public Integer getDateCal(){
        return Integer.valueOf(yu.substring(0,8));
    }
}


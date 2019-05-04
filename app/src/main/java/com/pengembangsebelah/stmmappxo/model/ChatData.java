package com.pengembangsebelah.stmmappxo.model;

public class ChatData {
    public String country;
    public String date;
    public String ip;
    public String message;
    public String name;
    public Integer priority;
    public String vendor;
    public String uid;
    public String imagedisplay;
    public ChatData(){

    }
    public ChatData(String country,String date,String ip,String message,String name,Integer priority,String vendor,String uid){
        this.country=country;
        this.date=date;
        this.ip=ip;
        this.message=message;
        this.name=name;
        this.priority=priority;
        this.vendor=vendor;
        this.uid=uid;
    }

    public ChatData(String date,String message,String name,String imagedisplay){
        this.date=date;
        this.message=message;
        this.name=name;
        this.imagedisplay=imagedisplay;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

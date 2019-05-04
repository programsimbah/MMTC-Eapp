package com.pengembangsebelah.stmmappxo.model;

import com.google.firebase.auth.FirebaseUser;

public class UserData {
    public String display_name;
    public String url_profile;
    public String uid;
    public String vendor;
    public String email;
    public String phone_number;
    public boolean isAnynnomuse;

    public UserData(){
    }
    public UserData(String display_name, String url_profile,String uid,String profider,String email,String phone_number,boolean isAnynnomuse){
        this.display_name=display_name;
        this.url_profile=url_profile;
        this.uid=uid;
        this.vendor=profider;
        this.email=email;
        this.phone_number=phone_number;
        this.isAnynnomuse=isAnynnomuse;
    }
    public UserData(FirebaseUser firebaseUser){
        this.display_name=firebaseUser.getDisplayName();
        this.url_profile=String.valueOf(firebaseUser.getPhotoUrl());
        this.uid=firebaseUser.getUid();
        this.vendor=firebaseUser.getProviderId();
        this.email=firebaseUser.getEmail();
        this.phone_number=firebaseUser.getPhoneNumber();
        this.isAnynnomuse=firebaseUser.isAnonymous();
    }
}

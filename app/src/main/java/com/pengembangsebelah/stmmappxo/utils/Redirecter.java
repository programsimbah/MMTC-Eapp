package com.pengembangsebelah.stmmappxo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class Redirecter {

    private Intent GetIntentLine(Context context){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("con.package.address");
        return intent;
    }

    public Intent GetIntentFacebook(Context context,String username){
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana",0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+username));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+username));
        }
    }

    public Intent GetIntentTwiter(Context context,String username){
        return new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/intent/follow?screen_name="+username+"&original_referer=https://dev.twitter.com/web/intents"));
    }

    public Intent GetInstagramIntent(Context context,String username){
        try {
            context.getPackageManager().getPackageInfo("com.instagram.android",0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/"+username));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+username));
        }
    }

    public Intent GetIntentWebsite(Context context,String url){
        return new Intent(Intent.ACTION_VIEW,Uri.parse(url));
    }

}

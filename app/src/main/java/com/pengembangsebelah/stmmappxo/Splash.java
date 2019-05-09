package com.pengembangsebelah.stmmappxo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.pengembangsebelah.stmmappxo.utils.Redirecter;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.pengembangsebelah.stmmappxo.R.layout.activity_splash);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if(Langsung()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.dialog_gdpr_basic, null);
            final AppCompatCheckBox f = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.check_gdpr);
            f.setVisibility(View.GONE);
            Button oke = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.button_oke_gdpr);
            Button pay = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.gdpr_pay);
            TextView iu = layout.findViewById(R.id.detaiolio);
            iu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Redirecter redirecter = new Redirecter();
                    startActivity(redirecter.GetIntentWebsite(Splash.this, getString(R.string.url_privcy_police_lala)));
                }
            });
            pay.setVisibility(View.GONE);
            oke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(LOADGDPR,false);
                    editor.apply();
                    ConsentInformation.getInstance(Splash.this).setConsentStatus(ConsentStatus.PERSONALIZED);
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Splash.this, "sorry undermaintenance this", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            alertDialog.show();
        }else {
            ConsentInformation.getInstance(Splash.this).setConsentStatus(ConsentStatus.PERSONALIZED);
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    final String LOADGDPR="dasdsa";
    public Boolean Langsung(){
        Boolean d = sharedPref.getBoolean(LOADGDPR,true);
        return d;
    }
}

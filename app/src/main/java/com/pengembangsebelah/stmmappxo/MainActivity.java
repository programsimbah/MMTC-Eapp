package com.pengembangsebelah.stmmappxo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.widget.LoginButton;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pengembangsebelah.stmmappxo.core.LoginFirebase;
import com.pengembangsebelah.stmmappxo.core.UserUpdateDatabase;
import com.pengembangsebelah.stmmappxo.core.mediapapa.RadioManager;
import com.pengembangsebelah.stmmappxo.model.ChatData;
import com.pengembangsebelah.stmmappxo.model.PromoItem;
import com.pengembangsebelah.stmmappxo.model.ScheduleProgram;
import com.pengembangsebelah.stmmappxo.ui.adapter.SectionHomeAdapter;
import com.pengembangsebelah.stmmappxo.utils.Redirecter;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.utils.CustomViewPager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    String TAG="mainAct";

//    MobileAds.initialize(this,getString(R.string.ap))

    FirebaseUser _user;
    static LoginFirebase loginFirebase;

    //variabels
    SectionHomeAdapter sectionHomeAdapter;
    CustomViewPager viewPager;
    public static RadioManager radioManager;
    public static List<ScheduleProgram> scheduleProgramse;
    //var end
    // Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(viewPager!=null) {
                switch (item.getItemId()) {
                    case com.pengembangsebelah.stmmappxo.R.id.navigation_home:
                        viewPager.setCurrentItem(0, true);
                        return true;
                    case com.pengembangsebelah.stmmappxo.R.id.navigation_radio:
                        viewPager.setCurrentItem(1, true);
                        return true;
//                    case R.id.navigation_tv:
//                        viewPager.setCurrentItem(2, true);
//                        return true;
                    case com.pengembangsebelah.stmmappxo.R.id.navigation_promo:
                        viewPager.setCurrentItem(2, true);
                        return true;
                }
            }
            return false;
        }
    };
    //navigation end

    @Override
    public void onStart() {
        super.onStart();
       // EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
       // EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        radioManager.unbind();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        radioManager.bind();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "LoginSucces: ");
        loginFirebase.SetCalbackFacebookResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.custom_dialog_exit,null);
        Button rate = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.rata_appa);
        Button exit = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.ekit_apap);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirecter redirecter = new Redirecter();
                startActivity(redirecter.GetIntentWebsite(MainActivity.this,"https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName()));
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.pengembangsebelah.stmmappxo.R.layout.activity_main);
        loginFirebase =new LoginFirebase(this);

        radioManager = RadioManager.with(this);
        sectionHomeAdapter = new SectionHomeAdapter(getSupportFragmentManager());

        viewPager = findViewById(com.pengembangsebelah.stmmappxo.R.id.container_home);
        viewPager.setAdapter(sectionHomeAdapter);
        viewPager.SetPagingEnable(false);

        BottomNavigationView navigation = findViewById(com.pengembangsebelah.stmmappxo.R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        _user = FirebaseAuth.getInstance().getCurrentUser();

        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        String[] pubId= {getString(com.pengembangsebelah.stmmappxo.R.string.pubID)};
        consentInformation.requestConsentInfoUpdate(pubId, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                Log.d(TAG, "onConsentInfoUpdated: MAMSAM "+consentStatus);
            }

            @Override
            public void onFailedToUpdateConsentInfo(String reason) {

            }
        });
//        Log.d("SARIMI KeyHash:", getApplicationContext().getPackageName());
//
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getApplicationContext().getPackageName()+".MainActivity",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("SARIMI KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
//
//        }

    }

    //Menu Item Start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.pengembangsebelah.stmmappxo.R.menu.menu_default, menu);
        return true;
    }
    public static String StremingMMTC = "Generation Channel";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if(id==R.id.home_info){
//            Toast.makeText(this, "info_app", Toast.LENGTH_SHORT).show();
//        }else
            if(id== com.pengembangsebelah.stmmappxo.R.id.home_manage){
            LoginKuy(item.getActionView());
        }else if(id== com.pengembangsebelah.stmmappxo.R.id.radio_menu_play){
            if(scheduleProgramse!=null) {
                radioManager.playOrPause("http://radio.mmtc.ac.id:8000/liveradio", scheduleProgramse);
            }else {
                Toast.makeText(this, "please wait we have load", Toast.LENGTH_SHORT).show();
            }
        }else if(id== com.pengembangsebelah.stmmappxo.R.id.promo_add){
            AddEvent();
        }else if(id== com.pengembangsebelah.stmmappxo.R.id.radio_info){
            AbotUs();
        }else if(id== com.pengembangsebelah.stmmappxo.R.id.tv_info){
            Toast.makeText(this, "tv_info", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //Menu Item End

    //void
    void LookTermsUses(){
        Redirecter redirecter =new Redirecter();
        startActivity(redirecter.GetIntentWebsite(this,getString(R.string.url_term_use_lala)));
    }

    void AbotUs(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.dialog_about_us,null);

        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    void LoginKuy(View view){
        if(_user == null){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.custom_dialog_login,null);
            loginFirebase.loginButton((LoginButton) layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.LoginFacebook));
            TextView textView = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.term_uses_login);
            SignInButton signInButton = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.LoginGoogle);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LookTermsUses();
                }
            });
            builder.setView(layout);

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginFirebase.LoginGoogle(MainActivity.this,MainActivity.this, new LoginFirebase.ListenerLogin() {
                        @Override
                        public void LoginSucces(FirebaseUser user) {
                            alertDialog.dismiss();
                            _user=user;
                            UserUpdateDatabase n = new UserUpdateDatabase(new UserUpdateDatabase.ListenerCompliteUser() {
                                @Override
                                public void OnComplite(@NonNull Task<Void> task) {
                                    Log.d(TAG, "OnComplite: dsedede");
                                }
                            });
                            n.Update(user);
                        }

                        @Override
                        public void LoginFailed(String message) {
                            loginFirebase.SignOut();
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            loginFirebase.LoginFacebook(this, new LoginFirebase.ListenerLogin() {
                @Override
                public void LoginSucces(FirebaseUser user) {
                    alertDialog.dismiss();
                    _user=user;
                    UserUpdateDatabase n = new UserUpdateDatabase(new UserUpdateDatabase.ListenerCompliteUser() {
                        @Override
                        public void OnComplite(@NonNull Task<Void> task) {
                            Log.d(TAG, "OnComplite: dsedede");
                        }
                    });
                    n.Update(user);
                }

                @Override
                public void LoginFailed(String message) {
                    loginFirebase.SignOut();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog.show();
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.custom_dialog_logout_attention,null);
            Button btn = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.custom_dialog_logout_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Thank you "+_user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    loginFirebase.SignOut();
                    _user=null;
                    Hide();
                    LoginKuy(null);
                }
            });
            TextView textView=layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.custom_dialoge_logout_message);
            textView.setText(getString(com.pengembangsebelah.stmmappxo.R.string.logout_message));
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            SetDialog(alertDialog);
            alertDialog.show();
        }
    }
    AlertDialog debugDialog;
    void  SetDialog(AlertDialog debugDialog){
        this.debugDialog=debugDialog;
    }
    void Hide(){
        if(debugDialog!=null){
            debugDialog.dismiss();
        }
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
    public void Message(){
        if(_user == null){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.custom_dialog_login,null);
            loginFirebase.loginButton((LoginButton) layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.LoginFacebook));
            SignInButton signInButton = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.LoginGoogle);
            TextView textView = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.term_uses_login);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LookTermsUses();
                }
            });
            builder.setView(layout);

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginFirebase.LoginGoogle(MainActivity.this,MainActivity.this, new LoginFirebase.ListenerLogin() {
                        @Override
                        public void LoginSucces(FirebaseUser user) {
                            alertDialog.dismiss();
                            _user=user;
                            UserUpdateDatabase n = new UserUpdateDatabase(new UserUpdateDatabase.ListenerCompliteUser() {
                                @Override
                                public void OnComplite(@NonNull Task<Void> task) {
                                    Log.d(TAG, "OnComplite: dsedede");
                                }
                            });
                            n.Update(user);
                            Message();
                        }

                        @Override
                        public void LoginFailed(String message) {
                            loginFirebase.SignOut();
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            loginFirebase.LoginFacebook(this, new LoginFirebase.ListenerLogin() {
                @Override
                public void LoginSucces(FirebaseUser user) {
                    alertDialog.dismiss();
                    _user=user;
                    UserUpdateDatabase n = new UserUpdateDatabase(new UserUpdateDatabase.ListenerCompliteUser() {
                        @Override
                        public void OnComplite(@NonNull Task<Void> task) {
                            Log.d(TAG, "OnComplite: dsedede");
                        }
                    });
                    n.Update(user);
                    Message();
                }

                @Override
                public void LoginFailed(String message) {
                    loginFirebase.SignOut();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog.show();
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.cutom_dialog_sendi,null);
            final Button btn = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.kirim_pesan);
            EditText peses = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.edit_pesan_uye);
            TextView textView = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.pesan_nama_user);
            textView.setText("Display name : "+_user.getDisplayName());
            ImageView imageView = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.profile_pesan_user);
            Glide.with(this).load(_user.getPhotoUrl()).into(imageView);

            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            SetDialog(alertDialog);
            peses.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d(TAG, "afterTextChanged:ds "+s);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "afterTextChanged:f "+s);
                }

                @Override
                public void afterTextChanged(final Editable s) {
                    Log.d(TAG, "afterTextChanged: "+s);
                    if(!s.toString().equals("")){
                        btn.setEnabled(true);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChatData chatData = new ChatData(GetTime(),s.toString(),_user.getDisplayName(),_user.getPhotoUrl().toString());
                                chatData.setUid(_user.getUid());
                                chatData.setPriority(1);
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                                databaseReference.child(Kontent.ARG_RADIO_APK).child(Kontent.ARG_RADIO_CHAT).child(GetTime()).setValue(chatData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "send message", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog.dismiss();
//                    loginFirebase.SignOut();
//                    _user=null;
//                    Hide();
//                    LoginKuy(null);
                            }
                        });
                    }else {
                        btn.setEnabled(false);
                    }
                }
            });
            alertDialog.show();
        }
    }
    public void AddEvent(){
        if(_user == null){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.custom_dialog_login,null);
            loginFirebase.loginButton((LoginButton) layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.LoginFacebook));
            SignInButton signInButton = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.LoginGoogle);
            TextView textView = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.term_uses_login);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LookTermsUses();
                }
            });
            builder.setView(layout);

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginFirebase.LoginGoogle(MainActivity.this,MainActivity.this, new LoginFirebase.ListenerLogin() {
                        @Override
                        public void LoginSucces(FirebaseUser user) {
                            alertDialog.dismiss();
                            _user=user;
                            UserUpdateDatabase n = new UserUpdateDatabase(new UserUpdateDatabase.ListenerCompliteUser() {
                                @Override
                                public void OnComplite(@NonNull Task<Void> task) {
                                    Log.d(TAG, "OnComplite: dsedede");
                                }
                            });
                            n.Update(user);
                            AddEvent();
                        }

                        @Override
                        public void LoginFailed(String message) {
                            loginFirebase.SignOut();
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            loginFirebase.LoginFacebook(this, new LoginFirebase.ListenerLogin() {
                @Override
                public void LoginSucces(FirebaseUser user) {
                    alertDialog.dismiss();
                    _user=user;
                    UserUpdateDatabase n = new UserUpdateDatabase(new UserUpdateDatabase.ListenerCompliteUser() {
                        @Override
                        public void OnComplite(@NonNull Task<Void> task) {
                            Log.d(TAG, "OnComplite: dsedede");
                        }
                    });
                    n.Update(user);
                    AddEvent();
                }

                @Override
                public void LoginFailed(String message) {
                    loginFirebase.SignOut();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog.show();
        }else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View layout = layoutInflater.inflate(com.pengembangsebelah.stmmappxo.R.layout.cutom_dialog_add_promo,null);
            final Button btn = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.add_promo);

            final EditText link = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.edit_link);
            final EditText thumblink = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.edit_link_image);
            final EditText title = layout.findViewById(com.pengembangsebelah.stmmappxo.R.id.edit_title_prom);

            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            SetDialog(alertDialog);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(link.getText())){
                        Toast.makeText(MainActivity.this, "Whay leave link empty?", Toast.LENGTH_SHORT).show();
                        link.setFocusable(true);
                    }else if(TextUtils.isEmpty(title.getText())){
                        Toast.makeText(MainActivity.this, "Dont make audience boom?", Toast.LENGTH_SHORT).show();
                        title.setFocusable(true);
                    }else {
                        PromoItem promoItemme = new PromoItem(title.getText().toString(),link.getText().toString());
                        if(!TextUtils.isEmpty(thumblink.getText())){
                            promoItemme.setImageLink(thumblink.getText().toString());
                        }


                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                        databaseReference.child(Kontent.ARG_RADIO_MO).child(GetTime()+"_"+_user.getUid()).setValue(promoItemme).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this, "added", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                }
            });

            alertDialog.show();
        }
    }
}

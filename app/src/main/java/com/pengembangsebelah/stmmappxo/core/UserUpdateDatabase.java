package com.pengembangsebelah.stmmappxo.core;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pengembangsebelah.stmmappxo.model.Kontent;
import com.pengembangsebelah.stmmappxo.model.UserData;

public class UserUpdateDatabase {
    public interface ListenerCompliteUser{
        void OnComplite(@NonNull Task<Void> task);
    }
    UserUpdateDatabase.ListenerCompliteUser listenerCompliteUser;
    public UserUpdateDatabase(){

    }

    public void Update(FirebaseUser user){
        UserData userData=new UserData(user);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Kontent.ARG_RADIO_USER).child(userData.uid).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listenerCompliteUser.OnComplite(task);
            }
        });
    }

    public UserUpdateDatabase(final UserUpdateDatabase.ListenerCompliteUser listenerCompliteUser){
        this.listenerCompliteUser=listenerCompliteUser;

    }
}

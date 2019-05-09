package com.pengembangsebelah.stmmappxo.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pengembangsebelah.stmmappxo.R;

public class LoginFirebase {
    private static final int RC_SIGN_IN=9001;
    public interface ListenerLogin{
        void LoginSucces(FirebaseUser user);
        void LoginFailed(String message);
    }

    public LoginFirebase(Context ctx){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ctx.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(ctx,googleSignInOptions);

        mAuth = FirebaseAuth.getInstance();
    }

    private static final String TAG = "LoginFirebase";

    /* *************************************
     *              GENERAL                *
     ***************************************/
    /* TextView that is used to display information about the logged in user */
    ProgressDialog progressDialog;
    //private TextView mLoggedInStatusTextView;
    LoginFirebase.ListenerLogin listenerLogin;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private static FirebaseUser user;
    // [END declare_auth]

    Context context;
    private CallbackManager mCallbackManager;

    /* *************************************
     *              FACEBOOK               *
     ***************************************/

    void ShowProgressDialog(){
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Get Credential User");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    void ShowProgressDialog(Context c){
        progressDialog=new ProgressDialog(c);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Get Credential User");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    void HideProgressDialog(){
        progressDialog.dismiss();

    }

    void onFacebookAccessTokenChange(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        ShowProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser _user = mAuth.getCurrentUser();
                            user=_user;
                            listenerLogin.LoginSucces(_user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            listenerLogin.LoginFailed("signInWithCredential:failure");
                            user = null;
                        }

                        // [START_EXCLUDE]
                        HideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    public LoginButton loginButton (LoginButton loginButton){
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onFacebookAccessTokenChange(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                listenerLogin.LoginFailed("facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                listenerLogin.LoginFailed("facebook:onError "+error);
            }
        });
        return loginButton;
    }

    void onGoogleAuth(GoogleSignInAccount account){
        ShowProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser _user = mAuth.getCurrentUser();
                            user=_user;
                            listenerLogin.LoginSucces(_user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            listenerLogin.LoginFailed("signInWithCredential:failure");
                            user = null;
                        }

                        // [START_EXCLUDE]
                        HideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    public void SetCalbackFacebookResult(int requestCode, int resultCode, Intent data){
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onGoogleAuth(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void LoginFacebook(@NonNull final Context ctx, final LoginFirebase.ListenerLogin listenerLogin){
        this.listenerLogin = listenerLogin;
        this.context=ctx;
        mAuth = FirebaseAuth.getInstance();
    }

    GoogleSignInClient googleSignInClient;

    public void LoginGoogle(@NonNull final Context ctx, final LoginFirebase.ListenerLogin listenerLogin){
        this.listenerLogin = listenerLogin;
        this.context=ctx;

        Intent signi = googleSignInClient.getSignInIntent();
        ((Activity)ctx).startActivityForResult(signi,RC_SIGN_IN);
    }
    public void LoginGoogle(Activity s,@NonNull final Context ctx, final LoginFirebase.ListenerLogin listenerLogin){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ctx.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(ctx,googleSignInOptions);
        mAuth = FirebaseAuth.getInstance();

        this.listenerLogin = listenerLogin;
        this.context=ctx;

        Intent signi = googleSignInClient.getSignInIntent();
        s.startActivityForResult(signi,RC_SIGN_IN);
    }

    public void SignOut(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void Destroy(){
        
    }
}

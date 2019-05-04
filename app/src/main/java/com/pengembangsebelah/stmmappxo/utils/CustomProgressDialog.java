package com.pengembangsebelah.stmmappxo.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomProgressDialog {
    ProgressDialog progressDialog;

    public CustomProgressDialog(String title, String message, Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
    }

    public void SetCancel(Boolean value){
        progressDialog.setCancelable(value);
    }

    public void Show(){
        progressDialog.show();
    }

    public void Destroy(){
        progressDialog.dismiss();
    }
}

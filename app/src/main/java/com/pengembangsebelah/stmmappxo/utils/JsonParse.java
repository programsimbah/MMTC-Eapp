package com.pengembangsebelah.stmmappxo.utils;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonParse extends AsyncTask<Void,Void,Void>{

    public JsonParse(){

    }
    public JsonParse(int i){
        this.code=i;
    }

    public interface Listener{
        void OnFinish(String data, JSONArray object,int code);
    }


    String url;
    String lines="";
    String data="";

    JSONArray Jas;

    int code=0;
    public void SetUrl(String url){
        this.url=url;
    }

    JsonParse.Listener listener;

    public void SetListenter(JsonParse.Listener listener){
        this.listener=listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BufferedReader bufferedReader = null;
        if(url!=null) {
            try {
                URL urlC = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlC.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String _line = bufferedReader.readLine();
                Log.d("JsonParse", "doInBackground: data \n"+_line);

                while (_line!=lines){
                    data = _line+"\n";
                    lines=_line;
                }

                JSONArray JA = new JSONArray(data);
                Jas=JA;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("JsonParse", "doInBackground: err "+e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("JsonParse", "doInBackground: err"+e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JsonParse", "doInBackground: err json "+e.getMessage());
            }
        }else {
            Log.d("JsonParse", "doInBackground: url not set");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.OnFinish(data,Jas,code);
    }
}

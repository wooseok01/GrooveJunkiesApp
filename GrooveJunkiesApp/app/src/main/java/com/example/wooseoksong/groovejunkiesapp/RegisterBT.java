package com.example.wooseoksong.groovejunkiesapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.Map;

/**
 * Created by wooseoksong on 2017. 3. 7..
 */

public class RegisterBT extends AsyncTask<File, Integer, String> {
    private HttpClient hc;
    private String email;
    private String password;
    private String name;
    private String imageUrl;
    private Context mContext;


    //constructor
    public RegisterBT(){}
    public RegisterBT(HttpClient hc, String email, String password, String name, String imageUrl, Context mContext) {
        this.hc = hc;
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.mContext = mContext;
    }

    public HttpClient getHc() {return hc;}
    public void setHc(HttpClient hc) {this.hc = hc;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    @Override
    protected String doInBackground(File... params) {
        //Log.d("test",params.);
        hc.sendImageFile(email, password, name, imageUrl,mContext);
        return null;
    }
//    @Override
//    protected String doInBackground(Map<String, String>... params) {
//        //hc.uploadRegisterInfo(email, password, stageName, imageUrl);
//  //      hc.sendRegisterInfo();
//        hc.sendImageFile(email, password, name, imageUrl);
//        Log.d("BackgroundTask","Job complete!");
//        return null;
//    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Log.d("result -> ",s);
    }
}

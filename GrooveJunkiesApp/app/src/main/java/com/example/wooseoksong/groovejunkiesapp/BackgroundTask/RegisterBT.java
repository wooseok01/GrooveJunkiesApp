package com.example.wooseoksong.groovejunkiesapp.BackgroundTask;

import android.os.AsyncTask;

import com.example.wooseoksong.groovejunkiesapp.HttpClient.HttpClient;

import org.json.JSONObject;

/**
 * Created by wooseoksong on 2017. 3. 7..
 */

public class RegisterBT extends AsyncTask<String, Integer, JSONObject> {
    private HttpClient hc;
    private String email;
    private String password;
    private String name;
    private String imageUrl;


    //constructor
    public RegisterBT(){}
    public RegisterBT(String url, String email, String password, String name, String imageUrl) {
        this.hc = new HttpClient(url);
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
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
    protected JSONObject doInBackground(String... params) {

        return hc.sendRegisterInfo(email, password, name, imageUrl);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}

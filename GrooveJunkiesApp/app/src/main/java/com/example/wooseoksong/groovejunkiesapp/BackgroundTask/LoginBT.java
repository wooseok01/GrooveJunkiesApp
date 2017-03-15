package com.example.wooseoksong.groovejunkiesapp.BackgroundTask;

import android.os.AsyncTask;

import com.example.wooseoksong.groovejunkiesapp.HttpClient.HttpClient;

import org.json.JSONObject;

/**
 * Created by wooseoksong on 2017. 3. 16..
 */

public class LoginBT extends AsyncTask<String, Integer, JSONObject> {
    private String email;
    private String password;
    private String url;

    public LoginBT(){}
    public LoginBT(String email, String password, String url) {
        this.email = email;
        this.password = password;
        this.url = url;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    @Override
    protected JSONObject doInBackground(String... params) {
        HttpClient client = new HttpClient(url);
        return client.loginAction(email, password);

    }
}
